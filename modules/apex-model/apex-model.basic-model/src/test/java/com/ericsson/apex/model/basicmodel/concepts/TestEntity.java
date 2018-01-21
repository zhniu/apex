/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.basicmodel.concepts;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.ericsson.apex.model.basicmodel.concepts.AxConcept;
import com.ericsson.apex.model.basicmodel.concepts.AxKey;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.basicmodel.xml.AxReferenceKeyAdapter;

@Entity
@Table(name = "TestEntity")

public class TestEntity extends AxConcept {
    private static final long serialVersionUID = -2962570563281067894L;

    @EmbeddedId()
    @XmlElement(name = "key", required = true)
    @XmlJavaTypeAdapter(AxReferenceKeyAdapter.class)
    protected AxReferenceKey key;

    private double doubleValue;
   
    public TestEntity() {
        this.key = new AxReferenceKey();
        this.doubleValue = 0;
    }
   
    public TestEntity(Double doubleValue) {
        this.key = new AxReferenceKey();
        this.doubleValue = doubleValue;
    }
   
    public TestEntity(AxReferenceKey key, Double doubleValue) {
        this.key = key;
        this.doubleValue = doubleValue;
    }
   
    public AxReferenceKey getKey() {
        return key;
    }

    public List<AxKey> getKeys() {
        return Arrays.asList((AxKey) getKey());
    }

    public void setKey(AxReferenceKey key) {
        this.key = key;
    }

    public boolean checkSetKey() {
        return (this.key != null);
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    @Override
    public AxValidationResult validate(AxValidationResult result) {
        return key.validate(result);
    }

    @Override
    public void clean() {
        key.clean();
    }
   
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("doubleValue=");
        builder.append(doubleValue);
        return builder.toString();
    }

    @Override
    public Object clone() {
        return copyTo(new TestEntity());
    }

    @Override
    public Object copyTo(Object target) {
        final Object copyObject = ((target == null) ? new TestEntity(): target);
        if (copyObject instanceof TestEntity) {
            final TestEntity copy = ((TestEntity) copyObject);
            if (this.checkSetKey()) {
                copy.setKey((AxReferenceKey)key.clone());
            }
            else {
                copy.key = null;
            }
            copy.doubleValue = doubleValue;
        }
        return copyObject;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (getClass() != obj.getClass())
            return false;
        TestEntity other = (TestEntity) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        }
        else
            if (!key.equals(other.key))
                return false;
        if (doubleValue != other.doubleValue)
                return false;
        return true;
    }
   
    @Override
    public int compareTo(AxConcept otherObj) {
        if (otherObj == null)
            return -1;
        if (this == otherObj)
            return 0;
        TestEntity other = (TestEntity) otherObj;
        if (key == null) {
            if (other.key != null)
                return 1;
        }
        else
            if (!key.equals(other.key))
                return key.compareTo(other.key);
        if (doubleValue != other.doubleValue)
            return new Double(doubleValue).compareTo(other.doubleValue);
       
        return 0;
    }
}
