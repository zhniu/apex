/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.modelapi.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.ApexRuntimeException;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.basicmodel.dao.ApexDao;
import com.ericsson.apex.model.basicmodel.dao.ApexDaoFactory;
import com.ericsson.apex.model.basicmodel.dao.DAOParameters;
import com.ericsson.apex.model.basicmodel.handling.ApexModelException;
import com.ericsson.apex.model.basicmodel.handling.ApexModelFileWriter;
import com.ericsson.apex.model.basicmodel.handling.ApexModelReader;
import com.ericsson.apex.model.basicmodel.handling.ApexModelStringWriter;
import com.ericsson.apex.model.basicmodel.handling.ApexModelWriter;
import com.ericsson.apex.model.modelapi.ApexAPIResult;
import com.ericsson.apex.model.modelapi.ApexModel;
import com.ericsson.apex.model.policymodel.concepts.AxPolicy;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;
import com.ericsson.apex.model.policymodel.handling.PolicyAnalyser;
import com.ericsson.apex.model.policymodel.handling.PolicyAnalysisResult;
import com.ericsson.apex.model.policymodel.handling.PolicyModelComparer;
import com.ericsson.apex.model.policymodel.handling.PolicyModelMerger;
import com.ericsson.apex.model.policymodel.handling.PolicyModelSplitter;
import com.ericsson.apex.model.utilities.Assertions;
import com.ericsson.apex.model.utilities.ResourceUtils;
import com.ericsson.apex.model.utilities.TextFileUtils;

