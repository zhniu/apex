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

import java.io.File;
import java.io.FileOutputStream;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxModel;

/**
 * This class writes an Apex model to a file.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 * @param <M> the type of Apex model to write to file, must be a sub class of {@link AxModel}
 */
public class ApexModelFileWriter<M extends AxModel> {
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(ApexModelFileWriter.class);

    // Should models being written to files be valid
    private boolean validateFlag;

    /**
     * Constructor, set the validation flag.
     *
     * @param validateFlag indicates if validation be performed prior to output
     */
    public ApexModelFileWriter(final boolean validateFlag) {
        this.validateFlag = validateFlag;
    }

    /**
     * Write a model to an XML file.
     *
     * @param model The model to write
     * @param rootModelClass The concept class
     * @param modelFileName The name of the file to write to
     * @throws ApexException thrown on errors
     */
    public void apexModelWriteXMLFile(final M model, final Class<M> rootModelClass, final String modelFileName)
            throws ApexException {
        LOGGER.debug("running apexModelWriteXMLFile . . .");

        final ApexModelWriter<M> modelWriter = new ApexModelWriter<>(rootModelClass);
        modelWriter.setValidateFlag(validateFlag);
        modelWriter.getCDataFieldSet().add("description");
        modelWriter.getCDataFieldSet().add("logic");
        modelWriter.getCDataFieldSet().add("uiLogic");

        writeModelFile(model, modelWriter, modelFileName);

        LOGGER.debug("ran apexModelWriteXMLFile");
    }

    /**
     * Write a model to an JSON file.
     *
     * @param model The model to write
     * @param rootModelClass The concept class
     * @param modelFileName The name of the file to write to
     * @throws ApexException thrown on errors
     */
    public void apexModelWriteJSONFile(final M model, final Class<M> rootModelClass, final String modelFileName)
            throws ApexException {
        LOGGER.debug("running apexModelWriteJSONFile . . .");

        final ApexModelWriter<M> modelWriter = new ApexModelWriter<>(rootModelClass);
        modelWriter.setJsonOutput(true);
        modelWriter.setValidateFlag(validateFlag);

        writeModelFile(model, modelWriter, modelFileName);

        LOGGER.debug("ran apexModelWriteJSONFile");
    }

    /**
     * Checks if the validation flag is set.
     *
     * @return true, the validation flag is set
     */
    public boolean isValidateFlag() {
        return validateFlag;
    }

    /**
     * Sets the validate flag.
     *
     * @param validateFlag the validate flag value
     */
    public void setValidateFlag(final boolean validateFlag) {
        this.validateFlag = validateFlag;
    }

    /**
     * Write a model to a file using a model writer.
     *
     * @param model The model to write
     * @param modelWriter the model writer to use to write the model to the file
     * @param modelFileName the file name of the file to write to
     * @throws ApexException on exceptions writing the model
     */
    private void writeModelFile(final M model, final ApexModelWriter<M> modelWriter, final String modelFileName) throws ApexException {
        final File modelFile = new File(modelFileName);
        if (!modelFile.getParentFile().exists() && !modelFile.getParentFile().mkdirs()) {
            LOGGER.warn("could not create directory  " + modelFile.getParentFile());
            throw new ApexException("could not create directory  " + modelFile.getParentFile());
        }

        try {
            final FileOutputStream fileOutputStream = new FileOutputStream(modelFile);
            modelWriter.write(model, fileOutputStream);
            fileOutputStream.close();
        }
        catch (final Exception e) {
            LOGGER.warn("error processing file " + modelFile.getAbsolutePath(), e);
            throw new ApexException("error processing file " + modelFile.getAbsolutePath(), e);
        }
    }
}
