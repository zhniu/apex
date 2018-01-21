/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.myfirstpolicy.model;

import com.ericsson.apex.model.basicmodel.concepts.ApexRuntimeException;
import com.ericsson.apex.model.basicmodel.handling.ApexModelReader;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;
import com.ericsson.apex.model.utilities.ResourceUtils;

/**
 * A factory for creating MFPDomainModel objects.
 */
public class MFPDomainModelFactory {
    private static final String MFP1PATH = "examples/models/MyFirstPolicy/1/MyFirstPolicyModel_0.0.1.json";
    private static final String MFP1_ALT_PATH = "examples/models/MyFirstPolicy/1/MyFirstPolicyModel_0.0.1.alt.json";
    private static final String MFP2PATH = "examples/models/MyFirstPolicy/2/MyFirstPolicyModel_0.0.1.json";
    /**
     * Gets the MyFirstPolicy#1 policy model.
     *
     * @return the MyFirstPolicy#1 policy model
     */
    public AxPolicyModel getMFP1PolicyModel() {
        java.util.TimeZone.getTimeZone("gmt");
        try {
            final ApexModelReader<AxPolicyModel> reader = new ApexModelReader<>(AxPolicyModel.class);
            return reader.read(ResourceUtils.getResourceAsString(MFPDomainModelFactory.MFP1PATH));
        }
        catch (final Exception e) {
            throw new ApexRuntimeException("Failed to build MyFirstPolicy from path: " + MFPDomainModelFactory.MFP1PATH, e);
        }
    }

    /**
     * Gets the MyFirstPolicy#1 policy model, with alternate JavaScript task logic.
     *
     * @return the MyFirstPolicy#1 policy model
     */
    public AxPolicyModel getMFP1AltPolicyModel() {
        java.util.TimeZone.getTimeZone("gmt");
        try {
            final ApexModelReader<AxPolicyModel> reader = new ApexModelReader<>(AxPolicyModel.class);
            return reader.read(ResourceUtils.getResourceAsString(MFPDomainModelFactory.MFP1_ALT_PATH));
        }
        catch (final Exception e) {
            throw new ApexRuntimeException("Failed to build MyFirstPolicy_ALT from path: " + MFPDomainModelFactory.MFP1_ALT_PATH, e);
        }
    }

    /**
     * Gets the MyFirstPolicy#1 policy model.
     *
     * @return the MyFirstPolicy#1 policy model
     */
    public AxPolicyModel getMFP2PolicyModel() {
        try {
            final ApexModelReader<AxPolicyModel> reader = new ApexModelReader<>(AxPolicyModel.class);
            return reader.read(ResourceUtils.getResourceAsString(MFPDomainModelFactory.MFP2PATH));
        }
        catch (final Exception e) {
            throw new ApexRuntimeException("Failed to build MyFirstPolicy from path: " + MFPDomainModelFactory.MFP2PATH, e);
        }
    }

}
