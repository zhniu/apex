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
 * The Class TestContextItem004.
 */
public class TestContextItem004 implements Serializable {
    private static final long serialVersionUID = -3359180576903272400L;

    private static final int HASH_PRIME_1 = 31;

    private float floatValue = 0;

    /**
     * The Constructor.
     */
    public TestContextItem004() {
    }

    /**
     * The Constructor.
     *
     * @param floatValue the float value
     */
    public TestContextItem004(final Float floatValue) {
        this.floatValue = floatValue;
    }

    /**
     * Gets the float value.
     *
     * @return the float value
     */
    public float getFloatValue() {
        return floatValue;
    }

    /**
     * Sets the float value.
     *
     * @param floatValue the float value
     */
    public void setFloatValue(final float floatValue) {
        this.floatValue = floatValue;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = HASH_PRIME_1;
        int result = 1;
        result = prime * result + Float.floatToIntBits(floatValue);
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
        TestContextItem004 other = (TestContextItem004) obj;
        if (Float.floatToIntBits(floatValue) != Float.floatToIntBits(other.floatValue)) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TestContextItem004 [floatValue=" + floatValue + "]";
    }
}
