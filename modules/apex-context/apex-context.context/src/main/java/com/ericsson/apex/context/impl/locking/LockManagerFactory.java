/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.context.impl.locking;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.context.LockManager;
import com.ericsson.apex.context.parameters.LockManagerParameters;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.service.ParameterService;

/**
 * This class returns a {@link LockManager} for the particular type of locking mechanism that has been configured for use.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class LockManagerFactory {
    // Get a reference to the logger
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(LockManagerFactory.class);

    /**
     * Return a {@link LockManager} for the particular type of locking mechanism configured for use.
     *
     * @param key The key for the lock manager
     * @return a lock manager that can generate locks using some underlying mechanism
     * @throws ContextException on errors in getting a lock manager
     */
    public LockManager createLockManager(final AxArtifactKey key) throws ContextException {
        LOGGER.entry("Lock Manager factory, key=" + key);

        final LockManagerParameters lockManagerParameters = ParameterService.getParameters(LockManagerParameters.class);

        // Get the class for the lock manager using reflection
        Object lockManagerObject = null;
        final String pluginClass = lockManagerParameters.getPluginClass();
        try {
            lockManagerObject = Class.forName(pluginClass).newInstance();
        }
        catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            LOGGER.error("Apex context lock manager class not found for context lock manager plugin \"" + pluginClass + "\"", e);
            throw new ContextException("Apex context lock manager class not found for context lock manager plugin \"" + pluginClass + "\"", e);
        }

        // Check the class is a lock manager
        if (!(lockManagerObject instanceof LockManager)) {
            LOGGER.error("Specified Apex context lock manager plugin class \"" + pluginClass + "\" does not implement the LockManager interface");
            throw new ContextException("Specified Apex context lock manager plugin class \"" + pluginClass + "\" does not implement the LockManager interface");
        }

        // The context lock manager to return
        final LockManager lockManager = (LockManager) lockManagerObject;

        // Lock and load (OK sorry!!!) the lock manager
        lockManager.init(key);

        LOGGER.exit("Lock manager factory, key=" + key + ", selected lock manager of class " + lockManager.getClass());
        return lockManager;
    }
}
