/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.basicmodel.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;

/**
 * This class manages marshaling and unmarshaling of Apex {@link AxReferenceKey} concepts using JAXB. The local name in reference keys must have specific
 * handling.
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(namespace = "http://www.ericsson.com/apex")
public class AxReferenceKeyAdapter extends XmlAdapter<String, AxReferenceKey> implements Serializable {

    private static final long serialVersionUID = -3480405083900107029L;

    /*
     * (non-Javadoc)
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
     */
    @Override
    public final String marshal(final AxReferenceKey key) throws Exception {
        return key.getLocalName();
    }

    /*
     * (non-Javadoc)
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
     */
    @Override
    public final AxReferenceKey unmarshal(final String key) throws Exception {
        final AxReferenceKey axReferenceKey = new AxReferenceKey();
        axReferenceKey.setLocalName(key);
        return axReferenceKey;
    }
}
