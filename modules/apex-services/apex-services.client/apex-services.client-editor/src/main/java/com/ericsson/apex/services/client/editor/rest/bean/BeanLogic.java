/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.services.client.editor.rest.bean;

import javax.xml.bind.annotation.XmlType;

/**
 * The Logic Bean.
 */
@XmlType
public class BeanLogic extends BeanBase {

    private String logic = null, logicFlavour = null;

    /**
     * Gets the logic flavour.
     *
     * @return the logic flavour
     */
    public String getLogicFlavour() {
        return logicFlavour;
    }

    /**
     * Gets the logic.
     *
     * @return the logic
     */
    public String getLogic() {
        return logic;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Logic [logicFlavour=" + logicFlavour + ", logic=" + logic + "]";
    }

}
