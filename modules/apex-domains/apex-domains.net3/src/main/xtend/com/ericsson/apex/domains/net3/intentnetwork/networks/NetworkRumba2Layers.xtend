/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.net3.intentnetwork.networks

class NetworkRumba2Layers {
	
}

/*
		super(
				"r-2layers", "0.0.1",
				"eu.ict_arcfire.net.intnet.networks",
				"topology of the Rumba example 2-layers"
		);

		IT_NormalDif n1 = this.createNormalDif("n1", "N1 DIF");
		IT_NormalDif n2 = this.createNormalDif("n2", "N1 DIF");
		IT_NormalDif n3 = this.createNormalDif("n3", "N1 DIF");
		IT_NormalDif n4 = this.createNormalDif("n4", "N1 DIF");

		this.createPtpDifLinkname("a", "b");
		IT_PtpDif e2 = this.createPtpDifLinkname("b", "c");
		IT_PtpDif e3 = this.createPtpDifLinkname("c", "d");

		IT_Node node;

		node = this.createNodeLinkname("a");
		node.addRegistration(n4, n1);
		node.addRegistration(n3, n1);
		this.doNodePtpRegistrationLinkname(node, n1);

		node = this.createNodeLinkname("b");
		this.doNodePtpRegistrationLinkname(node, n1);

		node = this.createNodeLinkname("c");
		node.addRegistration(n4, n1);
		node.addRegistration(n3, n1, n2);
		node.addRegistration(n1, e2);
		node.addRegistration(n2, e3);

		node = this.createNodeLinkname("d");
		node.addRegistration(n3, n2);
		this.doNodePtpRegistrationLinkname(node, n2);

		this.build();
 */

/*
	@Test
	public void test_Constructor(){
		IT_Rumba_2Layers intnet = new IT_Rumba_2Layers();
		assertEquals(4, intnet.getNodes().size());
		assertEquals(4, intnet.getNormalDifs().size());
		assertEquals(3, intnet.getPtpDifs().size());
	}

	@Test
	public void test_Validation(){
		IT_Rumba_2Layers intnet = new IT_Rumba_2Layers();
		IT_Validator validator = new IT_Validator();

		validator.validate(intnet);
		assertEquals(0, validator.getWarnings().size());
		assertEquals(0, validator.getErrors().size());
	}
 */