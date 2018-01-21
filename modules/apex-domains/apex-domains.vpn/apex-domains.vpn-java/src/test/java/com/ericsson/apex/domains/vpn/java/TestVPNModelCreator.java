/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.vpn.java;

import com.ericsson.apex.domains.vpn.java.model.VPNDomainModelFactory;
import com.ericsson.apex.model.basicmodel.test.TestApexModelCreator;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestVPNModelCreator implements TestApexModelCreator<AxPolicyModel> {

	@Override
	public AxPolicyModel getModel() {
		return new VPNDomainModelFactory().getVPNPolicyModel();
	}

	@Override
	public AxPolicyModel getMalstructuredModel() {
		return null;
	}

	@Override
	public AxPolicyModel getObservationModel() {
		return null;
	}

	@Override
	public AxPolicyModel getWarningModel() {
		return getModel();
	}

	@Override
	public AxPolicyModel getInvalidModel() {
		return null;
	}
}
