/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.auth.xtext.utilities.validators

import com.ericsson.apex.auth.xtext.utilities.ApexErrorCodes
import com.ericsson.apex.auth.xtext.utilities.ApexWarningCodes
import com.ericsson.apex.auth.xtext.utilities.ValidationReturn
import com.ericsson.apex.auth.xtext.utilities.ValidationReturnType
import java.util.List
import org.eclipse.emf.ecore.EStructuralFeature

/**
 * Validates names (identifiers) for various conditions.
 * 
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
class ElementNameValidator {

	/**
	 * Validates a standard identifier.
	 * The validation means that the identifier:
	 * <ul>
	 * 		<li>is not empty,</li>
	 * 		<li>contains no dash, and</li>
	 * 		<li>contains no version.</li>
	 * </ul>
	 * 
	 * @param id the name of the identifier to validate
	 * @param msgList a list for placing resulting messages
	 * @param feature the feature for messages
	 */
	def void validateID(String id, List<ValidationReturn> msgList, EStructuralFeature feature){
		validateNotEmpty(id, msgList, feature)
		validateNoDash(id, msgList, feature)
		validateNoVersion(id, msgList, feature)
	}

	/**
	 * Validates a declaration identifier.
	 * The validation means that the identifier:
	 * <ul>
	 * 		<li>is not empty,</li>
	 * 		<li>contains no dash, and</li>
	 * 		<li>ends with '_decl'.</li>
	 * </ul>
	 * 
	 * @param id the name of the identifier to validate
	 * @param msgList a list for placing resulting messages
	 * @param feature the feature for messages
	 */
	def void validateDeclID(String id, List<ValidationReturn> msgList, EStructuralFeature feature){
		validateNotEmpty(id, msgList, feature)
		validateNoDash(id, msgList, feature)
		validateEndsWithDecl(id, msgList, feature)
	}

	/**
	 * Validates a versioned identifier.
	 * The validation means that the identifier:
	 * <ul>
	 * 		<li>is not empty and</li>
	 * 		<li>contains no dash.</li>
	 * </ul>
	 * 
	 * @param id the name of the identifier to validate
	 * @param msgList a list for placing resulting messages
	 * @param feature the feature for messages
	 */
	def void validateVersionedID(String id, List<ValidationReturn> msgList, EStructuralFeature feature){
		validateNotEmpty(id, msgList, feature)
		validateNoDash(id, msgList, feature)
	}

	/**
	 * Validates a name for being empty, that is null or empty string after a trim.
	 * 
	 * @param name the name to validate, can be null
	 * @param msgList a list for placing resulting messages
	 * @param feature the feature for messages
	 */
	def void validateNotEmpty(String name, List<ValidationReturn> msgList, EStructuralFeature feature){
		if(name===null){
			msgList += new ValidationReturn(
				ValidationReturnType::ERROR,
				"Invalid name for an event declaration: " + ApexErrorCodes::ERROR_NAME_IS_NULL,
				ApexErrorCodes::ERROR_NAME_IS_NULL,
				feature,
				#[]
			)
		}
		else if(name.trim.isEmpty){
			msgList += new ValidationReturn(
				ValidationReturnType::ERROR,
				"Invalid name for an event declaration: " + ApexErrorCodes::ERROR_NAME_IS_EMPTY,
				ApexErrorCodes::ERROR_NAME_IS_EMPTY,
				feature,
				#[name]
			)
		}
	}

	/**
	 * Validates a name for not containing a dash, error if it does.
	 * 
	 * @param name the name to validate, null does not result in error
	 * @param msgList a list for placing resulting messages
	 * @param feature the feature for messages
	 */
	def void validateNoDash(String name, List<ValidationReturn> msgList, EStructuralFeature feature){
		if(name!==null && name.contains("-")){
			msgList += new ValidationReturn(
				ValidationReturnType::ERROR,
				"Invalid name for an event declaration: " + ApexErrorCodes::ERROR_NAME_CONTAINS_DASH,
				ApexErrorCodes::ERROR_NAME_CONTAINS_DASH,
				feature,
				#[name]
			)
		}
	}

	/**
	 * Validates a name for ending with '_decl', warning if not.
	 * 
	 * @param name the name to validate, null does not result in error
	 * @param msgList a list for placing resulting messages
	 * @param feature the feature for messages
	 */
	def void validateEndsWithDecl(String name, List<ValidationReturn> msgList, EStructuralFeature feature){
		if(name!==null && !name.endsWith("_decl")){
			msgList += new ValidationReturn(
				ValidationReturnType::WARNING,
				ApexWarningCodes::WARN_EVENTDECL_NAME_NODECL,
				ApexWarningCodes::WARN_EVENTDECL_NAME_NODECL,
				feature,
				#[name]
			)
		}
	}

	/**
	 * Validates a name for containing version information, that is containing a ':'.
	 * 
	 * @param name the name to validate, null does not result in error
	 * @param msgList a list for placing resulting messages
	 * @param feature the feature for messages
	 */
	def void validateNoVersion(String name, List<ValidationReturn> msgList, EStructuralFeature feature){
		if(name!==null && name.contains(":")){
			msgList += new ValidationReturn(
				ValidationReturnType::ERROR,
				ApexErrorCodes::ERROR_NAME_CONTAINS_VERSION,
				ApexErrorCodes::ERROR_NAME_CONTAINS_VERSION,
				feature,
				#[name]
			)
		}
	}
}