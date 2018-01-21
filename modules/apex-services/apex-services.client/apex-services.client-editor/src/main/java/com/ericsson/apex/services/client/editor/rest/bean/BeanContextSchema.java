/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.services.client.editor.rest.bean;

import javax.xml.bind.annotation.XmlType;

/**
 * The ContextSchema Bean.
 */
@XmlType
public class BeanContextSchema extends BeanBase {

    private String name = null, version = null, schemaFlavour = null, schemaDefinition = null, uuid = null, description = null;

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Gets the uuid.
     *
     * @return the uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the schema flavour.
     *
     * @return the schema flavour
     */
    public String getSchemaFlavour() {
        return schemaFlavour;
    }

    /**
     * Gets the schema definition.
     *
     * @return the schema definition
     */
    public String getSchemaDefinition() {
        return schemaDefinition;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ContextSchema [name=" + name + ", version=" + version + ", schemaFlavour=" + schemaFlavour + ", schemaDefinition=" + schemaDefinition
                + ", uuid=" + uuid + ", description=" + description + "]";
    }
}
