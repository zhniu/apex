/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.basicmodel.handling;

import java.util.UUID;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxKeyInfo;
import com.ericsson.apex.model.basicmodel.concepts.AxKeyInformation;
import com.ericsson.apex.model.basicmodel.concepts.AxModel;
import com.ericsson.apex.model.basicmodel.test.TestApexModelCreator;

public class TestApexBasicModelCreator implements TestApexModelCreator<AxModel> {

    @Override
    public AxModel getModel() {
        AxModel basicModel = new AxModel();

        basicModel.setKey(new AxArtifactKey("BasicModel", "0.0.1"));
        basicModel.setKeyInformation(new AxKeyInformation(new AxArtifactKey("KeyInfoMapKey", "0.0.1")));

        basicModel.getKeyInformation().getKeyInfoMap().put(basicModel.getKey(), new AxKeyInfo(basicModel.getKey()));
        basicModel.getKeyInformation().getKeyInfoMap().put(basicModel.getKeyInformation().getKey(), new AxKeyInfo(basicModel.getKeyInformation().getKey()));

        AxKeyInfo intKI = new AxKeyInfo(new AxArtifactKey("IntegerKIKey", "0.0.1"), UUID.randomUUID(), "IntegerKIKey description");
        basicModel.getKeyInformation().getKeyInfoMap().put(intKI.getKey(), new AxKeyInfo(intKI.getKey()));

        AxKeyInfo floatKI = new AxKeyInfo(new AxArtifactKey("FloatKIKey", "0.0.1"), UUID.randomUUID(), "FloatKIKey description");
        basicModel.getKeyInformation().getKeyInfoMap().put(floatKI.getKey(), new AxKeyInfo(floatKI.getKey()));

        return basicModel;
    }

    @Override
    public final AxModel getMalstructuredModel() {
        AxModel basicModel = new AxModel();

        // Note: No Data types
        basicModel.setKey(new AxArtifactKey("BasicModelKey", "0.0.1"));
        basicModel.setKeyInformation(new AxKeyInformation(new AxArtifactKey("KeyInfoMapKey", "0.0.1")));

        basicModel.getKeyInformation().getKeyInfoMap().put(
                basicModel.getKey(),
                new AxKeyInfo(
                        basicModel.getKey(),
                        UUID.fromString("00000000-0000-0000-0000-000000000000"),
                        "\nbasic model description\nThis is a multi line description\nwith another line of text."));

        return basicModel;
    }

    @Override
    public final AxModel getObservationModel() {
        AxModel basicModel = getModel();

        // Set key information as blank
        basicModel.getKeyInformation().getKeyInfoMap().get(basicModel.getKey()).setDescription("");

        return basicModel;
    }

    @Override
    public final AxModel getWarningModel() {
        AxModel basicModel = getModel();

        // Add unreferenced key information
        AxKeyInfo unreferencedKeyInfo0 = new AxKeyInfo(new AxArtifactKey("Unref0", "0.0.1"));
        AxKeyInfo unreferencedKeyInfo1 = new AxKeyInfo(new AxArtifactKey("Unref1", "0.0.1"));

        basicModel.getKeyInformation().getKeyInfoMap().put(unreferencedKeyInfo0.getKey(), unreferencedKeyInfo0);
        basicModel.getKeyInformation().getKeyInfoMap().put(unreferencedKeyInfo1.getKey(), unreferencedKeyInfo1);

        return basicModel;
    }

    @Override
    public final AxModel getInvalidModel() {
        AxModel basicModel = new AxModel();

        basicModel.setKey(new AxArtifactKey("BasicModelKey", "0.0.1"));
        basicModel.setKeyInformation(new AxKeyInformation(new AxArtifactKey("KeyInfoMapKey", "0.0.1")));

        basicModel.getKeyInformation().getKeyInfoMap().put(
                basicModel.getKey(),
                new AxKeyInfo(
                        basicModel.getKey(),
                        UUID.fromString("00000000-0000-0000-0000-000000000000"),
                        "nbasic model description\nThis is a multi line description\nwith another line of text."));
        basicModel.getKeyInformation().getKeyInfoMap().put(
                basicModel.getKeyInformation().getKey(),
                new AxKeyInfo(
                        basicModel.getKeyInformation().getKey(),
                        UUID.fromString("00000000-0000-0000-0000-000000000000"),
                        ""));

        return basicModel;
    }
    
    public final AxModelWithReferences getModelWithReferences() {
        AxModel model = getModel();
        
        AxModelWithReferences modelWithReferences = new AxModelWithReferences(model.getKey());
        modelWithReferences.setKeyInformation(model.getKeyInformation());
        modelWithReferences.setReferenceKeyList();
        
        return modelWithReferences;
    }
}
