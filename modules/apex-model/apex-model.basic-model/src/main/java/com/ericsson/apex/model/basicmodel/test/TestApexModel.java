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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxModel;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.basicmodel.dao.ApexDao;
import com.ericsson.apex.model.basicmodel.dao.ApexDaoFactory;
import com.ericsson.apex.model.basicmodel.dao.DAOParameters;
import com.ericsson.apex.model.basicmodel.handling.ApexModelFileWriter;
import com.ericsson.apex.model.basicmodel.handling.ApexModelReader;
import com.ericsson.apex.model.basicmodel.handling.ApexModelWriter;
import com.ericsson.apex.model.utilities.ResourceUtils;

/**
 * This class tests reading and writing of Apex models to file and to a database using JPA. It also tests validation of Apex models. This class is designed for
 * use in unit tests in modules that define Apex models.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 * @param <MODEL> the generic type
 */
public class TestApexModel<MODEL extends AxModel> {
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(TestApexModel.class);

    // The root model class that specifies the root to import and export from
    private final Class<MODEL> rootModelClass;

    // The class that provides the model
    private TestApexModelCreator<MODEL> modelCreator = null;

    /**
     * Constructor, defines the subclass of {@link AxModel} that is being tested and the {@link TestApexModelCreator} object that is used to generate Apex
     * models.
     *
     * @param rootModelClass the Apex model class, a sub class of {@link AxModel}
     * @param modelCreator the @link TestApexModelCreator} that will generate Apex models of various types for testing
     */
    public TestApexModel(final Class<MODEL> rootModelClass, final TestApexModelCreator<MODEL> modelCreator) {
        this.rootModelClass = rootModelClass;
        this.modelCreator = modelCreator;
    }

    /**
     * Get a test Apex model using the model creator.
     *
     * @return the test Apex model
     */
    public final MODEL getModel() {
        return modelCreator.getModel();
    }

    /**
     * Test write and read in XML format.
     *
     * @throws ApexException on write/read errors
     */
    public final void testApexModelWriteReadXML() throws ApexException {
        LOGGER.debug("running testApexModelWriteReadXML . . .");

        final MODEL model = modelCreator.getModel();

        // Write the file to disk
        File xmlFile;
        
        try {
            xmlFile = File.createTempFile("ApexModel", ".xml");
            xmlFile.deleteOnExit();
        }
        catch (final Exception e) {
            LOGGER.warn("error creating temporary file for Apex model", e);
            throw new ApexException("error creating temporary file for Apex model", e);
        }
        new ApexModelFileWriter<MODEL>(true).apexModelWriteXMLFile(model, rootModelClass, xmlFile.getPath());

        // Read the file from disk
        final ApexModelReader<MODEL> modelReader = new ApexModelReader<>(rootModelClass);

        try {
            final URL apexModelURL = ResourceUtils.getLocalFile(xmlFile.getAbsolutePath());
            final MODEL fileModel = modelReader.read(apexModelURL.openStream());
            if (!model.equals(fileModel)) {
                LOGGER.warn("test model does not equal model read from XML file " + xmlFile.getAbsolutePath());
                throw new ApexException("test model does not equal model read from XML file " + xmlFile.getAbsolutePath());
            }
        }
        catch (final Exception e) {
            LOGGER.warn("error processing file " + xmlFile.getAbsolutePath(), e);
            throw new ApexException("error processing file " + xmlFile.getAbsolutePath(), e);
        }

        final ApexModelWriter<MODEL> modelWriter = new ApexModelWriter<>(rootModelClass);
        modelWriter.getCDataFieldSet().add("description");
        modelWriter.getCDataFieldSet().add("logic");
        modelWriter.getCDataFieldSet().add("uiLogic");

        final ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        modelWriter.write(model, baOutputStream);
        final ByteArrayInputStream baInputStream = new ByteArrayInputStream(baOutputStream.toByteArray());
        final MODEL byteArrayModel = modelReader.read(baInputStream);
        if (!model.equals(byteArrayModel)) {
            LOGGER.warn("test model does not equal XML marshalled and unmarshalled model");
            throw new ApexException("test model does not equal XML marshalled and unmarshalled model");
        }

        LOGGER.debug("ran testApexModelWriteReadXML");
    }

    /**
     * Test write and read in JSON format.
     *
     * @throws ApexException on write/read errors
     */
    public final void testApexModelWriteReadJSON() throws ApexException {
        LOGGER.debug("running testApexModelWriteReadJSON . . .");

        final MODEL model = modelCreator.getModel();

        // Write the file to disk
        File jsonFile;
        try {
            jsonFile = File.createTempFile("ApexModel", ".xml");
            jsonFile.deleteOnExit();
        }
        catch (final Exception e) {
            LOGGER.warn("error creating temporary file for Apex model", e);
            throw new ApexException("error creating temporary file for Apex model", e);
        }
        new ApexModelFileWriter<MODEL>(true).apexModelWriteJSONFile(model, rootModelClass, jsonFile.getPath());

        // Read the file from disk
        final ApexModelReader<MODEL> modelReader = new ApexModelReader<>(rootModelClass);

        try {
            final URL apexModelURL = ResourceUtils.getLocalFile(jsonFile.getAbsolutePath());
            final MODEL fileModel = modelReader.read(apexModelURL.openStream());
            if (!model.equals(fileModel)) {
                LOGGER.warn("test model does not equal model read from XML file " + jsonFile.getAbsolutePath());
                throw new ApexException("test model does not equal model read from XML file " + jsonFile.getAbsolutePath());
            }
        }
        catch (final Exception e) {
            LOGGER.warn("error processing file " + jsonFile.getAbsolutePath(), e);
            throw new ApexException("error processing file " + jsonFile.getAbsolutePath(), e);
        }

        final ApexModelWriter<MODEL> modelWriter = new ApexModelWriter<>(rootModelClass);
        modelWriter.setJsonOutput(true);

        final ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        modelWriter.write(model, baOutputStream);
        final ByteArrayInputStream baInputStream = new ByteArrayInputStream(baOutputStream.toByteArray());
        final MODEL byteArrayModel = modelReader.read(baInputStream);
        if (!model.equals(byteArrayModel)) {
            LOGGER.warn("test model does not equal JSON marshalled and unmarshalled model");
            throw new ApexException("test model does not equal JSON marshalled and unmarshalled model");
        }

        LOGGER.debug("ran testApexModelWriteReadJSON");
    }

