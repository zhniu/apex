/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.vpn.avro.model;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.handling.ApexModelSaver;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;

/**
 * This class saves sample domain models to disk in XML and JSON format.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public final class VPNDomainModelSaver {
    /**
     * Private default constructor to prevent subclassing.
     */
    private VPNDomainModelSaver() {
    }

    /**
     * Write the VPM model to args[0].
     *
     * @param args Not used
     * @throws ApexException the apex exception
     */
    public static void main(final String[] args) throws ApexException {
        if (args.length != 1) {
            System.err.println("usage: " + VPNDomainModelSaver.class.getCanonicalName() + " modelDirectory");
            return;
        }

        // Save VPN model
        final AxPolicyModel vpnPolicyModel = new VPNDomainModelFactory().getVPNPolicyModel();
        final ApexModelSaver<AxPolicyModel> vpnModelSaver = new ApexModelSaver<>(AxPolicyModel.class, vpnPolicyModel, args[0]);
        vpnModelSaver.apexModelWriteJSON();
        vpnModelSaver.apexModelWriteXML();
    }
}
