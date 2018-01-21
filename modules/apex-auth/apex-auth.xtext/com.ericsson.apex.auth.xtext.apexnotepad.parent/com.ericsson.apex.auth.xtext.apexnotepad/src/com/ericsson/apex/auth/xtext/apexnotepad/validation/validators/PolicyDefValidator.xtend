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
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.PolicyDef
import com.ericsson.apex.auth.xtext.utilities.ApexErrorCodes
import com.ericsson.apex.auth.xtext.utilities.ValidationReturn
import com.ericsson.apex.auth.xtext.utilities.validators.EObjectValidator
import com.ericsson.apex.auth.xtext.utilities.validators.ElementNameValidator
import java.util.ArrayList
import java.util.List

/**
 * Validates a PolicyDef.
 * 
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
class PolicyDefValidator {

	/**
	 * Main validation method.
	 * 
	 * @param policy the validation object
	 * @return validation results
	 */
	def validate(PolicyDef policy){
		var List<ValidationReturn> ret = ArrayList.newInstance

		ElementNameValidator.newInstance.validateVersionedID(
			policy.name,
			ret,
			ApexNotepadPackage.Literals::POLICY_DEF__NAME
		)

		EObjectValidator.newInstance.validateListNull(
			policy.states,
			ret,
			ApexNotepadPackage.Literals.POLICY_DEF__STATES,
			ApexErrorCodes::ERROR_POLICY_WO_STATES
		)

		EObjectValidator.newInstance.validateObjectNull(
			policy.firstState,
			ret,
			ApexNotepadPackage.Literals::POLICY_DEF__FIRST_STATE,
			ApexErrorCodes::ERROR_POLICY_WO_FIRST_STATE
		)
		return ret
	}

}