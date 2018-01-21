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
 * The Class TestContextItem005.
 */
public class TestContextItem005 implements Serializable {
    private static final long serialVersionUID = -2958758261076734821L;

    private static final int HASH_PRIME_1 = 31;
    private static final int FOUR_BYTES = 32;

    private double doubleValue = 0;

    /**
     * The Constructor.
     */
    public TestContextItem005() {
    }

    /**
     * The Constructor.
     *
     * @param doubleValue the double value
     */
    public TestContextItem005(final Double doubleValue) {
        this.doubleValue = doubleValue;
    }

    /**
     * Gets the double value.
     *
     * @return the double value
     */
    public double getDoubleValue() {
        return doubleValue;
    }

    /**
     * Sets the double value.
     *
     * @param doubleValue the double value
     */
    public void setDoubleValue(final double doubleValue) {
        this.doubleValue = doubleValue;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = HASH_PRIME_1;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(doubleValue);
        result = prime * result + (int) (temp ^ (temp >>> FOUR_BYTES));
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
        TestContextItem005 other = (TestContextItem005) obj;
        if (Double.doubleToLongBits(doubleValue) != Double.doubleToLongBits(other.doubleValue)) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TestContextItem005 [doubleValue=" + doubleValue + "]";
    }
}
