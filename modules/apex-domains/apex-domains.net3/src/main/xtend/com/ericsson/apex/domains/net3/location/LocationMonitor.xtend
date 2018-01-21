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

import java.util.List
import org.eclipse.xtend.lib.annotations.Data

@Data class LocationMonitor {
	IsLocation original
	List<IsLocation> lifeTime

	/**
	 * Adds a new location to the life time
	 * 
	 * @param location the new location, ignored if null
	 * @return true on success, false otherwise
	 */
	def addLifeTime(IsLocation location){
		if(location !== null) lifeTime.add(location)
	}
}
