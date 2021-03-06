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
import com.ericsson.apex.model.contextmodel.concepts.AxContextAlbum;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchema;
import com.ericsson.apex.model.modelapi.ApexAPIResult;
import com.ericsson.apex.model.modelapi.ApexModel;

/**
 * This class acts as a facade for operations towards a policy model for context album operations.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ContextAlbumFacade {
	private static final String CONCEPT = "concept ";
	private static final String CONCEPT_S = "concept(s) ";
	private static final String DOES_NOT_EXIST = " does not exist";
    private static final String DO_ES_NOT_EXIST = " do(es) not exist";

	// Apex model we're working towards
    private final ApexModel apexModel;

    // Properties to use for the model
    private final Properties apexProperties;

    // Facade classes for working towards the real Apex model
    private final KeyInformationFacade keyInformationFacade;

    // JSON output on list/delete if set
    private final boolean jsonMode;

    /**
     * Constructor that creates a context album facade for the Apex Model API.
     *
     * @param apexModel the apex model
     * @param apexProperties Properties for the model
     * @param jsonMode set to true to return JSON strings in list and delete operations, otherwise set to false
     */
    public ContextAlbumFacade(final ApexModel apexModel, final Properties apexProperties, final boolean jsonMode) {
        this.apexModel = apexModel;
        this.apexProperties = apexProperties;
        this.jsonMode = jsonMode;

        keyInformationFacade = new KeyInformationFacade(apexModel, apexProperties, jsonMode);
    }

    /**
     * Create a context album.
     *
     * @param name name of the context album
     * @param version version of the context album, set to null to use the default version
     * @param scope of the context album
     * @param writable "true" or "t" if the context album is writable, set to null or any other value for a read-only album
     * @param contextSchemaName name of the parameter context schema
     * @param contextSchemaVersion version of the parameter context schema, set to null to use the latest version
     * @param uuid context album UUID, set to null to generate a UUID
     * @param description context album description, set to null to generate a description
     * @return result of the operation
     */
    // CHECKSTYLE:OFF: checkstyle:parameterNumber
    public ApexAPIResult createContextAlbum(final String name, final String version, final String scope, final String writable, final String contextSchemaName,
            final String contextSchemaVersion, final String uuid, final String description) {
       try {
            AxArtifactKey key = new AxArtifactKey();
            key.setName(name);
            if (version != null) {
                key.setVersion(version);
            }
            else {
                key.setVersion(apexProperties.getProperty("DEFAULT_CONCEPT_VERSION"));
            }

            if (apexModel.getPolicyModel().getAlbums().getAlbumsMap().containsKey(key)) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_EXISTS, CONCEPT + key.getID() + " already exists");
            }

            AxContextSchema schema = apexModel.getPolicyModel().getSchemas().get(contextSchemaName, contextSchemaVersion);
            if (schema == null) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST,
                        CONCEPT + contextSchemaName + ':' + contextSchemaVersion + DOES_NOT_EXIST);
            }

            AxContextAlbum contextAlbum = new AxContextAlbum(key);
            contextAlbum.setScope(scope);
            contextAlbum.setItemSchema(schema.getKey());

            if (writable != null && (writable.trim().equalsIgnoreCase("true") || writable.trim().equalsIgnoreCase("t"))) {
                contextAlbum.setWritable(true);
            }
            else {
                contextAlbum.setWritable(false);
            }

            apexModel.getPolicyModel().getAlbums().getAlbumsMap().put(key, contextAlbum);

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
    // CHECKSTYLE:ON: checkstyle:parameterNumber

    /**
     * Update a context album.
     *
     * @param name name of the context album
     * @param version version of the context album, set to null to use the default version
     * @param scope of the context album
     * @param writable "true" or "t" if the context album is writable, set to null or any other value for a read-only album
     * @param contextSchemaName name of the parameter context schema
     * @param contextSchemaVersion version of the parameter context schema, set to null to use the latest version
     * @param uuid context album UUID, set to null to generate a UUID
     * @param description context album description, set to null to generate a description
     * @return result of the operation
     */
    // CHECKSTYLE:OFF: checkstyle:parameterNumber
   public ApexAPIResult updateContextAlbum(final String name, final String version, final String scope, final String writable, final String contextSchemaName,
            final String contextSchemaVersion, final String uuid, final String description) {
        try {
            AxContextAlbum contextAlbum = apexModel.getPolicyModel().getAlbums().get(name, version);
            if (contextAlbum == null) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, CONCEPT + name + ':' + version + DOES_NOT_EXIST);
            }

            if (scope != null) {
                contextAlbum.setScope(scope);
            }
            if (writable != null) {
                if (writable.trim().equalsIgnoreCase("true") || writable.trim().equalsIgnoreCase("t")) {
                    contextAlbum.setWritable(true);
                }
                else {
                    contextAlbum.setWritable(false);
                }
            }

            if (contextSchemaName != null) {
                AxContextSchema schema = apexModel.getPolicyModel().getSchemas().get(contextSchemaName, contextSchemaVersion);
                if (schema == null) {
                    return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST,
                            CONCEPT + contextSchemaName + ':' + contextSchemaVersion + DOES_NOT_EXIST);
                }
                contextAlbum.setItemSchema(schema.getKey());
            }

            return keyInformationFacade.updateKeyInformation(name, version, uuid, description);
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }
   // CHECKSTYLE:ON: checkstyle:parameterNumber

   /**
    * List context albums.
    *
    * @param name name of the context album, set to null to list all
    * @param version starting version of the context album, set to null to list all versions
    * @return result of the operation
    */
    public ApexAPIResult listContextAlbum(final String name, final String version) {
        try {
            Set<AxContextAlbum> contextAlbumSet = apexModel.getPolicyModel().getAlbums().getAll(name, version);
            if (name != null && contextAlbumSet.isEmpty()) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, CONCEPT_S + name + ':' + version + DO_ES_NOT_EXIST);
            }

            ApexAPIResult result = new ApexAPIResult();
            for (AxContextAlbum contextAlbum : contextAlbumSet) {
                result.addMessage(new ApexModelStringWriter<AxContextAlbum>(false).writeString(contextAlbum, AxContextAlbum.class, jsonMode));
            }
            return result;
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Delete a context album.
     *
     * @param name name of the context album
     * @param version version of the context album, set to null to delete versions
     * @return result of the operation
     */
    public ApexAPIResult deleteContextAlbum(final String name, final String version) {
        try {
            if (version != null) {
                AxArtifactKey key = new AxArtifactKey(name, version);
                if (apexModel.getPolicyModel().getAlbums().getAlbumsMap().remove(key) != null) {
                    return new ApexAPIResult();
                }
                else {
                    return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, CONCEPT + key.getID() + DOES_NOT_EXIST);
                }
            }

            Set<AxContextAlbum> contextAlbumSet = apexModel.getPolicyModel().getAlbums().getAll(name, version);
            if (contextAlbumSet.isEmpty()) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, CONCEPT_S + name + ':' + version + DO_ES_NOT_EXIST);
            }

            ApexAPIResult result = new ApexAPIResult();
            for (AxContextAlbum contextAlbum : contextAlbumSet) {
                result.addMessage(new ApexModelStringWriter<AxContextAlbum>(false).writeString(contextAlbum, AxContextAlbum.class, jsonMode));
                apexModel.getPolicyModel().getAlbums().getAlbumsMap().remove(contextAlbum.getKey());
                keyInformationFacade.deleteKeyInformation(name, version);
            }
            return result;
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Validate context albums.
     *
     * @param name name of the context album, set to null to list all
     * @param version starting version of the context album, set to null to list all versions
     * @return result of the operation
     */
    public ApexAPIResult validateContextAlbum(final String name, final String version) {
        try {
            Set<AxContextAlbum> contextAlbumSet = apexModel.getPolicyModel().getAlbums().getAll(name, version);
            if (contextAlbumSet.isEmpty()) {
                return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_DOES_NOT_EXIST, CONCEPT_S + name + ':' + version + DO_ES_NOT_EXIST);
            }

            ApexAPIResult result = new ApexAPIResult();
            for (AxContextAlbum contextAlbum : contextAlbumSet) {
                AxValidationResult validationResult = contextAlbum.validate(new AxValidationResult());
                result.addMessage(new ApexModelStringWriter<AxArtifactKey>(false).writeString(contextAlbum.getKey(), AxArtifactKey.class, jsonMode));
                result.addMessage(validationResult.toString());
            }
            return result;
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }
}
