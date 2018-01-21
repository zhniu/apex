/**
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.apex.auth.xtext.apexnotepad.ui.labeling

import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.EventDeclField
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.MultiInput
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.PolicyModel
import com.google.inject.Inject
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider
import org.eclipse.xtext.ui.label.DefaultEObjectLabelProvider

/**
 * Provides labels for EObjects.
 * 
 * @see <a href="https://www.eclipse.org/Xtext/documentation/310_eclipse_support.html#label-provider">XText Documentation: UI Label Provider</a>
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
class ApexNotepadLabelProvider extends DefaultEObjectLabelProvider {

	@Inject
	new(AdapterFactoryLabelProvider delegate) {
		super(delegate);
	}

	/** 
	 * Generates the text for a PolicyModel element.
	 * @param elem the element
	 * @return the generated text
	 */
	def text(PolicyModel elem) {"Policy Model: " + elem.name}

	/**
	 * Generates the name of an icon for a PolicyModel element.
	 * @param elem the element
	 * @return the name of the icon
	 */
	def image(PolicyModel elem) {'PolicyModel.gif'}


	/** 
	 * Generates the text for a EventDeclField element.
	 * @param elem the element
	 * @return the generated text
	 */
	def text(EventDeclField elem) {"Event Field: " + elem.name}

	/**
	 * Generates the name of an icon for a EventDeclField element.
	 * @param elem the element
	 * @return the name of the icon
	 */
	def image(EventDeclField elem) {"EventDeclField.gif"}


	/** 
	 * Generates the text for a MultiInput element.
	 * @param elem the element
	 * @return the generated text
	 */
	def text(MultiInput elem) {
		if(elem.asString!==null){
			"String: " + elem.asString
		}
		else if(elem.block!==null){
			"Block: «...»"
		}
		else if(elem.file!==null){
			"Macro File: " + elem.file.substring(elem.file.indexOf('"')+1, elem.file.lastIndexOf('"'))
		}
	}

	/**
	 * Generates the name of an icon for a MultiInput element.
	 * @param elem the element
	 * @return the name of the icon
	 */
	def image(MultiInput elem) {
		if(elem.asString!==null){
			"String.gif"
		}
		else if(elem.block!==null){
			"Block.gif"
		}
		else if(elem.file!==null){
			"MacroFile.gif"
		}
	}
}
