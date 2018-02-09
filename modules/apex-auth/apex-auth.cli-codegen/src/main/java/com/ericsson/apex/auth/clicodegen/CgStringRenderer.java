/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.auth.clicodegen;

import java.util.Locale;

import org.stringtemplate.v4.AttributeRenderer;
import org.stringtemplate.v4.StringRenderer;

/**
 * String object renderer for the code generator.
 *
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 * @author John Keeney (John.Keeney@ericsson.com)
 */
public class CgStringRenderer implements AttributeRenderer {

    /* (non-Javadoc)
     * @see org.stringtemplate.v4.AttributeRenderer#toString(java.lang.Object, java.lang.String, java.util.Locale)
     */
    @Override
    public String toString(final Object o, final String format, final Locale locale) {
        if ("doQuotes".equals(format)) {
            if (o == null) {
                return null;
            }
            String ret = o.toString();
            if (ret.length() == 0) {
                return "\"\"";
            }
            if (!ret.startsWith("\"")) {
                ret = "\"" + ret + "\"";
            }
            return ret;
        }

        if ("doDescription".equals(format)) {
            String ret = o.toString();
            if (ret.contains("\n") || ret.contains("\"")) {
                ret = "LS" + "\n" + ret + "\n" + "LE";
            }
            else {
                ret = this.toString(o, "doQuotes", locale);
            }
            return ret;
        }

        // return the default string renderer if we don't know otherwise
        return new StringRenderer().toString(o, format, locale);
    }
}
