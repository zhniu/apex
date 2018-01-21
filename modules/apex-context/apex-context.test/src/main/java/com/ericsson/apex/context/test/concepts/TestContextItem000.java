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
 * The Class TestContextItem000.
 */
public class TestContextItem000 implements Serializable {
    private static final int HASH_PRIME_1 = 31;
    private static final int HASH_PRIME_2 = 1231;
    private static final int HASH_PRIME_3 = 1237;

    private static final long serialVersionUID = 7241008665286367796L;

    private boolean flag = false;;

    /**
     * The Constructor.
     */
    public TestContextItem000() {
    }

    /**
     * The Constructor.
     *
     * @param flag the flag
     */
    public TestContextItem000(final Boolean flag) {
        this.flag = flag;
    }

    /**
     * Gets the flag.
     *
     * @return the flag
     */
    public boolean getFlag() {
        return flag;
    }

    /**
     * Sets the flag.
     *
     * @param flag the flag
     */
    public void setFlag(final boolean flag) {
        this.flag = flag;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = HASH_PRIME_1;
        int result = 1;
        result = prime * result + (flag ? HASH_PRIME_2 : HASH_PRIME_3);
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
        TestContextItem000 other = (TestContextItem000) obj;
        if (flag != other.flag) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TestContextItem000 [flag=" + flag + "]";
    }
}
