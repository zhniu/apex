/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.auth.xtext.utilities

/**
 * A collection of error messages, also used as code.
 * 
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
interface ApexErrorCodes {

	/** Error: a default definition without a default version defined. */
	static final String ERROR_DEFAULTS_WO_VERSION = "No version defined for defaults, required if defaults are used"

	/** Error: No policy model defined. */
	static final String ERROR_NO_MODEL_DEFINED = "No policy model defined"

	/** Error: a required name is null. */
	static final String ERROR_NAME_IS_NULL = "Name is null"

	/** Error: a required name is empty (null or empty string after trim). */
	static final String ERROR_NAME_IS_EMPTY = "Name is empty"

	/** Error: a name contains a dash, unexpectedly. */
	static final String ERROR_NAME_CONTAINS_DASH = "Name contains '-'"

	/** Error: a model requires schema declarations but does not have one. */
	static final String ERROR_MODEL_WO_SCHEMAS = "No schema declared, at least 1 required"

	/** Error: a model requires task definitions but does not have one. */
	static final String ERROR_MODEL_WO_TASKS = "No task defined, at least 1 required"

	/** Error: a model requires event declarations but does not have one. */
	static final String ERROR_MODEL_WO_EVENTS = "No event declared, at least 1 required"

	/** Error: a model requires policy definitions but does not have one. */
	static final String ERROR_MODEL_WO_POLICIES = "No policy defined, at least 1 required"

	/** Error: a required description was null. */
	static final String ERROR_DESCRIPTION_NULL = "Description is null"

	/** Error: a required description was empty (null or empty string after trim). */
	static final String ERROR_DESCRIPTION_EMPTY = "Description is empty"

	/** Error: a model requires policy state definitions but does not have one. */
	static final String ERROR_POLICY_WO_STATES = "No state defined, at least 1 required"

	/** Error: a model requires a policy defining a first state, but policy does not have one. */
	static final String ERROR_POLICY_WO_FIRST_STATE = "No first state defined"

	/** Error: a name contains version information, unexpectedly. */
	static final String ERROR_NAME_CONTAINS_VERSION = "A simple identifier should not contain version information"

	/** Error: a single string from a multi-input is empty (null or empty string after trim). */
	static final String ERROR_MULTIINPUT_EMPTY_STRING = "empty description/logic/schema"

	/** Error: a multi-line input from a multi-input is empty (null or empty string after trim). */
	static final String ERROR_MULTIINPUT_EMPTY_MULTISTRING = "empty description/logic/schema"

	/** Error: a filename from a multi-input is empty (null or empty string after trim). */
	static final String ERROR_MULTIINPUT_EMPTY_FILENAME = "empty file name for description/logic/schema"

	/** Error: a file named in a multi-input does not exist. */
	static final String ERROR_MULTIINPUT_FILE_NOTEXIST = "file does not exist"

	/** Error: a file named in a multi-input cannot be read. */
	static final String ERROR_MULTIINPUT_FILE_CANTREAD = "cannot read file"

	/** Error: a file named in a multi-input is empty (no contents after trim). */
	static final String ERROR_MULTIINPUT_FILE_EMPTY = "file seems to be empty"

	/** Error: a Java FQCN contains a dash. */
	static final String ERROR_JAVAFQCN_HASDASH = "Java package names must not contain '-'"

	/** Error: a Java FQCN contains no dot and default package or main package is not supported. */
	static final String ERROR_JAVAFQCN_NODOT = "Java package names must contain '.', default package not supported"

	/** Error: a Java FQCN has package names with upper case characters. */
	static final String ERROR_JAVAFQCN_PGK_UPPER = "Java package name should not contain upper case characters"

	/** Error: a Java FQCN points to a class that does not start with an upper case character. */
	static final String ERROR_JAVAFQCN_CLASSSTART_NOTUPPER = "Java class name should not start with upper case character"

	/** Error: an event declaration is missing fields. */
	static final String ERROR_EVENT_WO_FIELDS = "No field defined, at least 1 required"

	/** Error: a task definition is missing input fields. */
	static final String ERROR_TASK_WO_INFIELDS = "No input field defined, at least 1 required"

	/** Error: a task definition is missing output fields. */
	static final String ERROR_TASK_WO_OUTFIELDS = "No output field defined, at least 1 required"

	/** Error: a policy state definition is missing outputs. */
	static final String ERROR_POLICYSTATE_WO_OUTPUTS = "No state output object defined, at least 1 required"

	/** Error: a policy state definition is missing tasks. */
	static final String ERROR_POLICYSTATE_WO_TASKS = "No state task object defined, at least 1 required"

	/** Error: a task infield is not in the referenced state's trigger event. */
	static final String ERROR_TASKINFIELD_NOTIN_STATETRIGGER = "Task infield not in state trigger event"

	/** Error: a task outfield is not in the referenced state output. */
	static final String ERROR_TASKOUTFIELD_NOTIN_STATEOUTPUT = "Task outfield not in state output"
}
