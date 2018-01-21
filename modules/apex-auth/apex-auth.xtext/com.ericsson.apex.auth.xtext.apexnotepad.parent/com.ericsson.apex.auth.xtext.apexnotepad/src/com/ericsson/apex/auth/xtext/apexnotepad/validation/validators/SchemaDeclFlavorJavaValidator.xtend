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
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.SchemaDeclFlavorJava
import com.ericsson.apex.auth.xtext.utilities.ApexErrorCodes
import com.ericsson.apex.auth.xtext.utilities.ValidationReturn
import com.ericsson.apex.auth.xtext.utilities.ValidationReturnType
import java.util.ArrayList
import java.util.List

/**
 * Validates a SchemaDeclFlavorJava.
 * 
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
class SchemaDeclFlavorJavaValidator {

	/**
	 * Main validation method.
	 * 
	 * @param flavor the validation object
	 * @return validation results
	 */
	def validate(SchemaDeclFlavorJava flavor){
		var List<ValidationReturn> ret = ArrayList.newInstance
		valName(flavor.javaClassFQCN, ret)
		valPackages(flavor.javaClassFQCN, ret)
		return ret
	}

	/**
	 * Validates FQCN name.
	 * 
	 * @param fqcn the validation object
	 * @param list the return list for errors and warnings
	 */
	private def void valName(String fqcn, List<ValidationReturn> list){
		if(fqcn!==null){
			if(fqcn.contains("-")){
				list += new ValidationReturn(
					ValidationReturnType::ERROR,
					ApexErrorCodes::ERROR_JAVAFQCN_HASDASH,
					ApexErrorCodes::ERROR_JAVAFQCN_HASDASH,
					ApexNotepadPackage.Literals::SCHEMA_DECL_FLAVOR_JAVA__JAVA_CLASS_FQCN,
					#[fqcn]
				)
			}
			if(!fqcn.contains(".")){
				list += new ValidationReturn(
					ValidationReturnType::ERROR,
					ApexErrorCodes::ERROR_JAVAFQCN_NODOT,
					ApexErrorCodes::ERROR_JAVAFQCN_NODOT,
					ApexNotepadPackage.Literals::SCHEMA_DECL_FLAVOR_JAVA__JAVA_CLASS_FQCN,
					#[fqcn]
				)
			}
		}
	}

	/**
	 * Validates package names.
	 * 
	 * @param fqcn the validation object
	 * @param list the return list for errors and warnings
	 */
	private def void valPackages(String fqcn, List<ValidationReturn> list){
		if(fqcn!==null){
			val String[] array = fqcn.split("\\.")
			for (i : 0 ..< array.size) {
				val str = array.get(i)
				if(i<array.length-1){
					if(str !== str.toLowerCase){
						list += new ValidationReturn(
							ValidationReturnType::ERROR,
							ApexErrorCodes::ERROR_JAVAFQCN_PGK_UPPER + ": " + str,
							ApexErrorCodes::ERROR_JAVAFQCN_PGK_UPPER,
							ApexNotepadPackage.Literals::SCHEMA_DECL_FLAVOR_JAVA__JAVA_CLASS_FQCN,
							#[fqcn]
						)
					}
				}
				else if(i==(array.length-1)) {
					if(!Character.isUpperCase(str.codePointAt(0))) {
						list += new ValidationReturn(
							ValidationReturnType::ERROR,
							ApexErrorCodes::ERROR_JAVAFQCN_CLASSSTART_NOTUPPER + ": " + str,
							ApexErrorCodes::ERROR_JAVAFQCN_CLASSSTART_NOTUPPER,
							ApexNotepadPackage.Literals::SCHEMA_DECL_FLAVOR_JAVA__JAVA_CLASS_FQCN,
							#[fqcn]
						)
					}
				}
			}
		}
	}
}