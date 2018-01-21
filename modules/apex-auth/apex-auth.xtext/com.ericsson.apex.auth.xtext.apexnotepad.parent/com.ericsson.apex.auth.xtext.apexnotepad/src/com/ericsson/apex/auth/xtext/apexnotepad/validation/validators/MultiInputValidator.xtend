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
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.MultiInput
import com.ericsson.apex.auth.xtext.utilities.ApexErrorCodes
import com.ericsson.apex.auth.xtext.utilities.ValidationReturn
import com.ericsson.apex.auth.xtext.utilities.ValidationReturnType
import java.util.ArrayList
import java.util.List
import java.util.Scanner
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.Path

/**
 * Validates a MultiInput.
 * 
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
class MultiInputValidator {

	/**
	 * Main validation method.
	 * 
	 * @param mi the validation object
	 * @return validation results
	 */
	def validate(MultiInput mi){
		var List<ValidationReturn> ret = ArrayList.newInstance
		valAsString(mi.asString, ret)
		valBlock(mi.block, ret)
		valFilename(mi, ret)
		return ret
	}

	/**
	 * Validates string value.
	 * 
	 * @param asString the validation object
	 * @param list the return list for errors and warnings
	 */
	private def void valAsString(String asString, List<ValidationReturn> list){
		if(asString!==null){
			if(asString.trim.length==0){
				list += new ValidationReturn(
					ValidationReturnType::ERROR,
					ApexErrorCodes::ERROR_MULTIINPUT_EMPTY_STRING,
					ApexErrorCodes::ERROR_MULTIINPUT_EMPTY_STRING,
					ApexNotepadPackage.Literals::MULTI_INPUT__AS_STRING,
					#[asString]
				)
			}
		}
	}

	/**
	 * Validates block value.
	 * 
	 * @param block the validation object
	 * @param list the return list for errors and warnings
	 */
	private def void valBlock(String block, List<ValidationReturn> list){
		if(block!==null){
			if(block.substring(1, block.length-1).trim.length==0){
				list += new ValidationReturn(
					ValidationReturnType::ERROR,
					ApexErrorCodes::ERROR_MULTIINPUT_EMPTY_MULTISTRING,
					ApexErrorCodes::ERROR_MULTIINPUT_EMPTY_MULTISTRING,
					ApexNotepadPackage.Literals::MULTI_INPUT__BLOCK,
					#[block]
				)
			}
		}
	}

	/**
	 * Validates filename.
	 * 
	 * @param mi the validation object
	 * @param list the return list for errors and warnings
	 */
	private def void valFilename(MultiInput mi, List<ValidationReturn> list){
		if(mi.file!==null){
			val fileName = mi.file.substring(mi.file.indexOf('"')+1, mi.file.lastIndexOf('"'))
			if(fileName.length==0){
				list += new ValidationReturn(
					ValidationReturnType::ERROR,
					ApexErrorCodes::ERROR_MULTIINPUT_EMPTY_FILENAME,
					ApexErrorCodes::ERROR_MULTIINPUT_EMPTY_FILENAME,
					ApexNotepadPackage.Literals::MULTI_INPUT__FILE,
					#[mi.file]
				)
			}
			else{
				val platformString = mi.eResource.URI.toPlatformString(true);
				val fsaRoot = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(platformString));
				val projectRoot = fsaRoot.getProject();
				val srcFile = projectRoot.getFile(fileName)
				if(!srcFile.exists){
					list += new ValidationReturn(
						ValidationReturnType::ERROR,
						ApexErrorCodes::ERROR_MULTIINPUT_FILE_NOTEXIST + ": " + fileName,
						ApexErrorCodes::ERROR_MULTIINPUT_FILE_NOTEXIST,
						ApexNotepadPackage.Literals::MULTI_INPUT__FILE,
						#[mi.file]
					)
				}
				else if(srcFile.readOnly){
					list += new ValidationReturn(
						ValidationReturnType::ERROR,
						ApexErrorCodes::ERROR_MULTIINPUT_FILE_CANTREAD + ": " + fileName,
						ApexErrorCodes::ERROR_MULTIINPUT_FILE_CANTREAD,
						ApexNotepadPackage.Literals::MULTI_INPUT__FILE,
						#[mi.file]
					)
				}
				else{
					val Scanner s = new Scanner(srcFile.contents).useDelimiter("\\A");
					var String contents = ""
					if(s.hasNext){
						contents = s.next
					}
					if(contents===null || contents.trim.length==0){
						list += new ValidationReturn(
							ValidationReturnType::ERROR,
							ApexErrorCodes::ERROR_MULTIINPUT_FILE_EMPTY + ": " + fileName,
							ApexErrorCodes::ERROR_MULTIINPUT_FILE_EMPTY,
							ApexNotepadPackage.Literals::MULTI_INPUT__FILE,
							#[mi.file]
						)
					}
				}
			}
		}
	}
}
