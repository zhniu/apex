/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.parameters;

/**
 * This interface is implemented by Apex parameter classes so that they can be validated.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public interface ApexParameterValidator {
    /**
     * Validate a parameter java bean, if the parameter bean is valid, an empty string is returned, otherwise the string gives details of the invalid
     * parameters.
     *
     * @return the string with validation errors
     */
    String validate();
}
