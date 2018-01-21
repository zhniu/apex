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
 * The ContextAlbum Bean.
 */
@XmlType
public class BeanContextAlbum extends BeanBase {

    private String name = null, version = null, scope = null, uuid = null, description = null;
    private BeanKeyRef itemSchema = null;
    private boolean writeable;

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
     * Gets the scope.
     *
     * @return the scope
     */
    public String getScope() {
        return scope;
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
     * Gets the item schema.
     *
     * @return the item schema
     */
    public BeanKeyRef getItemSchema() {
        return itemSchema;
    }

    /**
     * Gets the writeable.
     *
     * @return the writeable
     */
    public boolean getWriteable() {
        return writeable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ContextAlbum [name=" + name + ", version=" + version + ", scope=" + scope + ", uuid=" + uuid + ", description=" + description + ", itemSchema="
                + itemSchema + ", writeable=" + writeable + "]";
    }

}
