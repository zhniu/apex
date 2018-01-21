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
import java.util.TimeZone;

/**
 * The Class TestContextItem009.
 */
public class TestContextItem009 implements Serializable {
    private static final long serialVersionUID = 5604426823170331706L;

    private static final int HASH_PRIME_1 = 31;
    private static final int HASH_PRIME_2 = 1231;
    private static final int HASH_PRIME_3 = 1237;

    private TestContextItem008 dateValue = new TestContextItem008(System.currentTimeMillis());
    private String timeZoneString = TimeZone.getTimeZone("Europe/Dublin").getDisplayName();
    private boolean dst = false;

    /**
     * The Constructor.
     */
    public TestContextItem009() {
        dst = true;
    }

    /**
     * The Constructor.
     *
     * @param dateValue the date value
     * @param tzValue the tz value
     * @param dst the dst
     */
    public TestContextItem009(final TestContextItem008 dateValue, final String tzValue, final boolean dst) {
        this.dateValue = dateValue;
        this.timeZoneString = TimeZone.getTimeZone(tzValue).getDisplayName();
        this.dst = dst;
    }

    /**
     * The Constructor.
     *
     * @param original the original
     */
    public TestContextItem009(final TestContextItem009 original) {
        this.dateValue = original.dateValue;
        this.timeZoneString = original.timeZoneString;
        this.dst = original.dst;
    }

    /**
     * Gets the date value.
     *
     * @return the date value
     */
    public TestContextItem008 getDateValue() {
        return dateValue;
    }

    /**
     * Sets the date value.
     *
     * @param dateValue the date value
     */
    public void setDateValue(final TestContextItem008 dateValue) {
        this.dateValue = dateValue;
    }

    /**
     * Gets the TZ value.
     *
     * @return the TZ value
     */
    public String getTZValue() {
        return timeZoneString;
    }

    /**
     * Sets the TZ value.
     *
     * @param tzValue the TZ value
     */
    public void setTZValue(final String tzValue) {
        this.timeZoneString = TimeZone.getTimeZone(tzValue).getDisplayName();
    }

    /**
     * Gets the DST.
     *
     * @return the dst
     */
    public boolean getDST() {
        return dst;
    }

    /**
     * Sets the DST.
     *
     * @param newDst the dst
     */
    public void setDST(final boolean newDst) {
        this.dst = newDst;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = HASH_PRIME_1;
        int result = 1;
        result = prime * result + ((dateValue == null) ? 0 : dateValue.hashCode());
        result = prime * result + (dst ? HASH_PRIME_2 : HASH_PRIME_3);
        result = prime * result + ((timeZoneString == null) ? 0 : timeZoneString.hashCode());
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
        TestContextItem009 other = (TestContextItem009) obj;
        if (dateValue == null) {
            if (other.dateValue != null) {
                return false;
            }
        }
        else if (!dateValue.equals(other.dateValue)) {
            return false;
        }
        if (dst != other.dst) {
            return false;
        }
        if (timeZoneString == null) {
            if (other.timeZoneString != null) {
                return false;
            }
        }
        else if (!timeZoneString.equals(other.timeZoneString)) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TestContextItem009 [dateValue=" + dateValue + ", tzValue=" + timeZoneString + ", dst=" + dst + "]";
    }
}
