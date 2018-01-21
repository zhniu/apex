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

import com.ericsson.apex.auth.xtext.utilities.ValidationReturn
import com.ericsson.apex.auth.xtext.utilities.ValidationReturnType
import java.util.List
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EStructuralFeature

/**
 * Validates EObjects and ELists, mainly for being null or empty.
 * 
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
class EObjectValidator {

	/**
	 * Validates a list being null or empty (of size 0).
	 * 
	 * @param valList the list to validate
	 * @param msgList the list for placing messages
	 * @param feature the feature for the validation
	 * @param errorCode the error code to use for messages
	 */
	def void validateListNull(EList<? extends EObject> valList, List<ValidationReturn> msgList, EStructuralFeature feature, String errorCode){
		if(valList === null || valList.size == 0){
			msgList += new ValidationReturn(
				ValidationReturnType::ERROR,
				errorCode,
				errorCode,
				feature,
				#[]
			)
		}
	}

	/**
	 * Validates an object being null.
	 * 
	 * @param valObj the object to validate
	 * @param msgList the list for placing messages
	 * @param feature the feature for the validation
	 * @param errorCode the error code to use for messages
	 */
	def void validateObjectNull(EObject valObj, List<ValidationReturn> msgList, EStructuralFeature feature, String errorCode){
		if(valObj === null){
			msgList += new ValidationReturn(
				ValidationReturnType::ERROR,
				errorCode,
				errorCode,
				feature,
				#[]
			)
		}
	}
}