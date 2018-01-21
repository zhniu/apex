/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.net3.basics

import org.junit.Assert
import org.junit.Test

class Test_Identity {

	@Test
	def void testOK(){
		val id1 = new Net3Identity(
			"name1",
			"ns1",
			"1.2.3",
			"descr1"
		)
		id1.validate

		Assert.assertEquals(1, id1.major)
		Assert.assertEquals(2, id1.minor)
		Assert.assertEquals(3, id1.patch)
	}
}
