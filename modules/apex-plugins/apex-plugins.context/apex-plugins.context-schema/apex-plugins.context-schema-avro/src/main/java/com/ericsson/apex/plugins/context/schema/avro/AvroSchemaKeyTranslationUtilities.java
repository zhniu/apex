/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.context.schema.avro;

import java.util.Map.Entry;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * This static final class contains utility methods for Avro schemas.
 * 
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public final class AvroSchemaKeyTranslationUtilities {
    // Constants for key replacements
    private static final String DOT_STRING               = "\\.";
    private static final String DOT_STRING_REPLACEMENT   = "_DoT_";
    private static final String DASH_STRING              = "-";
    private static final String DASH_STRING_REPLACEMENT  = "_DasH_";

    /**
     * Default constructor to avoid subclassing.
     */
    private AvroSchemaKeyTranslationUtilities() {
    }

    /**
     * Translate characters in JSON keys to values that are legal in Avro.
     * Avro names must start with [A-Za-z_] and subsequently contain only [A-Za-z0-9_]
     *
     * @param jsonString The JSON string to translate
     * @param revert True if we want to revert the field names to their original values
     * @return the translated JSON string
     */
    public static String translateIllegalKeys(final String jsonString, final boolean revert) {
        if (jsonString == null) {
            return jsonString;
        }

        // Create a JSON element for the incoming JSON string
        JsonElement jsonElement = new GsonBuilder().serializeNulls().create().fromJson(jsonString,  JsonElement.class);

        JsonElement translatedJsonElement = translateIllegalKeys(jsonElement, revert);

        return new GsonBuilder().serializeNulls().create().toJson(translatedJsonElement);
    }

    /**
     * Translate characters in JSON keys to values that are legal in Avro.
     * Avro names must start with [A-Za-z_] and subsequently contain only [A-Za-z0-9_]
     *
     * @param jsonElement The JSON element to translate
     * @param revert True if we want to revert the field names to their original values
     * @return the translated JSON element
     */
    public static JsonElement translateIllegalKeys(final JsonElement jsonElement, final boolean revert) {
        // We only act on JSON objects and arrays
        if (jsonElement.isJsonObject()) {
            return translateIllegalKeys(jsonElement.getAsJsonObject(), revert);
        }
        else if (jsonElement.isJsonArray()) {
            return translateIllegalKeys(jsonElement.getAsJsonArray(), revert);
        }
        else {
            return jsonElement;
        }
    }

    /**
     * Translate characters in JSON keys to values that are legal in Avro.
     * Avro names must start with [A-Za-z_] and subsequently contain only [A-Za-z0-9_]
     *
     * @param jsonObject The JSON object to translate
     * @param revert True if we want to revert the field names to their original values
     * @return the translated JSON element
     */
    public static JsonElement translateIllegalKeys(final JsonObject jsonObject, final boolean revert) {
        JsonObject newJsonObject = new JsonObject();

        for (Entry<String, JsonElement> jsonObjectEntry: jsonObject.entrySet()) {
            newJsonObject.add(translateIllegalKey(jsonObjectEntry.getKey(), revert), translateIllegalKeys(jsonObjectEntry.getValue(), revert));
        }

        return newJsonObject;
    }

    /**
     * Translate characters in JSON keys to values that are legal in Avro.
     * Avro names must start with [A-Za-z_] and subsequently contain only [A-Za-z0-9_]
     *
     * @param jsonArray The JSON array to translate
     * @param revert True if we want to revert the field names to their original values
     * @return the translated JSON element
     */
    public static JsonElement translateIllegalKeys(final JsonArray jsonArray, final boolean revert) {
        JsonArray newJsonArray = new JsonArray();

        for (int i = 0; i < jsonArray.size(); i++) {
            newJsonArray.add(translateIllegalKeys(jsonArray.get(i), revert));
        }

        return newJsonArray;
    }

    /**
     * Translate characters in a single JSON key to values that are legal in Avro.
     * Avro names must start with [A-Za-z_] and subsequently contain only [A-Za-z0-9_]
     *
     * @param key The key to translate
     * @param revert True if we want to revert the field names to their original values
     * @return the translated key
     */
    private static String translateIllegalKey(final String key, final boolean revert) {
        if (revert) {
            return key.replaceAll(DOT_STRING_REPLACEMENT, DOT_STRING).replaceAll(DASH_STRING_REPLACEMENT, DASH_STRING);
        }
        else {
            return key.replaceAll(DOT_STRING, DOT_STRING_REPLACEMENT).replaceAll(DASH_STRING, DASH_STRING_REPLACEMENT);
        }
    }
}
