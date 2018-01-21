/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.auth.xtext.apexnotepad.validation

import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.ContextAlbumDecl
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.Defaults
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.EventDecl
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.EventDeclField
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.Model
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.MultiInput
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.PolicyDef
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.PolicyModel
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.PolicyStateDef
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.PolicyStateTask
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.SchemaDecl
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.SchemaDeclFlavorJava
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.TaskDef
import com.ericsson.apex.auth.xtext.apexnotepad.validation.validators.ContextAlbumDeclValidator
import com.ericsson.apex.auth.xtext.apexnotepad.validation.validators.DefaultsValidator
import com.ericsson.apex.auth.xtext.apexnotepad.validation.validators.EntryModelValidator
import com.ericsson.apex.auth.xtext.apexnotepad.validation.validators.EventDeclFieldValidator
import com.ericsson.apex.auth.xtext.apexnotepad.validation.validators.EventDeclValidator
import com.ericsson.apex.auth.xtext.apexnotepad.validation.validators.MultiInputValidator
import com.ericsson.apex.auth.xtext.apexnotepad.validation.validators.PolicyDefValidator
import com.ericsson.apex.auth.xtext.apexnotepad.validation.validators.PolicyModelValidator
import com.ericsson.apex.auth.xtext.apexnotepad.validation.validators.PolicyStateDefValidator
import com.ericsson.apex.auth.xtext.apexnotepad.validation.validators.PolicyStateTaskValidator
import com.ericsson.apex.auth.xtext.apexnotepad.validation.validators.SchemaDeclFlavorJavaValidator
import com.ericsson.apex.auth.xtext.apexnotepad.validation.validators.SchemaDeclValidator
import com.ericsson.apex.auth.xtext.apexnotepad.validation.validators.TaskDefValidator
import com.ericsson.apex.auth.xtext.utilities.ValidationReturn
import com.ericsson.apex.auth.xtext.utilities.ValidationReturnType
import java.util.List
import org.eclipse.xtext.validation.Check

/**
 * Provides custom validation rules.
 * 
 * @see <a href="https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation">XText Documentation: Validation</a>
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
class ApexNotepadValidator extends AbstractApexNotepadValidator {

	/**
	 * Validates model defaults {@link Defaults}.
	 * 
	 * @param defaults the validation object
	 */
	@Check
	def defaults(Defaults defaults){
		issueMessages(DefaultsValidator.newInstance.validate(defaults))
	}

	/**
	 * Validates a model {@link Model}.
	 * 
	 * @param model the validation object
	 */
	@Check
	def model(Model model){
		issueMessages(EntryModelValidator.newInstance.validate(model))
	}

	/**
	 * Validates a schema declaration {@link SchemaDecl}.
	 * 
	 * @param schemaDecl the validation object
	 */
	@Check
	def schemaDecl(SchemaDecl schemaDecl){
		issueMessages(SchemaDeclValidator.newInstance.validate(schemaDecl))
	}

	/**
	 * Validates the Java flavor of a schema declaration {@link SchemaDeclFlavorJava}.
	 * 
	 * @param flavor the validation object
	 */
	@Check
	def javaFQCN(SchemaDeclFlavorJava flavor) {
		issueMessages(SchemaDeclFlavorJavaValidator.newInstance.validate(flavor))
	}

	/**
	 * Validates a context album declaration {@link ContextAlbumDecl}.
	 * 
	 * @param album the validation object
	 */
	@Check
	def contextAlbumDecl(ContextAlbumDecl album) {
		issueMessages(ContextAlbumDeclValidator.newInstance.validate(album))
	}

	/**
	 * Validates an event declaration {@link EventDecl}.
	 * 
	 * @param decl the validation object
	 */
	@Check
	def eventDecl(EventDecl decl) {
		issueMessages(EventDeclValidator.newInstance.validate(decl))
	}

	/**
	 * Validates a field definition for an event declaration {@link EventDeclField}.
	 * 
	 * @param field the validation object
	 */
	@Check
	def eventDeclField(EventDeclField field) {
		issueMessages(EventDeclFieldValidator.newInstance.validate(field))
	}

	/**
	 * Validates a task definition {@link TaskDef}.
	 * 
	 * @param task the validation object
	 */
	@Check
	def taskDef(TaskDef task) {
		issueMessages(TaskDefValidator.newInstance.validate(task))
	}

	/**
	 * Validates a policy model {@link PolicyModel}.
	 * 
	 * @param pm the validation object
	 */
	@Check
	def policyModel(PolicyModel pm){
		issueMessages(PolicyModelValidator.newInstance.validate(pm))
	}

	/**
	 * Validates a policy definition {@link PolicyDef}.
	 * 
	 * @param policy the validation object
	 */
	@Check
	def policDefinition(PolicyDef policy){
		issueMessages(PolicyDefValidator.newInstance.validate(policy))
	}

	/**
	 * Validates a policy state definition {@link PolicyStateDef}.
	 * 
	 * @param state the validation object
	 */
	@Check
	def policStateDefinition(PolicyStateDef state){
		issueMessages(PolicyStateDefValidator.newInstance.validate(state))
	}

	/**
	 * Validates a policy state task {@link PolicyStateTask}.
	 * 
	 * @param task the validation object
	 */
	@Check
	def policyStateTask(PolicyStateTask task){
		issueMessages(PolicyStateTaskValidator.newInstance.validate(task))
	}

	/**
	 * Validates a multi-input {@link MultiInput}.
	 * 
	 * @param mi the validation object
	 */
	@Check
	def multiInput(MultiInput mi){
		issueMessages(MultiInputValidator.newInstance.validate(mi))
	}

	/**
	 * Issues errors and warnings for given messages.
	 * 
	 * @param messages the error and warning messages
	 */
	def issueMessages(List<ValidationReturn> messages){
		for(valret : messages.filter[it.type==ValidationReturnType.ERROR]){
			error(
				valret.message,
				valret.feature,
				valret.code,
				valret.issueData
			)
		}
		for(valret : messages.filter[it.type==ValidationReturnType.WARNING]){
			warning(
				valret.message,
				valret.feature,
				valret.code,
				valret.issueData
			)
		}
	}
}
