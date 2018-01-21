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

import java.util.Date;

import com.ericsson.apex.core.engine.executor.context.TaskExecutionContext;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;

/**
 * The Class EvalTask_Logic is default evaluation task logic in Java.
 */
//CHECKSTYLE:OFF: checkstyle:typeNames
public class EvalTask_Logic {
 // CHECKSTYLE:ON: checkstyle:typeNames
    private static int stateNo = 0;

    /**
     * Gets the event.
     *
     * @param executor the executor
     * @return the event
     * @throws ApexException the apex exception
     */
    public boolean getEvent(final TaskExecutionContext executor) throws ApexException {
        executor.logger.debug(executor.subject.getId());
        executor.logger.debug(executor.inFields.toString());
        System.err.println(executor.inFields);
        executor.outFields.putAll(executor.inFields);

        final Date timeNow = new Date();
        executor.outFields.put("State" + (stateNo + 1) + "Timestamp", timeNow.getTime());
        executor.logger.debug(executor.outFields.toString());
        stateNo++;
        return true;
    }
}
