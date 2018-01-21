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
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.PolicyStateTask
import com.ericsson.apex.auth.xtext.utilities.ApexErrorCodes
import com.ericsson.apex.auth.xtext.utilities.ValidationReturn
import com.ericsson.apex.auth.xtext.utilities.ValidationReturnType
import java.util.ArrayList
import java.util.List
import org.eclipse.xtext.EcoreUtil2

class PolicyStateTaskValidator {

	/**
	 * Main validation method.
	 * 
	 * @param task the validation object
	 * @return validation results
	 */
	def validate(PolicyStateTask task){
		var List<ValidationReturn> ret = ArrayList.newInstance



		return ret
	}

	def void validateOutfieldStateout(PolicyStateTask task, List<ValidationReturn> list){
		var taskDef = task.taskName
		var eventDecl = task.outputName.eventName
		for(outField : taskDef.outfields){
			var found = false
			for(eventField : eventDecl.fields){
				if(outField.name == eventField.name){
					if(outField.schemaName.name == eventField.schemaName.name){
						found = true
					}
				}
			}
			if(found==false){
				list += new ValidationReturn(
					ValidationReturnType::ERROR,
					"task out field <" + outField.name + "> not in state output <" + task.outputName.name + "> with event <" + eventDecl.name + ">",
					ApexErrorCodes::ERROR_TASKOUTFIELD_NOTIN_STATEOUTPUT,
					ApexNotepadPackage.Literals::POLICY_STATE_TASK__OUTPUT_NAME,
					#[outField.name]
				)
			}
		}
	}

	def void validateSomething(PolicyStateTask task, List<ValidationReturn> list){
		var taskDef = task.taskName
		var PolicyStateDef state = EcoreUtil2.getContainerOfType(task, PolicyStateDef)
		var trigger = state.triggerName
		for(inField : taskDef.infields){
			var found = false
			for(eventField : trigger.fields){
				if(inField.name == eventField.name){
					if(inField.schemaName.name == eventField.schemaName.name){
						found = true
					}
				}
			}
			if(found==false){
				list += new ValidationReturn(
					ValidationReturnType::ERROR,
					"task in field <" + inField.name + "> not in state trigger event <" + trigger.name + ">",
					ApexErrorCodes::ERROR_TASKINFIELD_NOTIN_STATETRIGGER,
					ApexNotepadPackage.Literals::POLICY_STATE_TASK__TASK_NAME,
					#[inField.name]
				)
			}
		}
	}

/*
	//TODO add schema error if schemas are different
	def taskDef(PolicyStateTask task){
		var taskDef = task.taskName
		var eventDecl = task.outputName.eventName
		for(outField : taskDef.outfields){
			var found = false
			for(eventField : eventDecl.fields){
				if(outField.name == eventField.name){
					if(outField.schemaName.name == eventField.schemaName.name){
						found = true
					}
				}
			}
			if(found==false){
				error(
					"task out field <" + outField.name + "> not in state output <" + task.outputName.name + "> with event <" + eventDecl.name + ">",
					ApexNotepadPackage.Literals.POLICY_STATE_TASK__OUTPUT_NAME,
					"task out field <" + outField.name + "> not in state output <" + task.outputName.name + "> with event <" + eventDecl.name + ">"
				)
			}
		}

		var PolicyStateDef state = EcoreUtil2.getContainerOfType(task, PolicyStateDef)
		var trigger = state.triggerName
		for(inField : taskDef.infields){
			var found = false
			for(eventField : trigger.fields){
				if(inField.name == eventField.name){
					if(inField.schemaName.name == eventField.schemaName.name){
						found = true
					}
				}
			}
			if(found==false){
				error(
					"task in field <" + inField.name + "> not in state trigger event <" + trigger.name + ">",
					ApexNotepadPackage.Literals.POLICY_STATE_TASK__TASK_NAME,
					"task in field <" + inField.name + "> not in state trigger event <" + trigger.name + ">"
				)
			}
		}
	}
*/

}
