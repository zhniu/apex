/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.protocols;

/**
 * This interface is used to enforce a common type on actions in the Apex messasging protocol. Action types the Apex messaging protocol supports implement this
 * interface.
 *
 * @author Sajeevan Achuthan (sajeevan.achuthan@ericsson.com)
 */
public interface Action {

    /**
     * This method returns a string representation of each action.
     *
     * @return the action string
     */
    String getActionString();
}
