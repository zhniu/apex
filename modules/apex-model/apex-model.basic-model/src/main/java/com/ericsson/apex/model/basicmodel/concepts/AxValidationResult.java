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

import java.util.LinkedList;
import java.util.List;

/**
 * This class records the result of a validation and holds all validatino observation messages.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class AxValidationResult {
    /**
     * The ValidationResult enumeration describes the severity of a validation result.
     */
    public enum ValidationResult {
        /** No problems or observations were detected during validation. */
        VALID,
        /** Observations were made on a concept (such as blank descriptions) of a nature that will not affect the use of the concept. */
        OBSERVATION,
        /** Warnings were made on a concept (such as defined but unused concepts) of a nature that may affect the use of the concept. */
        WARNING,
        /**
         * Errors were detected on a concept (such as referenced but undefined concepts) of a nature that will affect the use of the concept.
         */
        INVALID
    }

    // The actual verification result
    private ValidationResult validationResult = ValidationResult.VALID;

    // Messages collected during the verification process
    private final List<AxValidationMessage> messageList = new LinkedList<>();

    /**
     * Check if a validation reported a valid concept, returns true if the model is usable (that is, even if the model has warnings or
     * observations).
     *
     * @return true, if the concept is reported as valid and can be used
     */
    public boolean isValid() {
        return validationResult != ValidationResult.INVALID;
    }

    /**
     * Check if a validation reported a concept with no errors or warnings, returns true if the model is OK to use.
     *
     * @return true, if the concept has no warnings or errors
     */
    public boolean isOK() {
        return validationResult == ValidationResult.VALID || validationResult == ValidationResult.OBSERVATION;
    }

    /**
     * Gets the validation result.
     *
     * @return the validation result on a concept
     */
    public ValidationResult getValidationResult() {
        return validationResult;
    }

    /**
     * Gets the list of validation results on the concept.
     *
     * @return the list of validaiton results
     */
    public List<AxValidationMessage> getMessageList() {
        return messageList;
    }

    /**
     * Adds a validation message to the validation result, used by validate() implementations on {@link AxConcept} subclasses to report
     * validaiton observations.
     *
     * @param validationMessage the validation message
     */
    public void addValidationMessage(final AxValidationMessage validationMessage) {
        messageList.add(validationMessage);

        // Check if the incoming message has a more sever status than the
        // current one on the overall validation result,
        // if so, the overall result goes to that level
        if (validationMessage.getValidationResult().ordinal() > validationResult.ordinal()) {
            validationResult = validationMessage.getValidationResult();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        switch (validationResult) {
        case VALID:

        builder.append("***validation of model successful***");
            return builder.toString();
        case OBSERVATION:

        builder.append("\n***observations noted during validation of model***\n");
            break;
        case WARNING:

        builder.append("\n***warnings issued during validation of model***\n");
            break;
        case INVALID:
        builder.append("\n***validation of model failed***\n");
            break;
        default:
            break;
        }

        for (final AxValidationMessage message : messageList) {
            builder.append(message);
            builder.append("\n");
        }

        builder.append("********************************");
        return builder.toString();
    }
}
