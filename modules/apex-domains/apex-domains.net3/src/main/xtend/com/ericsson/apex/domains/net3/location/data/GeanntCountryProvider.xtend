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

import com.ericsson.apex.domains.net3.location.CountryLocation
import com.ericsson.apex.domains.net3.location.LocationProvider
import java.io.File
import java.io.FileReader

import static extension com.google.common.io.CharStreams.*

class GeanntCountryProvider implements LocationProvider<CountryLocation> {

	val namespace = "com.ericsson.apex.net3.location.geannt.country"

	val countries = <String, CountryLocation>newHashMap()

	public new(){
		val classLoader = getClass().getClassLoader();
		val file = new File(classLoader.getResource('com/ericsson/apex/domains/net3/location/data/geannt-countries.csv').getFile());
		val entries = new FileReader(file).readLines.map [ line |
			val segments = line.split(', ').iterator
			return new CountryLocation(
				segments.next,
				namespace,
				segments.next,
				segments.next,
				segments.next
			)
		]
		for (entry : entries){
			countries.put(entry.name, entry)
		}
	}

	override getNameSpace() {
		return namespace
	}

	override getLocation(String name) {
		return countries.get(name)
	}

	override getLocations() {
		return countries
	}

}