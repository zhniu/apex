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
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.Model
import com.ericsson.apex.auth.xtext.utilities.ApexErrorCodes
import com.ericsson.apex.auth.xtext.utilities.ValidationReturn
import com.ericsson.apex.auth.xtext.utilities.ValidationReturnType
import java.util.ArrayList
import java.util.List

/**
 * Validates a Model.
 * 
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
class EntryModelValidator {

	/**
	 * Main validation method.
	 * 
	 * @param model the validation object
	 * @return validation results
	 */
	def validate(Model model){
		var List<ValidationReturn> ret = ArrayList.newInstance
		if(model.model === null){
			ret += new ValidationReturn(
				ValidationReturnType::ERROR,
				ApexErrorCodes::ERROR_NO_MODEL_DEFINED,
				ApexErrorCodes::ERROR_NO_MODEL_DEFINED,
				ApexNotepadPackage.Literals::MODEL__MODEL,
				#[]
			)
		}
		return ret
	}
}