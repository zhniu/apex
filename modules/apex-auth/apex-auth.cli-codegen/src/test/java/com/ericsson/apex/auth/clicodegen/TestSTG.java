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

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

/**
 * Test for the CG CLI Editor STG file.
 *
 * @author Sven van der Meer (sven.van.der.meer@ericsson.com)
 */
public class TestSTG {

    /**
     * Get the chunks for the codegen.
     *
     * @return the chunks
     */
    private static Map<String, List<String>> getCodeGenChunks() {
        //CHECKSTYLE:OFF: LineLength

        final Map<String, List<String>> chunks = new LinkedHashMap<>();

        chunks.put("/policyModel",                    Arrays.asList("name", "version", "uuid", "description", "declarations", "definitions"));
        chunks.put("/schemaDecl",                     Arrays.asList("name", "version", "uuid", "description", "flavour", "schema"));
        chunks.put("/ctxAlbumDecl",                   Arrays.asList("name", "version", "uuid", "description", "scope", "writable", "schemaName", "schemaVersion"));
        chunks.put("/eventDecl",                      Arrays.asList("name", "version", "uuid", "description", "nameSpace", "source", "target", "fields"));
        chunks.put("/eventDefField",                  Arrays.asList("eventName", "version", "fieldName", "fieldSchema", "fieldSchemaVersion", "optional"));
        chunks.put("/taskDecl",                       Arrays.asList("name", "version", "uuid", "description", "infields", "outfields", "logic"));
        chunks.put("/taskDefInputFields",             Arrays.asList("taskName", "version", "fieldName", "fieldSchema", "fieldSchemaVersion"));
        chunks.put("/taskDefOutputFields",            Arrays.asList("taskName", "version", "fieldName", "fieldSchema", "fieldSchemaVersion"));
        chunks.put("/taskDefLogic",                   Arrays.asList("taskName", "version", "flavour", "logic"));
        chunks.put("/taskDefParameter",               Arrays.asList("name", "version", "parName", "defaultValue"));
        chunks.put("/taskDefCtxRef",                  Arrays.asList("name", "version", "albumName", "albumVersion"));
        chunks.put("/policyDef",                      Arrays.asList("name", "version", "uuid", "description", "template", "firstState"));
        chunks.put("/policyStateDef",                 Arrays.asList("policyName", "version", "stateName", "triggerName", "triggerVersion", "defaultTask", "defaultTaskVersion", "outputs", "tasks"));
        chunks.put("/policyStateOutput",              Arrays.asList("policyName", "version", "stateName", "outputName", "eventName", "eventVersion", "nextState"));
        chunks.put("/policyStateTaskSelectionLogic",  Arrays.asList("name", "version", "stateName", "logicFlavour", "logic"));
        chunks.put("/policyStateTask",                Arrays.asList("policyName", "version", "stateName", "taskLocalName", "taskName", "taskVersion", "outputType", "outputName"));
        chunks.put("/policyStateFinalizerLogic",      Arrays.asList("name", "version", "stateName", "finalizerLogicName", "logicFlavour", "logic"));
        chunks.put("/policyStateContextRef",          Arrays.asList("name", "version", "stateName", "albumName", "albumVersion"));

        return chunks;
        //CHECKSTYLE:ON: LineLength
    }

    /** Test STG load. */
    @Test
    public void testSTGLoad() {
        final StErrorListener errListener = new StErrorListener();
        final STGroupFile stg = new STGroupFile(CGCliEditor.STG_FILE);
        stg.setListener(errListener);

        stg.getTemplateNames(); // dummy to compile group and get errors
        assertEquals(0, errListener.getErrorCount());
    }

    /** Test STG chunks. */
    @Test
    public void testSTGChunks() {
        final StErrorListener errListener = new StErrorListener();
        final STGroupFile stg = new STGroupFile(CGCliEditor.STG_FILE);
        stg.setListener(errListener);

        stg.getTemplateNames(); // dummy to compile group and get errors
        final Map<String, List<String>> chunks = getCodeGenChunks();
        String error = "";
        final Set<String> definedNames = stg.getTemplateNames();
        for (final STGroup group : stg.getImportedGroups()) {
            definedNames.addAll(group.getTemplateNames());
        }
        final Set<String> requiredNames = chunks.keySet();

        for (final String required : requiredNames) {
            if (!definedNames.contains(required)) {
                error += " - target STG does not define template for <" + required + ">\n";
            }
            else {
                final Set<String> definedParams = (
                        (stg.getInstanceOf(required).getAttributes() == null) ? 
                                new TreeSet<String>()
                                : 
                                stg.getInstanceOf(required).getAttributes().keySet());
                final List<String> requiredParams = chunks.get(required);
                for (final String reqArg : requiredParams) {
                    if (!definedParams.contains(reqArg)) {
                        error += " - target STG with template <" + required + "> does not define argument <" + reqArg + ">\n";
                    }
                }
            }
        }

        if (!("".equals(error))) {
            System.err.println(error);
        }
        assertEquals(0, error.length());
    }
}
