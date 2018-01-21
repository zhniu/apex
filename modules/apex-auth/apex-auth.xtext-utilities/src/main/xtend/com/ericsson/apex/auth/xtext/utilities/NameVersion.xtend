/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.auth.xtext.utilities

import org.eclipse.xtend.lib.annotations.Data
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

/**
 * A data object for name/version string.
 * 
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
@Data
@FinalFieldsConstructor
class NameVersion {
	String name
	String version

	/**
	 * Constructor for a name/version string
	 * 
	 * @param idversion the name/version string
	 */
	new(String idversion) {
		val idv = idversion.split(":")
		this.name = idv.get(0)
		if(idv.size > 1){
			this.version = idv.get(1)
		}
		else{
			this.version = null
		}
	}
}