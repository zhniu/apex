/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.basicmodel.concepts;

import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult.ValidationResult;
import com.ericsson.apex.model.utilities.Assertions;

/**
 * A validation message is created for each validation observation observed during validation of a concept. The message holds the key and
 * the class of the concept on which the observation was made as well as the type of observation and a message describing the observation.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class AxValidationMessage {
    private final AxKey observedKey;
    private ValidationResult validationResult = ValidationResult.VALID;
    private final String observedClass;
    private final String message;

    /**
     * Create an validation observation with the given fields.
     *
     * @param observedKey the key of the class on which the validation observation was made
     * @param observedClass the class on which the validation observation was made
     * @param validationResult the type of observation made
     * @param message a message describing the observation
     */
    public AxValidationMessage(final AxKey observedKey, final Class<?> observedClass, final ValidationResult validationResult, final String message) {
        Assertions.argumentNotNull(observedKey, "observedKey may not be null");
        Assertions.argumentNotNull(observedClass, "observedClass may not be null");
        Assertions.argumentNotNull(validationResult, "validationResult may not be null");
        Assertions.argumentNotNull(message, "message may not be null");

        this.observedKey = observedKey;
        this.observedClass = observedClass.getCanonicalName();
        this.validationResult = validationResult;
        this.message = message;
    }

    /**
     * Gets the key of the observation.
     *
     * @return the key of the observation
     */
    public AxKey getObservedKey() {
        return observedKey;
    }

    /**
     * Gets the observed class.
     *
     * @return the observed class
     */
    public String getObservedClass() {
        return observedClass;
    }

    /**
     * Gets the type of observation made.
     *
     * @return the type of observation made
     */
    public ValidationResult getValidationResult() {
        return validationResult;
    }

    /**
     * Get a description of the observation.
     *
     * @return the observation description
     */
    public String getMessage() {
        return message;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return observedKey.toString() + ':' + observedClass + ':' + validationResult.name() + ':' + message;
    }
}
