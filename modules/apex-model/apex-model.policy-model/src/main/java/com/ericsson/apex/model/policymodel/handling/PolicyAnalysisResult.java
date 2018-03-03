/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.policymodel.handling;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxKey;
import com.ericsson.apex.model.contextmodel.concepts.AxContextAlbum;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;

/**
 * This class finds and holds the usage of context schemas, context albums, events, and tasks by the policies in a policy model.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class PolicyAnalysisResult {
    // Usage of context schemas
    private final Map<AxArtifactKey, Set<AxKey>> contextSchemaUsage = new TreeMap<>();

    // Usage of context maps
    private final Map<AxArtifactKey, Set<AxKey>> contextAlbumUsage = new TreeMap<>();

    // Usage of events
    private final Map<AxArtifactKey, Set<AxKey>> eventUsage = new TreeMap<>();

    // Usage of tasks
    private final Map<AxArtifactKey, Set<AxKey>> taskUsage = new TreeMap<>();

    /**
     * This constructor creates a {@link PolicyAnalysisResult} instance that holds maps that contain the usage of context schemas, contxt albums, events, and
     * tasks by all policies in a policy model.
     *
     * @param policyModel the policy model to analyse
     */
    public PolicyAnalysisResult(final AxPolicyModel policyModel) {
        for (final AxArtifactKey contextSchemaKey : policyModel.getSchemas().getSchemasMap().keySet()) {
            contextSchemaUsage.put(contextSchemaKey, new TreeSet<AxKey>());
        }

        for (final Entry<AxArtifactKey, AxContextAlbum> contextAlbumEntry : policyModel.getAlbums().getAlbumsMap().entrySet()) {
            contextAlbumUsage.put(contextAlbumEntry.getKey(), new TreeSet<AxKey>());
        }

        for (final AxArtifactKey eventKey : policyModel.getEvents().getEventMap().keySet()) {
            eventUsage.put(eventKey, new TreeSet<AxKey>());
        }

        for (final AxArtifactKey taskKey : policyModel.getTasks().getTaskMap().keySet()) {
            taskUsage.put(taskKey, new TreeSet<AxKey>());
        }
    }

    /**
     * Gets the context schemas used by policies in the policy model.
     *
     * @return the context schemas used by policies in the policy model
     */
    public Map<AxArtifactKey, Set<AxKey>> getContextSchemaUsage() {
        return contextSchemaUsage;
    }

    /**
     * Gets the context albums used by policies in the policy model.
     *
     * @return the context albums used by policies in the policy model
     */
    public Map<AxArtifactKey, Set<AxKey>> getContextAlbumUsage() {
        return contextAlbumUsage;
    }

    /**
     * Gets the events used by policies in the policy model.
     *
     * @return the events used by policies in the policy model
     */
    public Map<AxArtifactKey, Set<AxKey>> getEventUsage() {
        return eventUsage;
    }

    /**
     * Gets the tasks used by policies in the policy model.
     *
     * @return the tasks used by policies in the policy model
     */
    public Map<AxArtifactKey, Set<AxKey>> getTaskUsage() {
        return taskUsage;
    }

    /**
     * Gets the context schemas used by policies in the policy model.
     *
     * @return the context schemas used by policies in the policy model
     */
    public Set<AxArtifactKey> getUsedContextSchemas() {
        return getUsedKeySet(contextSchemaUsage);
    }

    /**
     * Gets the context albums used by policies in the policy model.
     *
     * @return the context albums used by policies in the policy model
     */
    public Set<AxArtifactKey> getUsedContextAlbums() {
        return getUsedKeySet(contextAlbumUsage);
    }

    /**
     * Gets the events used by policies in the policy model.
     *
     * @return the events used by policies in the policy model
     */
    public Set<AxArtifactKey> getUsedEvents() {
        return getUsedKeySet(eventUsage);
    }

    /**
     * Gets the tasks used by policies in the policy model.
     *
     * @return the tasks used by policies in the policy model
     */
    public Set<AxArtifactKey> getUsedTasks() {
        return getUsedKeySet(taskUsage);
    }

    /**
     * Gets the context schemas in the policy model that were not used by any policies in the policy model.
     *
     * @return the unused context schemas
     */
    public Set<AxArtifactKey> getUnusedContextSchemas() {
        return getUnusedKeySet(contextSchemaUsage);
    }

    /**
     * Gets the context albums in the policy model that were not used by any policies in the policy model.
     *
     * @return the unused context albums
     */
    public Set<AxArtifactKey> getUnusedContextAlbums() {
        return getUnusedKeySet(contextAlbumUsage);
    }

    /**
     * Gets the events in the policy model that were not used by any policies in the policy model.
     *
     * @return the unused events
     */
    public Set<AxArtifactKey> getUnusedEvents() {
        return getUnusedKeySet(eventUsage);
    }

    /**
     * Gets the tasks in the policy model that were not used by any policies in the policy model.
     *
     * @return the unused tasks
     */
    public Set<AxArtifactKey> getUnusedTasks() {
        return getUnusedKeySet(taskUsage);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        builder.append(getUsageMapString("Context Schema usage", contextSchemaUsage));
        builder.append(getUsageMapString("Context Album usage", contextAlbumUsage));
        builder.append(getUsageMapString("Event usage", eventUsage));
        builder.append(getUsageMapString("Task usage", taskUsage));

        return builder.toString();
    }

    /**
     * Gets the usage map string.
     *
     * @param header the header
     * @param usageMap the usage map
     * @return the usage map string
     */
    private String getUsageMapString(final String header, final Map<? extends AxKey, Set<AxKey>> usageMap) {
        final StringBuilder builder = new StringBuilder();

        builder.append(header);
        builder.append('\n');
        for (final Entry<? extends AxKey, Set<AxKey>> usageEntry : usageMap.entrySet()) {
            builder.append(" ");
            builder.append(usageEntry.getKey().getID());
            if (usageEntry.getValue().isEmpty()) {
                builder.append(" (unused)\n");
                continue;
            }

            builder.append('\n');
            for (final AxKey usageKey : usageEntry.getValue()) {
                builder.append("  ");
                builder.append(usageKey.getID());
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    /**
     * Gets the used key set.
     *
     * @param usageMap the usage map
     * @return the used key set
     */
    private Set<AxArtifactKey> getUsedKeySet(final Map<AxArtifactKey, Set<AxKey>> usageMap) {
        final Set<AxArtifactKey> usedKeySet = new TreeSet<>();

        for (final Entry<AxArtifactKey, Set<AxKey>> usageEntry : usageMap.entrySet()) {
            if (!usageEntry.getValue().isEmpty()) {
                usedKeySet.add(usageEntry.getKey());
            }
        }

        return usedKeySet;
    }

    /**
     * Gets the unused key set.
     *
     * @param usageMap the usage map
     * @return the unused key set
     */
    private Set<AxArtifactKey> getUnusedKeySet(final Map<AxArtifactKey, Set<AxKey>> usageMap) {
        final Set<AxArtifactKey> usedKeySet = new TreeSet<>();

        for (final Entry<AxArtifactKey, Set<AxKey>> usageEntry : usageMap.entrySet()) {
            if (usageEntry.getValue().isEmpty()) {
                usedKeySet.add(usageEntry.getKey());
            }
        }

        return usedKeySet;
    }
}
