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

public class TestApexTestModelCreator1 implements TestApexModelCreator<AxModel> {

    @Override
    public AxModel getModel() {
        return getInvalidModel();
    }

    @Override
    public final AxModel getMalstructuredModel() {
        return getInvalidModel();
    }

    @Override
    public final AxModel getObservationModel() {
        return getInvalidModel();
    }

    @Override
    public final AxModel getWarningModel() {
        return getInvalidModel();
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
}
