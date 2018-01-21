/**
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.apex.auth.xtext.apexnotepad.ui.contentassist

import java.util.UUID
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.Assignment
import org.eclipse.xtext.RuleCall
import org.eclipse.xtext.ui.editor.contentassist.ConfigurableCompletionProposal
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor

/**
 * Provides content assistants.
 * 
 * @see <a href="https://www.eclipse.org/Xtext/documentation/310_eclipse_support.html#content-assist">XText Documentation: UI Content Assist</a>
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
class ApexNotepadProposalProvider extends AbstractApexNotepadProposalProvider {

	override complete_MultiInput(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		acceptor.accept(createCompletionProposal('""', '"" ➜ a string', null, context))
		acceptor.accept(createCompletionProposal("«\n\n»", "« »  ➜ block with start/end", null, context))
		acceptor.accept(createCompletionProposal('file:"<file>"', 'file:"<file>" an import file', null, context))
	}

	override completeSchemaDeclFlavorJava_JavaClassFQCN(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		super.completeSchemaDeclFlavorJava_JavaClassFQCN(model, assignment, context, acceptor)

		acceptor.accept(createCompletionProposal("java.lang.String", context))
		acceptor.accept(createCompletionProposal("java.lang.Integer", context))
		acceptor.accept(createCompletionProposal("java.lang.Boolean", context))
		acceptor.accept(createCompletionProposal("java.lang.Character", context))
		acceptor.accept(createCompletionProposal("java.lang.Long", context))
		acceptor.accept(createCompletionProposal("java.lang.Double", context))
		acceptor.accept(createCompletionProposal("java.lang.Float", context))
	}

	override complete_UUID(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		super.complete_UUID(model, ruleCall, context, acceptor)
		val String proposal = UUID.randomUUID.toString
		acceptor.accept(createCompletionProposal(proposal, context))
	}

	override complete_VERSION(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		super.complete_VERSION(model, ruleCall, context, acceptor)

		var top = createCompletionProposal("Major.Minor.Patch", context)
		if (top instanceof ConfigurableCompletionProposal) {
			top.additionalProposalInfo = 'Semantic versioning, see <a href="http://www.semanticversion.org">ttt</> for details.'
		}
		acceptor.accept(top);

		acceptor.accept(createCompletionProposal("0.0.1", context))
		acceptor.accept(createCompletionProposal("0.1.0", context))
		acceptor.accept(createCompletionProposal("1.0.0", context))
	}

	override complete_BOOLEAN(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		super.complete_BOOLEAN(model, ruleCall, context, acceptor)
		acceptor.accept(createCompletionProposal("true", context))
		acceptor.accept(createCompletionProposal("false", context))
	}

	override complete_LogicFlavor(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		super.complete_LogicFlavor(model, ruleCall, context, acceptor)
		acceptor.accept(createCompletionProposal('JAVASCRIPT', 'JavaScript logic', null, context))
		acceptor.accept(createCompletionProposal('JAVA', 'Java logic', null, context))
		acceptor.accept(createCompletionProposal('JYTHON', 'Jython (Java port for Python) logic', null, context))
		acceptor.accept(createCompletionProposal('JRUBY', 'Jruby (Java port for Ruby) logic', null, context))
		acceptor.accept(createCompletionProposal('MVEL', 'MVEL logic (not recommended)', null, context))
		acceptor.accept(createCompletionProposal('""', '"" ➜ free form as string', null, context))
	}

	override completePolicyStateTask_OutputType(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		super.completePolicyStateTask_OutputType(model, assignment, context, acceptor)
		acceptor.accept(createCompletionProposal('DIRECT', 'DIRECT for direct output to a state logic', null, context))
		acceptor.accept(createCompletionProposal('LOGIC', 'LOGIC for output to state finalizer logic', null, context))
	}

	override completeContextAlbumDecl_Scope(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		super.completeContextAlbumDecl_Scope(model, assignment, context, acceptor)
		acceptor.accept(createCompletionProposal('APPLICATION', 'available for policy instances using the album', null, context))
		acceptor.accept(createCompletionProposal('EXTERNAL', 'available as external context', null, context))
		acceptor.accept(createCompletionProposal('GLOBAL', 'available as global', null, context))
	}

}
