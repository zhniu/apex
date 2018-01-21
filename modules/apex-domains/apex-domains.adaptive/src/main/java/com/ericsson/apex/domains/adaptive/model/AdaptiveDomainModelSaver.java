/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.adaptive.model;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.handling.ApexModelSaver;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;

/**
 * This class saves sample domain models to disk in XML and JSON format.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public final class AdaptiveDomainModelSaver {
    /**
     * Private default constructor to prevent subclassing.
     */
    private AdaptiveDomainModelSaver() {
    }

    /**
     * Write the AADM model to args[0].
     *
     * @param args Not used
     * @throws ApexException the apex exception
     */
    public static void main(final String[] args) throws ApexException {
        if (args.length != 1) {
            System.err.println("usage: " + AdaptiveDomainModelSaver.class.getCanonicalName() + " modelDirectory");
            return;
        }

        // Save Anomaly Detection model
        final AxPolicyModel adPolicyModel = new AdaptiveDomainModelFactory().getAnomalyDetectionPolicyModel();
        final ApexModelSaver<AxPolicyModel> adModelSaver = new ApexModelSaver<>(AxPolicyModel.class, adPolicyModel, args[0]);
        adModelSaver.apexModelWriteJSON();
        adModelSaver.apexModelWriteXML();

        // Save Auto Learn model
        final AxPolicyModel alPolicyModel = new AdaptiveDomainModelFactory().getAutoLearnPolicyModel();
        final ApexModelSaver<AxPolicyModel> alModelSaver = new ApexModelSaver<>(AxPolicyModel.class, alPolicyModel, args[0]);
        alModelSaver.apexModelWriteJSON();
        alModelSaver.apexModelWriteXML();
    }
}
