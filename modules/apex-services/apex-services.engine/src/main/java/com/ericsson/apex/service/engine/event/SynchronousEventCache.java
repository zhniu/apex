/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.engine.event;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;

/**
 * This class holds a cache of the synchronous events sent into Apex and that have not yet been replied to. It runs a thread to time out events that have not
 * been replied to in the specified timeout.
 * 
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class SynchronousEventCache implements Runnable {
    // Get a reference to the logger
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(SynchronousEventCache.class);

    // The default amount of time to wait for a synchronous event to be replied to is 1 second
    private static final long DEFAULT_SYNCHRONOUS_EVENT_TIMEOUT = 1000;

    // The timeout to wait between event polls in milliseconds and the time to wait for the thread to stop
    private static final long OUTSTANDING_EVENT_POLL_TIMEOUT = 50;
    private static final long CACHE_STOP_WAIT_INTERVAL = 10;

    // The synchronous consumer putting events into this synchronous event cache
    private ApexEventConsumer synchronousConsumer;

    // The synchronous consumer taking events out of this synchronous event cache
    private ApexEventProducer synchronousProducer = null;

    // The time in milliseconds to wait for the reply to a sent synchronous event
    private long synchronousEventTimeout = DEFAULT_SYNCHRONOUS_EVENT_TIMEOUT;

    // Map holding outstanding synchronous events
    private Map<Long, SimpleEntry<Long, Object>> toApexEventMap = new HashMap<Long, SimpleEntry<Long, Object>>();

    // Map holding reply events
    private Map<Long, SimpleEntry<Long, Object>> fromApexEventMap = new HashMap<Long, SimpleEntry<Long, Object>>();

    // The message listener thread and stopping flag
    private Thread synchronousEventCacheThread;
    private boolean stopOrderedFlag = false;

    /**
     * Create a synchronous event cache that caches outstanding synchronous Apex events.
     * 
     * @param consumer the consumer that is populating the cache
     * @param producer the producer that is emptying the cache
     * @param synchronousEventTimeout the time in milliseconds to wait for the reply to a sent synchronous event
     */
    public SynchronousEventCache(final ApexEventConsumer consumer, final ApexEventProducer producer, final long synchronousEventTimeout) {
        this.synchronousConsumer = consumer;
        this.synchronousProducer = producer;

        if (synchronousEventTimeout != 0) {
            this.synchronousEventTimeout = synchronousEventTimeout;
        }
        else {
            this.synchronousEventTimeout = DEFAULT_SYNCHRONOUS_EVENT_TIMEOUT;
        }

        // Set the cache on the producer and consumer
        synchronousConsumer.setSynchronousEventCache(this);
        synchronousProducer.setSynchronousEventCache(this);

        // Start scanning the outstanding events
        synchronousEventCacheThread = new Thread(this);
        synchronousEventCacheThread.setDaemon(true);
        synchronousEventCacheThread.start();

    }

    /**
     * Gets the synchronous consumer putting events into the cache.
     *
     * @return the source synchronous consumer
     */
    public ApexEventConsumer getSynchronousConsumer() {
        return synchronousConsumer;
    }

    /**
     * Gets the synchronous producer taking events from the cache.
     *
     * @return the synchronous producer that is taking events from the cache
     */
    public ApexEventProducer getSynchronousProducer() {
        return synchronousProducer;
    }

    /**
     * Gets the timeout value for synchronous events.
     *
     * @return the synchronous event timeout
     */
    public long getSynchronousEventTimeout() {
        return synchronousEventTimeout;
    }

    /**
     * Cache a synchronized event sent into Apex in the event cache.
     *
     * @param executionId the execution ID that was assigned to the event
     * @param event the apex event
     */
    public void cacheSynchronizedEventToApex(final long executionId, final Object event) {
        // Add the event to the map
        synchronized (toApexEventMap) {
            cacheSynchronizedEvent(toApexEventMap, executionId, event);
        }
    }

    /**
     * Remove the record of an event sent to Apex if it exists in the cache.
     * 
     * @param executionId the execution ID of the event
     * @return The removed event
     */
    public Object removeCachedEventToApexIfExists(final long executionId) {
        synchronized (toApexEventMap) {
            return removeCachedEventIfExists(toApexEventMap, executionId);
        }
    }

    /**
     * Check if an event exists in the to apex cache.
     * 
     * @param executionId the execution ID of the event
     * @return true if the event exists, false otherwise
     */
    public boolean existsEventToApex(final long executionId) {
        synchronized (toApexEventMap) {
            return toApexEventMap.containsKey(executionId);
        }
    }

    /**
     * Cache synchronized event received from Apex in the event cache.
     *
     * @param executionId the execution ID of the event
     * @param event the apex event
     */
    public void cacheSynchronizedEventFromApex(final long executionId, final Object event) {
        // Add the event to the map
        synchronized (fromApexEventMap) {
            cacheSynchronizedEvent(fromApexEventMap, executionId, event);
        }
    }

    /**
     * Remove the record of an event received from Apex if it exists in the cache.
     * 
     * @param executionId the execution ID of the event
     * @return The removed event
     */
    public Object removeCachedEventFromApexIfExists(final long executionId) {
        synchronized (fromApexEventMap) {
            return removeCachedEventIfExists(fromApexEventMap, executionId);
        }
    }

    /**
     * Check if an event exists in the from apex cache.
     * 
     * @param executionId the execution ID of the event
     * @return true if the event exists, false otherwise
     */
    public boolean existsEventFromApex(final long executionId) {
        synchronized (fromApexEventMap) {
            return fromApexEventMap.containsKey(executionId);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        LOGGER.entry();

        // Periodic scan of outstanding events
        while (synchronousEventCacheThread.isAlive() && !stopOrderedFlag) {
            ThreadUtilities.sleep(OUTSTANDING_EVENT_POLL_TIMEOUT);

            // Check for timeouts on events
            synchronized (toApexEventMap) {
                timeoutEventsOnCache(toApexEventMap);
            }
            synchronized (fromApexEventMap) {
                timeoutEventsOnCache(fromApexEventMap);
            }
        }

        LOGGER.exit();
    }

    /**
     * Stops the scanning thread and clears the cache.
     */
    public synchronized void stop() {
        LOGGER.entry();
        stopOrderedFlag = true;

        while (synchronousEventCacheThread.isAlive()) {
            ThreadUtilities.sleep(CACHE_STOP_WAIT_INTERVAL);
        }

        // Check if there are any unprocessed events
        if (!toApexEventMap.isEmpty()) {
            LOGGER.warn(toApexEventMap.size() + " synchronous events dropped due to system shutdown");
        }

        toApexEventMap.clear();
        LOGGER.exit();
    }

    /**
     * Cache a synchronized event sent in an event cache.
     * @param eventCacheMap the map to cache the event on
     * @param executionId the execution ID of the event
     * @param event the event to cache
     */
    private void cacheSynchronizedEvent(final Map<Long, SimpleEntry<Long, Object>> eventCacheMap, final long executionId, final Object event) {
        LOGGER.entry("Adding event with execution ID: " + executionId);

        // Check if the event is already in the cache
        if (eventCacheMap.containsKey(executionId)) {
            // If there was no sent event then the event timed out or some unexpected event was received
            String errorMessage = "an event with ID " + executionId
            + " already exists in the synchronous event cache, execution IDs must be unique in the system";
            LOGGER.warn(errorMessage);
            throw new ApexEventRuntimeException(errorMessage);
        }

        // Add the event to the map
        eventCacheMap.put(executionId, new SimpleEntry<Long, Object>(System.currentTimeMillis(), event));

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("event has been cached:" + event);
        }

        LOGGER.exit("Added: " + executionId);
    }

    /**
     * Remove the record of an event if it exists in the cache.
     * 
     * @param eventCacheMap the map to remove the event from
     * @param executionId the execution ID of the event
     * @return The removed event
     */
    private Object removeCachedEventIfExists(final Map<Long, SimpleEntry<Long, Object>> eventCacheMap, final long executionId) {
        LOGGER.entry("Removing: " + executionId);

        SimpleEntry<Long, Object> removedEventEntry = eventCacheMap.remove(executionId);

        if (removedEventEntry != null) {
            LOGGER.exit("Removed: " + executionId);
            return removedEventEntry.getValue();
        }
        else {
            // The event may not be one of the events in our cache, so we just ignore removal failures
            return null;
        }
    }

    /**
     * Time out events on an event cache map. Events that have a timeout longer than the configured timeout are timed out.
     * @param eventCacheMap the event cache to operate on
     */
    private void timeoutEventsOnCache(final Map<Long, SimpleEntry<Long, Object>> eventCacheMap) {
        // Use a set to keep track of the events that have timed out
        Set<Long> timedOutEventSet = new HashSet<Long>();

        for (Entry<Long, SimpleEntry<Long, Object>> cachedEventEntry : eventCacheMap.entrySet()) {
            // The amount of time we are waiting for the event reply
            long eventWaitTime = System.currentTimeMillis() - cachedEventEntry.getValue().getKey();

            // Have we a timeout?
            if (eventWaitTime > synchronousEventTimeout) {
                timedOutEventSet.add(cachedEventEntry.getKey());
            }
        }

        // Remove timed out events from the map
        for (long timedoutEventExecutionID : timedOutEventSet) {
            // Remove the map entry and issue a warning
            SimpleEntry<Long, Object> timedOutEventEntry = eventCacheMap.remove(timedoutEventExecutionID);

            LOGGER.warn("synchronous event timed out, reply not received in " + synchronousEventTimeout + " milliseconds on event "
                    + timedOutEventEntry.getValue());
        }
    }
}
