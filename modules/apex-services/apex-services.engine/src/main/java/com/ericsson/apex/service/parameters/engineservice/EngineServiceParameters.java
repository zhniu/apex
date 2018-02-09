/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.service.parameters.engineservice;

import java.io.File;
import java.net.URL;

import com.ericsson.apex.core.engine.EngineParameters;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.service.AbstractParameters;
import com.ericsson.apex.model.basicmodel.service.ParameterService;
import com.ericsson.apex.model.utilities.ResourceUtils;
import com.ericsson.apex.service.parameters.ApexParameterValidator;

/**
 * This class holds the parameters for an Apex Engine Service with multiple engine threads running multiple engines.
 *
 * <p>
 * The following parameters are defined:
 * <ol>
 * <li>name: The name of the Apex engine service, which can be set to any value that matches the regular expression
 * {@link com.ericsson.apex.model.basicmodel.concepts.AxKey#NAME_REGEXP}.
 * <li>version: The name of the Apex engine service, which can be set to any value that matches the regular expression
 * {@link com.ericsson.apex.model.basicmodel.concepts.AxKey#VERSION_REGEXP}.
 * <li>id: The ID of the Apex engine service, which can be set to any integer value by a user.
 * <li>instanceCount: The number of Apex engines to spawn in this engine service. Each engine executes in its own thread.
 * <li>deploymentPort: The port that the Apex Engine Service will open so that it can be managed using the EngDep protocol. The EngDep protocol allows the
 * engine service to be monitored, to start and stop engines in the engine service, and to update the policy model of the engine service.
 * <li>engineParameters: Parameters (a {@link EngineParameters} instance) that all of the engines in the engine service will use. All engine threads use the
 * same parameters and act as a pool of engines. Engine parameters specify the executors and context management for the engines.
 * <li>policyModelFileName: The full path to the policy model file name to deploy on the engine service.
 * </ol>
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class EngineServiceParameters extends AbstractParameters implements ApexParameterValidator {
    private static final int MAX_PORT = 65535;

    // @formatter:off
    /** The default name of the Apex engine service. */
    public static final String DEFAULT_NAME = "ApexEngineService";

    /** The default version of the Apex engine service. */
    public static final String DEFAULT_VERSION = "1.0.0";

    /** The default ID of the Apex engine service. */
    public static final int DEFAULT_ID = -1;

    /** The default instance count for the Apex engine service. */
    public static final int DEFAULT_INSTANCE_COUNT  = 1;

    /** The default EngDep deployment port of the Apex engine service. */
    public static final int DEFAULT_DEPLOYMENT_PORT = 34421;

    // Apex engine service parameters
    private String name                = DEFAULT_NAME;
    private String version             = DEFAULT_VERSION;
    private int    id                  = DEFAULT_ID;
    private int    instanceCount       = DEFAULT_INSTANCE_COUNT;
    private int    deploymentPort      = DEFAULT_DEPLOYMENT_PORT;
    private String policyModelFileName = null;
    // @formatter:on

    // Apex engine internal parameters
    private EngineParameters engineParameters = new EngineParameters();

    /**
     * Constructor to create an apex engine service parameters instance and register the instance with the parameter service.
     */
    public EngineServiceParameters() {
        super(EngineServiceParameters.class.getCanonicalName());
        ParameterService.registerParameters(EngineServiceParameters.class, this);
    }

    /**
     * Gets the key of the Apex engine service.
     *
     * @return the Apex engine service key
     */
    public AxArtifactKey getEngineKey() {
        return new AxArtifactKey(name, version);
    }

    /**
     * Sets the key of the Apex engine service.
     * 
     * @param key the the Apex engine service key
     */
    public void setEngineKey(final AxArtifactKey key) {
        this.setName(key.getName());
        this.setVersion(key.getVersion());
    }

    /**
     * Gets the name of the engine service.
     *
     * @return the name of the engine service
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the engine service.
     *
     * @param name the name of the engine service
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Gets the version of the engine service.
     *
     * @return the version of the engine service
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the version of the engine service.
     *
     * @param version the version of the engine service
     */
    public void setVersion(final String version) {
        this.version = version;
    }

    /**
     * Gets the id of the engine service.
     *
     * @return the id of the engine service
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the engine service.
     *
     * @param id the id of the engine service
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * Gets the instance count of the engine service.
     *
     * @return the instance count of the engine service
     */
    public int getInstanceCount() {
        return instanceCount;
    }

    /**
     * Sets the instance count of the engine service.
     *
     * @param instanceCount the instance count of the engine service
     */
    public void setInstanceCount(final int instanceCount) {
        this.instanceCount = instanceCount;
    }

    /**
     * Gets the deployment port of the engine service.
     *
     * @return the deployment port of the engine service
     */
    public int getDeploymentPort() {
        return deploymentPort;
    }

    /**
     * Sets the deployment port of the engine service.
     *
     * @param deploymentPort the deployment port of the engine service
     */
    public void setDeploymentPort(final int deploymentPort) {
        this.deploymentPort = deploymentPort;
    }

    /**
     * Gets the file name of the policy engine for deployment on the engine service.
     *
     * @return the file name of the policy engine for deployment on the engine service
     */
    public String getPolicyModelFileName() {
        return ResourceUtils.getFilePath4Resource(policyModelFileName);
    }

    /**
     * Sets the file name of the policy engine for deployment on the engine service.
     *
     * @param policyModelFileName the file name of the policy engine for deployment on the engine service
     */
    public void setPolicyModelFileName(final String policyModelFileName) {
        this.policyModelFileName = policyModelFileName;
    }

    /**
     * Gets the engine parameters for engines in the engine service.
     *
     * @return the engine parameters for engines in the engine service
     */
    public EngineParameters getEngineParameters() {
        return engineParameters;
    }

    /**
     * Sets the engine parameters for engines in the engine service.
     *
     * @param engineParameters the engine parameters for engines in the engine service
     */
    public void setEngineParameters(final EngineParameters engineParameters) {
        this.engineParameters = engineParameters;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.apps.uservice.parameters.ApexParameterValidator#validate()
     */
    @Override
    public String validate() {
        StringBuilder errorMessageBuilder = new StringBuilder();

        try {
            new AxArtifactKey(name, version);
        }
        catch (Exception e) {
            errorMessageBuilder.append("  name [" + name + "] and/or version [" + version + "] invalid\n");
            errorMessageBuilder.append("   " + e.getMessage() + "\n");
        }

        if (id < 0) {
            errorMessageBuilder.append("  id not specified or specified value [" + id + "] invalid, must be specified as id >= 0\n");
        }

        if (instanceCount < 1) {
            errorMessageBuilder.append("  instanceCount [" + instanceCount + "] invalid, must be specified as instanceCount >= 1\n");
        }

        if (deploymentPort < 1 || deploymentPort > MAX_PORT) {
            errorMessageBuilder.append("  deploymentPort [" + deploymentPort + "] invalid, must be specified as 1024 <= port <= 65535\n");
        }

        if (policyModelFileName != null) {
            if (policyModelFileName.trim().length() == 0) {
                errorMessageBuilder.append("  policyModelFileName [" + policyModelFileName + "] invalid, must be specified as a non-empty string\n");
            }
            else {
                // The file name can refer to a resource on the local file system or on the class path
                URL fileURL = ResourceUtils.getURL4Resource(policyModelFileName);
                if (fileURL == null) {
                    errorMessageBuilder.append("  policyModelFileName [" + policyModelFileName + "] not found or is not a plain file\n");
                }
                else {
                    final File policyModelFile = new File(fileURL.getPath());
                    if (!policyModelFile.isFile()) {
                        errorMessageBuilder.append("  policyModelFileName [" + policyModelFileName + "] not found or is not a plain file\n");
                    }
                }
            }
        }

        return errorMessageBuilder.toString();
    }
}