/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.basicmodel.service;

import com.ericsson.apex.model.basicmodel.concepts.ApexRuntimeException;
import com.ericsson.apex.model.utilities.Assertions;

/**
 * This class defines an abstract parameter class that acts as a base class for all parameters in Apex. The abstract parameter class holds the name of a
 * subclass of this abstract parameter class {@link AbstractParameters}. The class of the parameter class is checked at construction and on calls to the
 * {@link #getParameterClass()} method.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public abstract class AbstractParameters {
    // The name of the parameter subclass
    private String parameterClassName = this.getClass().getCanonicalName();

    /**
     * Constructor, creates a parameter class that must be a subclass of {@link AbstractParameters}.
     *
     * @param parameterClassName the full canonical class name of the parameter class
     */
    public AbstractParameters(final String parameterClassName) {
        try {
            Assertions.assignableFrom(Class.forName(parameterClassName), AbstractParameters.class);
        }
        catch (IllegalArgumentException | ClassNotFoundException e) {
            throw new ApexRuntimeException(
                    "class \"" + parameterClassName + "\" not found or not an instance of \"" + this.getClass().getCanonicalName() + "\"", e);
        }
    }

    /**
     * Gets the parameter class.
     *
     * @return the parameter class
     */
    @SuppressWarnings("unchecked")
    public final Class<? extends AbstractParameters> getParameterClass() {
        try {
            return (Class<? extends AbstractParameters>) Class.forName(parameterClassName);
        }
        catch (final ClassNotFoundException e) {
            throw new ApexRuntimeException("class not found for parameter class name \"" + parameterClassName + "\"");
        }
    }

    /**
     * Gets the parameter class name.
     *
     * @return the parameter class name
     */
    public final String getParameterClassName() {
        return parameterClassName;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AbstractParameters [parameterClassName=" + parameterClassName + "]";
    }
}
