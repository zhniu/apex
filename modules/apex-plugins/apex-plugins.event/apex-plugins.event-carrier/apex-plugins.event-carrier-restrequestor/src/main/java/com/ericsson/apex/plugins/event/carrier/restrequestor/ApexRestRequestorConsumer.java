/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.event.carrier.restrequestor;

import java.net.URL;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.apex.core.infrastructure.threading.ApplicationThreadFactory;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;
import com.ericsson.apex.plugins.event.carrier.restrequestor.RESTRequestorCarrierTechnologyParameters.HTTP_METHOD;
import com.ericsson.apex.service.engine.event.ApexEventConsumer;
import com.ericsson.apex.service.engine.event.ApexEventException;
import com.ericsson.apex.service.engine.event.ApexEventReceiver;
import com.ericsson.apex.service.engine.event.ApexEventRuntimeException;
import com.ericsson.apex.service.engine.event.PeeredReference;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerParameters;
import com.ericsson.apex.service.parameters.eventhandler.EventHandlerPeeredMode;

/**
 * This class implements an Apex event consumer that receives events from a REST server.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ApexRestRequestorConsumer implements ApexEventConsumer, Runnable {
	// Get a reference to the logger
	private static final Logger LOGGER = LoggerFactory.getLogger(ApexRestRequestorConsumer.class);

	// The amount of time to wait in milliseconds between checks that the consumer thread has stopped
	private static final long REST_REQUESTOR_WAIT_SLEEP_TIME = 50;

	// The REST parameters read from the parameter service
	private RESTRequestorCarrierTechnologyParameters restConsumerProperties;

	// The timeout for REST requests
	private long restRequestTimeout = RESTRequestorCarrierTechnologyParameters.DEFAULT_REST_REQUEST_TIMEOUT;

	// The event receiver that will receive events from this consumer
	private ApexEventReceiver eventReceiver;

	// The HTTP client that makes a REST call to get an input event for Apex
	private Client client;

	// The name for this consumer
	private String name = null;

	// The peer references for this event handler
	private Map<EventHandlerPeeredMode, PeeredReference> peerReferenceMap = new EnumMap<>(EventHandlerPeeredMode.class);

	// The consumer thread and stopping flag
	private Thread consumerThread;
	private boolean stopOrderedFlag = false;

	// Temporary request holder for incoming REST requests
	private final BlockingQueue<ApexRestRequest> incomingRestRequestQueue = new LinkedBlockingQueue<>();

	// Map of ongoing REST request threads indexed by the time they started at
	private final Map<ApexRestRequest, RestRequestRunner> ongoingRestRequestMap = new ConcurrentHashMap<>();

	// The number of events received to date
	private Object eventsReceivedLock = new Object();
	private Integer eventsReceived = 0;

	// The number of the next request runner thread
	private static long nextRequestRunnerThreadNo = 0;

	@Override
	public void init(final String consumerName, final EventHandlerParameters consumerParameters, final ApexEventReceiver incomingEventReceiver)
			throws ApexEventException {
		this.eventReceiver = incomingEventReceiver;
		this.name = consumerName;

		// Check and get the REST Properties
		if (!(consumerParameters.getCarrierTechnologyParameters() instanceof RESTRequestorCarrierTechnologyParameters)) {
			String errorMessage = "specified consumer properties are not applicable to REST Requestor consumer (" + this.name + ")";
			LOGGER.warn(errorMessage);
			throw new ApexEventException(errorMessage);
		}
		restConsumerProperties = (RESTRequestorCarrierTechnologyParameters) consumerParameters.getCarrierTechnologyParameters();

		// Check if we are in synchronous mode
		if (!consumerParameters.isPeeredMode(EventHandlerPeeredMode.REQUESTOR)) {
			String errorMessage = "REST Requestor consumer (" + this.name + ") must run in peered requestor mode with a REST Requestor producer";
			LOGGER.warn(errorMessage);
			throw new ApexEventException(errorMessage);
		}

		// Check if the HTTP method has been set
		if (restConsumerProperties.getHttpMethod() == null) {
			restConsumerProperties.setHttpMethod(RESTRequestorCarrierTechnologyParameters.DEFAULT_REQUESTOR_HTTP_METHOD);
		}

		if (!(restConsumerProperties.getHttpMethod() instanceof HTTP_METHOD)) {
			String errorMessage = "specified HTTP method of \"" + restConsumerProperties.getHttpMethod()
			+ "\" is invalid, only HTTP methods " + HTTP_METHOD.values() + " are valid on REST Requestor consumer (" + this.name + ")";
			LOGGER.warn(errorMessage);
			throw new ApexEventException(errorMessage);
		}

		// Check if the HTTP URL has been set
		if (restConsumerProperties.getURL() == null) {
			String errorMessage = "no URL has been specified on REST Requestor consumer (" + this.name + ")";
			LOGGER.warn(errorMessage);
			throw new ApexEventException(errorMessage);
		}

		// Check if the HTTP URL is valid
		try {
			new URL(restConsumerProperties.getURL());
		}
		catch (Exception e) {
			String errorMessage = "invalid URL has been specified on REST Requestor consumer (" + this.name + ")";
			LOGGER.warn(errorMessage);
			throw new ApexEventException(errorMessage, e);
		}

		// Set the peer timeout to the default value if its not set
		if (consumerParameters.getPeerTimeout(EventHandlerPeeredMode.REQUESTOR) != 0) {
			restRequestTimeout = consumerParameters.getPeerTimeout(EventHandlerPeeredMode.REQUESTOR);
		}

		// Initialize the HTTP client
		client = ClientBuilder.newClient();
	}

	/**
	 * Receive an incoming REST request from the synchronized REST Requestor producer and queue it
	 * @param restRequest the incoming rest request to queue
	 * @throws ApexEventRuntimeException on queueing errors
	 */
	public void processRestRequest(final ApexRestRequest restRequest) {
		// Push the event onto the queue for handling
		try {
			incomingRestRequestQueue.add(restRequest);
		}
		catch (final Exception e) {
			String errorMessage = "could not queue request \"" + restRequest + "\" on REST Requestor consumer (" + this.name + ")";
			LOGGER.warn(errorMessage);
			throw new ApexEventRuntimeException(errorMessage);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ericsson.apex.service.engine.event.ApexEventConsumer#start()
	 */
	@Override
	public void start() {
		// Configure and start the event reception thread
		final String threadName = this.getClass().getName() + ":" + this.name;
		consumerThread = new ApplicationThreadFactory(threadName).newThread(this);
		consumerThread.setDaemon(true);
		consumerThread.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ericsson.apex.service.engine.event.ApexEventConsumer#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Get the number of events received to date
	 * @return the number of events received
	 */
	public int getEventsReceived() {
		return eventsReceived;
	}

	/* (non-Javadoc)
	 * @see com.ericsson.apex.service.engine.event.ApexEventConsumer#getPeeredReference(com.ericsson.apex.service.parameters.eventhandler.EventHandlerPeeredMode)
	 */
	@Override
	public PeeredReference getPeeredReference(EventHandlerPeeredMode peeredMode) {
		return peerReferenceMap.get(peeredMode);
	}

	/* (non-Javadoc)
	 * @see com.ericsson.apex.service.engine.event.ApexEventConsumer#setPeeredReference(com.ericsson.apex.service.parameters.eventhandler.EventHandlerPeeredMode, com.ericsson.apex.service.engine.event.PeeredReference)
	 */
	@Override
	public void setPeeredReference(EventHandlerPeeredMode peeredMode, PeeredReference peeredReference) {
		peerReferenceMap.put(peeredMode, peeredReference);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// The endless loop that receives events using REST calls
		while (consumerThread.isAlive() && !stopOrderedFlag) {
			try {
				// Take the next event from the queue
				final ApexRestRequest restRequest = incomingRestRequestQueue.poll(REST_REQUESTOR_WAIT_SLEEP_TIME, TimeUnit.MILLISECONDS);
				if (restRequest == null) {
					// Poll timed out, check for request timeouts
					timeoutExpiredRequests();
					continue;
				}

				// Set the time stamp of the REST request
				restRequest.setTimestamp(System.currentTimeMillis());

				// Create a thread to process the REST request and place it on the map of ongoing requests
				RestRequestRunner restRequestRunner = new RestRequestRunner(restRequest);
				ongoingRestRequestMap.put(restRequest, restRequestRunner);

				// Start execution of the request
				Thread restRequestRunnerThread = new Thread(restRequestRunner);
				restRequestRunnerThread.setName("RestRequestRunner_" + nextRequestRunnerThreadNo);
				restRequestRunnerThread.start();
			}
			catch (final InterruptedException e) {
				LOGGER.debug("Thread interrupted, Reason {}", e.getMessage());
				Thread.currentThread().interrupt();
			}
		}

		client.close();
	}

	/**
	 * This method times out REST requests that have expired
	 */
	private void timeoutExpiredRequests() {
		// Hold a list of timed out requests
		List<ApexRestRequest> timedoutRequestList = new ArrayList<>();

		// Check for timeouts
		for (Entry<ApexRestRequest, RestRequestRunner> requestEntry : ongoingRestRequestMap.entrySet()) {
			if (System.currentTimeMillis() - requestEntry.getKey().getTimestamp() > restRequestTimeout) {
				requestEntry.getValue().stop();
				timedoutRequestList.add(requestEntry.getKey());
			}
		}

		// Interrupt timed out requests and remove them from the ongoing map
		for (ApexRestRequest timedoutRequest : timedoutRequestList) {
			String errorMessage = "REST Requestor consumer (" + this.name + "), REST request timed out: " + timedoutRequest;
			LOGGER.warn(errorMessage);

			ongoingRestRequestMap.remove(timedoutRequest);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.apps.uservice.producer.ApexEventConsumer#stop()
	 */
	@Override
	public void stop() {
		stopOrderedFlag = true;

		while (consumerThread.isAlive()) {
			ThreadUtilities.sleep(REST_REQUESTOR_WAIT_SLEEP_TIME);
		}
	}

	/**
	 * This class is used to start a thread for each request issued.
	 * 
	 * @author Liam Fallon (liam.fallon@ericsson.com)
	 */
	private class RestRequestRunner implements Runnable {
		private static final String APPLICATION_JSON = "application/json";

		// The REST request being processed by this thread
		private final ApexRestRequest request;

		// The thread executing the REST request
		private Thread restRequestThread;

		/**
		 * Constructor, initialise the request runner with the request
		 * @param request the request this runner will issue
		 */
		private RestRequestRunner(final ApexRestRequest request) {
			this.request = request;
		}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			// Get the thread for the request
			restRequestThread = Thread.currentThread();

			try {
				// Execute the REST request
				Response response = sendEventAsRESTRequest();

				// Check that the event request worked
				if (response.getStatus() != Response.Status.OK.getStatusCode()) {
					String errorMessage = "reception of response to \"" + request + "\" from URL \""
							+ restConsumerProperties.getURL() + "\" failed with status code "
							+ response.getStatus() + " and message \"" + response.readEntity(String.class) + "\"";
					throw new ApexEventRuntimeException(errorMessage);
				}

				// Get the event we received
				String eventJSONString = response.readEntity(String.class);

				// Check there is content
				if (eventJSONString == null || eventJSONString.trim().length() == 0) {
					String errorMessage = "received an enpty response to \"" + request + "\" from URL \"" + restConsumerProperties.getURL() + "\"";
					throw new ApexEventRuntimeException(errorMessage);
				}

				// Send the event into Apex
				eventReceiver.receiveEvent(name + "_" + eventsReceived, eventJSONString);

				synchronized (eventsReceivedLock) {
					eventsReceived++;
				}
			}
			catch (final Exception e) {
				LOGGER.warn("error receiving events on thread {}", consumerThread.getName(), e);
			}
			finally {
				// Remove the request from the map of ongoing requests
				ongoingRestRequestMap.remove(request);
			}
		}

		/**
		 * Stop the REST request 
		 */
		private void stop() {
			restRequestThread.interrupt();
		}

		/**
		 * Execute the REST request.
		 *
		 * @return the response to the REST request
		 */
		public Response sendEventAsRESTRequest() {
			switch (restConsumerProperties.getHttpMethod()) {
			case GET:
				return client.target(restConsumerProperties.getURL()).request(APPLICATION_JSON).get();

			case PUT:
				return client.target(restConsumerProperties.getURL()).request(APPLICATION_JSON).put(Entity.json(request.getEvent()));

			case POST:
				return client.target(restConsumerProperties.getURL()).request(APPLICATION_JSON).post(Entity.json(request.getEvent()));

			case DELETE:
				return client.target(restConsumerProperties.getURL()).request(APPLICATION_JSON).delete();
			}

			return null;
		}
	}
}
