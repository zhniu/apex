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
 * The Class TestContextItem002.
 */
public class TestContextItem002 implements Serializable {
    private static final long serialVersionUID = -8978435658277900984L;

    private static final int HASH_PRIME_1 = 31;

    private int intValue = 0;

    /**
     * The Constructor.
     */
    public TestContextItem002() {
    }

    /**
     * The Constructor.
     *
     * @param intValue the int value
     */
    public TestContextItem002(final Integer intValue) {
        this.intValue = intValue;
    }

    /**
     * The Constructor.
     *
     * @param original the original
     */
    public TestContextItem002(final TestContextItem002 original) {
        this.intValue = original.intValue;
    }

    /**
     * Gets the int value.
     *
     * @return the int value
     */
    public int getIntValue() {
        return intValue;
    }

    /**
     * Sets the int value.
     *
     * @param intValue the int value
     */
    public void setIntValue(final int intValue) {
        this.intValue = intValue;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = HASH_PRIME_1;
        int result = 1;
        result = prime * result + intValue;
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
        TestContextItem002 other = (TestContextItem002) obj;
        if (intValue != other.intValue) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TestContextItem002 [intValue=" + intValue + "]";
    }
}
