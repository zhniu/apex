/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.context.test.concepts;

import java.io.Serializable;

/**
 * The Class TestContextItem006.
 */
public class TestContextItem006 implements Serializable {
    private static final long serialVersionUID = -1074772190611125121L;

    private static final int HASH_PRIME_1 = 31;

    private String stringValue = "";

    /**
     * The Constructor.
     */
    public TestContextItem006() {
    }

    /**
     * The Constructor.
     *
     * @param stringValue the string value
     */
    public TestContextItem006(final String stringValue) {
        this.stringValue = stringValue;
    }

    /**
     * Gets the string value.
     *
     * @return the string value
     */
    public String getStringValue() {
        return stringValue;
    }

    /**
     * Sets the string value.
     *
     * @param stringValue the string value
     */
    public void setStringValue(final String stringValue) {
        this.stringValue = stringValue;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = HASH_PRIME_1;
        int result = 1;
        result = prime * result + ((stringValue == null) ? 0 : stringValue.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TestContextItem006 other = (TestContextItem006) obj;
        if (stringValue == null) {
            if (other.stringValue != null) {
                return false;
            }
        }
        else if (!stringValue.equals(other.stringValue)) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TestContextItem006 [stringValue=" + stringValue + "]";
    }
}
