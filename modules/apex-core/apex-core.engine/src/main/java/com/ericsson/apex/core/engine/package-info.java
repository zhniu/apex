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
 * Provides the core engine implementation for Apex.
 * It builds a state machine for execution for each policy in its policy model.
 * It provides the infrastructure for running policies and their states, for running executors provided by executor plugins, for supplying events
 * and context to running policies, states, and tasks, and for handling event transmission into and out of policies and between states in policies.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
package com.ericsson.apex.core.engine;
