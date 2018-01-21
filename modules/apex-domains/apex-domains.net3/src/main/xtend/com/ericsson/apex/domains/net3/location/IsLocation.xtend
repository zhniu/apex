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

import com.ericsson.apex.domains.net3.basics.Net3DoesValidate

interface IsLocation extends Net3DoesValidate {

	def String getName()

	def String getDisplayName()

	def String getNamespace()

//	def String getAvroSchema()

//	def String asAvro()
}