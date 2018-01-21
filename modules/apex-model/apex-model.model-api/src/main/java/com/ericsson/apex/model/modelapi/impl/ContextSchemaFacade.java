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

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.basicmodel.handling.ApexModelStringWriter;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchema;
import com.ericsson.apex.model.modelapi.ApexAPIResult;
import com.ericsson.apex.model.modelapi.ApexModel;
import com.ericsson.apex.model.utilities.Assertions;

/**
 * This class acts as a facade for operations towards a policy model for context schema operations.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ContextSchemaFacade {
    // Apex model we're working towards
    private final ApexModel apexModel;

    // Properties to use for the model
    private final Properties apexProperties;

    // Facade classes for working towards the real Apex model
    private final KeyInformationFacade keyInformationFacade;

    // JSON output on list/delete if set
    private final boolean jsonMode;

    /**
     * Constructor to create the context schema facade for the Model API.
     *
     * @param apexModel the apex model
     * @param apexProperties Properties for the model
     * @param jsonMode set to true to return JSON strings in list and delete operations, otherwise set to false
     */
    public ContextSchemaFacade(final ApexModel apexModel, final Properties apexProperties, final boolean jsonMode) {
        this.apexModel = apexModel;
        this.apexProperties = apexProperties;
        this.jsonMode = jsonMode;

        keyInformationFacade = new KeyInformationFacade(apexModel, apexProperties, jsonMode);
    }

    /**
     * Create a context schema.
     *
     * @param name name of the context schema
     * @param version version of the context schema, set to null to use the default version
     * @param schemaFlavour a string identifying the flavour of this context schema
     * @param schemaDefinition a string containing the definition of this context schema
     * @param uuid context schema UUID, set to null to generate a UUID
     * @param description context schema description, set to null to generate a description
     * @return result of the operation
     */
    public ApexAPIResult createContextSchema(final String name, final String version, final String schemaFlavour, final String schemaDefinition,
            final String uuid, final String description) {
        try {
            Assertions.argumentNotNull(schemaFlavour, "schemaFlavour may not be null");

            AxArtifactKey key = new AxArtifactKey();
            key.setName(name);
            if (version != null) {
                key.setVersion(version);
            }
            else {
                key.setVersion(apexProperties.getProperty("DEFAULT_CONCEPT_VERSION"));
            }

            if (apexModel.getPolicyModel().getSchemas().getSchemasMap().containsKey(key)) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_EXISTS, "concept " + key.getID() + " already exists");
            }

            apexModel.getPolicyModel().getSchemas().getSchemasMap().put(key, new AxContextSchema(key, schemaFlavour, schemaDefinition));

            if (apexModel.getPolicyModel().getKeyInformation().getKeyInfoMap().containsKey(key)) {
                return keyInformationFacade.updateKeyInformation(name, version, uuid, description);
            }
            else {
                return keyInformationFacade.createKeyInformation(name, version, uuid, description);
            }
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Update a context schema.
     *
     * @param name name of the context schema
     * @param version version of the context schema, set to null to update the latest version
     * @param schemaFlavour a string identifying the flavour of this context schema
     * @param schemaDefinition a string containing the definition of this context schema
     * @param uuid context schema UUID, set to null to not update
     * @param description context schema description, set to null to not update
     * @return result of the operation
     */
    public ApexAPIResult updateContextSchema(final String name, final String version, final String schemaFlavour, final String schemaDefinition,
            final String uuid, final String description) {
        try {
            AxContextSchema schema = apexModel.getPolicyModel().getSchemas().get(name, version);
            if (schema == null) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept " + name + ':' + version + " does not exist");
            }

            if (schemaFlavour != null) {
                schema.setSchemaFlavour(schemaFlavour);
            }

            if (schemaDefinition != null) {
                schema.setSchema(schemaDefinition);
            }

            return keyInformationFacade.updateKeyInformation(name, version, uuid, description);
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * List context schemas.
     *
     * @param name name of the context schema, set to null to list all
     * @param version starting version of the context schema, set to null to list all versions
     * @return result of the operation
     */
    public ApexAPIResult listContextSchemas(final String name, final String version) {
        try {
            Set<AxContextSchema> schemaSet = apexModel.getPolicyModel().getSchemas().getAll(name, version);
            if (name != null && schemaSet.size() == 0) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept(s) " + name + ':' + version + " do(es) not exist");
            }

            ApexAPIResult result = new ApexAPIResult();
            for (AxContextSchema schema : schemaSet) {
                result.addMessage(new ApexModelStringWriter<AxContextSchema>(false).writeString(schema, AxContextSchema.class, jsonMode));
            }
            return result;
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Delete a context schema.
     *
     * @param name name of the context schema
     * @param version version of the context schema, set to null to delete all versions
     * @return result of the operation
     */
    public ApexAPIResult deleteContextSchema(final String name, final String version) {
        try {
            if (version != null) {
                AxArtifactKey key = new AxArtifactKey(name, version);
                AxContextSchema removedSchema = apexModel.getPolicyModel().getSchemas().getSchemasMap().remove(key);
                if (removedSchema != null) {
                    return new ApexAPIResult(ApexAPIResult.RESULT.SUCCESS,
                            new ApexModelStringWriter<AxContextSchema>(false).writeString(removedSchema, AxContextSchema.class, jsonMode));
                }
                else {
                    return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept " + key.getID() + " does not exist");
                }
            }

            Set<AxContextSchema> schemaSet = apexModel.getPolicyModel().getSchemas().getAll(name, version);
            if (schemaSet.size() == 0) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept(s) " + name + ':' + version + " do(es) not exist");
            }

            ApexAPIResult result = new ApexAPIResult();
            for (AxContextSchema schema : schemaSet) {
                result.addMessage(new ApexModelStringWriter<AxContextSchema>(false).writeString(schema, AxContextSchema.class, jsonMode));
                apexModel.getPolicyModel().getSchemas().getSchemasMap().remove(schema.getKey());
                keyInformationFacade.deleteKeyInformation(name, version);
            }
            return result;
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Validate context schemas.
     *
     * @param name name of the context schema, set to null to list all
     * @param version starting version of the context schema, set to null to list all versions
     * @return result of the operation
     */
    public ApexAPIResult validateContextSchemas(final String name, final String version) {
        try {
            Set<AxContextSchema> schemaSet = apexModel.getPolicyModel().getSchemas().getAll(name, version);
            if (schemaSet.size() == 0) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, "concept(s) " + name + ':' + version + " do(es) not exist");
            }

            ApexAPIResult result = new ApexAPIResult();
            for (AxContextSchema schema : schemaSet) {
                AxValidationResult validationResult = schema.validate(new AxValidationResult());
                result.addMessage(new ApexModelStringWriter<AxArtifactKey>(false).writeString(schema.getKey(), AxArtifactKey.class, jsonMode));
                result.addMessage(validationResult.toString());
            }
            return result;
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }
}
