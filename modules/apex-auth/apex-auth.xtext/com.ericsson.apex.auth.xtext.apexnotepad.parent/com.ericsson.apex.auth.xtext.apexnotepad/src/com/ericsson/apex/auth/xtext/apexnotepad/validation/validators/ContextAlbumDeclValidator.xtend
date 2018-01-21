/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.auth.xtext.apexnotepad.validation.validators

import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.ApexNotepadPackage
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.ContextAlbumDecl
import com.ericsson.apex.auth.xtext.utilities.ValidationReturn
import com.ericsson.apex.auth.xtext.utilities.validators.ElementNameValidator
import java.util.ArrayList
import java.util.List

/**
 * Validates a ContextAlbumDecl.
 * 
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
class ContextAlbumDeclValidator {

	/**
	 * Main validation method.
	 * 
	 * @param album the validation object
	 * @return validation results
	 */
	def validate(ContextAlbumDecl album){
		var List<ValidationReturn> ret = ArrayList.newInstance

		ElementNameValidator.newInstance.validateDeclID(
			album.name,
			ret,
			ApexNotepadPackage.Literals::CONTEXT_ALBUM_DECL__NAME
		)

		return ret
	}

}