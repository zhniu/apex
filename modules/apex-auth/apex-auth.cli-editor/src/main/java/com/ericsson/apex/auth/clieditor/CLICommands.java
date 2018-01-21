/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.auth.clieditor;

import java.util.Set;
import java.util.TreeSet;

/**
 * This class contains the CLI commands read in from a JSON file.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class CLICommands {
    private final Set<CLICommand> commandList = new TreeSet<>();

    /**
     * Gets the command set.
     *
     * @return the command set
     */
    public Set<CLICommand> getCommandSet() {
        return commandList;
    }
}
