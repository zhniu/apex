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
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.PolicyStateDef
import com.ericsson.apex.auth.xtext.utilities.ApexErrorCodes
import com.ericsson.apex.auth.xtext.utilities.ValidationReturn
import com.ericsson.apex.auth.xtext.utilities.validators.EObjectValidator
import com.ericsson.apex.auth.xtext.utilities.validators.ElementNameValidator
import java.util.ArrayList
import java.util.List

/**
 * Validates a PolicyStateDef.
 * 
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
class PolicyStateDefValidator {

	/**
	 * Main validation method.
	 * 
	 * @param state the validation object
	 * @return validation results
	 */
	def validate(PolicyStateDef state){
		var List<ValidationReturn> ret = ArrayList.newInstance

		ElementNameValidator.newInstance.validateVersionedID(
			state.name,
			ret,
			ApexNotepadPackage.Literals::POLICY_STATE_DEF__NAME
		)

		EObjectValidator.newInstance.validateListNull(
			state.outputs,
			ret,
			ApexNotepadPackage.Literals.POLICY_STATE_DEF__OUTPUTS,
			ApexErrorCodes::ERROR_POLICYSTATE_WO_OUTPUTS
		)

		EObjectValidator.newInstance.validateListNull(
			state.tasks,
			ret,
			ApexNotepadPackage.Literals.POLICY_STATE_DEF__TASKS,
			ApexErrorCodes::ERROR_POLICYSTATE_WO_TASKS
		)

		return ret
	}

}