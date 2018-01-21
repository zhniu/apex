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

import java.io.ByteArrayOutputStream;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxConcept;
import com.ericsson.apex.model.utilities.Assertions;

/**
 * This class writes an Apex concept to a string.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 * @param <CONCEPT> the type of Apex concept to write to a string, must be a sub class of {@link AxConcept}
 */
public class ApexModelStringWriter<CONCEPT extends AxConcept> {
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(ApexModelStringWriter.class);

    // Should concepts being written to files be valid
    private boolean validateFlag;

    /**
     * Constructor, set the validation flag.
     *
     * @param validateFlag Should validation be performed prior to output
     */
    public ApexModelStringWriter(final boolean validateFlag) {
        this.validateFlag = validateFlag;
    }

    /**
     * Write a concept to a string.
     *
     * @param concept The concept to write
     * @param rootConceptClass The concept class
     * @param jsonFlag writes JSON if true, and a generic string if false
     * @return The string with the concept
     * @throws ApexException thrown on errors
     */
    public String writeString(final CONCEPT concept, final Class<CONCEPT> rootConceptClass, final boolean jsonFlag) throws ApexException {
        Assertions.argumentNotNull(concept, "concept may not be null");
        
        if (jsonFlag) {
            return writeJSONString(concept, rootConceptClass);
        }
        else {
            return concept.toString();
        }
    }

    /**
     * Write a concept to an XML string.
     *
     * @param concept The concept to write
     * @param rootConceptClass The concept class
     * @return The string with the concept
     * @throws ApexException thrown on errors
     */
    public String writeXMLString(final CONCEPT concept, final Class<CONCEPT> rootConceptClass) throws ApexException {
        LOGGER.debug("running writeXMLString . . .");

        final ApexModelWriter<CONCEPT> conceptWriter = new ApexModelWriter<>(rootConceptClass);
        conceptWriter.setValidateFlag(validateFlag);
        conceptWriter.getCDataFieldSet().add("description");
        conceptWriter.getCDataFieldSet().add("logic");
        conceptWriter.getCDataFieldSet().add("uiLogic");

        final ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        try {
            conceptWriter.write(concept, baOutputStream);
            baOutputStream.close();
        }
        catch (final Exception e) {
            LOGGER.warn("error writing XML string", e);
            throw new ApexException("error writing XML string", e);
        }

        LOGGER.debug("ran writeXMLString");
        return baOutputStream.toString();
    }

    /**
     * Write a concept to a JSON string.
     *
     * @param concept The concept to write
     * @param rootConceptClass The concept class
     * @return The string with the concept
     * @throws ApexException thrown on errors
     */
    public String writeJSONString(final CONCEPT concept, final Class<CONCEPT> rootConceptClass) throws ApexException {
        LOGGER.debug("running writeJSONString . . .");

        final ApexModelWriter<CONCEPT> conceptWriter = new ApexModelWriter<>(rootConceptClass);
        conceptWriter.setJsonOutput(true);
        conceptWriter.setValidateFlag(validateFlag);

        final ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        try {
            conceptWriter.write(concept, baOutputStream);
            baOutputStream.close();
        }
        catch (final Exception e) {
            LOGGER.warn("error writing JSON string", e);
            throw new ApexException("error writing JSON string", e);
        }

        LOGGER.debug("ran writeJSONString");
        return baOutputStream.toString();
    }

    /**
     * Checks if is validate flag.
     *
     * @return true, if checks if is validate flag
     */
    public boolean isValidateFlag() {
        return validateFlag;
    }

    /**
     * Sets the validate flag.
     *
     * @param validateFlag the validate flag
     */
    public void setValidateFlag(final boolean validateFlag) {
        this.validateFlag = validateFlag;
    }
}
