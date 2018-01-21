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

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxKeyInfo;
import com.ericsson.apex.model.basicmodel.concepts.AxKeyInformation;
import com.ericsson.apex.model.basicmodel.concepts.AxModel;
import com.ericsson.apex.model.basicmodel.test.TestApexModelCreator;

public class TestApexTestModelCreator0 implements TestApexModelCreator<AxModel> {

    @Override
    public AxModel getModel() {
        AxModel basicModel = new AxModel();

        basicModel.setKey(new AxArtifactKey("BasicModel", "0.0.1"));
        basicModel.setKeyInformation(new AxKeyInformation(new AxArtifactKey("KeyInfoMapKey", "0.0.1")));

        basicModel.getKeyInformation().getKeyInfoMap().put(basicModel.getKey(), new AxKeyInfo(basicModel.getKey()));
        basicModel.getKeyInformation().getKeyInfoMap().put(basicModel.getKeyInformation().getKey(), new AxKeyInfo(basicModel.getKeyInformation().getKey()));

        return basicModel;
    }

    @Override
    public final AxModel getMalstructuredModel() {
        return getModel();
    }

    @Override
    public final AxModel getObservationModel() {
        return getModel();
    }

    @Override
    public final AxModel getWarningModel() {
        return getModel();
    }

    @Override
    public final AxModel getInvalidModel() {
        return getModel();
    }
}
