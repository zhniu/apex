/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.utilities;

import java.util.Map.Entry;

/**
 * Convenience methods for handling Java properties class instances.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public abstract class PropertyUtils {
    /**
     * Private constructor used to prevent sub class instantiation.
     */
    private PropertyUtils() {
    }

    /**
     * Return all properties as a string.
     *
     * @return a string containing all the property values
     */
    public static String getAllProperties() {
        final StringBuilder builder = new StringBuilder();

        for (final Entry<Object, Object> property : System.getProperties().entrySet()) {
            builder.append(property.getKey().toString());
            builder.append('=');
            builder.append(property.getValue().toString());
            builder.append('\n');
        }

        return builder.toString();
    }

    /**
     * Checks if a property is set. If the property is set with no value or with a value of "true", this method returns true. It returns "false" if the property
     * is not set or is set to false
     *
     * @param propertyName The property to check
     * @return true if the property is set to true, false otherwise
     */
    public static boolean propertySetOrTrue(final String propertyName) {
        if (propertyName == null) {
            return false;
        }

        final String propertyValue = System.getProperty(propertyName);
        if (propertyValue == null) {
            return false;
        }

        if (propertyValue.trim().length() == 0) {
            return true;
        }

        return new Boolean(propertyValue);
    }
}
