/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.basicmodel.test;

import com.ericsson.apex.model.basicmodel.concepts.AxModel;
import com.ericsson.apex.model.basicmodel.handling.ApexModelCreator;

/**
 * The Interface TestApexModelCreator is used to create models for Apex model tests. It is mainly used by unit tests for Apex domain models so that
 * developers can write test Java programs to create models.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 * @param <MODEL> the generic type
 */
public interface TestApexModelCreator<MODEL extends AxModel> extends ApexModelCreator<MODEL> {

    /**
     * Gets the malstructured model.
     *
     * @return the malstructured model
     */
    MODEL getMalstructuredModel();

    /**
     * Gets the observation model.
     *
     * @return the observation model
     */
    MODEL getObservationModel();

    /**
     * Gets the warning model.
     *
     * @return the warning model
     */
    MODEL getWarningModel();

    /**
     * Gets the invalid model.
     *
     * @return the invalid model
     */
    MODEL getInvalidModel();
}
