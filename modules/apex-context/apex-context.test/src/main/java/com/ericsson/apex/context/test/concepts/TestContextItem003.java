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
 * The Class TestContextItem003.
 */
public class TestContextItem003 implements Serializable {
    private static final long serialVersionUID = 3599267534512489386L;

    private static final int HASH_PRIME_1 = 31;
    private static final int FOUR_BYTES = 33;

    private long longValue = 0;

    /**
     * The Constructor.
     */
    public TestContextItem003() {
    }

    /**
     * The Constructor.
     *
     * @param longValue the long value
     */
    public TestContextItem003(final Long longValue) {
        this.longValue = longValue;
    }

    /**
     * Gets the long value.
     *
     * @return the long value
     */
    public long getLongValue() {
        return longValue;
    }

    /**
     * Sets the long value.
     *
     * @param longValue the long value
     */
    public void setLongValue(final long longValue) {
        this.longValue = longValue;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = HASH_PRIME_1;
        int result = 1;
        result = prime * result + (int) (longValue ^ (longValue >>> FOUR_BYTES));
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
        TestContextItem003 other = (TestContextItem003) obj;
        if (longValue != other.longValue) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TestContextItem003 [longValue=" + longValue + "]";
    }
}
