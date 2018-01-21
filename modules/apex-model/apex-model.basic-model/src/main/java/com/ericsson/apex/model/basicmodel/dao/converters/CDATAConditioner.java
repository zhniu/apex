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

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * The Class CDATAConditioner converts a CDATA String to and from database format by removing spaces at the ends of lines and
 * platform-specific new line endings.
 *
 * @author John Keeney (John.Keeney@ericsson.com)
 */
@Converter
public class CDATAConditioner extends XmlAdapter<String, String> implements AttributeConverter<String, String> {

    private static final String NL = "\n";

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
     */
    @Override
    public String convertToDatabaseColumn(final String raw) {
        return clean(raw);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.persistence.AttributeConverter#convertToEntityAttribute(java.lang.Object)
     */
    @Override
    public String convertToEntityAttribute(final String db) {
        return clean(db);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.xml.bind.annotation.adapters.XmlAdapter
     */
    @Override
    public String unmarshal(final String v) throws Exception {
        return this.convertToEntityAttribute(v);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.xml.bind.annotation.adapters.XmlAdapter
     */
    @Override
    public String marshal(final String v) throws Exception {
        return this.convertToDatabaseColumn(v);
    }

    /**
     * Clean.
     *
     * @param in the in
     * @return the string
     */
    public static final String clean(final String in) {
        if (in == null) {
            return null;
        }
        else {
            return in.replaceAll("\\s+$", "").replaceAll("\\r?\\n", NL);
        }
    }
}
