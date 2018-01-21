/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.basicmodel.dao;

import java.util.Properties;

/**
 * This class is a POJO that holds properties for Apex DAOs.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class DAOParameters {
    /** The default Apex DAO plugin class. */
    public static final String DEFAULT_PLUGIN_CLASS = "com.ericsson.apex.model.basicmodel.dao.impl.DefaultApexDao";

    private String pluginClass = DEFAULT_PLUGIN_CLASS;
    private String persistenceUnit;

    private Properties jdbcProperties = new Properties();

    /**
     * Gets the DAO plugin class, this is the DAO class to use and it must implement the {@link ApexDao} interface.
     *
     * @return the DAO plugin class
     */
    public String getPluginClass() {
        return pluginClass;
    }

    /**
     * Sets the DAO plugin class, a class that implements the {@link ApexDao} interface.
     *
     * @param daoPluginClass the DAO plugin class
     */
    public void setPluginClass(final String daoPluginClass) {
        pluginClass = daoPluginClass;
    }

    /**
     * Gets the persistence unit for the DAO. The persistence unit defines the JDBC properties the DAO will use. The persistence unit must defined in the
     * {@code META-INF/persistence.xml} resource file
     *
     * @return the persistence unit to use for JDBC access
     */
    public String getPersistenceUnit() {
        return persistenceUnit;
    }

    /**
     * Sets the persistence unit for the DAO. The persistence unit defines the JDBC properties the DAO will use. The persistence unit must defined in the
     * {@code META-INF/persistence.xml} resource file
     *
     * @param daoPersistenceUnit the persistence unit to use for JDBC access
     */
    public void setPersistenceUnit(final String daoPersistenceUnit) {
        persistenceUnit = daoPersistenceUnit;
    }

    /**
     * Gets the JDBC properties.
     *
     * @return the JDBC properties
     */
    public Properties getJdbcProperties() {
        return jdbcProperties;
    }

    /**
     * Sets the JDBC properties.
     *
     * @param jdbcProperties the JDBC properties
     */
    public void setJdbcProperties(final Properties jdbcProperties) {
        this.jdbcProperties = jdbcProperties;
    }

    /**
     * Gets a single JDBC property.
     *
     * @param key the key of the property
     * @return the JDBC property
     */
    public String getJdbcProperty(final String key) {
        return jdbcProperties.getProperty(key);
    }

    /**
     * Sets a single JDBC property.
     *
     * @param key the key of the property
     * @param value the value of the JDBC property
     */
    public void setJdbcProperty(final String key, final String value) {
        jdbcProperties.setProperty(key, value);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "DAOParameters [pluginClass=" + pluginClass + ", persistenceUnit=" + persistenceUnit + ", jdbcProperties=" + jdbcProperties + "]";
    }
}
