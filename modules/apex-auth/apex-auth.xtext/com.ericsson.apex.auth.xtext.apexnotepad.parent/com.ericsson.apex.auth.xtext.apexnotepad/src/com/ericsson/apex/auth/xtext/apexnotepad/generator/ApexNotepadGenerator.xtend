/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.auth.xtext.apexnotepad.generator

import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.ContextAlbumDecl
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.Defaults
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.EventDecl
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.EventDeclField
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.MultiInput
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.PolicyDef
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.PolicyModel
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.PolicyStateContextRef
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.PolicyStateDef
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.PolicyStateFinalizerLogic
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.PolicyStateOutput
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.PolicyStateTask
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.PolicyStateTaskSelectionLogic
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.SchemaDecl
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.TaskDef
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.TaskDefContexRef
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.TaskDefInputField
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.TaskDefLogic
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.TaskDefOutputField
import com.ericsson.apex.auth.xtext.apexnotepad.apexNotepad.TaskDefParameter
import com.ericsson.apex.auth.xtext.utilities.NameVersion
import com.ericsson.apex.model.modelapi.ApexAPIResult
import com.ericsson.apex.model.modelapi.ApexModel
import com.ericsson.apex.model.modelapi.ApexModelFactory
import java.util.Properties
import java.util.Scanner
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.Path
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext

