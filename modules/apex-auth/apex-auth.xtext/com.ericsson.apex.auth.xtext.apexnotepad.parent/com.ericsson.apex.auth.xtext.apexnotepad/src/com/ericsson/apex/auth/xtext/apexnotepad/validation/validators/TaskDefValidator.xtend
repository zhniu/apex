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
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.TaskDef
import com.ericsson.apex.auth.xtext.utilities.ApexErrorCodes
import com.ericsson.apex.auth.xtext.utilities.ValidationReturn
import com.ericsson.apex.auth.xtext.utilities.validators.EObjectValidator
import com.ericsson.apex.auth.xtext.utilities.validators.ElementNameValidator
import java.util.ArrayList
import java.util.List

/**
 * Validates a TaskDef.
 * 
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
class TaskDefValidator {

	/**
	 * Main validation method.
	 * 
	 * @param task the validation object
	 * @return validation results
	 */
	def validate(TaskDef task){
		var List<ValidationReturn> ret = ArrayList.newInstance

		ElementNameValidator.newInstance.validateVersionedID(
			task.name,
			ret,
			ApexNotepadPackage.Literals::TASK_DEF__NAME
		)

		EObjectValidator.newInstance.validateListNull(
			task.infields,
			ret,
			ApexNotepadPackage.Literals.TASK_DEF__INFIELDS,
			ApexErrorCodes::ERROR_POLICY_WO_STATES
		)

		EObjectValidator.newInstance.validateListNull(
			task.outfields,
			ret,
			ApexNotepadPackage.Literals.TASK_DEF__OUTFIELDS,
			ApexErrorCodes::ERROR_POLICY_WO_STATES
		)

		return ret
	}

}