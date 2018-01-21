/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.basicmodel.handling;

import com.ericsson.apex.model.basicmodel.concepts.AxModel;

/**
 * This interface is implemented by factories that create Apex models. It is mainly used by unit test classes that generate Apex models for test purposes.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 * @param <MODEL> the type of Apex model to create, must be a sub class of {@link AxModel}
 */
public interface ApexModelCreator<MODEL extends AxModel> {

    /**
     * Gets the model created by the model creator.
     *
     * @return the model created by the model creator
     */
    MODEL getModel();
}
