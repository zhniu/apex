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

import java.util.Set;
import java.util.TreeSet;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.basicmodel.handling.ApexModelException;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;

/**
 * Helper class used to merge information from two policy models together into a single policy model.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public final class PolicyModelMerger {
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(PolicyModelMerger.class);

    /**
     * Private constructor used to prevent sub class instantiation.
     */
    private PolicyModelMerger() {
    }

    /**
     * Get a merged policy model with information from two policy models merged into a larger policy model.
     *
     * @param leftPolicyModel the source Apex Model
     * @param rightPolicyModel the policies to include in sub policy model
     * @param useLeftOnMatches if true, uses concepts from the left model if concepts with common keys are found, if false it uses the concepts from the right
     *            model
     * @return the new Destination Model
     * @throws ApexModelException on model transfer errors
     */
    public static AxPolicyModel getMergedPolicyModel(final AxPolicyModel leftPolicyModel, final AxPolicyModel rightPolicyModel, final boolean useLeftOnMatches)
            throws ApexModelException {
        return getMergedPolicyModel(leftPolicyModel, rightPolicyModel, useLeftOnMatches, false);
    }

    /**
     * Get a merged policy model with information from two policy models merged into a larger policy model.
     *
     * @param leftPolicyModel the source Apex Model
     * @param rightPolicyModel the policies to include in sub policy model
     * @param useLeftOnMatches if true, uses concepts from the left model if concepts with common keys are found, if false it uses the concepts from the right
     *            model
     * @param ignoreInvalidSource Ignore errors on the source model, do the best you can
     * @return the new Destination Model
     * @throws ApexModelException on model transfer errors
     */
    public static AxPolicyModel getMergedPolicyModel(final AxPolicyModel leftPolicyModel, final AxPolicyModel rightPolicyModel, final boolean useLeftOnMatches,
            final boolean ignoreInvalidSource) throws ApexModelException {
        // Validate the left model
        if (!ignoreInvalidSource) {
            final AxValidationResult leftValidationResult = new AxValidationResult();
            leftPolicyModel.validate(leftValidationResult);
            if (!leftValidationResult.isValid()) {
                LOGGER.warn("left model is invalid: " + leftValidationResult.toString());
                throw new ApexModelException("left model is invalid: " + leftValidationResult.toString());
            }
        }

        // Validate the right model
        if (!ignoreInvalidSource) {
            final AxValidationResult rightValidationResult = new AxValidationResult();
            rightPolicyModel.validate(rightValidationResult);
            if (!rightValidationResult.isValid()) {
                LOGGER.warn("right model is invalid: " + rightValidationResult.toString());
                throw new ApexModelException("right model is invalid: " + rightValidationResult.toString());
            }
        }

        // The new policy model uses the favoured copy side as its base
        final AxPolicyModel mergedPolicyModel = (AxPolicyModel) (useLeftOnMatches ? new AxPolicyModel(leftPolicyModel) : new AxPolicyModel(rightPolicyModel));

        // The Compared to policy model is the unfavoured side
        final AxPolicyModel copyFromPolicyModel = (useLeftOnMatches ? rightPolicyModel : leftPolicyModel);

        //  Get the keys to copy over
        final Set<AxArtifactKey> copyOverKeyInfoKeys = new TreeSet<>(copyFromPolicyModel.getKeyInformation().getKeyInfoMap().keySet());
        final Set<AxArtifactKey> copyOverContextSchemaKeys = new TreeSet<>(copyFromPolicyModel.getSchemas().getSchemasMap().keySet());
        final Set<AxArtifactKey> copyOverEventKeys = new TreeSet<>(copyFromPolicyModel.getEvents().getEventMap().keySet());
        final Set<AxArtifactKey> copyOverContextAlbumKeys = new TreeSet<>(copyFromPolicyModel.getAlbums().getAlbumsMap().keySet());
        final Set<AxArtifactKey> copyOverTaskKeys = new TreeSet<>(copyFromPolicyModel.getTasks().getTaskMap().keySet());
        final Set<AxArtifactKey> copyOverPolicyKeys = new TreeSet<>(copyFromPolicyModel.getPolicies().getPolicyMap().keySet());

        //  Remove keys that already exist
        copyOverKeyInfoKeys.removeAll(mergedPolicyModel.getKeyInformation().getKeyInfoMap().keySet());
        copyOverContextSchemaKeys.removeAll(mergedPolicyModel.getSchemas().getSchemasMap().keySet());
        copyOverEventKeys.removeAll(mergedPolicyModel.getEvents().getEventMap().keySet());
        copyOverContextAlbumKeys.removeAll(mergedPolicyModel.getAlbums().getAlbumsMap().keySet());
        copyOverTaskKeys.removeAll(mergedPolicyModel.getTasks().getTaskMap().keySet());
        copyOverPolicyKeys.removeAll(mergedPolicyModel.getPolicies().getPolicyMap().keySet());

        // Now add all the concepts that must be copied over
        for (final AxArtifactKey keyInfoKey : copyOverKeyInfoKeys) {
            mergedPolicyModel.getKeyInformation().getKeyInfoMap().put(keyInfoKey, copyFromPolicyModel.getKeyInformation().getKeyInfoMap().get(keyInfoKey));
        }
        for (final AxArtifactKey contextSchemaKey : copyOverContextSchemaKeys) {
            mergedPolicyModel.getSchemas().getSchemasMap().put(contextSchemaKey, copyFromPolicyModel.getSchemas().getSchemasMap().get(contextSchemaKey));
        }
        for (final AxArtifactKey eventKey : copyOverEventKeys) {
            mergedPolicyModel.getEvents().getEventMap().put(eventKey, copyFromPolicyModel.getEvents().getEventMap().get(eventKey));
        }
        for (final AxArtifactKey contextAlbumKey : copyOverContextAlbumKeys) {
            mergedPolicyModel.getAlbums().getAlbumsMap().put(contextAlbumKey, copyFromPolicyModel.getAlbums().getAlbumsMap().get(contextAlbumKey));
        }
        for (final AxArtifactKey taskKey : copyOverTaskKeys) {
            mergedPolicyModel.getTasks().getTaskMap().put(taskKey, copyFromPolicyModel.getTasks().getTaskMap().get(taskKey));
        }
        for (final AxArtifactKey policyKey : copyOverPolicyKeys) {
            mergedPolicyModel.getPolicies().getPolicyMap().put(policyKey, copyFromPolicyModel.getPolicies().getPolicyMap().get(policyKey));
        }

        // That's it, return the model
        return mergedPolicyModel;
    }
}
