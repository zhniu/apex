/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.engine.runtime.impl;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.core.infrastructure.threading.ApplicationThreadFactory;
import com.ericsson.apex.core.infrastructure.threading.ThreadUtilities;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.handling.ApexModelException;
import com.ericsson.apex.model.basicmodel.handling.ApexModelReader;
import com.ericsson.apex.model.basicmodel.service.ModelService;
import com.ericsson.apex.model.enginemodel.concepts.AxEngineState;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;
import com.ericsson.apex.service.engine.event.ApexEvent;
import com.ericsson.apex.service.engine.event.ApexPeriodicEventGenerator;
import com.ericsson.apex.service.engine.runtime.ApexEventListener;
import com.ericsson.apex.service.engine.runtime.EngineService;
import com.ericsson.apex.service.engine.runtime.EngineServiceEventInterface;
import com.ericsson.apex.service.parameters.engineservice.EngineServiceParameters;

/**
 * The Class EngineServiceImpl controls a thread pool that runs a set of Apex engine workers, each of which is running on an identical Apex model. This class
 * handles the management of the engine worker instances, their threads, and event forwarding to and from the engine workers.
 *
 * @author Sajeevan Achuthan (sajeevan.achuthan@ericsson.com)
 * @author Liam Fallon (liam.fallon@ericsson.com)
 * @author John Keeney (john.keeney@ericsson.com)
 */
public final class EngineServiceImpl implements EngineService, EngineServiceEventInterface {
	// Logging static variables
	private static final XLogger LOGGER = XLoggerFactory.getXLogger(EngineServiceImpl.class);
	private static final boolean DEBUG_ENABLED = LOGGER.isDebugEnabled();

	// Constants for timing
	private static final long MAX_START_WAIT_TIME = 5000; // 5 seconds
	private static final long MAX_STOP_WAIT_TIME = 5000; // 5 seconds
	private static final int  ENGINE_SERVICE_STOP_START_WAIT_INTERVAL = 200;

	// The ID of this engine
	private AxArtifactKey engineServiceKey = null;

	// The Apex engine workers this engine service is handling
	private final Map<AxArtifactKey, EngineService> engineWorkerMap = Collections.synchronizedMap(new LinkedHashMap<AxArtifactKey, EngineService>());

	// Event queue for events being sent into the Apex engines, it used by all engines within a group.
	private final BlockingQueue<ApexEvent> queue = new LinkedBlockingQueue<>();

	// Thread factory for thread management
	private final ApplicationThreadFactory tFactory = new ApplicationThreadFactory("apex-engine-service", 512);

	// Periodic event generator and its period in milliseconds
	private ApexPeriodicEventGenerator periodicEventGenerator = null;
	private long periodicEventPeriod;

	/**
	 * This constructor instantiates engine workers and adds them to the set of engine workers to be managed. The constructor is private to prevent subclassing.
	 *
	 * @param engineServiceKey the engine service key
	 * @param incomingThreadCount the thread count, the number of engine workers to start
	 * @param periodicEventPeriod the period in milliseconds at which periodic events are generated
	 * @throws ApexException on worker instantiation errors
	 */
	private EngineServiceImpl(final AxArtifactKey engineServiceKey, final int incomingThreadCount, final long periodicEventPeriod) throws ApexException {
		LOGGER.entry(engineServiceKey, incomingThreadCount);

		this.engineServiceKey = engineServiceKey;
		this.periodicEventPeriod = periodicEventPeriod;

		int threadCount = incomingThreadCount;
		if (threadCount <= 0) {
			// Just start one engine worker
			threadCount = 1;
		}

		// Start engine workers
		for (int engineCounter = 0; engineCounter < threadCount; engineCounter++) {
			final AxArtifactKey engineWorkerKey = new AxArtifactKey(engineServiceKey.getName() + '-' + engineCounter, engineServiceKey.getVersion());
			engineWorkerMap.put(engineWorkerKey, new EngineWorker(engineWorkerKey, queue, tFactory));
			LOGGER.info("Created apex engine {} .", engineWorkerKey.getID());
		}

		LOGGER.info("APEX service created.");
		LOGGER.exit();
	}

