/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.protocols.engdep;

import com.ericsson.apex.core.protocols.Action;

/**
 * Action types the EngDep messaging protocol supports.
 *
 * @author Sajeevan Achuthan (sajeevan.achuthan@ericsson.com)
 */
public enum EngDepAction implements Action {
    /** Action to get information on the running engine service. */
    GET_ENGINE_SERVICE_INFO {
        @Override
        public String getActionString() {
            return "Apex engine service information";
        }
    },
    /** Action to update the policy model in an engine service. */
    UPDATE_MODEL {
        @Override
        public String getActionString() {
            return "update model on Apex engine service";
        }
    },
    /** Action to start an engine service. */
    START_ENGINE {
        @Override
        public String getActionString() {
            return "starts an Apex engine";
        }
    },
    /** Action to stop an engine service. */
    STOP_ENGINE {
        @Override
        public String getActionString() {
            return "stops an Apex engine service";
        }
    },
    /** Action to start sending periodic events to an engine service. */
    START_PERIODIC_EVENTS {
        @Override
        public String getActionString() {
            return "starts periodic events on an Apex engine service";
        }
    },
    /** Action to stop sending periodic events to an engine service. */
    STOP_PERIODIC_EVENTS {
        @Override
        public String getActionString() {
            return "stops periodic events on an Apex engine service";
        }
    },
    /** Action to get the status of an engine in the engine service. */
    GET_ENGINE_STATUS {
        @Override
        public String getActionString() {
            return "gets the status of an Apex engine service";
        }
    },
    /** Action to get information on an engine in the engine service. */
    GET_ENGINE_INFO {
        @Override
        public String getActionString() {
            return "gets runtime information an Apex engine service";
        }
    },
    /** The response message to all actions. */
    RESPONSE {
        @Override
        public String getActionString() {
            return "response from Apex engine service";
        }
    };
}
