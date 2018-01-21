/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.modelapi;

import java.util.Properties;

import com.ericsson.apex.model.modelapi.impl.ApexModelImpl;

/**
 * A factory for creating ApexModel objects using the Apex Model implementation.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class ApexModelFactory {

    /**
     * Creates a new ApexModel object from its implementation.
     *
     * @param apexProperties default values and other configuration information for the apex model
     * @param jsonMode set to true to return JSON strings in list and delete operations, otherwise set to false
     * @return the apex model
     */
    public ApexModel createApexModel(final Properties apexProperties, final boolean jsonMode) {
        return new ApexModelImpl(setDefaultPropertyValues(apexProperties), jsonMode);
    }

    /**
     * Sets default property values for Apex properties that must be set for the Apex model implementation if those properties are not already set.
     *
     * @param apexPropertiesIn the default property values
     * @return the properties
     */
    private Properties setDefaultPropertyValues(final Properties apexPropertiesIn) {
        Properties apexProperties = apexPropertiesIn;

        if (apexProperties == null) {
            apexProperties = new Properties();
        }

        if (apexProperties.getProperty("DEFAULT_CONCEPT_VERSION") == null) {
            apexProperties.setProperty("DEFAULT_CONCEPT_VERSION", "0.0.1");
        }
        if (apexProperties.getProperty("DEFAULT_EVENT_NAMESPACE") == null) {
            apexProperties.setProperty("DEFAULT_EVENT_NAMESPACE", "com.ericsson.apex");
        }
        if (apexProperties.getProperty("DEFAULT_EVENT_SOURCE") == null) {
            apexProperties.setProperty("DEFAULT_EVENT_SOURCE", "source");
        }
        if (apexProperties.getProperty("DEFAULT_EVENT_TARGET") == null) {
            apexProperties.setProperty("DEFAULT_EVENT_TARGET", "target");
        }
        if (apexProperties.getProperty("DEFAULT_POLICY_TEMPLATE") == null) {
            apexProperties.setProperty("DEFAULT_POLICY_TEMPLATE", "FREEFORM");
        }

        return apexProperties;
    }
}
