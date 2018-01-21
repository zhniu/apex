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

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxModel;
import com.ericsson.apex.model.utilities.Assertions;

/**
 * This class is used to save Apex models to file in XML or JSON format.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 * @param <MODEL> the type of Apex model to save to file, must be a sub class of {@link AxModel}
 */
public class ApexModelSaver<MODEL extends AxModel> {
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(ApexModelSaver.class);

    // The class of the model and the model to write to disk
    private final Class<MODEL> rootModelClass;
    private final MODEL model;

    // The path into which to write the models
    private final String writePath;

    /**
     * Constructor, specifies the type of the Apex model (a sub class of {@link AxModel}), the model to write, and the path of a directory to which to write the
     * model.
     *
     * @param rootModelClass the class of the model, a sub class of {@link AxModel}
     * @param model the model to write, an instance of a sub class of {@link AxModel}
     * @param writePath the directory to which models will be written. The name of the written model will be the Model Name for its key with the suffix
     *            {@code .xml} or {@code .json}.
     */
    public ApexModelSaver(final Class<MODEL> rootModelClass, final MODEL model, final String writePath) {
        Assertions.argumentNotNull(rootModelClass, "argument rootModelClass may not be null");
        Assertions.argumentNotNull(model, "argument model may not be null");
        Assertions.argumentNotNull(writePath, "writePath rootModelClass may not be null");

        this.rootModelClass = rootModelClass;
        this.model = model;
        this.writePath = writePath;
    }

    /**
     * Write an Apex model to a file in XML format. The model will be written to {@code <writePath/modelKeyName.xml>}
     *
     * @throws ApexException on errors writing the Apex model
     */
    public void apexModelWriteXML() throws ApexException {
        LOGGER.debug("running apexModelWriteXML . . .");

        // Write the file to disk
        final File xmlFile = new File(writePath + "/" + model.getKey().getName() + ".xml");
        new ApexModelFileWriter<MODEL>(true).apexModelWriteXMLFile(model, rootModelClass, xmlFile.getPath());

        LOGGER.debug("ran apexModelWriteXML");
    }

    /**
     * Write an Apex model to a file in JSON format. The model will be written to {@code <writePath/modelKeyName.json>}
     *
     * @throws ApexException on errors writing the Apex model
     */
    public void apexModelWriteJSON() throws ApexException {
        LOGGER.debug("running apexModelWriteJSON . . .");

        // Write the file to disk
        final File jsonFile = new File(writePath + "/" + model.getKey().getName() + ".json");
        new ApexModelFileWriter<MODEL>(true).apexModelWriteJSONFile(model, rootModelClass, jsonFile.getPath());

        LOGGER.debug("ran apexModelWriteJSON");
    }
}
