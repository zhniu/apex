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
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.PolicyModel
import com.ericsson.apex.auth.xtext.utilities.ApexErrorCodes
import com.ericsson.apex.auth.xtext.utilities.ValidationReturn
import com.ericsson.apex.auth.xtext.utilities.validators.EObjectValidator
import com.ericsson.apex.auth.xtext.utilities.validators.ElementNameValidator
import java.util.ArrayList
import java.util.List

/**
 * Validates a PolicyModel.
 * 
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
class PolicyModelValidator {

	/**
	 * Main validation method.
	 * 
	 * @param model the validation object
	 * @return validation results
	 */
	def validate(PolicyModel model){
		var List<ValidationReturn> ret = ArrayList.newInstance

		ElementNameValidator.newInstance.validateVersionedID(
			model.name,
			ret,
			ApexNotepadPackage.Literals::POLICY_MODEL__NAME
		)

		EObjectValidator.newInstance.validateListNull(
			model.schemas,
			ret,
			ApexNotepadPackage.Literals.POLICY_MODEL__SCHEMAS,
			ApexErrorCodes::ERROR_MODEL_WO_SCHEMAS
		)

		EObjectValidator.newInstance.validateListNull(
			model.events,
			ret,
			ApexNotepadPackage.Literals.POLICY_MODEL__EVENTS,
			ApexErrorCodes::ERROR_MODEL_WO_EVENTS
		)

		EObjectValidator.newInstance.validateListNull(
			model.tasks,
			ret,
			ApexNotepadPackage.Literals.POLICY_MODEL__TASKS,
			ApexErrorCodes::ERROR_MODEL_WO_TASKS
		)

		EObjectValidator.newInstance.validateListNull(
			model.policies,
			ret,
			ApexNotepadPackage.Literals.POLICY_MODEL__POLICIES,
			ApexErrorCodes::ERROR_MODEL_WO_POLICIES
		)

		return ret
	}

}
