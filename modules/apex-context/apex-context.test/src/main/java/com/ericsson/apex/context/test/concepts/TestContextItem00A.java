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
import java.util.Locale;
import java.util.TimeZone;

/**
 * The Class TestContextItem00A.
 */
public class TestContextItem00A implements Serializable {
    private static final long serialVersionUID = -6579903685538233754L;

    private static final int HASH_PRIME_1 = 31;
    private static final int HASH_PRIME_2 = 1231;
    private static final int HASH_PRIME_3 = 1237;

    private TestContextItem008 dateValue = new TestContextItem008(System.currentTimeMillis());
    private String timeZoneString = TimeZone.getTimeZone("Europe/Dublin").getDisplayName();
    private boolean dst = false;
    private int utcOffset = 0;
    private String localeLanguage = Locale.ENGLISH.getLanguage();
    private String localeCountry = Locale.ENGLISH.getCountry();

    /**
     * The Constructor.
     */
    public TestContextItem00A() {
    }

    /**
     * The Constructor.
     *
     * @param dateValue the date value
     * @param tzValue the tz value
     * @param dst the dst
     * @param utcOffset the utc offset
     * @param language the language
     * @param country the country
     */
    public TestContextItem00A(final TestContextItem008 dateValue, final String tzValue, final boolean dst, final int utcOffset, final String language,
            final String country) {
        this.dateValue = dateValue;
        this.timeZoneString = TimeZone.getTimeZone(tzValue).getDisplayName();
        this.dst = dst;
        this.utcOffset = utcOffset;

        Locale locale = new Locale(language, country);
        this.localeLanguage = locale.getLanguage();
        this.localeCountry = locale.getCountry();
    }

    /**
     * The Constructor.
     *
     * @param original the original
     */
    public TestContextItem00A(final TestContextItem00A original) {
        this.dateValue = original.dateValue;
        this.timeZoneString = TimeZone.getTimeZone(original.timeZoneString).getDisplayName();
        this.dst = original.dst;
        this.utcOffset = original.utcOffset;

        Locale locale = new Locale(original.localeLanguage, original.localeCountry);
        this.localeLanguage = locale.getLanguage();
        this.localeCountry = locale.getCountry();
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

    /**
     * Gets the UTC offset.
     *
     * @return the UTC offset
     */
    public int getUTCOffset() {
        return utcOffset;
    }

    /**
     * Sets the UTC offset.
     *
     * @param newUtcOffset the UTC offset
     */
    public void setUTCOffset(final int newUtcOffset) {
        this.utcOffset = newUtcOffset;
    }

    /**
     * Gets the locale.
     *
     * @return the locale
     */
    public Locale getLocale() {
        return new Locale(localeLanguage, localeCountry);
    }

    /**
     * Sets the locale.
     *
     * @param locale the locale
     */
    public void setLocale(final Locale locale) {
        this.localeLanguage = locale.getLanguage();
        this.localeCountry = locale.getCountry();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = HASH_PRIME_1;
        int result = 1;
        result = prime * result + ((dateValue == null) ? 0 : dateValue.hashCode());
        result = prime * result + (dst ? HASH_PRIME_2 : HASH_PRIME_3);
        result = prime * result + ((localeCountry == null) ? 0 : localeCountry.hashCode());
        result = prime * result + ((localeLanguage == null) ? 0 : localeLanguage.hashCode());
        result = prime * result + ((timeZoneString == null) ? 0 : timeZoneString.hashCode());
        result = prime * result + utcOffset;
        return result;
    }

    /*
     * (non-Javadoc)
     *
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
        TestContextItem00A other = (TestContextItem00A) obj;
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
        if (localeCountry == null) {
            if (other.localeCountry != null) {
                return false;
            }
        }
        else if (!localeCountry.equals(other.localeCountry)) {
            return false;
        }
        if (localeLanguage == null) {
            if (other.localeLanguage != null) {
                return false;
            }
        }
        else if (!localeLanguage.equals(other.localeLanguage)) {
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
        if (utcOffset != other.utcOffset) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TestContextItem00A [dateValue=" + dateValue + ", timeZoneString=" + timeZoneString + ", dst=" + dst + ", utcOffset=" + utcOffset
                + ", localeLanguage=" + localeLanguage + ", localeCountry=" + localeCountry + "]";
    }
}
