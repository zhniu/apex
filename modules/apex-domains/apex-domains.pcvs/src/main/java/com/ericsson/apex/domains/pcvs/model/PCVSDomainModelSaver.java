/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.pcvs.model;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.handling.ApexModelSaver;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;

/**
 * The Class PCVSDomainModelSaver.
 * 
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
public final class PCVSDomainModelSaver {

	/** Private constructor to prevent instantiation. */
	private PCVSDomainModelSaver() {
	}

	/**
	 * Write all PCVS models to args[0].
	 *
	 * @param args
	 *            uses <code>arg[0]</code> for directory information
	 * @throws ApexException
	 *             the apex exception
	 */
	public static void main(final String[] args) throws ApexException {
		if (args.length != 1) {
			System.err.println("usage: " + PCVSDomainModelSaver.class.getCanonicalName() + " modelDirectory");
			return;
		}

		AxPolicyModel pcvsPolicyModel = new PCVSDomainModelFactory().getPCVVpnSlaSPolicyModel();
		ApexModelSaver<AxPolicyModel> pcvsModelSaver = new ApexModelSaver<>(AxPolicyModel.class, pcvsPolicyModel,
				args[0] + "vpnsla/");
		pcvsModelSaver.apexModelWriteJSON();
		pcvsModelSaver.apexModelWriteXML();

	}
}
