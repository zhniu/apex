/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.services.client.editor.rest.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * The base class for Beans.
 */
public abstract class BeanBase {

    /**
     * Gets a named field from the bean.
     *
     * @param field the field name
     * @return the value for the field
     */
    public String get(final String field) {
        //CHECKSTYLE:OFF: MagicNumber
        // use getter preferably
        for (final Method method : this.getClass().getMethods()) {
            if ((method.getName().startsWith("get")) && (method.getName().length() == (field.length() + 3))) {
                if (method.getName().toLowerCase().endsWith(field.toLowerCase())) {
                    try {
                        return (String) method.invoke(this);
                    }
                    catch (final Exception e) {
                        throw new IllegalArgumentException("Problem retrieving field called ('" + field + "') from JSON bean " + this, e);
                    }
                }
            }
        }
        // Use field approach
        if (field != null) {
            try {
                final Field f = this.getClass().getDeclaredField(field);
                if (f != null) {
                    f.setAccessible(true);
                    return (String) (f.get(this));
                }
            }
            catch (final Exception e) {
                throw new IllegalArgumentException("Problem retrieving field called ('" + field + "') from JSON bean " + this, e);
            }
        }
        throw new IllegalArgumentException("Problem retrieving field called ('" + field + "') from JSON bean " + this);
    }
}
