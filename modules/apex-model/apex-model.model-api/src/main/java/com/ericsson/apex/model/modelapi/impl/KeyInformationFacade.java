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
import java.util.Set;
import java.util.UUID;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxKeyInfo;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.basicmodel.handling.ApexModelStringWriter;
import com.ericsson.apex.model.modelapi.ApexAPIResult;
import com.ericsson.apex.model.modelapi.ApexModel;

/**
 * This class acts as a facade for operations towards a policy model for key information operations.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class KeyInformationFacade {
    // Apex model we're working towards
    private final ApexModel apexModel;

    // Properties to use for the model
    private final Properties apexProperties;

    // JSON output on list/delete if set
    private final boolean jsonMode;

    /**
     * Constructor to create a key information facade for the Model API.
     *
     * @param apexModel the apex model
     * @param apexProperties Properties for the model
     * @param jsonMode set to true to return JSON strings in list and delete operations, otherwise set to false
     */
    public KeyInformationFacade(final ApexModel apexModel, final Properties apexProperties, final boolean jsonMode) {
        this.apexModel = apexModel;
        this.apexProperties = apexProperties;
        this.jsonMode = jsonMode;
    }

    /**
     * Create key information.
     *
     * @param name name of the concept for the key information
     * @param version version of the concept for the key information, set to null to use the default version
     * @param uuid key information UUID, set to null to generate a UUID
     * @param description key information description, set to null to generate a description
     * @return result of the operation
     */
    public ApexAPIResult createKeyInformation(final String name, final String version, final String uuid, final String description) {
        try {
            AxArtifactKey key = new AxArtifactKey();
            key.setName(name);
            if (version != null) {
                key.setVersion(version);
            }
            else {
                key.setVersion(apexProperties.getProperty("DEFAULT_CONCEPT_VERSION"));
            }

            if (apexModel.getPolicyModel().getKeyInformation().getKeyInfoMap().containsKey(key)) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_EXISTS, "concept " + key.getID() + " already exists");
            }

            AxKeyInfo keyInfo = new AxKeyInfo(key);
            if (description != null) {
                keyInfo.setDescription(description);
            }
            if (uuid != null) {
                keyInfo.setUuid(UUID.fromString(uuid));
            }
            else {
                //generate a reproducible UUID
                keyInfo.setUuid(AxKeyInfo.generateReproducibleUUID(keyInfo.getID() + keyInfo.getDescription()));
            }
            apexModel.getPolicyModel().getKeyInformation().getKeyInfoMap().put(key, keyInfo);
            return new ApexAPIResult();
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Update key information.
     *
     * @param name name of the concept for the key information
     * @param version version of the concept for the key information, set to null to update the latest version
     * @param uuid key information UUID, set to null to not update
     * @param description key information description, set to null to not update
     * @return result of the operation
     */
    public ApexAPIResult updateKeyInformation(final String name, final String version, final String uuid, final String description) {
        try {
            AxKeyInfo keyInfo = apexModel.getPolicyModel().getKeyInformation().get(name, version);
            if (keyInfo == null) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept " + name + ":" + version + " does not exist");
            }

            if (description != null) {
                keyInfo.setDescription(description);
            }

            if (uuid != null) {
                keyInfo.setUuid(UUID.fromString(uuid));
            }
            else {
                //generate a reproducible UUID
                keyInfo.setUuid(AxKeyInfo.generateReproducibleUUID(keyInfo.getID() + keyInfo.getDescription()));
            }

            return new ApexAPIResult();
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * List key information.
     *
     * @param name name of the concept for the key information, set to null to list all
     * @param version starting version of the concept for the key information, set to null to list all versions
     * @return result of the operation
     */
    public ApexAPIResult listKeyInformation(final String name, final String version) {
        try {
            Set<AxKeyInfo> keyInfoSet = apexModel.getPolicyModel().getKeyInformation().getAll(name, version);
            if (name != null && keyInfoSet.isEmpty()) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept(s) " + name + ':' + version + " do(es) not exist");
            }

            ApexAPIResult result = new ApexAPIResult();
            for (AxKeyInfo keyInfo : keyInfoSet) {
                result.addMessage(new ApexModelStringWriter<AxKeyInfo>(false).writeString(keyInfo, AxKeyInfo.class, jsonMode));
            }
            return result;
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Delete key information.
     *
     * @param name name of the concept for the key information
     * @param version version of the concept for the key information, set to null to delete all versions
     * @return result of the operation
     */
    public ApexAPIResult deleteKeyInformation(final String name, final String version) {
        try {
            if (version != null) {
                AxArtifactKey key = new AxArtifactKey(name, version);
                AxKeyInfo removedKeyInfo = apexModel.getPolicyModel().getKeyInformation().getKeyInfoMap().remove(key);
                if (removedKeyInfo != null) {
                    return new ApexAPIResult(ApexAPIResult.RESULT.SUCCESS,
                            new ApexModelStringWriter<AxKeyInfo>(false).writeString(removedKeyInfo, AxKeyInfo.class, jsonMode));
                }
                else {
                    return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept " + key.getID() + " does not exist");
                }
            }

            Set<AxKeyInfo> keyInfoSet = apexModel.getPolicyModel().getKeyInformation().getAll(name, version);
            if (keyInfoSet.isEmpty()) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept(s) " + name + ':' + version + " do(es) not exist");
            }

            ApexAPIResult result = new ApexAPIResult();
            for (AxKeyInfo keyInfo : keyInfoSet) {
                result.addMessage(new ApexModelStringWriter<AxKeyInfo>(false).writeString(keyInfo, AxKeyInfo.class, jsonMode));
                apexModel.getPolicyModel().getKeyInformation().getKeyInfoMap().remove(keyInfo.getKey());
            }
            return result;
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Validate key information.
     *
     * @param name name of the concept for the key information
     * @param version version of the concept for the key information, set to null to validate all versions
     * @return result of the operation
     */
    public ApexAPIResult validateKeyInformation(final String name, final String version) {
        try {
            Set<AxKeyInfo> keyInfoSet = apexModel.getPolicyModel().getKeyInformation().getAll(name, version);
            if (keyInfoSet.isEmpty()) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept(s) " + name + ':' + version + " do(es) not exist");
            }

            ApexAPIResult result = new ApexAPIResult();
            for (AxKeyInfo keyInfo : keyInfoSet) {
                AxValidationResult validationResult = keyInfo.validate(new AxValidationResult());
                result.addMessage(new ApexModelStringWriter<AxArtifactKey>(false).writeString(keyInfo.getKey(), AxArtifactKey.class, jsonMode));
                result.addMessage(validationResult.toString());
            }
            return result;
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

}
