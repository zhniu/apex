/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.infrastructure.json;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This class reads objects of the given class from an input stream.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 * @param <TYPE> the generic type
 */
public class JSONHandler<TYPE> {

    /**
     * This method reads objects of a given class from an input stream.
     *
     * @param inputClass The class to read
     * @param inputStream the input stream to read from
     * @return the object read
     */
    public TYPE read(final Class<TYPE> inputClass, final InputStream inputStream) {
        // Register the adapters for our carrier technologies and event protocols with GSON
        final GsonBuilder gsonBuilder = new GsonBuilder();

        final Gson gson = gsonBuilder.create();
        final Reader jsonResourceReader = new InputStreamReader(inputStream);
        return gson.fromJson(jsonResourceReader, inputClass);
    }
}
