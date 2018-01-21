/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.net3.location

import com.ericsson.apex.domains.net3.location.CountryLocation
import com.ericsson.apex.domains.net3.location.LocationLatLong
import com.ericsson.apex.domains.net3.location.SimpleLocation
import org.apache.commons.lang3.Validate
import org.eclipse.xtend.lib.annotations.Data

@Data class CityLocation extends SimpleLocation {
	String localName
	String acronym
	CountryLocation country
	LocationLatLong latlong

	override validate() {
		super.validate()

		Validate.notBlank(localName)
		Validate.notBlank(acronym)

		Validate.notNull(country)
		country.validate

		Validate.notNull(latlong)
		latlong.validate

		true
	}
}