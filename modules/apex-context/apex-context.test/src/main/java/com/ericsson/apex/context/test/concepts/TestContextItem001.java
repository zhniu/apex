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
 * The Class TestContextItem001.
 */
public class TestContextItem001 implements Serializable {
    private static final long serialVersionUID = 1361938145823720386L;

    private static final int HASH_PRIME_1 = 31;

    private byte byteValue = 0;

    /**
     * The Constructor.
     */
    public TestContextItem001() {
    }

    /**
     * The Constructor.
     *
     * @param byteValue the byte value
     */
    public TestContextItem001(final Byte byteValue) {
        this.byteValue = byteValue;
    }

    /**
     * Gets the byte value.
     *
     * @return the byte value
     */
    public byte getByteValue() {
        return byteValue;
    }

    /**
     * Sets the byte value.
     *
     * @param byteValue the byte value
     */
    public void setByteValue(final byte byteValue) {
        this.byteValue = byteValue;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = HASH_PRIME_1;
        int result = 1;
        result = prime * result + byteValue;
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
        TestContextItem001 other = (TestContextItem001) obj;
        if (byteValue != other.byteValue) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TestContextItem001 [byteValue=" + byteValue + "]";
    }
}
