/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.modelapi.impl;

import java.util.Properties;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.handling.ApexModelStringWriter;
import com.ericsson.apex.model.modelapi.ApexAPIResult;
import com.ericsson.apex.model.modelapi.ApexModel;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;
import com.ericsson.apex.model.utilities.Assertions;

/**
 * This class acts as a facade for operations towards a policy model.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ModelFacade {
    // Apex model we're working towards
    private final ApexModel apexModel;

    // Properties to use for the model
    private final Properties apexProperties;

    // Facade classes for working towards the real Apex model
    private final KeyInformationFacade keyInformationFacade;

    // JSON output on list/delete if set
    private final boolean jsonMode;

    /**
     * Constructor to create a model facade for the Apex model.
     *
     * @param apexModel the apex model
     * @param apexProperties Properties for the model
     * @param jsonMode set to true to return JSON strings in list and delete operations, otherwise set to false
     */
    public ModelFacade(final ApexModel apexModel, final Properties apexProperties, final boolean jsonMode) {
        Assertions.argumentNotNull(apexModel, "apexModel may not be null");
        Assertions.argumentNotNull(apexProperties, "apexProperties may not be null");

        this.apexModel = apexModel;
        this.apexProperties = apexProperties;
        this.jsonMode = jsonMode;

        keyInformationFacade = new KeyInformationFacade(apexModel, apexProperties, jsonMode);
    }

    /**
     * Create model.
     *
     * @param name name of the model
     * @param version version of the model, set to null to use the default version
     * @param uuid model UUID, set to null to generate a UUID
     * @param description model description, set to null to generate a description
     * @return result of the operation
     */
    public ApexAPIResult createModel(final String name, final String version, final String uuid, final String description) {
        try {
            AxArtifactKey key = new AxArtifactKey();
            key.setName(name);
            if (version != null) {
                key.setVersion(version);
            }
            else {
                String defaultVersion = apexProperties.getProperty("DEFAULT_CONCEPT_VERSION");
                if (defaultVersion != null) {
                    key.setVersion(defaultVersion);
                }
                else {
                    return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, "concept " + name + ", no version specified");
                }
            }

            if (!apexModel.getPolicyModel().getKey().equals(AxArtifactKey.getNullKey())) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_EXISTS, "concept " + apexModel.getPolicyModel().getKey().getID() + " already created");
            }

            apexModel.setPolicyModel(new AxPolicyModel(key));

            ApexAPIResult result;

            result = keyInformationFacade.createKeyInformation(name, version, uuid, description);
            if (result.getResult().equals(ApexAPIResult.RESULT.SUCCESS)) {
                apexModel.getPolicyModel().getKeyInformation().generateKeyInfo(apexModel.getPolicyModel());
            }
            return result;
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Update model.
     *
     * @param name name of the model
     * @param version version of the model, set to null to update the latest version
     * @param uuid key information UUID, set to null to not update
     * @param description policy description, set to null to not update
     * @return result of the operation
     */
    public ApexAPIResult updateModel(final String name, final String version, final String uuid, final String description) {
        try {
            AxArtifactKey key = new AxArtifactKey();
            key.setName(name);
            if (version != null) {
                key.setVersion(version);
            }
            else {
                String defaultVersion = apexProperties.getProperty("DEFAULT_CONCEPT_VERSION");
                if (defaultVersion != null) {
                    key.setVersion(defaultVersion);
                }
                else {
                    return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, "concept " + apexModel.getPolicyModel().getKey().getID() + ", no version specified");
                }
            }

            if (apexModel.getPolicyModel().getKey().equals(AxArtifactKey.getNullKey())) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST,
                        "concept " + apexModel.getPolicyModel().getKey().getID() + " does not exist");
            }

            return keyInformationFacade.updateKeyInformation(name, version, uuid, description);
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Get the key of an Apex model.
     *
     * @return the result of the operation
     */
    public ApexAPIResult getModelKey() {
        try {
            ApexAPIResult result = new ApexAPIResult();
            AxArtifactKey modelkey = apexModel.getPolicyModel().getKey();
            result.addMessage(new ApexModelStringWriter<AxArtifactKey>(false).writeString(modelkey, AxArtifactKey.class, jsonMode));
            return result;
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * List an Apex model.
     *
     * @return the result of the operation
     */
    public ApexAPIResult listModel() {
        try {
            ApexAPIResult result = new ApexAPIResult();
            result.addMessage(new ApexModelStringWriter<AxPolicyModel>(false).writeString(apexModel.getPolicyModel(), AxPolicyModel.class, jsonMode));
            return result;
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Delete an Apex model, clear all the concepts in the model.
     *
     * @return the result of the operation
     */
    public ApexAPIResult deleteModel() {
        // @formatter:off
        apexModel.getPolicyModel().getSchemas()       .getSchemasMap() .clear();
        apexModel.getPolicyModel().getEvents()        .getEventMap()   .clear();
        apexModel.getPolicyModel().getAlbums()        .getAlbumsMap()  .clear();
        apexModel.getPolicyModel().getTasks()         .getTaskMap()    .clear();
        apexModel.getPolicyModel().getPolicies()      .getPolicyMap()  .clear();
        apexModel.getPolicyModel().getKeyInformation().getKeyInfoMap() .clear();
        // @formatter:on

        apexModel.setPolicyModel(new AxPolicyModel());

        return new ApexAPIResult();
    }
}
