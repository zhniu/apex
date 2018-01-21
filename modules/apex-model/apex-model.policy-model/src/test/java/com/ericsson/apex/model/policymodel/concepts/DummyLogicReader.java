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
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class DummyLogicReader implements AxLogicReader {

    /* (non-Javadoc)
     * @see com.ericsson.apex.model.policymodel.concepts.AxLogicReader#getLogicPackage()
     */
    @Override
    public String getLogicPackage() {
        return null;
    }

    /* (non-Javadoc)
     * @see com.ericsson.apex.model.policymodel.concepts.AxLogicReader#setLogicPackage(java.lang.String)
     */
    @Override
    public AxLogicReader setLogicPackage(String logicPackage) {
        return null;
    }

    /* (non-Javadoc)
     * @see com.ericsson.apex.model.policymodel.concepts.AxLogicReader#getDefaultLogic()
     */
    @Override
    public String getDefaultLogic() {
        return null;
    }

    /* (non-Javadoc)
     * @see com.ericsson.apex.model.policymodel.concepts.AxLogicReader#setDefaultLogic(java.lang.String)
     */
    @Override
    public AxLogicReader setDefaultLogic(String defaultLogic) {
        return null;
    }

    /* (non-Javadoc)
     * @see com.ericsson.apex.model.policymodel.concepts.AxLogicReader#readLogic(com.ericsson.apex.model.policymodel.concepts.AxLogic)
     */
    @Override
    public String readLogic(AxLogic axLogic) {
        return "Dummy Logic";
    }
}
