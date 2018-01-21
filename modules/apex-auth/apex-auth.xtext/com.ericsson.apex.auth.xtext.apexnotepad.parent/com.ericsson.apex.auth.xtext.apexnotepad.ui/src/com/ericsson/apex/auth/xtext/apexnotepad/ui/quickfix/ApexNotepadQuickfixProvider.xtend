/**
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.apex.auth.xtext.apexnotepad.ui.quickfix

import org.eclipse.xtext.ui.editor.quickfix.DefaultQuickfixProvider

/**
 * Provides custom quick fixe.
 *
 * @see <a href="https://www.eclipse.org/Xtext/documentation/310_eclipse_support.html#quick-fixes">XText Documentation: UI Quick Fixes</a>
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
class ApexNotepadQuickfixProvider extends DefaultQuickfixProvider {

//	@Fix(ApexNotepadValidator.INVALID_NAME)
//	def capitalizeName(Issue issue, IssueResolutionAcceptor acceptor) {
//		acceptor.accept(issue, 'Capitalize name', 'Capitalize the name.', 'upcase.png') [
//			context |
//			val xtextDocument = context.xtextDocument
//			val firstLetter = xtextDocument.get(issue.offset, 1)
//			xtextDocument.replace(issue.offset, 1, firstLetter.toUpperCase)
//		]
//	}
}
