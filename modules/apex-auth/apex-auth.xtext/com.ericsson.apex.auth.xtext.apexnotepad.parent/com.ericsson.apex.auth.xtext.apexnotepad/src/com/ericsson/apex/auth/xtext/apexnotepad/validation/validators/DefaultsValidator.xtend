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
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.Defaults
import com.ericsson.apex.auth.xtext.utilities.ApexErrorCodes
import com.ericsson.apex.auth.xtext.utilities.ValidationReturn
import com.ericsson.apex.auth.xtext.utilities.ValidationReturnType
import com.ericsson.apex.auth.xtext.utilities.validators.ElementNameValidator
import java.util.ArrayList
import java.util.List

/**
 * Validates Defaults.
 * 
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
class DefaultsValidator {

	/**
	 * Main validation method.
	 * 
	 * @param defaults the validation object
	 * @return validation results
	 */
	def validate(Defaults defaults){
		var List<ValidationReturn> ret = ArrayList.newInstance
		if(defaults.version === null){
			ret += new ValidationReturn(
				ValidationReturnType::ERROR,
				ApexErrorCodes::ERROR_DEFAULTS_WO_VERSION,
				ApexErrorCodes::ERROR_DEFAULTS_WO_VERSION,
				ApexNotepadPackage.Literals.DEFAULTS__VERSION,
				#[]
			)
		}

		ElementNameValidator.newInstance.validateNoVersion(
			defaults.source,
			ret,
			ApexNotepadPackage.Literals.DEFAULTS__SOURCE
		)

		ElementNameValidator.newInstance.validateNoVersion(
			defaults.target,
			ret,
			ApexNotepadPackage.Literals.DEFAULTS__TARGET
		)

		return ret
	}

}