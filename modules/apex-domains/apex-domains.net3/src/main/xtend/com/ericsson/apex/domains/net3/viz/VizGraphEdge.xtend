/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.net3.viz

import com.ericsson.apex.domains.net3.basics.Net3DoesValidate
import org.apache.commons.lang3.Validate
import org.eclipse.xtend.lib.annotations.Data

@Data class VizGraphEdge implements Net3DoesValidate {

	/** Edge name. */
	String name;

	/** Edge start. */
	String start;

	/** Edge end. */
	String end;

	override validate() {
		Validate.notBlank(name)
		Validate.notBlank(start)
		Validate.notBlank(end)
		true
	}
}