/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.contextmodel.handling;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.contextmodel.concepts.AxContextAlbum;
import com.ericsson.apex.model.contextmodel.concepts.AxContextModel;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchema;

/**
 * This class creates sample Policy Models
 * 
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestContextComparisonFactory {

    public AxContextModel getFullModel() {
        AxContextSchema testContextSchema000 = new AxContextSchema(new AxArtifactKey("TestContextSchema000", "0.0.1"), "JAVA",
                "com.ericsson.apex.concept.TestContextSchema000");
        AxContextSchema testContextSchema00A = new AxContextSchema(new AxArtifactKey("TestContextSchema00A", "0.0.1"), "JAVA",
                "com.ericsson.apex.concept.TestContextSchema00A");
        AxContextSchema testContextSchema00C = new AxContextSchema(new AxArtifactKey("TestContextSchema00C", "0.0.1"), "JAVA",
                "com.ericsson.apex.concept.TestContextSchema00C");

        AxContextAlbum externalContextAlbum = new AxContextAlbum(new AxArtifactKey("ExternalContextAlbum", "0.0.1"), "EXTERNAL", false,
                testContextSchema000.getKey());
        AxContextAlbum globalContextAlbum = new AxContextAlbum(new AxArtifactKey("GlobalContextAlbum", "0.0.1"), "GLOBAL", true, testContextSchema00A.getKey());
        AxContextAlbum policy0ContextAlbum = new AxContextAlbum(new AxArtifactKey("Policy0ContextAlbum", "0.0.1"), "APPLICATION", true,
                testContextSchema00C.getKey());
        AxContextAlbum policy1ContextAlbum = new AxContextAlbum(new AxArtifactKey("Policy1ContextAlbum ", "0.0.1"), "APPLICATION", true,
                testContextSchema00C.getKey());

        AxContextModel contextModel = new AxContextModel(new AxArtifactKey("ContextModel", "0.0.1"));
        contextModel.getSchemas().getSchemasMap().put(testContextSchema000.getKey(), testContextSchema000);
        contextModel.getSchemas().getSchemasMap().put(testContextSchema00A.getKey(), testContextSchema00A);
        contextModel.getSchemas().getSchemasMap().put(testContextSchema00C.getKey(), testContextSchema00C);

        contextModel.getAlbums().getAlbumsMap().put(externalContextAlbum.getKey(), externalContextAlbum);
        contextModel.getAlbums().getAlbumsMap().put(globalContextAlbum.getKey(), globalContextAlbum);
        contextModel.getAlbums().getAlbumsMap().put(policy0ContextAlbum.getKey(), policy0ContextAlbum);
        contextModel.getAlbums().getAlbumsMap().put(policy1ContextAlbum.getKey(), policy1ContextAlbum);

        return contextModel;
    }

    public AxContextModel getEmptyModel() {
        return new AxContextModel(new AxArtifactKey("Context", "0.0.1"));
    }

    public AxContextModel getShellModel() {
        AxContextSchema testContextSchema000 = new AxContextSchema(new AxArtifactKey("TestContextSchema000", "0.0.1"), "JAVA",
                "com.ericsson.apex.concept.TestContextSchema000");
        AxContextSchema testContextSchema00A = new AxContextSchema(new AxArtifactKey("TestContextSchema00A", "0.0.1"), "JAVA",
                "com.ericsson.apex.concept.TestContextSchema00A");
        AxContextSchema testContextSchema00C = new AxContextSchema(new AxArtifactKey("TestContextSchema00C", "0.0.1"), "JAVA",
                "com.ericsson.apex.concept.TestContextSchema00C");

        AxContextModel contextModel = new AxContextModel(new AxArtifactKey("ContextModel", "0.0.1"));
        contextModel.getSchemas().getSchemasMap().put(testContextSchema000.getKey(), testContextSchema000);
        contextModel.getSchemas().getSchemasMap().put(testContextSchema00A.getKey(), testContextSchema00A);
        contextModel.getSchemas().getSchemasMap().put(testContextSchema00C.getKey(), testContextSchema00C);

        return contextModel;
    }

    public AxContextModel getSingleEntryModel() {
        AxContextSchema testContextSchema000 = new AxContextSchema(new AxArtifactKey("TestContextSchema000", "0.0.1"), "JAVA",
                "com.ericsson.apex.concept.TestContextSchema000");

        AxContextAlbum policy1ContextAlbum = new AxContextAlbum(new AxArtifactKey("Policy1ContextAlbum ", "0.0.1"), "APPLICATION", true,
                testContextSchema000.getKey());

        AxContextModel contextModel = new AxContextModel(new AxArtifactKey("ContextModel", "0.0.1"));
        contextModel.getSchemas().getSchemasMap().put(testContextSchema000.getKey(), testContextSchema000);

        contextModel.getAlbums().getAlbumsMap().put(policy1ContextAlbum.getKey(), policy1ContextAlbum);

        return contextModel;
    }

    public AxContextModel getNoGlobalContextModel() {
        AxContextSchema testContextSchema000 = new AxContextSchema(new AxArtifactKey("TestContextSchema000", "0.0.1"), "JAVA",
                "com.ericsson.apex.concept.TestContextSchema000");
        AxContextSchema testContextSchema00C = new AxContextSchema(new AxArtifactKey("TestContextSchema00C", "0.0.1"), "JAVA",
                "com.ericsson.apex.concept.TestContextSchema00C");

        AxContextAlbum externalContextAlbum = new AxContextAlbum(new AxArtifactKey("ExternalContextAlbum", "0.0.1"), "EXTERNAL", false,
                testContextSchema000.getKey());
        AxContextAlbum policy0ContextAlbum = new AxContextAlbum(new AxArtifactKey("Policy0ContextAlbum", "0.0.1"), "APPLICATION", true,
                testContextSchema00C.getKey());
        AxContextAlbum policy1ContextAlbum = new AxContextAlbum(new AxArtifactKey("Policy1ContextAlbum ", "0.0.1"), "APPLICATION", true,
                testContextSchema00C.getKey());

        AxContextModel contextModel = new AxContextModel(new AxArtifactKey("ContextModel", "0.0.1"));
        contextModel.getSchemas().getSchemasMap().put(testContextSchema000.getKey(), testContextSchema000);
        contextModel.getSchemas().getSchemasMap().put(testContextSchema00C.getKey(), testContextSchema00C);

        contextModel.getAlbums().getAlbumsMap().put(externalContextAlbum.getKey(), externalContextAlbum);
        contextModel.getAlbums().getAlbumsMap().put(policy0ContextAlbum.getKey(), policy0ContextAlbum);
        contextModel.getAlbums().getAlbumsMap().put(policy1ContextAlbum.getKey(), policy1ContextAlbum);

        return contextModel;
    }
}
