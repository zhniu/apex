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

import java.io.File;

import com.ericsson.apex.auth.clieditor.ApexCLIEditorMain;
import com.ericsson.apex.model.basicmodel.concepts.ApexRuntimeException;
import com.ericsson.apex.model.basicmodel.handling.ApexModelReader;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;
import com.ericsson.apex.model.utilities.ResourceUtils;

/**
 * A factory for creating PCVSDomainModel objects.
 *
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
public class PCVSDomainModelFactory {

	/**
	 * Generates the PCVS VPN-SLA policy model from CLI commands and creates an APEX
	 * model.
	 * @param workingDirectory The working directory for the CLI editor for includes
	 *
	 * @return the PCVS VPN-SLA policy model
	 */
	public AxPolicyModel getPCVVpnSlaSPolicyModel(final String workingDirectory) {
		String path = "target/model-gen/PCVS/vpnsla";
		String file = "policy.json";
		String full = path + "/" + file;

		File pathFile = new File(path);
		pathFile.mkdirs();

		String[] args = new String[] {
				"-c",
				"src/main/resources/com/ericsson/apex/domains/PCVS/vpnsla/vpnsla.apex",
				"-wd",
				workingDirectory,
				"-o",
				full
		};
		
		ApexCLIEditorMain cliEditor = new ApexCLIEditorMain(args);
		if (cliEditor.getErrorCount() > 0) {
			throw new ApexRuntimeException("Apex CLI editor execution failed with " + cliEditor.getErrorCount() + " errors");
		}

		java.util.TimeZone.getTimeZone("gmt");
		try {
			final ApexModelReader<AxPolicyModel> reader = new ApexModelReader<>(AxPolicyModel.class);
			return reader.read(ResourceUtils.getResourceAsString(full));
		} catch (final Exception e) {
			throw new ApexRuntimeException("Failed to build PCVS SLA1 policy from path: " + full, e);
		}
	}

}