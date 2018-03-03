/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.policymodel.handling;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.model.basicmodel.concepts.AxKey;
import com.ericsson.apex.model.policymodel.concepts.AxLogic;
import com.ericsson.apex.model.policymodel.concepts.AxLogicReader;
import com.ericsson.apex.model.policymodel.concepts.PolicyRuntimeException;
import com.ericsson.apex.model.utilities.ResourceUtils;

/**
 * This class is used to read Task Logic and Task Selection Logic from files into a string. A {@link PolicyLogicReader} can then be used to provide the logic on
 * a {@link AxLogic} class constructor.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class PolicyLogicReader implements AxLogicReader {
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(PolicyModelSplitter.class);

    // The path of the logic package
    private String logicPackage = "";

    // Flag indicating if default logic should be returned
    private String defaultLogic;

    /*
     * (non-Javadoc)
     *
     * @see .policymodel.concepts.AxLogicReader#getLogicPackage()
     */
    @Override
    public String getLogicPackage() {
        return logicPackage;
    }

    /*
     * (non-Javadoc)
     *
     * @see .policymodel.concepts.AxLogicReader#setLogicPackage(java.lang.String)
     */
    @Override
    public AxLogicReader setLogicPackage(final String incomingLogicPackage) {
        this.logicPackage = incomingLogicPackage;
        return this;
    }

    /*
     * (non-Javadoc)
     *
     * @see .policymodel.concepts.AxLogicReader#getDefaultLogic()
     */
    @Override
    public String getDefaultLogic() {
        return defaultLogic;
    }

    /*
     * (non-Javadoc)
     *
     * @see .policymodel.concepts.AxLogicReader#setDefaultLogic(boolean)
     */
    @Override
    public AxLogicReader setDefaultLogic(final String incomingDefaultLogic) {
        this.defaultLogic = incomingDefaultLogic;
        return this;
    }

    /*
     * (non-Javadoc)
     *
     * @see .policymodel.concepts.AxLogicReader#readLogic(.policymodel.concepts.AxLogic)
     */
    @Override
    public String readLogic(final AxLogic axLogic) {
        // Java uses compiled logic, other executor types run scripts
        if (axLogic.getLogicFlavour().equals("JAVA")) {
            // Check if we're using the default logic
            if (defaultLogic != null) {
                // Return the java class name for the default logic
                return logicPackage + ".java." + defaultLogic;
            }
            else {
                // Return the java class name for the logic
                if (axLogic.getKey().getParentLocalName().equals(AxKey.NULL_KEY_NAME)) {
                    return logicPackage + ".java." + axLogic.getKey().getParentKeyName() + '_' + axLogic.getKey().getLocalName();
                }
                else {
                    return logicPackage + ".java." + axLogic.getKey().getParentKeyName() + '_' + axLogic.getKey().getParentLocalName() + '_'
                            + axLogic.getKey().getLocalName();
                }
            }
        }
        // Now, we read in the script

        // Get the package name of the current package and convert dots to slashes for the file path
        String fullLogicFilePath = logicPackage.replaceAll("\\.", "/");

        // Now, the logic should be in a sub directory for the logic executor type
        fullLogicFilePath += "/" + axLogic.getLogicFlavour().toLowerCase();

        // Check if we're using the default logic
        if (defaultLogic != null) {
            // Default logic
            fullLogicFilePath += "/" + defaultLogic;
        }
        else {
            if (axLogic.getKey().getParentLocalName().equals(AxKey.NULL_KEY_NAME)) {
                fullLogicFilePath += "/" + axLogic.getKey().getParentKeyName() + "_" + axLogic.getKey().getLocalName();
            }
            else {
                fullLogicFilePath += "/" + axLogic.getKey().getParentKeyName() + "_" + axLogic.getKey().getParentLocalName() + "_"
                        + axLogic.getKey().getLocalName();
            }
        }

        // Now get the type of executor to find the extension of the file
        fullLogicFilePath += "." + axLogic.getLogicFlavour().toLowerCase();

        final String logicString = ResourceUtils.getResourceAsString(fullLogicFilePath);

        // Check if the logic was found
        if (logicString == null || logicString.length() == 0) {
            LOGGER.warn("logic not found for logic \"" + fullLogicFilePath + "\"");
            throw new PolicyRuntimeException("logic not found for logic \"" + fullLogicFilePath + "\"");
        }

        // Return the right trimmed logic string
        return logicString.replaceAll("\\s+$", "");
    }
}
