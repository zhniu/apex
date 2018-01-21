/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.net3.location.data

import org.junit.Assert
import org.junit.Test

class Test_GeanntCountryProvider {

	@Test
	def void test_FileSize(){
		val provider = GeanntCountryProvider.newInstance
		Assert.assertEquals(38, provider.getLocations.size)
		println("Geant Locations: " + provider.getLocations.size + " countries")
	}

	@Test
	def void test_ValidateEntries(){
		val provider = GeanntCountryProvider.newInstance
		for(country : provider.locations.values){
			country.validate
		}
	}
}