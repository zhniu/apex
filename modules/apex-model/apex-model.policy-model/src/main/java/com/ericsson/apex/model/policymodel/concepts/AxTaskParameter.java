/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.policymodel.concepts;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.ericsson.apex.model.basicmodel.concepts.AxConcept;
import com.ericsson.apex.model.basicmodel.concepts.AxKey;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationMessage;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult.ValidationResult;
import com.ericsson.apex.model.utilities.Assertions;

/**
 * This class is used to specify the configuration parameters that may be passed to a task {@link AxTask}. Task parameters are read from a configuration file
 * when Apex starts and are passed to the task by the Apex engine when a task is executed. Each task parameter has a key and a default value. If the task
 * parameter is not set in a configuration file, the task uses its default value.
 */

@Entity
@Table(name = "AxTaskParameter")

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "apexTaskParameter", namespace = "http://www.ericsson.com/apex")
@XmlType(name = "AxTaskParameter", namespace = "http://www.ericsson.com/apex", propOrder = { "key", "defaultValue" })

public class AxTaskParameter extends AxConcept {
    private static final long serialVersionUID = 7351688156934099977L;

    @EmbeddedId
    @XmlElement(name = "key", required = true)
    private AxReferenceKey key;

    @Column(name = "defaultValue")
    @XmlElement
    private String defaultValue;

    /**
     * The Default Constructor creates a task parameter with a null reference key and a null default value.
     */
    public AxTaskParameter() {
        this(new AxReferenceKey());
    }

    /**
     * Copy constructor
     * @param copyConcept the concept to copy from
     */
    public AxTaskParameter(final AxTaskParameter copyConcept) {
    		super(copyConcept);
    }

    /**
     * The Keyed Constructor creates a task parameter with the given reference key and a null default value.
     *
     * @param taskParameterKey the task parameter key
     */
    public AxTaskParameter(final AxReferenceKey taskParameterKey) {
        this(taskParameterKey, "");
    }

    /**
     * The Default Constructor creates a task parameter with the given reference key and default value.
     *
     * @param key the reference key of the task parameter
     * @param defaultValue the default value of the task parameter
     */
    public AxTaskParameter(final AxReferenceKey key, final String defaultValue) {
        super();
        Assertions.argumentNotNull(key, "key may not be null");
        Assertions.argumentNotNull(defaultValue, "defaultValue may not be null");

        this.key = key;
        this.defaultValue = defaultValue.trim();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxConcept#getKey()
     */
    @Override
    public AxReferenceKey getKey() {
        return key;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxConcept#getKeys()
     */
    @Override
    public List<AxKey> getKeys() {
        return key.getKeys();
    }

    /**
     * Sets the reference key of the task parameter.
     *
     * @param key the reference key of the task parameter
     */
    public void setKey(final AxReferenceKey key) {
        Assertions.argumentNotNull(key, "key may not be null");
        this.key = key;
    }

    /**
     * Gets the default value of the task parameter.
     *
     * @return the default value of the task parameter
     */
    public String getTaskParameterValue() {
        return defaultValue;
    }

    /**
     * Sets the default value of the task parameter.
     *
     * @param defaultValue the default value of the task parameter
     */
    public void setDefaultValue(final String defaultValue) {
        Assertions.argumentNotNull(defaultValue, "defaultValue may not be null");
        this.defaultValue = defaultValue.trim();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxConcept#validate(com.ericsson.apex.model.basicmodel.concepts.AxValidationResult)
     */
    @Override
    public AxValidationResult validate(final AxValidationResult resultIn) {
        AxValidationResult result = resultIn;

        if (key.equals(AxReferenceKey.getNullKey())) {
            result.addValidationMessage(new AxValidationMessage(key, this.getClass(), ValidationResult.INVALID, "key is a null key"));
        }

        result = key.validate(result);

        if (defaultValue.trim().length() == 0) {
            result.addValidationMessage(
                    new AxValidationMessage(key, this.getClass(), ValidationResult.WARNING, "no defaultValue specified, defaultValue is blank"));
        }

        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxConcept#clean()
     */
    @Override
    public void clean() {
        key.clean();
        defaultValue = defaultValue.trim();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxConcept#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName());
        builder.append(":(");
        builder.append("key=");
        builder.append(key);
        builder.append(",defaultValue=");
        builder.append(defaultValue);
        builder.append(")");
        return builder.toString();
    }

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ericsson.apex.model.basicmodel.concepts.AxConcept#copyTo(com.ericsson.apex.model.basicmodel.concepts.AxConcept)
	 */
	@Override
	public AxConcept copyTo(final AxConcept targetObject) {
        Assertions.argumentNotNull(targetObject, "target may not be null");

        final Object copyObject = targetObject;
        Assertions.instanceOf(copyObject, AxTaskParameter.class);

        final AxTaskParameter copy = ((AxTaskParameter) copyObject);
        copy.setKey(new AxReferenceKey(key));
        copy.setDefaultValue(defaultValue);

        return copy;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxConcept#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + key.hashCode();
        result = prime * result + defaultValue.hashCode();
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ericsson.apex.model.basicmodel.concepts.AxConcept#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final AxTaskParameter other = (AxTaskParameter) obj;
        if (!key.equals(other.key)) {
            return false;
        }
        return defaultValue.equals(other.defaultValue);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final AxConcept otherObj) {
        if (otherObj == null) {
            return -1;
        }
        if (this == otherObj) {
            return 0;
        }
        if (getClass() != otherObj.getClass()) {
            return this.hashCode() - otherObj.hashCode();
        }

        final AxTaskParameter other = (AxTaskParameter) otherObj;
        if (!key.equals(other.key)) {
            return key.compareTo(other.key);
        }
        return defaultValue.compareTo(other.defaultValue);
    }
}