/**
 * Generates code from your model files on save.
 * 
 * @see <a href="https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#code-generation">XText Documentation: Code Generation</a>
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
class ApexNotepadGenerator extends AbstractGenerator {

	/** Local member holding an APEX model during code generation. */
	private ApexModel apexModel 

	/** Local member for results on APEX model operations. */
	private ApexAPIResult result

	/**
	 * Generates APEX model JSON from a Notepad specification.
	 * 
	 * @param resource persistent document, Eclipse resource
	 * @param fsa access to the file system
	 * @param context generator context
	 */
	override void doGenerate(Resource resource, IFileSystemAccess2 fsa, IGeneratorContext context) {
		var Properties properties = Properties.newInstance
		for(defaults : resource.allContents.toIterable.filter(Defaults)){
			properties.put("DEFAULT_CONCEPT_VERSION", defaults.version);
			if(defaults.namespace!==null){
				properties.put("DEFAULT_EVENT_NAMESPACE", defaults.version);
			}
			if(defaults.source!==null){
				properties.put("DEFAULT_EVENT_SOURCE", defaults.version);
			}
			if(defaults.target!==null){
				properties.put("DEFAULT_EVENT_TARGET", defaults.version);
			}
		}
		apexModel = new ApexModelFactory().createApexModel(properties, true);

		for(pm : resource.allContents.toIterable.filter(PolicyModel)){
			var idv = new NameVersion(pm.name)
			result = apexModel.createModel(
				idv.getName,
				idv.version,
				null,
				this.processMultiInput(pm.description)
			)
			pm.compile

			result = apexModel.validate
System.err.println(result)//TODO

			fsa.generateFile(pm.name + ".json", apexModel.listModel.messages.get(0))
		}
	}

	/** 
	 * Compiles a PolicyModel.
	 * 
	 * @param pm the policy model to compile, must not be null
	 */
	def void compile(PolicyModel pm) {
		for(schemaDecl : pm.schemas){
			compile(schemaDecl)
		}
		for(album : pm.albums){
			compile(album)
		}
		for(eventDecl : pm.events){
			compile(eventDecl)
		}
		for(task : pm.tasks){
			compile(task)
		}
		for(policy : pm.policies){
			compile(policy)
		}
	}

	/** 
	 * Compiles a SchemaDecl.
	 * 
	 * @param schema the schema declaration to compile, must not be null
	 */
	def void compile(SchemaDecl schema){
		var String flavor
		var String definition
		if(schema.java!==null){
			flavor = 'JAVA'
			definition = schema.java.javaClassFQCN
		}
		if(schema.avro!==null){
			flavor = 'AVRO'
			definition = this.processMultiInput(schema.avro.schema)
		}

		var idv = new NameVersion(schema.name)
		result = apexModel.createContextSchema(
			idv.name,
			idv.version,
			flavor,
			definition,
			schema.uuid,
			this.processMultiInput(schema.description)
		)
	}

	/** 
	 * Compiles a ContextAlbumDecl.
	 * 
	 * @param ctx the context album declaration to compile, must not be null
	 */
	def void compile(ContextAlbumDecl ctx){
		var idv = new NameVersion(ctx.name)
		var schemaIdv = new NameVersion(ctx.schemaName.name)
		result = apexModel.createContextAlbum(
			idv.name,
			idv.version,
			ctx.scope,
			String.valueOf(ctx.writable),
			schemaIdv.name,
			schemaIdv.version,
			ctx.uuid,
			this.processMultiInput(ctx.description)
		)
	}

	/** 
	 * Compiles an EventDecl.
	 * 
	 * @param event the event declaration to compile, must not be null
	 */
	def void compile(EventDecl event){
		var idv = new NameVersion(event.name)
		result = apexModel.createEvent(
			idv.name,
			idv.version,
			event.namespace,
			event.source,
			event.target,
			event.uuid,
			this.processMultiInput(event.description)
		)
		for(eventField : event.fields){
			compile(eventField, idv)
		}
	}

	/** 
	 * Compiles an EventDeclField.
	 * 
	 * @param field the event field to compile, must not be null
	 * @param event the parent event
	 */
	def void compile(EventDeclField field, NameVersion event){
		var idv = new NameVersion(field.schemaName.name)
		result = apexModel.createEventPar(
			event.name,
			event.version,
			field.name,
			idv.name,
			idv.version,
			Boolean.valueOf(field.optional)
		)
	}

	/** 
	 * Compiles a TaskDef.
	 * 
	 * @param task the task to compile, must not be null
	 */
	def void compile(TaskDef task){
		var idv = new NameVersion(task.name)
		result = apexModel.createTask(
			idv.name,
			idv.version,
			task.uuid,
			this.processMultiInput(task.description)
		)

		for(inField : task.infields){
			compile(inField, idv)
		}
		for(outField : task.outfields){
			compile(outField, idv)
		}
		compile(task.logic, idv)
		if(task.parameters!==null){
			for(param : task.parameters){
				compile(param, idv)
			}
		}
		if(task.contextRefs!==null){
			for(ctxt : task.contextRefs){
				compile(ctxt, idv)
			}
		}
	}

	/** 
	 * Compiles a TaskDefInputField.
	 * 
	 * @param inField the input field to compile, must not be null
	 * @param task the parent task
	 */
	def void compile(TaskDefInputField inField, NameVersion task){
		var idv = new NameVersion(inField.schemaName.name)
		result = apexModel.createTaskInputField(
			task.name,
			task.version,
			inField.name,
			idv.name,
			idv.version,
			true
		)
	}

	/** 
	 * Compiles a TaskDefOutputField.
	 * 
	 * @param outField the output field to compile, must not be null
	 * @param task the parent task
	 */
	def void compile(TaskDefOutputField outField, NameVersion task){
		var idv = new NameVersion(outField.schemaName.name)
		result = apexModel.createTaskOutputField(
			task.name,
			task.version,
			outField.name,
			idv.name,
			idv.version,
			true
		)
	}

	/** 
	 * Compiles a TaskDefLogic.
	 * 
	 * @param logic the logic to compile, must not be null
	 * @param task the parent task
	 */
	def void compile(TaskDefLogic logic, NameVersion task){
		result = apexModel.createTaskLogic(
			task.name,
			task.version,
			logic.flavor,
			this.processMultiInput(logic.logic)
		)
	}

	/** 
	 * Compiles a TaskDefParameter.
	 * 
	 * @param param the task parameter to compile, must not be null
	 * @param task the parent task
	 */
	def void compile(TaskDefParameter param, NameVersion task){
		result = apexModel.createTaskParameter(
			task.name,
			task.version,
			param.name,
			param.defaultValue
		)
	}

	/** 
	 * Compiles a TaskDefContexRef.
	 * 
	 * @param ctxRef the task context reference to compile, must not be null
	 * @param task the parent task
	 */
	def void compile(TaskDefContexRef ctxRef, NameVersion task){
		var idv = new NameVersion(ctxRef.albumName.name)
		result = apexModel.createTaskContextRef(
			task.name,
			task.version,
			idv.name,
			idv.version
		)
	}

	/** 
	 * Compiles a PolicyDef.
	 * 
	 * @param policy the policy definition to compile, must not be null
	 */
	def void compile(PolicyDef policy){
		var idv = new NameVersion(policy.name)
		result = apexModel.createPolicy(
			idv.name,
			idv.version,
			'FREEFORM',
			policy.firstState.name,
			policy.uuid,
			this.processMultiInput(policy.description)
		)

		for(state : policy.states){
			compile(state, idv)
		}
	}

	/** 
	 * Compiles a PolicyStateDef.
	 * 
	 * @param state the policy state definition to compile, must not be null
	 * @param policy the parent policy
	 */
	def void compile(PolicyStateDef state, NameVersion policy){
		var idvTrigger = new NameVersion(state.triggerName.name)
		var idvDefaultTask = new NameVersion(state.defaultTask.name)
		result = apexModel.createPolicyState(
			policy.name,
			policy.version,
			state.name,
			idvTrigger.name,
			idvTrigger.version,
			idvDefaultTask.name,
			idvDefaultTask.version
		)

		for(output : state.outputs){
			compile(output, policy, state.name)
		}
		for(task : state.tasks){
			compile(task, policy, state.name)
		}
		if(state.tslogic!==null){
			compile(state.tslogic, policy, state.name)
		}
		if(state.finalizers!==null){
			for(finalizer : state.finalizers){
				compile(finalizer, policy, state.name)
			}
		}
		if(state.ctxrefs!==null){
			for(ctxref : state.ctxrefs){
				compile(ctxref, policy, state.name)
			}
		}
	}

	/** 
	 * Compiles a PolicyStateOutput.
	 * 
	 * @param output the policy state output to compile, must not be null
	 * @param policy the parent policy
	 * @param stateId the parent state
	 */
	def void compile(PolicyStateOutput output, NameVersion policy, String stateId){
		var String nextState = null
		if(output.nextState!==null){
			nextState = output.nextState.name
		}
		var idv = new NameVersion(output.eventName.name)
		result = apexModel.createPolicyStateOutput(
			policy.name,
			policy.version,
			stateId,
			output.name,
			idv.name,
			idv.version,
			nextState
		)
	}

	/** 
	 * Compiles a PolicyStateTask.
	 * 
	 * @param task the policy state task to compile, must not be null
	 * @param policy the parent policy
	 * @param stateId the parent state
	 */
	def void compile(PolicyStateTask task, NameVersion policy, String stateId){
		var idv = new NameVersion(task.taskName.name)
		result = apexModel.createPolicyStateTaskRef(
			policy.name,
			policy.version,
			stateId,
			null,
			idv.name,
			idv.version,
			task.outputType,
			task.outputName.name
		)
	}

	/** 
	 * Compiles a PolicyStateTaskSelectionLogic.
	 * 
	 * @param tslogic the policy state task selection logic to compile, must not be null
	 * @param policy the parent policy
	 * @param stateId the parent state
	 */
	def void compile(PolicyStateTaskSelectionLogic tslogic, NameVersion policy, String stateId){
		result = apexModel.createPolicyStateTaskSelectionLogic(
			policy.name,
			policy.version,
			stateId,
			tslogic.flavor,
			this.processMultiInput(tslogic.logic)
		)
	}

	/** 
	 * Compiles a PolicyStateFinalizerLogic.
	 * 
	 * @param finalizer the policy state finalizer logic to compile, must not be null
	 * @param policy the parent policy
	 * @param stateId the parent state
	 */
	def void compile(PolicyStateFinalizerLogic finalizer, NameVersion policy, String stateId){
		result = apexModel.createPolicyStateFinalizerLogic(
			policy.name,
			policy.version,
			stateId,
			finalizer.name,
			finalizer.flavor,
			this.processMultiInput(finalizer.logic)
		)
	}

	/** 
	 * Compiles a PolicyStateContextRef.
	 * 
	 * @param ctxref the policy state context reference to compile, must not be null
	 * @param policy the parent policy
	 * @param stateId the parent state
	 */
	def void compile(PolicyStateContextRef ctxref, NameVersion policy, String stateId){
		var idv = new NameVersion(ctxref.albumName.name)
		result = apexModel.createPolicyStateContextRef(
			policy.name,
			policy.version,
			stateId,
			idv.name,
			idv.version
		)
	}

	/** 
	 * Processes a MultiInput.
	 * 
	 * @param mi the multi input to process, can be null
	 * @return the computed string as single line string (asString), multi-line string (block) or read from file.
	 * 		If nothing worked, an empty string.
	 */
	def processMultiInput(MultiInput mi){
		if(mi===null){
			return ""
		}
		if(mi.asString!==null){
			return mi.asString
		}
		if(mi.block!==null){
			return mi.block.substring(1, mi.block.length-1)
		}
		if(mi.file!==null){
			val fileName = mi.file.substring(mi.file.indexOf('"')+1, mi.file.lastIndexOf('"'))
			val platformString = mi.eResource.URI.toPlatformString(true);
			val fsaRoot = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(platformString));
			val projectRoot = fsaRoot.getProject();
			val srcFile = projectRoot.getFile(fileName)
			val Scanner s = new Scanner(srcFile.contents).useDelimiter("\\A");
			return s.next
		}
		return ""
	}

}