/**
 * This class acts as a facade for model handling for the Apex Model API.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ModelHandlerFacade {
    // Apex model we're working towards
    private final ApexModel apexModel;

    // JSON output on list/delete if set
    private final boolean jsonMode;

    /**
     * This Constructor creates a model handling facade for the given {@link ApexModel}.
     *
     * @param apexModel the apex model to manipulate
     * @param apexProperties properties for the model
     * @param jsonMode set to true to return JSON strings in list and delete operations, otherwise set to false
     */
    public ModelHandlerFacade(final ApexModel apexModel, final Properties apexProperties, final boolean jsonMode) {
        Assertions.argumentNotNull(apexModel, "apexModel may not be null");
        Assertions.argumentNotNull(apexProperties, "apexProperties may not be null");

        this.apexModel = apexModel;
        this.jsonMode = jsonMode;
    }

    /**
     * Load an Apex model from a string.
     *
     * @param modelString the string with the model
     * @return the result of the operation
     */
    public ApexAPIResult loadFromString(final String modelString) {
        Assertions.argumentNotNull(modelString, "modelString may not be null");

        if (!apexModel.getPolicyModel().getKey().equals(AxArtifactKey.getNullKey())) {
            return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_EXISTS, "model " + apexModel.getPolicyModel().getKey().getID() + " already loaded");
        }

        ApexAPIResult result = new ApexAPIResult();
        AxPolicyModel newPolicyModel = loadModelFromString(modelString, result);
        apexModel.setPolicyModel(newPolicyModel != null ? newPolicyModel : new AxPolicyModel());

        return result;
    }

    /**
     * Load an Apex model from a file.
     *
     * @param fileName the file name of the file with the model
     * @return the result of the operation
     */
    public ApexAPIResult loadFromFile(final String fileName) {
        Assertions.argumentNotNull(fileName, "fileName may not be null");

        if (!apexModel.getPolicyModel().getKey().equals(AxArtifactKey.getNullKey())) {
            return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_EXISTS, "model " + apexModel.getPolicyModel().getKey().getID() + " already loaded");
        }

        ApexAPIResult result = new ApexAPIResult();
        AxPolicyModel newPolicyModel = loadModelFromFile(fileName, result);
        apexModel.setPolicyModel(newPolicyModel != null ? newPolicyModel : new AxPolicyModel());

        return result;
    }

    /**
     * Save an Apex model to a file.
     *
     * @param fileName the file name
     * @param xmlFlag if true, save the file in XML format, otherwise save the file in the default JSON format
     * @return the result of the operation
     */
    public ApexAPIResult saveToFile(final String fileName, final boolean xmlFlag) {
        Assertions.argumentNotNull(fileName, "fileName may not be null");

        ApexModelFileWriter<AxPolicyModel> apexModelFileWriter = new ApexModelFileWriter<>(false);

        try {
            if (xmlFlag) {
                apexModelFileWriter.apexModelWriteXMLFile(apexModel.getPolicyModel(), AxPolicyModel.class, fileName);
            }
            else {
                apexModelFileWriter.apexModelWriteJSONFile(apexModel.getPolicyModel(), AxPolicyModel.class, fileName);
            }
            return new ApexAPIResult();
        }
        catch (ApexException e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Load an Apex model from a database.
     *
     * @param modelName the name of the model to load
     * @param modelVersion the version of the model to load, loads the policy model from the database with this name, if more than one exist, an exception is
     *            thrown
     * @param daoParameters the parameters to use to access the database over JDBC
     * @return the result of the operation
     */
    public ApexAPIResult loadFromDatabase(final String modelName, final String modelVersion, final DAOParameters daoParameters) {
        Assertions.argumentNotNull(modelName, "modelName may not be null");
        Assertions.argumentNotNull(daoParameters, "daoParameters may not be null");

        if (!apexModel.getPolicyModel().getKey().equals(AxArtifactKey.getNullKey())) {
            return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_EXISTS, "model " + apexModel.getPolicyModel().getKey().getID() + " already loaded");
        }

        ApexDao apexDao = null;
        try {
            apexDao = new ApexDaoFactory().createApexDao(daoParameters);
            apexDao.init(daoParameters);

            // Single specific model requested
            if (modelVersion != null) {
                AxPolicyModel daoPolicyModel = apexDao.get(AxPolicyModel.class, new AxArtifactKey(modelName, modelVersion));

                if (daoPolicyModel != null) {
                    apexModel.setPolicyModel(daoPolicyModel);
                    return new ApexAPIResult();
                }
                else {
                    apexModel.setPolicyModel(new AxPolicyModel());
                    return new ApexAPIResult(ApexAPIResult.RESULT.FAILED,
                            "no policy model with name " + modelName + " and version " + modelVersion + " found in database");
                }
            }
            // Fishing expedition
            else {
                AxPolicyModel foundPolicyModel = null;

                List<AxPolicyModel> policyModelList = apexDao.getAll(AxPolicyModel.class);
                for (AxPolicyModel dbPolicyModel : policyModelList) {
                    if (dbPolicyModel.getKey().getName().equals(modelName)) {
                        if (foundPolicyModel == null) {
                            foundPolicyModel = dbPolicyModel;
                        }
                        else {
                            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, "more than one policy model with name " + modelName + " found in database");
                        }
                    }
                }

                if (foundPolicyModel != null) {
                    apexModel.setPolicyModel(foundPolicyModel);
                    return new ApexAPIResult();
                }
                else {
                    apexModel.setPolicyModel(new AxPolicyModel());
                    return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, "no policy model with name " + modelName + " found in database");
                }
            }
        }
        catch (ApexException | ApexRuntimeException e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
        finally {
            if (apexDao != null) {
                apexDao.close();
            }
        }
    }

    /**
     * Save an Apex model to a database.
     *
     * @param daoParameters the parameters to use to access the database over JDBC
     * @return the result of the operation
     */
    public ApexAPIResult saveToDatabase(final DAOParameters daoParameters) {
        ApexDao apexDao = null;

        try {
            apexDao = new ApexDaoFactory().createApexDao(daoParameters);
            apexDao.init(daoParameters);

            apexDao.create(apexModel.getPolicyModel());
            return new ApexAPIResult();
        }
        catch (ApexException e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
        finally {
            if (apexDao != null) {
                apexDao.close();
            }
        }
    }

    /**
     * Read an APEX model from a location identified by a URL.
     *
     * @param urlString the url string
     * @return the result of the operation
     */
    public ApexAPIResult readFromURL(final String urlString) {
        Assertions.argumentNotNull(urlString, "urlString may not be null");

        if (!apexModel.getPolicyModel().getKey().equals(AxArtifactKey.getNullKey())) {
            return new ApexAPIResult(ApexAPIResult.RESULT.CONCEPT_EXISTS, "model " + apexModel.getPolicyModel().getKey().getID() + " already loaded");
        }

        URL apexModelURL;
        try {
            apexModelURL = new URL(urlString);
        }
        catch (MalformedURLException e) {
            ApexAPIResult result = new ApexAPIResult(ApexAPIResult.RESULT.FAILED);
            result.addMessage("URL string " + urlString + " is not a valid URL");
            result.addThrowable(e);
            return result;
        }

        try {
            ApexModelReader<AxPolicyModel> apexModelReader = new ApexModelReader<>(AxPolicyModel.class);
            apexModelReader.setValidateFlag(false);
            AxPolicyModel newPolicyModel = apexModelReader.read(apexModelURL.openStream());
            apexModel.setPolicyModel(newPolicyModel != null ? newPolicyModel : new AxPolicyModel());
            return new ApexAPIResult();
        }
        catch (ApexModelException | IOException e) {
            apexModel.setPolicyModel(new AxPolicyModel());
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Write an APEX model to a location identified by a URL.
     *
     * @param urlString the URL to read the model from
     * @param xmlFlag if true, save the file in XML format, otherwise save the file in the default JSON format
     * @return the result of the operation
     */
    public ApexAPIResult writeToURL(final String urlString, final boolean xmlFlag) {
        Assertions.argumentNotNull(urlString, "urlString may not be null");

        URL apexModelURL;
        try {
            apexModelURL = new URL(urlString);
        }
        catch (MalformedURLException e) {
            ApexAPIResult result = new ApexAPIResult(ApexAPIResult.RESULT.FAILED);
            result.addMessage("URL string " + urlString + " is not a valid URL");
            result.addThrowable(e);
            return result;
        }

        try {
            ApexModelWriter<AxPolicyModel> apexModelWriter = new ApexModelWriter<>(AxPolicyModel.class);
            apexModelWriter.setValidateFlag(false);
            apexModelWriter.setJsonOutput(!xmlFlag);

            // Open the URL for output and write the model
            URLConnection urlConnection = apexModelURL.openConnection();
            urlConnection.setDoOutput(true);

            apexModelWriter.write(apexModel.getPolicyModel(), urlConnection.getOutputStream());
            return new ApexAPIResult();
        }
        catch (ApexModelException | IOException e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Analyse an Apex model that shows the concept usage references of a policy model.
     *
     * @return the result of the operation
     */
    public ApexAPIResult analyse() {
        PolicyAnalysisResult analysisResult = new PolicyAnalyser().analyse(apexModel.getPolicyModel());
        return new ApexAPIResult(ApexAPIResult.RESULT.SUCCESS, analysisResult.toString());
    }

    /**
     * Validate an Apex model, checking all concepts and references in the model.
     *
     * @return the result of the operation
     */
    public ApexAPIResult validate() {
        ApexAPIResult result = new ApexAPIResult();
        try {
            AxValidationResult validationResult = apexModel.getPolicyModel().validate(new AxValidationResult());

            if (!validationResult.isValid()) {
                result.setResult(ApexAPIResult.RESULT.FAILED);
            }
            result.addMessage(new ApexModelStringWriter<AxArtifactKey>(false).writeString(apexModel.getPolicyModel().getKey(), AxArtifactKey.class, jsonMode));
            result.addMessage(validationResult.toString());
            return result;
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Compare to Apex models, returning the differences between the models.
     *
     * @param otherModelFileName the file name of the other model
     * @param diffsOnly only returns differences between the model when set
     * @param keysOnly only returns the keys that are different when set, when not set values are also returned
     * @return the result of the operation
     */
    public ApexAPIResult compare(final String otherModelFileName, final boolean diffsOnly, final boolean keysOnly) {
        ApexAPIResult result = new ApexAPIResult();
        try {
            AxPolicyModel otherPolicyModel = loadModelFromFile(otherModelFileName, result);
            if (!result.getResult().equals(ApexAPIResult.RESULT.SUCCESS)) {
                return result;
            }

            PolicyModelComparer policyModelComparer = new PolicyModelComparer(apexModel.getPolicyModel(), otherPolicyModel);
            result.addMessage(new ApexModelStringWriter<AxArtifactKey>(false).writeString(apexModel.getPolicyModel().getKey(), AxArtifactKey.class, jsonMode));
            result.addMessage(policyModelComparer.toString());

            return result;
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Compare two Apex models, returning the differences between the models.
     *
     * @param otherModelString the other model as a string
     * @param diffsOnly only returns differences between the model when set
     * @param keysOnly only returns the keys that are different when set, when not set values are also returned
     * @return the result of the operation
     */
    public ApexAPIResult compareWithString(final String otherModelString, final boolean diffsOnly, final boolean keysOnly) {
        ApexAPIResult result = new ApexAPIResult();
        try {
            AxPolicyModel otherPolicyModel = loadModelFromString(otherModelString, result);
            if (!result.getResult().equals(ApexAPIResult.RESULT.SUCCESS)) {
                return result;
            }

            PolicyModelComparer policyModelComparer = new PolicyModelComparer(apexModel.getPolicyModel(), otherPolicyModel);
            result.addMessage(new ApexModelStringWriter<AxArtifactKey>(false).writeString(apexModel.getPolicyModel().getKey(), AxArtifactKey.class, jsonMode));
            result.addMessage(policyModelComparer.toString());

            return result;
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Split out a sub model from an Apex model that contains a given subset of the policies in the original model.
     *
     * @param targetModelName the file name of the target model in which to store the model split out from the original model
     * @param splitOutPolicies the policies form the original model to include in the split out model, specified as a comma delimited list of policy names
     * @return the result of the operation
     */
    public ApexAPIResult split(final String targetModelName, final String splitOutPolicies) {
        Set<AxArtifactKey> requiredPolicySet = new LinkedHashSet<>();

        // Split the policy names on comma
        String[] policyNames = splitOutPolicies.split(",");

        // Iterate over the policy names
        for (String policyName : policyNames) {
            // Split out this specific policy
            AxPolicy requiredPolicy = apexModel.getPolicyModel().getPolicies().get(policyName);

            if (requiredPolicy != null) {
                requiredPolicySet.add(requiredPolicy.getKey());
            }
            else {
                return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, "policy for policy name " + policyName + " not found in model");
            }
        }

        try {
            AxPolicyModel splitPolicyModel = PolicyModelSplitter.getSubPolicyModel(apexModel.getPolicyModel(), requiredPolicySet, false);

            ApexModelFileWriter<AxPolicyModel> apexModelFileWriter = new ApexModelFileWriter<>(false);
            apexModelFileWriter.apexModelWriteJSONFile(splitPolicyModel, AxPolicyModel.class, targetModelName);
            return new ApexAPIResult();
        }
        catch (ApexException e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Split out a sub model from an Apex model that contains a given subset of the policies in the original model, return the split model in the result as a
     * string.
     *
     * @param splitOutPolicies the policies form the original model to include in the split out model, specified as a comma delimited list of policy names
     * @return the result of the operation
     */
    public ApexAPIResult split(final String splitOutPolicies) {
        ApexAPIResult splitResult = new ApexAPIResult();
        File tempSplitPolicyFile = null;
        try {
            tempSplitPolicyFile = File.createTempFile("ApexTempPolicy", null);

            // Split the policy into a temporary file
            splitResult = split(tempSplitPolicyFile.getCanonicalPath(), splitOutPolicies);
            if (splitResult.isNOK()) {
                return splitResult;
            }

            // Get the policy model into a string
            String splitPolicyModelString = TextFileUtils.getTextFileAsString(tempSplitPolicyFile.getCanonicalPath());

            // Return the policy model
            splitResult.addMessage(splitPolicyModelString);
            return splitResult;
        }
        catch (Exception e) {
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, "split of policy model " + apexModel.getPolicyModel().getID() + " failed", e);
        }
        finally {
            if (tempSplitPolicyFile != null) {
                tempSplitPolicyFile.delete();
            }
        }
    }

    /**
     * Merge two Apex models together.
     *
     * @param mergeInModelName the file name of the model to merge into the current model
     * @param keepOriginal if this flag is set to true, if a concept exists in both models, the original model copy of that concept is kept, if the flag is set
     *            to false, then the copy of the concept from the mergeInModel overwrites the concept in the original model
     * @return the result of the operation
     */
    public ApexAPIResult merge(final String mergeInModelName, final boolean keepOriginal) {
        ApexAPIResult result = new ApexAPIResult();
        AxPolicyModel mergeInPolicyModel = loadModelFromFile(mergeInModelName, result);
        if (!result.getResult().equals(ApexAPIResult.RESULT.SUCCESS)) {
            return result;
        }

        try {
            AxPolicyModel mergedPolicyModel = PolicyModelMerger.getMergedPolicyModel(apexModel.getPolicyModel(), mergeInPolicyModel, keepOriginal, false);
            apexModel.setPolicyModel(mergedPolicyModel != null ? mergedPolicyModel : new AxPolicyModel());
            return new ApexAPIResult();
        }
        catch (ApexModelException e) {
            apexModel.setPolicyModel(new AxPolicyModel());
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Merge two Apex models together.
     *
     * @param otherModelString the model to merge as a string
     * @param keepOriginal if this flag is set to true, if a concept exists in both models, the original model copy of that concept is kept, if the flag is set
     *            to false, then the copy of the concept from the mergeInModel overwrites the concept in the original model
     * @return the result of the operation
     */
    public ApexAPIResult mergeWithString(final String otherModelString, final boolean keepOriginal) {
        ApexAPIResult result = new ApexAPIResult();
        AxPolicyModel mergeInPolicyModel = loadModelFromString(otherModelString, result);
        if (!result.getResult().equals(ApexAPIResult.RESULT.SUCCESS)) {
            return result;
        }

        try {
            AxPolicyModel mergedPolicyModel = PolicyModelMerger.getMergedPolicyModel(apexModel.getPolicyModel(), mergeInPolicyModel, keepOriginal, false);
            apexModel.setPolicyModel(mergedPolicyModel != null ? mergedPolicyModel : new AxPolicyModel());
            return new ApexAPIResult();
        }
        catch (ApexModelException e) {
            apexModel.setPolicyModel(new AxPolicyModel());
            return new ApexAPIResult(ApexAPIResult.RESULT.FAILED, e);
        }
    }

    /**
     * Load a policy model from a file.
     *
     * @param fileName the name of the file containing the model
     * @param result the result of the operation
     * @return the model
     */
    private AxPolicyModel loadModelFromFile(final String fileName, final ApexAPIResult result) {
        Assertions.argumentNotNull(fileName, "fileName may not be null");

        AxPolicyModel readModel = null;

        final URL apexModelURL = ResourceUtils.getLocalFile(fileName);
        if (apexModelURL == null) {
            result.setResult(ApexAPIResult.RESULT.FAILED);
            result.addMessage("file " + fileName + " not found");
            return null;
        }

        try {
            ApexModelReader<AxPolicyModel> apexModelReader = new ApexModelReader<>(AxPolicyModel.class);
            apexModelReader.setValidateFlag(false);
            readModel = apexModelReader.read(apexModelURL.openStream());
            result.setResult(ApexAPIResult.RESULT.SUCCESS);
            return readModel;
        }
        catch (Exception e) {
            result.setResult(ApexAPIResult.RESULT.FAILED);
            result.addThrowable(e);
            return null;
        }
    }

    /**
     * Load a policy model from a string.
     *
     * @param modelString the string containing the model
     * @param result the result of the operation
     * @return the model
     */
    private AxPolicyModel loadModelFromString(final String modelString, final ApexAPIResult result) {
        Assertions.argumentNotNull(modelString, "modelString may not be null");

        AxPolicyModel readModel = null;

        InputStream modelStringStream = new ByteArrayInputStream(modelString.getBytes());

        try {
            ApexModelReader<AxPolicyModel> apexModelReader = new ApexModelReader<>(AxPolicyModel.class);
            apexModelReader.setValidateFlag(false);
            readModel = apexModelReader.read(modelStringStream);
            result.setResult(ApexAPIResult.RESULT.SUCCESS);
            return readModel;
        }
        catch (Exception e) {
            result.setResult(ApexAPIResult.RESULT.FAILED);
            result.addThrowable(e);
            return null;
        }
    }
}
