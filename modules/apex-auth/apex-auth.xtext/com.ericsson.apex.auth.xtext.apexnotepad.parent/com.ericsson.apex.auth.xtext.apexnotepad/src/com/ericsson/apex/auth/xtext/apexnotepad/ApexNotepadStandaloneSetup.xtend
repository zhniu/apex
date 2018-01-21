/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.auth.xtext.apexnotepad

/**
 * Initialization support for running Xtext languages without Equinox extension registry.
 * 
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
class ApexNotepadStandaloneSetup extends ApexNotepadStandaloneSetupGenerated {

	def static void doSetup() {
		new ApexNotepadStandaloneSetup().createInjectorAndDoEMFRegistration()
	}
}
