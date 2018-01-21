/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.adaptive.model.java;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.ericsson.apex.context.ContextException;
import com.ericsson.apex.core.engine.executor.context.TaskSelectionExecutionContext;
import com.ericsson.apex.domains.adaptive.concepts.AutoLearn;

/**
 * The Class AutoLearnPolicy_Decide_TaskSelectionLogic.
 */
// CHECKSTYLE:OFF: checkstyle:typeName
public class AutoLearnPolicy_Decide_TaskSelectionLogic {
 // CHECKSTYLE:ON: checkstyle:typeName
    private static final Random RAND = new Random(System.currentTimeMillis());
    private static final double WANT = 50.0;
    private int size;

    /**
     * Gets the task.
     *
     * @param executor the executor
     * @return the task
     */
    public boolean getTask(final TaskSelectionExecutionContext executor) {
        executor.logger.debug(executor.subject.getId());
        executor.logger.debug(executor.inFields.toString());
        final List<String> tasks = executor.subject.getTaskNames();
        size = tasks.size();

        try {
            executor.getContextAlbum("AutoLearnAlbum").lockForWriting("AutoLearn");
        }
        catch (final ContextException e) {
            executor.logger.error("Failed to acquire write lock on \"autoLearn\" context", e);
            return false;
        }

        // Get the context object
        AutoLearn autoLearn = (AutoLearn) executor.getContextAlbum("AutoLearnAlbum").get("AutoLearn");
        if (autoLearn == null) {
            autoLearn = new AutoLearn();
        }

        // Check the lists are initialized
        if (!autoLearn.isInitialized()) {
            autoLearn.init(size);
        }

        final double now = (Double) (executor.inFields.get("MonitoredValue"));
        final double diff = now - WANT;
        final int option = getOption(diff, autoLearn);
        learn(option, diff, autoLearn);

        executor.getContextAlbum("AutoLearnAlbum").put("AutoLearnAlbum", autoLearn);

        try {
            executor.getContextAlbum("AutoLearnAlbum").unlockForWriting("AutoLearn");
        }
        catch (final ContextException e) {
            executor.logger.error("Failed to acquire write lock on \"autoLearn\" context", e);
            return false;
        }

        executor.subject.getTaskKey(tasks.get(option)).copyTo(executor.selectedTask);
        return true;
    }

    /**
     * Gets the option.
     *
     * @param diff the diff
     * @param autoLearn the auto learn
     * @return the option
     */
    private int getOption(final double diff, final AutoLearn autoLearn) {
        final Double[] avdiffs = autoLearn.getAvDiffs().toArray(new Double[autoLearn.getAvDiffs().size()]);
        final int r = RAND.nextInt(size);
        int closestupi = -1;
        int closestdowni = -1;
        double closestup = Double.MAX_VALUE;
        double closestdown = Double.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            if (Double.isNaN(avdiffs[i])) {
                return r;
            }
            if (avdiffs[i] >= diff && avdiffs[i] <= closestup) {
                closestup = avdiffs[i];
                closestupi = i;
            }
            if (avdiffs[i] <= diff && avdiffs[i] >= closestdown) {
                closestdown = avdiffs[i];
                closestdowni = i;
            }
        }
        if (closestupi == -1 || closestdowni == -1) {
            return r;
        }
        if (closestupi == closestdowni) {
            return closestupi;
        }
        if (Math.abs(closestdown - diff) > Math.abs(closestup - diff)) {
            return closestupi;
        }
        else {
            return closestdowni;
        }
    }

    /**
     * Learn.
     *
     * @param option the option
     * @param diff the diff
     * @param autoLearn the auto learn
     */
    private void learn(final int option, final double diff, final AutoLearn autoLearn) {
        final Double[] avdiffs = autoLearn.getAvDiffs().toArray(new Double[autoLearn.getAvDiffs().size()]);
        final Long[] counts = autoLearn.getCounts().toArray(new Long[autoLearn.getCounts().size()]);
        if (option < 0 || option >= avdiffs.length) {
            throw new IllegalArgumentException("Error: option" + option);
        }
        counts[option]++;
        if (Double.isNaN(avdiffs[option])) {
            avdiffs[option] = diff;
        }
        else {
            avdiffs[option] = (avdiffs[option] * (counts[option] - 1) + diff) / counts[option];
        }
        autoLearn.setAvDiffs(Arrays.asList(avdiffs));
        autoLearn.setCounts(Arrays.asList(counts));
    }
}