	/**
	 * Create an Apex Engine Service instance. This method is deprecated and will be removed in the next version.
	 *
	 * @param engineServiceKey the engine service key
	 * @param threadCount the thread count, the number of engine workers to start
	 * @return the Engine Service instance
	 * @throws ApexException on worker instantiation errors
	 * @deprecated Do not use this version. Use {@link #create(EngineServiceParameters)}
	 */
	@Deprecated
	public static EngineServiceImpl create(final AxArtifactKey engineServiceKey, final int threadCount) throws ApexException {
		// Check if the Apex model specified is sane
		if (engineServiceKey == null) {
			LOGGER.warn("engine service key is null");
			throw new ApexException("engine service key is null");
		}
		return new EngineServiceImpl(engineServiceKey, threadCount, 0);
	}

	/**
	 * Create an Apex Engine Service instance. This method does not load the policy so {@link #updateModel(AxArtifactKey, AxPolicyModel, boolean)} or
	 * {@link #updateModel(AxArtifactKey, AxPolicyModel, boolean)} must be used to load a model. This method does not start the Engine Service so
	 * {@link #start(AxArtifactKey)} or {@link #startAll()} must be used.
	 *
	 * @param config the configuration for this Apex Engine Service.
	 * @return the Engine Service instance
	 * @throws ApexException on worker instantiation errors
	 */
	public static EngineServiceImpl create(final EngineServiceParameters config) throws ApexException {
		if (config == null) {
			LOGGER.warn("Engine service configuration parameters is null");
			throw new ApexException("engine service configuration parameters is null");
		}
		String validation = config.validate();
		if (validation != null && validation.length() > 0) {
			LOGGER.warn("Invalid engine service configuration parameters: " + validation);
			throw new ApexException("Invalid engine service configuration parameters: " + validation);
		}
		final AxArtifactKey engineServiceKey = config.getEngineKey();
		final int threadCount = config.getInstanceCount();

		// Check if the Apex model specified is sane
		if (engineServiceKey == null) {
			LOGGER.warn("engine service key is null");
			throw new ApexException("engine service key is null");
		}

		return new EngineServiceImpl(engineServiceKey, threadCount, config.getPeriodicEventPeriod());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ericsson.apex.service.engine.runtime.EngineService#registerActionListener(java.lang.String,
	 * com.ericsson.apex.service.engine.runtime.ApexEventListener)
	 */
	@Override
	public void registerActionListener(final String listenerName, final ApexEventListener apexEventListener) {
		LOGGER.entry(apexEventListener);

		// Register the Apex event listener on all engine workers, each worker will return Apex events to the listening application
		for (final EngineService engineWorker : engineWorkerMap.values()) {
			engineWorker.registerActionListener(listenerName, apexEventListener);
		}

		LOGGER.info("Added the action listener to the engine");
		LOGGER.exit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ericsson.apex.service.engine.runtime.EngineService#deregisterActionListener(java.lang.String)
	 */
	@Override
	public void deregisterActionListener(final String listenerName) {
		LOGGER.entry(listenerName);

		// Register the Apex event listener on all engine workers, each worker will return Apex events to the listening application
		for (final EngineService engineWorker : engineWorkerMap.values()) {
			engineWorker.deregisterActionListener(listenerName);
		}

		LOGGER.info("Removed the action listener from the engine");
		LOGGER.exit();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.service.engine.runtime.EngineService#getEngineServiceEventInterface()
	 */
	@Override
	public EngineServiceEventInterface getEngineServiceEventInterface() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.service.engine.runtime.EngineService#getKey()
	 */
	@Override
	public AxArtifactKey getKey() {
		return engineServiceKey;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.service.engine.runtime.EngineService#getInfo()
	 */
	@Override
	public Collection<AxArtifactKey> getEngineKeys() {
		return engineWorkerMap.keySet();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.service.engine.runtime.EngineService#getApexModelKey()
	 */
	@Override
	public AxArtifactKey getApexModelKey() {
		if (engineWorkerMap.size() == 0) {
			return null;
		}

		return engineWorkerMap.entrySet().iterator().next().getValue().getApexModelKey();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.service.engine.runtime.EngineService#updateModel(com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey, java.lang.String,
	 * boolean)
	 */
	@Override
	public void updateModel(final AxArtifactKey incomingEngineServiceKey, final String apexModelString, final boolean forceFlag) throws ApexException {
		// Check if the Apex model specified is sane
		if (apexModelString == null || apexModelString.trim().length() == 0) {
			LOGGER.warn("model for updating on engine service with key " + incomingEngineServiceKey.getID() + " is empty");
			throw new ApexException("model for updating on engine service with key " + incomingEngineServiceKey.getID() + " is empty");
		}

		// Read the Apex model into memory using the Apex Model Reader
		AxPolicyModel apexPolicyModel = null;
		try {
			final ApexModelReader<AxPolicyModel> modelReader = new ApexModelReader<>(AxPolicyModel.class);
			apexPolicyModel = modelReader.read(new ByteArrayInputStream(apexModelString.getBytes()));
		}
		catch (final ApexModelException e) {
			LOGGER.error("failed to unmarshal the apex model on engine service " + incomingEngineServiceKey.getID(), e);
			throw new ApexException("failed to unmarshal the apex model on engine service " + incomingEngineServiceKey.getID(), e);
		}

		if (apexPolicyModel == null) {
			LOGGER.error("apex model null on engine service " + incomingEngineServiceKey.getID());
			throw new ApexException("apex model null on engine service " + incomingEngineServiceKey.getID());
		}

		// Update the model
		updateModel(incomingEngineServiceKey, apexPolicyModel, forceFlag);

		LOGGER.exit();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.service.engine.runtime.EngineService#updateModel(com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey,
	 * com.ericsson.apex.model.policymodel.concepts.AxPolicyModel, boolean)
	 */
	@Override
	public void updateModel(final AxArtifactKey incomingEngineServiceKey, final AxPolicyModel apexModel, final boolean forceFlag) throws ApexException {
		LOGGER.entry(incomingEngineServiceKey);

		// Check if the Apex model specified is sane
		if (apexModel == null) {
			LOGGER.warn("model for updating on engine service with key " + incomingEngineServiceKey.getID() + " is null");
			throw new ApexException("model for updating on engine service with key " + incomingEngineServiceKey.getID() + " is null");
		}

		// Check if the key on the update request is correct
		if (!this.engineServiceKey.equals(incomingEngineServiceKey)) {
			LOGGER.warn("engine service key " + incomingEngineServiceKey.getID() + " does not match the key" + engineServiceKey.getID()
			+ " of this engine service");
			throw new ApexException("engine service key " + incomingEngineServiceKey.getID() + " does not match the key" + engineServiceKey.getID()
			+ " of this engine service");
		}

		// Check model compatibility
		if (ModelService.existsModel(AxPolicyModel.class)) {
			// The current policy model may or may not be defined
			final AxPolicyModel currentModel = ModelService.getModel(AxPolicyModel.class);
			if (!currentModel.getKey().isCompatible(apexModel.getKey())) {
				if (forceFlag) {
					LOGGER.warn("apex model update forced, supplied model with key \"" + apexModel.getKey().getID()
							+ "\" is not a compatible model update from the existing engine model with key \"" + currentModel.getKey().getID() + "\"");
				}
				else {
					throw new ContextException("apex model update failed, supplied model with key \"" + apexModel.getKey().getID()
							+ "\" is not a compatible model update from the existing engine model with key \"" + currentModel.getKey().getID() + "\"");
				}
			}
		}

		final boolean wasstopped = isStopped();

		if (!wasstopped) {
			// Stop all engines on this engine service
			stop();
			final long stoptime = System.currentTimeMillis();
			while (!isStopped() && System.currentTimeMillis() - stoptime < MAX_STOP_WAIT_TIME) {
				ThreadUtilities.sleep(ENGINE_SERVICE_STOP_START_WAIT_INTERVAL);
			}
			// Check if all engines are stopped
			final StringBuilder notStoppedEngineIDBuilder = new StringBuilder();
			for (final Entry<AxArtifactKey, EngineService> engineWorkerEntry : engineWorkerMap.entrySet()) {
				if (engineWorkerEntry.getValue().getState() != AxEngineState.STOPPED) {
					notStoppedEngineIDBuilder.append(engineWorkerEntry.getKey().getID());
					notStoppedEngineIDBuilder.append('(');
					notStoppedEngineIDBuilder.append(engineWorkerEntry.getValue().getState());
					notStoppedEngineIDBuilder.append(") ");
				}
			}
			if (notStoppedEngineIDBuilder.length() > 0) {
				final String errorString = "cannot update model on engine service with key " + incomingEngineServiceKey.getID() + ", engines not stopped after "
						+ MAX_STOP_WAIT_TIME + "ms are: " + notStoppedEngineIDBuilder.toString().trim();
				LOGGER.warn(errorString);
				throw new ApexException(errorString);
			}
		}

		// Update the engines
		for (final Entry<AxArtifactKey, EngineService> engineWorkerEntry : engineWorkerMap.entrySet()) {
			LOGGER.info("Registering apex model on engine {}", engineWorkerEntry.getKey().getID());
			engineWorkerEntry.getValue().updateModel(engineWorkerEntry.getKey(), apexModel, forceFlag);
		}

		if (!wasstopped) {
			// start all engines on this engine service if it was not stopped before the update
			startAll();
			final long starttime = System.currentTimeMillis();
			while (!isStarted() && System.currentTimeMillis() - starttime < MAX_START_WAIT_TIME) {
				ThreadUtilities.sleep(ENGINE_SERVICE_STOP_START_WAIT_INTERVAL);
			}
			// Check if all engines are running
			final StringBuilder notRunningEngineIDBuilder = new StringBuilder();
			for (final Entry<AxArtifactKey, EngineService> engineWorkerEntry : engineWorkerMap.entrySet()) {
				if (engineWorkerEntry.getValue().getState() != AxEngineState.READY && engineWorkerEntry.getValue().getState() != AxEngineState.EXECUTING) {
					notRunningEngineIDBuilder.append(engineWorkerEntry.getKey().getID());
					notRunningEngineIDBuilder.append('(');
					notRunningEngineIDBuilder.append(engineWorkerEntry.getValue().getState());
					notRunningEngineIDBuilder.append(") ");
				}
			}
			if (notRunningEngineIDBuilder.length() > 0) {
				final String errorString = "engine start error on model update on engine service with key " + incomingEngineServiceKey.getID()
				+ ", engines not running are: " + notRunningEngineIDBuilder.toString().trim();
				LOGGER.warn(errorString);
				throw new ApexException(errorString);
			}
		}

		LOGGER.exit();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.service.engine.runtime.EngineService#getState()
	 */
	@Override
	public AxEngineState getState() {
		// If one worker is running then we are running, otherwise we are stopped
		for (final EngineService engine : engineWorkerMap.values()) {
			if (engine.getState() != AxEngineState.STOPPED) {
				return AxEngineState.EXECUTING;
			}
		}

		return AxEngineState.STOPPED;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.service.engine.runtime.EngineService#startAll()
	 */
	@Override
	public void startAll() throws ApexException {
		for (final EngineService engine : engineWorkerMap.values()) {
			start(engine.getKey());
		}

		// Check if periodic events should be turned on
		if (periodicEventPeriod > 0) {
			startPeriodicEvents(periodicEventPeriod);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.service.engine.runtime.EngineService#start(com.ericsson.apex.core.model.concepts.AxArtifactKey)
	 */
	@Override
	public void start(final AxArtifactKey engineKey) throws ApexException {
		LOGGER.entry(engineKey);

		// Check if we have this key on our map
		if (!engineWorkerMap.containsKey(engineKey)) {
			LOGGER.warn("engine with key " + engineKey.getID() + " not found in engine service");
			throw new ApexException("engine with key " + engineKey.getID() + " not found in engine service");
		}

		// Start the engine
		engineWorkerMap.get(engineKey).start(engineKey);

		LOGGER.exit(engineKey);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.service.engine.runtime.EngineService#stop()
	 */
	@Override
	public void stop() throws ApexException {
		LOGGER.entry();

		// Stop each engine
		for (final EngineService engine : engineWorkerMap.values()) {
			if (engine.getState() != AxEngineState.STOPPED) {
				engine.stop();
			}
		}

		LOGGER.exit();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.service.engine.runtime.EngineService#stop(com.ericsson.apex.core.model.concepts.AxArtifactKey)
	 */
	@Override
	public void stop(final AxArtifactKey engineKey) throws ApexException {
		LOGGER.entry(engineKey);

		// Check if we have this key on our map
		if (!engineWorkerMap.containsKey(engineKey)) {
			LOGGER.warn("engine with key " + engineKey.getID() + " not found in engine service");
			throw new ApexException("engine with key " + engineKey.getID() + " not found in engine service");
		}

		// Stop the engine
		engineWorkerMap.get(engineKey).stop(engineKey);

		LOGGER.exit(engineKey);
	}

	/**
	 * Check all engines are started.
	 *
	 * @return true if <i>all</i> engines are started
	 * @see com.ericsson.apex.service.engine.runtime.EngineService#isStarted()
	 */
	@Override
	public boolean isStarted() {
		for (final EngineService engine : engineWorkerMap.values()) {
			if (!engine.isStarted()) {
				return false;
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.service.engine.runtime.EngineService#isStarted(com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey)
	 */
	@Override
	public boolean isStarted(final AxArtifactKey engineKey) {
		// Check if we have this key on our map
		if (!engineWorkerMap.containsKey(engineKey)) {
			LOGGER.warn("engine with key " + engineKey.getID() + " not found in engine service");
		}
		return engineWorkerMap.get(engineKey).isStarted();
	}

	/**
	 * Check all engines are stopped.
	 *
	 * @return true if <i>all</i> engines are stopped
	 * @see com.ericsson.apex.service.engine.runtime.EngineService#isStopped()
	 */
	@Override
	public boolean isStopped() {
		for (final EngineService engine : engineWorkerMap.values()) {
			if (!engine.isStopped()) {
				return false;
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.service.engine.runtime.EngineService#isStopped(com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey)
	 */
	@Override
	public boolean isStopped(final AxArtifactKey engineKey) {
		// Check if we have this key on our map
		if (!engineWorkerMap.containsKey(engineKey)) {
			LOGGER.warn("engine with key " + engineKey.getID() + " not found in engine service");
		}
		return engineWorkerMap.get(engineKey).isStopped();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.service.engine.runtime.EngineService#startPeriodicEvents(long)
	 */
	@Override
	public void startPeriodicEvents(final long period) throws ApexException {
		// Check if periodic events are already started
		if (periodicEventGenerator != null) {
			LOGGER.warn("Peiodic event geneation already running on engine " + engineServiceKey.getID() + ", " + periodicEventGenerator.toString());
			throw new ApexException("Peiodic event geneation already running on engine " + engineServiceKey.getID() + ", " + periodicEventGenerator.toString());
		}

		// Set up periodic event execution, its a Java Timer/TimerTask
		periodicEventGenerator = new ApexPeriodicEventGenerator(this.getEngineServiceEventInterface(), period);
		
		// Record the periodic event period because it may have been set over the Web Socket admin interface
		this.periodicEventPeriod = period;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.service.engine.runtime.EngineService#stopPeriodicEvents()
	 */
	@Override
	public void stopPeriodicEvents() throws ApexException {
		// Check if periodic events are already started
		if (periodicEventGenerator == null) {
			LOGGER.warn("Peiodic event geneation not running on engine " + engineServiceKey.getID());
			throw new ApexException("Peiodic event geneation not running on engine " + engineServiceKey.getID());
		}

		// Stop periodic events
		periodicEventGenerator.cancel();
		periodicEventGenerator = null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.service.engine.runtime.EngineService#getStatus(com.ericsson.apex.core.model.concepts.AxArtifactKey)
	 */
	@Override
	public String getStatus(final AxArtifactKey engineKey) throws ApexException {
		// Check if we have this key on our map
		if (!engineWorkerMap.containsKey(engineKey)) {
			LOGGER.warn("engine with key " + engineKey.getID() + " not found in engine service");
			throw new ApexException("engine with key " + engineKey.getID() + " not found in engine service");
		}

		// Return the information for this worker
		return engineWorkerMap.get(engineKey).getStatus(engineKey);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.service.engine.runtime.EngineService#getRuntimeInfo(com.ericsson.apex.core.model.concepts.AxArtifactKey)
	 */
	@Override
	public String getRuntimeInfo(final AxArtifactKey engineKey) throws ApexException {
		// Check if we have this key on our map
		if (!engineWorkerMap.containsKey(engineKey)) {
			LOGGER.warn("engine with key " + engineKey.getID() + " not found in engine service");
			throw new ApexException("engine with key " + engineKey.getID() + " not found in engine service");
		}

		// Return the information for this worker
		return engineWorkerMap.get(engineKey).getRuntimeInfo(engineKey);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.service.engine.runtime.EngineServiceEventInterface#sendEvent(com.ericsson.apex.service.engine.event.ApexEvent)
	 */
	@Override
	public void sendEvent(final ApexEvent event) {
		// Check if we have this key on our map
		if (getState() == AxEngineState.STOPPED) {
			LOGGER.warn("event " + event.getName() + " not processed, no engines on engine service " + engineServiceKey.getID() + " are running");
			return;
		}

		if (event == null) {
			LOGGER.warn("Null events cannot be processed, in engine service " + engineServiceKey.getID());
			return;
		}

		if (DEBUG_ENABLED) {
			LOGGER.debug("Forwarding Apex Event {} to the processing engine", event);
		}

		// Add the incoming event to the queue, the next available worker will process it
		queue.add(event);
	}
}
