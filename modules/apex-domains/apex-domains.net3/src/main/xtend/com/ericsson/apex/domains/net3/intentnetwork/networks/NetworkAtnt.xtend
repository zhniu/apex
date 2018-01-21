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

class NetworkAtnt {
	
}

/*
		super(
				"att", "0.0.1",
				"eu.ict_arcfire.net.intnet.networks",
				"topology of the AT&T managed services network as of May 2017"
		);

		IT_NormalDif renum = this.createNormalDif("renumbering", "The renumbering DIF");

		this.createPtpDifGeoloc(ALBANY_NY, NEW_YORK_NY);
		this.createPtpDifGeoloc(ALBUQUERQUE_NM, PHOENIX_AZ);
		this.createPtpDifGeoloc(ANAHEIM_CA, LOS_ANGELES_CA);
		this.createPtpDifGeoloc(ANCHORAGE_AK, SEATTLE_WA);
		this.createPtpDifGeoloc(ASHBURN_VA, WASHINGTON_DC);
		this.createPtpDifGeoloc(ATLANTA_GA, DALLAS_TX);
		this.createPtpDifGeoloc(ATLANTA_GA, ST_LOUIS_MO);
		this.createPtpDifGeoloc(AUSTIN_TX, DALLAS_TX);
		this.createPtpDifGeoloc(AUSTIN_TX, HOUSTON_TX);
		this.createPtpDifGeoloc(BALTIMORE_MD, WASHINGTON_DC);
		this.createPtpDifGeoloc(BIRMINGHAM_AL, ATLANTA_GA);
		this.createPtpDifGeoloc(BIRMINGHAM_MI, DETROIT_MI);
		this.createPtpDifGeoloc(BOHEMIA_NY, NEW_YORK_NY);
		this.createPtpDifGeoloc(BRIDGEPORT_CT, NEW_YORK_NY);
		this.createPtpDifGeoloc(BUFFALO_NY, NEW_YORK_NY);
		this.createPtpDifGeoloc(CAMBRIDGE_MA, NEW_YORK_NY);
		this.createPtpDifGeoloc(CAMDEN_NJ, PHILADELPHIA_PA);
		this.createPtpDifGeoloc(CHARLOTTE_NC, ATLANTA_GA);
		this.createPtpDifGeoloc(CHESHIRE_CT, NEW_YORK_NY);
		this.createPtpDifGeoloc(CHICAGO_IL, ST_LOUIS_MO);
		this.createPtpDifGeoloc(CINCINNATI_OH, DETROIT_MI);
		this.createPtpDifGeoloc(CLEVELAND_OH, DETROIT_MI);
		this.createPtpDifGeoloc(COLORADO_SPRINGS_CO, DENVER_CO);
		this.createPtpDifGeoloc(COLUMBIA_OH, CHICAGO_IL);
		this.createPtpDifGeoloc(COLUMBIA_SC, ATLANTA_GA);
		this.createPtpDifGeoloc(DALLAS_TX, LOS_ANGELES_CA);
		this.createPtpDifGeoloc(DALLAS_TX, ST_LOUIS_MO);
		this.createPtpDifGeoloc(DAVENPORT_IA, CHICAGO_IL);
		this.createPtpDifGeoloc(DENVER_CO, DALLAS_TX);
		this.createPtpDifGeoloc(DENVER_CO, SAN_FRANCISCO_CA);
		this.createPtpDifGeoloc(DES_MOINE_IA, ST_LOUIS_MO);
		this.createPtpDifGeoloc(DETROIT_MI, CHICAGO_IL);
		this.createPtpDifGeoloc(DUNWOODY_GA, ATLANTA_GA);
		this.createPtpDifGeoloc(FLORISSANT_MO, ST_LOUIS_MO);
		this.createPtpDifGeoloc(FORT_LAUDERDALE_FL, ORLANDO_FL);
		this.createPtpDifGeoloc(FORT_LAUDERDALE_FL, TAMPA_FL);
		this.createPtpDifGeoloc(FORT_WORTH_TX, DALLAS_TX);
		this.createPtpDifGeoloc(FRAMINGHAM_MA, CAMBRIDGE_MA);
		this.createPtpDifGeoloc(FREEHOLD_NJ, NEW_YORK_NY);
		this.createPtpDifGeoloc(GARDENA_CA, LOS_ANGELES_CA);
		this.createPtpDifGeoloc(GLENVIEW_IL, CHICAGO_IL);
		this.createPtpDifGeoloc(GRAND_RAPIDS_MI, CHICAGO_IL);
		this.createPtpDifGeoloc(GREENSBORO_NC, WASHINGTON_DC);
		this.createPtpDifGeoloc(HARRISBURG_PA, PHILADELPHIA_PA);
		this.createPtpDifGeoloc(HARTFORD_CT, NEW_YORK_NY);
		this.createPtpDifGeoloc(HONOLULU_HI, SAN_FRANCISCO_CA);
		this.createPtpDifGeoloc(HOUSTON_TX, DALLAS_TX);
		this.createPtpDifGeoloc(HOUSTON_TX, ORLANDO_FL);
		this.createPtpDifGeoloc(INDIANAPOLIS_IN, CHICAGO_IL);
		this.createPtpDifGeoloc(JACKSONVILLE_FL, ORLANDO_FL);
		this.createPtpDifGeoloc(KANSAS_CITY_MO, OMAHA_NE);
		this.createPtpDifGeoloc(KANSAS_CITY_MO, ST_LOUIS_MO);
		this.createPtpDifGeoloc(LAS_VEGAS_NV, LOS_ANGELES_CA);
		this.createPtpDifGeoloc(LITTLE_ROCK_AR, ST_LOUIS_MO);
		this.createPtpDifGeoloc(LOS_ANGELES_CA, SAN_JOSE_CA);
		this.createPtpDifGeoloc(LOS_ANGELES_CA, ST_LOUIS_MO);
		this.createPtpDifGeoloc(LOUISVILLE_KY, ST_LOUIS_MO);
		this.createPtpDifGeoloc(MADISON_WI, CHICAGO_IL);
		this.createPtpDifGeoloc(MANCHESTER_NH, CAMBRIDGE_MA);
		this.createPtpDifGeoloc(MEMPHIS_TN, ST_LOUIS_MO);
		this.createPtpDifGeoloc(MIAMI_FL, ORLANDO_FL);
		this.createPtpDifGeoloc(MILWAUKEE_WI, CHICAGO_IL);
		this.createPtpDifGeoloc(MINNEAPOLIS_MN, CHICAGO_IL);
		this.createPtpDifGeoloc(MINNEAPOLIS_MN, OMAHA_NE);
		this.createPtpDifGeoloc(NASHVILLE_TN, ATLANTA_GA);
		this.createPtpDifGeoloc(NEWARK_NJ, NEW_YORK_NY);
		this.createPtpDifGeoloc(NEW_BRUNSWICK_NJ, NEW_YORK_NY);
		this.createPtpDifGeoloc(NEW_ORLEANS_LA, HOUSTON_TX);
		this.createPtpDifGeoloc(NEW_YORK_NY, CHICAGO_IL);
		this.createPtpDifGeoloc(NORCROSS_GA, ATLANTA_GA);
		this.createPtpDifGeoloc(NORFOLK_VA, WASHINGTON_DC);
		this.createPtpDifGeoloc(OAKLAND_CA, SAN_FRANCISCO_CA);
		this.createPtpDifGeoloc(OAK_BROOK_IL, CHICAGO_IL);
		this.createPtpDifGeoloc(OKLAHOMA_CITY_OK, DALLAS_TX);
		this.createPtpDifGeoloc(OMAHA_NE, ST_LOUIS_MO);
		this.createPtpDifGeoloc(ORLANDO_FL, ATLANTA_GA);
		this.createPtpDifGeoloc(PHILADELPHIA_PA, DETROIT_MI);
		this.createPtpDifGeoloc(PHILADELPHIA_PA, NEW_YORK_NY);
		this.createPtpDifGeoloc(PHOENIX_AZ, DALLAS_TX);
		this.createPtpDifGeoloc(PITTSBURGH_PA, DETROIT_MI);
		this.createPtpDifGeoloc(PITTSBURGH_PA, PHILADELPHIA_PA);
		this.createPtpDifGeoloc(PORTLAND_ME, CAMBRIDGE_MA);
		this.createPtpDifGeoloc(PORTLAND_OR, SEATTLE_WA);
		this.createPtpDifGeoloc(PROVIDENCE_RI, CAMBRIDGE_MA);
		this.createPtpDifGeoloc(RALEIGH_NC, ATLANTA_GA);
		this.createPtpDifGeoloc(REDWOOD_CITY_CA, SAN_FRANCISCO_CA);
		this.createPtpDifGeoloc(RICHMOND_VA, WASHINGTON_DC);
		this.createPtpDifGeoloc(ROCHESTER_NY, NEW_YORK_NY);
		this.createPtpDifGeoloc(ROLLING_MEADOWS_IL, CHICAGO_IL);
		this.createPtpDifGeoloc(SACRAMENTO_CA, SALT_LAKE_CITY_UH);
		this.createPtpDifGeoloc(SACRAMENTO_CA, SAN_FRANCISCO_CA);
		this.createPtpDifGeoloc(SAINT_PAUL_MN, CHICAGO_IL);
		this.createPtpDifGeoloc(SALT_LAKE_CITY_UH, DENVER_CO);
		this.createPtpDifGeoloc(SAN_ANTONIO_TX, AUSTIN_TX);
		this.createPtpDifGeoloc(SAN_BERNADINO_CA, LOS_ANGELES_CA);
		this.createPtpDifGeoloc(SAN_DIEGO_CA, LOS_ANGELES_CA);
		this.createPtpDifGeoloc(SAN_DIEGO_CA, PHOENIX_AZ);
		this.createPtpDifGeoloc(SAN_FRANCISCO_CA, CHICAGO_IL);
		this.createPtpDifGeoloc(SAN_FRANCISCO_CA, SAN_JOSE_CA);
		this.createPtpDifGeoloc(SAN_JUAN_PR, ORLANDO_FL);
		this.createPtpDifGeoloc(SEATTLE_WA, CHICAGO_IL);
		this.createPtpDifGeoloc(SEATTLE_WA, SAN_FRANCISCO_CA);
		this.createPtpDifGeoloc(SECAUCUS_NJ, NEW_YORK_NY);
		this.createPtpDifGeoloc(SHERMAN_OAKS_CA, LOS_ANGELES_CA);
		this.createPtpDifGeoloc(SOUTH_BEND_IN, CHICAGO_IL);
		this.createPtpDifGeoloc(SPOKANE_WA, SEATTLE_WA);
		this.createPtpDifGeoloc(SPRINGFIELD_MO, ST_LOUIS_MO);
		this.createPtpDifGeoloc(ST_LOUIS_MO, SAN_FRANCISCO_CA);
		this.createPtpDifGeoloc(SYRACUSE_NY, NEW_YORK_NY);
		this.createPtpDifGeoloc(TAMPA_FL, ORLANDO_FL);
		this.createPtpDifGeoloc(TULSA_OK, DALLAS_TX);
		this.createPtpDifGeoloc(WASHINGTON_DC, ATLANTA_GA);
		this.createPtpDifGeoloc(WASHINGTON_DC, RALEIGH_NC);
		this.createPtpDifGeoloc(WASHINGTON_DC, ST_LOUIS_MO);
		this.createPtpDifGeoloc(WEST_PALM_BEACH_FL, ORLANDO_FL);
		this.createPtpDifGeoloc(WHITE_PLAINS_NY, NEW_YORK_NY);
		this.createPtpDifGeoloc(WORCESTER_MA, CAMBRIDGE_MA);

		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(ALBANY_NY), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(ALBUQUERQUE_NM), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(ANAHEIM_CA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(ANCHORAGE_AK), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(ASHBURN_VA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(ATLANTA_GA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(AUSTIN_TX), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(BALTIMORE_MD), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(BIRMINGHAM_AL), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(BIRMINGHAM_MI), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(BOHEMIA_NY), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(BRIDGEPORT_CT), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(BUFFALO_NY), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(CAMBRIDGE_MA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(CAMDEN_NJ), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(CHARLOTTE_NC), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(CHESHIRE_CT), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(CHICAGO_IL), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(CINCINNATI_OH), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(CLEVELAND_OH), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(COLORADO_SPRINGS_CO), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(COLUMBIA_OH), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(COLUMBIA_SC), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(DALLAS_TX), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(DAVENPORT_IA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(DENVER_CO), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(DES_MOINE_IA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(DETROIT_MI), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(DUNWOODY_GA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(FLORISSANT_MO), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(FORT_LAUDERDALE_FL), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(FORT_WORTH_TX), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(FRAMINGHAM_MA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(FREEHOLD_NJ), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(GARDENA_CA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(GLENVIEW_IL), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(GRAND_RAPIDS_MI), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(GREENSBORO_NC), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(HARRISBURG_PA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(HARTFORD_CT), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(HONOLULU_HI), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(HOUSTON_TX), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(INDIANAPOLIS_IN), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(JACKSONVILLE_FL), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(KANSAS_CITY_MO), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(LAS_VEGAS_NV), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(LITTLE_ROCK_AR), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(LOS_ANGELES_CA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(LOUISVILLE_KY), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(MADISON_WI), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(MANCHESTER_NH), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(MEMPHIS_TN), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(MIAMI_FL), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(MILWAUKEE_WI), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(MINNEAPOLIS_MN), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(NASHVILLE_TN), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(NEWARK_NJ), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(NEW_BRUNSWICK_NJ), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(NEW_ORLEANS_LA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(NEW_YORK_NY), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(NORCROSS_GA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(NORFOLK_VA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(OAKLAND_CA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(OAK_BROOK_IL), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(OKLAHOMA_CITY_OK), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(OMAHA_NE), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(ORLANDO_FL), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(PHILADELPHIA_PA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(PHOENIX_AZ), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(PITTSBURGH_PA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(PORTLAND_ME), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(PORTLAND_OR), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(PROVIDENCE_RI), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(RALEIGH_NC), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(REDWOOD_CITY_CA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(RICHMOND_VA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(ROCHESTER_NY), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(ROLLING_MEADOWS_IL), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(SACRAMENTO_CA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(SAINT_PAUL_MN), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(SALT_LAKE_CITY_UH), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(SAN_ANTONIO_TX), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(SAN_BERNADINO_CA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(SAN_DIEGO_CA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(SAN_FRANCISCO_CA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(SAN_JOSE_CA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(SAN_JUAN_PR), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(SEATTLE_WA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(SECAUCUS_NJ), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(SHERMAN_OAKS_CA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(SOUTH_BEND_IN), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(SPOKANE_WA), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(SPRINGFIELD_MO), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(ST_LOUIS_MO), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(SYRACUSE_NY), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(TAMPA_FL), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(TULSA_OK), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(WASHINGTON_DC), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(WEST_PALM_BEACH_FL), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(WHITE_PLAINS_NY), renum);
		this.doNodePtpRegistrationGeoloc(this.createNodeGeoloc(WORCESTER_MA), renum);

		this.build();
 */

/*
	@Test
	public void test_Constructor(){
		IT_Renumbering_ATnT intnet = new IT_Renumbering_ATnT();
		assertEquals(101, intnet.getNodes().size());
		assertEquals(1, intnet.getNormalDifs().size());
		assertEquals(117, intnet.getPtpDifs().size());
	}

	@Test
	public void test_Validation(){
		IT_Renumbering_ATnT intnet = new IT_Renumbering_ATnT();
		IT_Validator validator = new IT_Validator();

		validator.validate(intnet);
		assertEquals(0, validator.getWarnings().size());
		assertEquals(0, validator.getErrors().size());
	}
 */