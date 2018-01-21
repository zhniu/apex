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

class NetworkGeant {
	
}

/*
		super(
				"geant", "0.0.1",
				"eu.ict_arcfire.net.intnet.networks",
				"topology of the European GÉANT research network as of January 2017"
		);

		IT_NormalDif renum = this.createNormalDif("renumbering", "The renumbering DIF");

		this.createPtpDifGeoloc(AMSTERDAM, BERLIN);
		this.createPtpDifGeoloc(AMSTERDAM, COPENHAGEN);
		this.createPtpDifGeoloc(AMSTERDAM, FRANKFURT);
		this.createPtpDifGeoloc(AMSTERDAM, LUXEMBOURG);
		this.createPtpDifGeoloc(BERLIN, FRANKFURT);
		this.createPtpDifGeoloc(BERLIN, TALLINN);
		this.createPtpDifGeoloc(BERN, MARSEILLE);
		this.createPtpDifGeoloc(BERN, ROME);
		this.createPtpDifGeoloc(BRATISLAVA, BUDAPEST);
		this.createPtpDifGeoloc(BRATISLAVA, VIENNA);
		this.createPtpDifGeoloc(BRUSSELS, AMSTERDAM);
		this.createPtpDifGeoloc(BUCHAREST, CHISINAU);
		this.createPtpDifGeoloc(BUDAPEST, ANKARA);
		this.createPtpDifGeoloc(BUDAPEST, BELGRADE);
		this.createPtpDifGeoloc(BUDAPEST, BUCHAREST);
		this.createPtpDifGeoloc(BUDAPEST, PODGORICA);
		this.createPtpDifGeoloc(BUDAPEST, SOFIA);
		this.createPtpDifGeoloc(BUDAPEST, ZAGREB);
		this.createPtpDifGeoloc(COPENHAGEN, BERLIN);
		this.createPtpDifGeoloc(COPENHAGEN, OSLO);
		this.createPtpDifGeoloc(COPENHAGEN, REYKJAVIK);
		this.createPtpDifGeoloc(COPENHAGEN, STOCKHOLM);
		this.createPtpDifGeoloc(FRANKFURT, ANKARA);
		this.createPtpDifGeoloc(FRANKFURT, LUXEMBOURG);
		this.createPtpDifGeoloc(FRANKFURT, BUDAPEST);
		this.createPtpDifGeoloc(FRANKFURT, BERN);
		this.createPtpDifGeoloc(FRANKFURT, NIKOSIA);
		this.createPtpDifGeoloc(FRANKFURT, PRAGUE);
		this.createPtpDifGeoloc(FRANKFURT, VIENNA);
		this.createPtpDifGeoloc(FRANKFURT, WARSAW);
		this.createPtpDifGeoloc(KIEV, WARSAW);
		this.createPtpDifGeoloc(LISBON, MADRID);
		this.createPtpDifGeoloc(LJUBLJANA, ZAGREB);
		this.createPtpDifGeoloc(LONDON, BRUSSELS);
		this.createPtpDifGeoloc(LONDON, DUBLIN);
		this.createPtpDifGeoloc(LONDON, LISBON);
		this.createPtpDifGeoloc(LONDON, NIKOSIA);
		this.createPtpDifGeoloc(LONDON, PARIS);
		this.createPtpDifGeoloc(LONDON, REYKJAVIK);
		this.createPtpDifGeoloc(MADRID, MARSEILLE);
		this.createPtpDifGeoloc(MADRID, PARIS);
		this.createPtpDifGeoloc(MINSK, WARSAW);
		this.createPtpDifGeoloc(OSLO, STOCKHOLM);
		this.createPtpDifGeoloc(PARIS, BERN);
		this.createPtpDifGeoloc(PRAGUE, BUDAPEST);
		this.createPtpDifGeoloc(PRAGUE, WARSAW);
		this.createPtpDifGeoloc(RIGA, TALLINN);
		this.createPtpDifGeoloc(RIGA, VILNIUS);
		this.createPtpDifGeoloc(ROME, ATHENS);
		this.createPtpDifGeoloc(ROME, MARSEILLE);
		this.createPtpDifGeoloc(ROME, VALLETTA);
		this.createPtpDifGeoloc(ROME, VIENNA);
		this.createPtpDifGeoloc(SOFIA, BUCHAREST);
		this.createPtpDifGeoloc(SOFIA, SKOPJE);
		this.createPtpDifGeoloc(STOCKHOLM, HELSINKI);
		this.createPtpDifGeoloc(VIENNA, ATHENS);
		this.createPtpDifGeoloc(VIENNA, BUCHAREST);
		this.createPtpDifGeoloc(VIENNA, BUDAPEST);
		this.createPtpDifGeoloc(VIENNA, LJUBLJANA);
		this.createPtpDifGeoloc(VIENNA, SOFIA);
		this.createPtpDifGeoloc(VIENNA, ZAGREB);
		this.createPtpDifGeoloc(WARSAW, VILNIUS);

		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(AMSTERDAM), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(ANKARA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(ATHENS), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(BELGRADE), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(BERLIN), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(BERN), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(BRATISLAVA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(BRUSSELS), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(BUCHAREST), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(BUDAPEST), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(CHISINAU), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(COPENHAGEN), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(DUBLIN), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(FRANKFURT), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(HELSINKI), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(KIEV), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(LISBON), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(LJUBLJANA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(LONDON), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(LUXEMBOURG), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(MADRID), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(MARSEILLE), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(MINSK), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(NIKOSIA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(OSLO), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(PARIS), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(PODGORICA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(PRAGUE), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(REYKJAVIK), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(RIGA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(ROME), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(SKOPJE), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(SOFIA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(STOCKHOLM), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(TALLINN), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(VALLETTA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(VIENNA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(VILNIUS), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(WARSAW), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(ZAGREB), renum);

		this.build();
 */

/*
	@Test
	public void test_Constructor(){
		IT_Renumbering_Geant intnet = new IT_Renumbering_Geant();
		assertEquals(40, intnet.getNodes().size());
		assertEquals(1, intnet.getNormalDifs().size());
		assertEquals(62, intnet.getPtpDifs().size());
	}

	@Test
	public void test_Validation(){
		IT_Renumbering_Geant intnet = new IT_Renumbering_Geant();
		IT_Validator validator = new IT_Validator();

		validator.validate(intnet);
		assertEquals(0, validator.getWarnings().size());
		assertEquals(0, validator.getErrors().size());
	}
 */