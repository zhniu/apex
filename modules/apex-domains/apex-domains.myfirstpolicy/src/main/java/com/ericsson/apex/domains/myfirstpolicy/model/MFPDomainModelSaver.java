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

import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.handling.ApexModelSaver;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;

/**
 * The Class MFPDomainModelSaver.
 * @author John Keeney (john.keeney@ericsson.com)
 */
public final class MFPDomainModelSaver {

    /** Private constructor to prevent instantiation. */
    private MFPDomainModelSaver() { }
    /**
     * Write the MyFirstPolicy model to args[0].
     *
     * @param args Not used
     * @throws ApexException the apex exception
     */
    public static void main(final String[] args) throws ApexException {
        if (args.length != 1) {
            System.err.println("usage: " + MFPDomainModelSaver.class.getCanonicalName() + " modelDirectory");
            return;
        }

        // Save Java model
        AxPolicyModel mfpPolicyModel = new MFPDomainModelFactory().getMFP1PolicyModel();
        ApexModelSaver<AxPolicyModel> mfpModelSaver = new ApexModelSaver<>(AxPolicyModel.class, mfpPolicyModel, args[0] + "/1/");
        mfpModelSaver.apexModelWriteJSON();
        mfpModelSaver.apexModelWriteXML();

        mfpPolicyModel = new MFPDomainModelFactory().getMFP2PolicyModel();
        mfpModelSaver = new ApexModelSaver<>(AxPolicyModel.class, mfpPolicyModel, args[0] + "/2/");
        mfpModelSaver.apexModelWriteJSON();
        mfpModelSaver.apexModelWriteXML();

    }
}
