/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.executor.testdomain.model.java;

import com.ericsson.apex.core.engine.executor.context.TaskSelectionExecutionContext;

/**
 * The Class DefaultState_Logic is default task selection logic in Java.
 */
// CHECKSTYLE:OFF: checkstyle:typeNames
public class DefaultState_Logic {
 // CHECKSTYLE:ON: checkstyle:typeNames
    /**
     * Gets the task.
     *
     * @param executor the executor
     * @return the task
     */
    public boolean getTask(final TaskSelectionExecutionContext executor) {
        executor.logger.debug(executor.subject.getId());
        executor.logger.debug(executor.getContextAlbum("GlobalContextAlbum").getName());
        executor.subject.getDefaultTaskKey().copyTo(executor.selectedTask);
        return true;
    }
}
