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
import java.util.Random;

import com.ericsson.apex.core.engine.executor.context.TaskExecutionContext;
import com.ericsson.apex.model.basicmodel.concepts.ApexException;

/**
 * The Class DefaultTask_Logic is default task logic in Java.
 */
//CHECKSTYLE:OFF: checkstyle:typeNames
public class DefaultTask_Logic {
// CHECKSTYLE:ON: checkstyle:typeNames
    private static final int BOUND_FOR_RANDOM_INT = 4;

   /**
     * Gets the event.
     *
     * @param executor the executor
     * @return the event
     * @throws ApexException the apex exception
     */
    public boolean getEvent(final TaskExecutionContext executor) throws ApexException {
        executor.logger.debug(executor.subject.getId());
        executor.logger.debug(executor.getContextAlbum("GlobalContextAlbum").getName());
        executor.logger.debug(executor.inFields.toString());

        final Date timeNow = new Date();
        final Random rand = new Random();

        if (executor.inFields.containsKey("TestDecideCaseSelected")) {
            executor.outFields.put("TestActCaseSelected", new Byte((byte) rand.nextInt(BOUND_FOR_RANDOM_INT)));
            executor.outFields.put("TestActStateTime", timeNow.getTime());
        }
        else if (executor.inFields.containsKey("TestEstablishCaseSelected")) {
            executor.outFields.put("TestDecideCaseSelected", new Byte((byte) rand.nextInt(BOUND_FOR_RANDOM_INT)));
            executor.outFields.put("TestDecideStateTime", timeNow.getTime());
        }
        else if (executor.inFields.containsKey("TestMatchCaseSelected")) {
            executor.outFields.put("TestEstablishCaseSelected", new Byte((byte) rand.nextInt(BOUND_FOR_RANDOM_INT)));
            executor.outFields.put("TestEstablishStateTime", timeNow.getTime());
        }
        else {
            executor.outFields.put("TestMatchCaseSelected", new Byte((byte) rand.nextInt(BOUND_FOR_RANDOM_INT)));
            executor.outFields.put("TestMatchStateTime", timeNow.getTime());
        }

        return true;
    }
}