    /**
     * Test write and read of an Apex model to database using JPA.
     *
     * @param daoParameters the DAO parameters to use for JPA/JDBC
     * @throws ApexException thrown on errors writing or reading the model to database
     */
    public final void testApexModelWriteReadJPA(final DAOParameters daoParameters) throws ApexException {
        LOGGER.debug("running testApexModelWriteReadJPA . . .");

        final MODEL model = modelCreator.getModel();

        final ApexDao apexDao = new ApexDaoFactory().createApexDao(daoParameters);
        apexDao.init(daoParameters);

        apexDao.create(model);
        final MODEL dbJPAModel = apexDao.get(rootModelClass, model.getKey());
        apexDao.close();

        if (!model.equals(dbJPAModel)) {
            LOGGER.warn("test model does not equal model written and read using generic JPA");
            throw new ApexException("test model does not equal model written and read using generic JPA");
        }

        LOGGER.debug("ran testApexModelWriteReadJPA");
    }

    /**
     * Test that an Apex model is valid.
     *
     * @return the result of the validation
     * @throws ApexException thrown on errors validating the Apex model
     */
    public final AxValidationResult testApexModelValid() throws ApexException {
        LOGGER.debug("running testApexModelVaid . . .");

        final MODEL model = modelCreator.getModel();
        final AxValidationResult result = model.validate(new AxValidationResult());

        if (!result.isValid()) {
            LOGGER.warn("model is invalid " + result.toString());
            throw new ApexException("model is invalid " + result.toString());
        }

        LOGGER.debug("ran testApexModelVaid");
        return result;
    }

    /**
     * Test that an Apex model is structured incorrectly.
     *
     * @return the result of the validation
     * @throws ApexException thrown on errors validating the Apex model
     */
    public final AxValidationResult testApexModelVaidateMalstructured() throws ApexException {
        LOGGER.debug("running testApexModelVaidateMalstructured . . .");

        final MODEL model = modelCreator.getMalstructuredModel();
        final AxValidationResult result = model.validate(new AxValidationResult());

        if (result.isValid()) {
            LOGGER.warn("model should not be valid " + result.toString());
            throw new ApexException("should not be valid " + result.toString());
        }

        LOGGER.debug("ran testApexModelVaidateMalstructured");
        return result;
    }

    /**
     * Test that an Apex model has observations.
     *
     * @return the result of the validation
     * @throws ApexException thrown on errors validating the Apex model
     */
    public final AxValidationResult testApexModelVaidateObservation() throws ApexException {
        LOGGER.debug("running testApexModelVaidateObservation . . .");

        final MODEL model = modelCreator.getObservationModel();
        final AxValidationResult result = model.validate(new AxValidationResult());

        if (!result.isValid()) {
            LOGGER.warn("model is invalid " + result.toString());
            throw new ApexException("model is invalid " + result.toString());
        }

        if (!result.getValidationResult().equals(AxValidationResult.ValidationResult.OBSERVATION)) {
            LOGGER.warn("model should have observations");
            throw new ApexException("model should have observations");
        }

        LOGGER.debug("ran testApexModelVaidateObservation");
        return result;
    }

    /**
     * Test that an Apex model has warnings.
     *
     * @return the result of the validation
     * @throws ApexException thrown on errors validating the Apex model
     */
    public final AxValidationResult testApexModelVaidateWarning() throws ApexException {
        LOGGER.debug("running testApexModelVaidateWarning . . .");

        final MODEL model = modelCreator.getWarningModel();
        final AxValidationResult result = model.validate(new AxValidationResult());

        if (!result.isValid()) {
            LOGGER.warn("model is invalid " + result.toString());
            throw new ApexException("model is invalid " + result.toString());
        }

        if (!result.getValidationResult().equals(AxValidationResult.ValidationResult.WARNING)) {
            LOGGER.warn("model should have warnings");
            throw new ApexException("model should have warnings");
        }

        LOGGER.debug("ran testApexModelVaidateWarning");
        return result;
    }

    /**
     * Test that an Apex model is invalid.
     *
     * @return the result of the validation
     * @throws ApexException thrown on errors validating the Apex model
     */
    public final AxValidationResult testApexModelVaidateInvalidModel() throws ApexException {
        LOGGER.debug("running testApexModelVaidateInvalidModel . . .");

        final MODEL model = modelCreator.getInvalidModel();
        final AxValidationResult result = model.validate(new AxValidationResult());

        if (result.isValid()) {
            LOGGER.warn("model should not be valid " + result.toString());
            throw new ApexException("should not be valid " + result.toString());
        }

        LOGGER.debug("ran testApexModelVaidateInvalidModel");
        return result;
    }
}
