/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/


/**
 * Provides context and facades for executing tasks, task selection logic, and state finalizer logic.
 * The public fields and methods of {@link TaskExecutionContext}, {@link TaskSelectionExecutionContext} and {@link StateFinalizerExecutionContext} are available
 * to task logic, task selection logic, and state finalizer logic respectively when that logic is executing in an executor plugin under the control of an APEX
 * engine.
 * 
 * The {@link AxStateFacade} and {@link AxTaskFacade} classes provide facades and convenience methods for state and task definition information for logic at
 * execution time.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
package com.ericsson.apex.core.engine.executor.context;
