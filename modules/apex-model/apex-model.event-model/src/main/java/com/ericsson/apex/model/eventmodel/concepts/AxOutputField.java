/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.eventmodel.concepts;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;

/**
 * This class specializes the {@link AxField} class for use as output fields on events.
 */
@Entity
@Table(name = "AxOutputField")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "apexOutputField", namespace = "http://www.ericsson.com/apex")
@XmlType(name = "AxOutputField", namespace = "http://www.ericsson.com/apex")

public class AxOutputField extends AxField {
    private static final long serialVersionUID = 2090324845463750391L;

    /**
     * The default constructor creates a field with a null artifact and schema key.
     */
    public AxOutputField() {
        super();
    }

    /**
     * The default constructor creates a field with the given artifact key and a null schema key.
     *
     * @param key the field key
     */
    public AxOutputField(final AxReferenceKey key) {
        super(key);
    }

    /**
     * Constructor to create the field with both its keys defined.
     *
     * @param key the field key
     * @param fieldSchemaKey the key of the field schema to use for this field
     */
    public AxOutputField(final AxReferenceKey key, final AxArtifactKey fieldSchemaKey) {
        super(key, fieldSchemaKey);
    }

    /**
     * Constructor to create the field with both its keys defined and optional flag specified.
     *
     * @param key the field key
     * @param fieldSchemaKey the key of the field schema to use for this field
     * @param optional true if the task field is optional, false otherwise
     */
    public AxOutputField(final AxReferenceKey key, final AxArtifactKey fieldSchemaKey, final boolean optional) {
        super(key, fieldSchemaKey, optional);
    }

    /**
     * Constructor to create the field with the local name of its reference key defined and its schema key defined.
     *
     * @param localName the local name of the field reference key
     * @param fieldSchemaKey the key of the field schema to use for this field
     */
    public AxOutputField(final String localName, final AxArtifactKey fieldSchemaKey) {
        super(localName, fieldSchemaKey);
    }

    /**
     * Copy constructor, create an output field as a copy of another output field.
     *
     * @param field the output field to copy from
     */
    public AxOutputField(final AxOutputField field) {
        super(new AxReferenceKey(field.getKey()), new AxArtifactKey(field.getSchema()));
    }
}
