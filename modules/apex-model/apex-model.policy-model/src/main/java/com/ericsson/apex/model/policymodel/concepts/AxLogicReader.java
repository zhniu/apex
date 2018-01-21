/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.policymodel.concepts;

/**
 * This interface is used to provide logic to a {@link AxLogic} instance. Implementations usually store logic on disk in a structure similar to Java package
 * naming conventions. The logic package gives the package path, a directory where a set of logic is defined. Default logic is logic that can be used as dummy
 * logic in tasks or states that are filler tasks or states. The actual logic is returned by the {@code readLogic()} method. The interface is used mainly by
 * unit test classes that generate test logic.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public interface AxLogicReader {

    /**
     * Get the logic package path.
     *
     * @return the logic package path
     */
    String getLogicPackage();

    /**
     * Set the logic package path.
     *
     * @param logicPackage the name of the package that contains the logic for this logic reader
     * @return the logic reader on which this method was called, used for daisy chaining configuration
     */
    AxLogicReader setLogicPackage(String logicPackage);

    /**
     * Get the default logic name.
     *
     * @return the default logic name
     */
    String getDefaultLogic();

    /**
     * Set the default logic name.
     *
     * @param defaultLogic the default logic name
     * @return the logic reader on which this method was called, used for daisy chaining configuration
     */
    AxLogicReader setDefaultLogic(String defaultLogic);

    /**
     * Read the logic for an AxLogic object.
     *
     * @param axLogic the AxLogic object
     * @return the logic as a string
     */
    String readLogic(AxLogic axLogic);
}
