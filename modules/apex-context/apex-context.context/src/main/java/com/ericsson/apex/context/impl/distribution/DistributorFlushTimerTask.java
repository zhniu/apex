/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.context.impl.distribution;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.context.Distributor;
import com.ericsson.apex.context.parameters.PersistorParameters;
import com.ericsson.apex.model.basicmodel.service.ParameterService;

/**
 * This class is used to periodically flush a context distributor.
 *
 * @author eeilfn
 */
public class DistributorFlushTimerTask extends TimerTask {
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(DistributorFlushTimerTask.class);

    // The timer for flushing
    private Timer timer = null;

    // The context distributor to flush
    private final Distributor contextDistributor;

    // Timing information
    private long period = 0;
    private long flushCount = 0;

    /**
     * Constructor, save a reference to the event stream handler.
     *
     * @param contextDistributor the distributor that this timer task is flushing
     * @throws ContextException On flush setup errors
     */
    public DistributorFlushTimerTask(final Distributor contextDistributor) throws ContextException {
        // Save the context distributor and period
        this.contextDistributor = contextDistributor;

        // Set the period for persistence flushing
        final PersistorParameters persistorParameters = ParameterService.getParameters(PersistorParameters.class);
        period = persistorParameters.getFlushPeriod();

        // Set up the timer
        timer = new Timer(DistributorFlushTimerTask.class.getSimpleName(), true);
        timer.schedule(this, period, period);

        LOGGER.debug("context distributor " + contextDistributor.getKey().getID() + " flushing set up with interval: " + period + "ms");
    }

    /**
     * Flush the context distributor.
     */
    @Override
    public void run() {
        // Increment the flush counter
        flushCount++;

        LOGGER.debug("context distributor " + contextDistributor.getKey().getID() + " flushing: period=" + period + ": count=" + flushCount);
        try {
            contextDistributor.flush();
            LOGGER.debug("context distributor " + contextDistributor.getKey().getID() + " flushed: period=" + period + ": count=" + flushCount);
        }
        catch (final ContextException e) {
            LOGGER.error("flush error on context distributor " + contextDistributor.getKey().getID() + ": period=" + period + ": count=" + flushCount, e);
        }
    }

    /**
     * Cancel the timer.
     *
     * @return true, if cancel
     */
    @Override
    public boolean cancel() {
        // Cancel the timer
        if (timer != null) {
            timer.cancel();
        }
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ContextDistributorFlushTimerTask [period=" + period + ", flushCount=" + flushCount + "]";
    }
}
