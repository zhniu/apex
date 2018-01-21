/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.auth.xtext.apexnotepad.ide

import com.ericsson.apex.auth.xtext.apexnotepad.ApexNotepadRuntimeModule
import com.ericsson.apex.auth.xtext.apexnotepad.ApexNotepadStandaloneSetup
import com.google.inject.Guice
import org.eclipse.xtext.util.Modules2

/**
 * Support for initializing running XText languages as a language service.
 * 
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
class ApexNotepadIdeSetup extends ApexNotepadStandaloneSetup {

	override createInjector() {
		Guice.createInjector(Modules2.mixin(new ApexNotepadRuntimeModule, new ApexNotepadIdeModule))
	}
	
}
