/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.basicmodel.dao.converters;

import java.util.UUID;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * The Class UUIDConverter converts a UUID to and from database format.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
@Converter
public class UUID2String extends XmlAdapter<String, UUID> implements AttributeConverter<UUID, String> {

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
     */
    @Override
    public String convertToDatabaseColumn(final UUID uuid) {
        String returnString;
        if (uuid == null) {
            returnString = "";
        }
        else {
            returnString = uuid.toString();
        }
        return returnString;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.AttributeConverter#convertToEntityAttribute(java.lang.Object)
     */
    @Override
    public UUID convertToEntityAttribute(final String uuidString) {
        return UUID.fromString(uuidString);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.xml.bind.annotation.adapters.XmlAdapter
     */
    @Override
    public UUID unmarshal(final String v) throws Exception {
        return this.convertToEntityAttribute(v);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.xml.bind.annotation.adapters.XmlAdapter
     */
    @Override
    public String marshal(final UUID v) throws Exception {
        return this.convertToDatabaseColumn(v);
    }
}
