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

import com.ericsson.apex.domains.net3.location.CityLocation
import com.ericsson.apex.domains.net3.location.LocationLatLong
import com.ericsson.apex.domains.net3.location.LocationProvider
import java.io.File
import java.io.FileReader

import static extension com.google.common.io.CharStreams.*

class GeanntCityProvider implements LocationProvider<CityLocation> {

	val namespace = "com.ericsson.apex.net3.location.geannt.city"

	val cities = <String, CityLocation>newHashMap()

	public new(){
		val GeanntCountryProvider countries = GeanntCountryProvider.newInstance
		val classLoader = getClass().getClassLoader();
		val file = new File(classLoader.getResource('com/ericsson/apex/domains/net3/location/data/geannt-cities.csv').getFile());
		val entries = new FileReader(file).readLines.map [ line |
			val segments = line.split(', ').iterator
			return new CityLocation(
				segments.next,
				namespace,
				segments.next, 
				segments.next,
				segments.next,
				countries.getLocation(segments.next),
				new LocationLatLong(
					Double.parseDouble(segments.next),
					Double.parseDouble(segments.next)
				)
			)
		]
		for (entry : entries){
			cities.put(entry.name, entry)
		}
	}

	override getNameSpace() {
		return namespace
	}

	override getLocation(String name) {
		return cities.get(name)
	}

	override getLocations() {
		return cities
	}
}