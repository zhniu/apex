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
import org.eclipse.xtext.ui.label.DefaultDescriptionLabelProvider

/**
 * Provides labels for IEObjectDescriptions and IResourceDescriptions.
 * 
 * @see <a href="https://www.eclipse.org/Xtext/documentation/310_eclipse_support.html#label-provider">XText Documentation: UI Label Provider</a>
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
class ApexNotepadDescriptionLabelProvider extends DefaultDescriptionLabelProvider {

	/**
	 * Defines text for an EventDeclField element.
	 * @param elem the element
	 * @return the text
	 */
	def text(EventDeclField elem) {"EVENT FIELD: " + elem.name}

	/**
	 * Generates the name of an icon for an EventDeclField element.
	 * @param elem the element
	 * @return the name of the icon
	 */
	def image(EventDeclField elem) {"EventDeclField.gif"}



	// Labels and icons can be computed like this:
	
//	override text(IEObjectDescription ele) {
//		ele.name.toString
//	}
//	 
//	override image(IEObjectDescription ele) {
//		ele.EClass.name + '.gif'
//	}
}
