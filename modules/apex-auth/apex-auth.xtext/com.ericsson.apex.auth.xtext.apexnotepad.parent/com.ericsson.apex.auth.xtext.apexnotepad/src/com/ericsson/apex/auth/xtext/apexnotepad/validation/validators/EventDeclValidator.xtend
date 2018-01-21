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
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.EventDecl
import com.ericsson.apex.auth.xtext.utilities.ApexErrorCodes
import com.ericsson.apex.auth.xtext.utilities.ValidationReturn
import com.ericsson.apex.auth.xtext.utilities.validators.EObjectValidator
import com.ericsson.apex.auth.xtext.utilities.validators.ElementNameValidator
import java.util.ArrayList
import java.util.List

/**
 * Validates an EventDecl.
 * 
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
class EventDeclValidator {

	/**
	 * Main validation method.
	 * 
	 * @param decl the validation object
	 * @return validation results
	 */
	def validate(EventDecl decl){
		var List<ValidationReturn> ret = ArrayList.newInstance

		ElementNameValidator.newInstance.validateDeclID(
			decl.name,
			ret,
			ApexNotepadPackage.Literals::EVENT_DECL__NAME
		)

		ElementNameValidator.newInstance.validateNoVersion(
			decl.source,
			ret,
			ApexNotepadPackage.Literals::EVENT_DECL__SOURCE
		)

		ElementNameValidator.newInstance.validateNoVersion(
			decl.source,
			ret,
			ApexNotepadPackage.Literals::EVENT_DECL__TARGET
		)

		EObjectValidator.newInstance.validateListNull(
			decl.fields,
			ret,
			ApexNotepadPackage.Literals.EVENT_DECL__FIELDS,
			ApexErrorCodes::ERROR_EVENT_WO_FIELDS
		)

		return ret
	}

}