/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.enginemodel.handling;

import java.util.UUID;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxKeyInfo;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.basicmodel.test.TestApexModelCreator;
import com.ericsson.apex.model.contextmodel.concepts.AxContextAlbums;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchema;
import com.ericsson.apex.model.contextmodel.concepts.AxContextAlbum;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchemas;
import com.ericsson.apex.model.enginemodel.concepts.AxEngineModel;
import com.ericsson.apex.model.enginemodel.concepts.AxEngineState;
import com.ericsson.apex.model.enginemodel.concepts.AxEngineStats;

public class TestApexEngineModelCreator implements TestApexModelCreator<AxEngineModel> {

    @Override
    public AxEngineModel getModel() {
        AxContextSchema schema0 = new AxContextSchema(new AxArtifactKey("StringType", "0.0.1"), "Java", "com.ericsson.apex.model.enginemodel.concepts.TestContextItem000");
        AxContextSchema schema1 = new AxContextSchema(new AxArtifactKey("MapType", "0.0.1"),    "Java", "com.ericsson.apex.model.enginemodel.concepts.TestContextItem00A");

        AxContextSchemas schemas = new AxContextSchemas(new AxArtifactKey("ContextSchemas", "0.0.1"));
        schemas.getSchemasMap().put(schema0.getKey(), schema0);
        schemas.getSchemasMap().put(schema1.getKey(), schema1);

        AxContextAlbum album0 = new AxContextAlbum(new AxArtifactKey("contextAlbum0", "0.0.1"), "APPLICATION", true, schema1.getKey());

        AxContextAlbums albums = new AxContextAlbums(new AxArtifactKey("context", "0.0.1"));
        albums.getAlbumsMap().put(album0.getKey(), album0);
       
        AxEngineModel engineModel = new AxEngineModel(new AxArtifactKey("AnEngine", "0.0.1"));
        engineModel.setSchemas(schemas);
        engineModel.setAlbums(albums);
        engineModel.setTimestamp(System.currentTimeMillis());
        engineModel.setState(AxEngineState.EXECUTING);
        engineModel.setStats(new AxEngineStats(new AxReferenceKey(engineModel.getKey(), "EngineStats"), System.currentTimeMillis(), 100, 205, 200, 12345, 9876));
        engineModel.getKeyInformation().generateKeyInfo(engineModel);

        AxValidationResult result = new AxValidationResult();
        engineModel.validate(result);
       
        return engineModel;
    }
   
    @Override
    public AxEngineModel getInvalidModel() {
        AxEngineModel engineModel = getModel();

        engineModel.setTimestamp(System.currentTimeMillis());
        engineModel.setState(AxEngineState.UNDEFINED);
        engineModel.setStats(new AxEngineStats(new AxReferenceKey(engineModel.getKey(), "EngineStats"), System.currentTimeMillis(), 100, 205, 200, 12345, 9876));
        engineModel.getKeyInformation().generateKeyInfo(engineModel);

        return engineModel;
    }

    public AxEngineModel getMalstructuredModel() {
        AxEngineModel engineModel = getModel();

        engineModel.setTimestamp(-1);
        engineModel.setState(AxEngineState.UNDEFINED);
       
        return engineModel;
    }

    @Override
    public AxEngineModel getObservationModel() {
        AxEngineModel engineModel = getModel();

        AxContextSchema schema0 = new AxContextSchema(new AxArtifactKey("StringType", "0.0.1"), "Java", "com.ericsson.apex.model.enginemodel.concepts.TestContextItem000");
        AxContextSchema schema1 = new AxContextSchema(new AxArtifactKey("MapType", "0.0.1"),    "Java", "com.ericsson.apex.model.enginemodel.concepts.TestContextItem00A");

        engineModel.getKeyInformation().getKeyInfoMap().put(schema0.getKey(), new AxKeyInfo(schema0.getKey(), UUID.fromString("00000000-0000-0000-0000-000000000001"), ""));
        engineModel.getKeyInformation().getKeyInfoMap().put(schema1.getKey(), new AxKeyInfo(schema1.getKey(), UUID.fromString("00000000-0000-0000-0000-000000000002"), ""));

        return engineModel;
    }

    @Override
    public AxEngineModel getWarningModel() {
        AxEngineModel engineModel = getModel();

        AxContextSchema schema0 = new AxContextSchema(new AxArtifactKey("StringType", "0.0.1"), "Java", "com.ericsson.apex.model.enginemodel.concepts.TestContextItem000");
        AxContextSchema schema1 = new AxContextSchema(new AxArtifactKey("MapType", "0.0.1"),    "Java", "com.ericsson.apex.model.enginemodel.concepts.TestContextItem00A");

        engineModel.getKeyInformation().getKeyInfoMap().put(schema0.getKey(), new AxKeyInfo(schema0.getKey(), UUID.fromString("00000000-0000-0000-0000-000000000000"), ""));
        engineModel.getKeyInformation().getKeyInfoMap().put(schema1.getKey(), new AxKeyInfo(schema1.getKey(), UUID.fromString("00000000-0000-0000-0000-000000000001"), ""));

        return engineModel;
    }
}
