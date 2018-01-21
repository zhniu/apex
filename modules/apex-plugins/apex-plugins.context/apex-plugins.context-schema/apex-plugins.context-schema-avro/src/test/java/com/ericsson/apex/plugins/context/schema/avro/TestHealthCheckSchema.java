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

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericData.Record;
import org.apache.avro.generic.GenericRecord;
import org.junit.Before;
import org.junit.Test;

import com.ericsson.apex.context.SchemaHelper;
import com.ericsson.apex.context.impl.schema.SchemaHelperFactory;
import com.ericsson.apex.context.parameters.SchemaParameters;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxKey;
import com.ericsson.apex.model.basicmodel.service.ModelService;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchema;
import com.ericsson.apex.model.contextmodel.concepts.AxContextSchemas;
import com.ericsson.apex.model.utilities.TextFileUtils;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestHealthCheckSchema {
    private AxKey            testKey = new AxArtifactKey("AvroTest", "0.0.1");
    private AxContextSchemas schemas;
    private String           healthCheckSchema;

    @Before
    public void initTest() throws IOException {
        schemas = new AxContextSchemas(new AxArtifactKey("AvroSchemas", "0.0.1"));
        ModelService.registerModel(AxContextSchemas.class, schemas);
        
        new SchemaParameters().getSchemaHelperParameterMap().put("Avro", new AvroSchemaHelperParameters());
        healthCheckSchema    = TextFileUtils.getTextFileAsString("src/test/resources/avsc/HealthCheckBodyType.avsc");
    }


    @Test
    public void testHealthCheck() throws IOException {
        AxContextSchema avroSchema = new AxContextSchema(new AxArtifactKey("AvroRecord", "0.0.1"), "Avro", healthCheckSchema);

        schemas.getSchemasMap().put(avroSchema.getKey(), avroSchema);
        SchemaHelper schemaHelper = new SchemaHelperFactory().createSchemaHelper(testKey, avroSchema.getKey());

        testUnmarshalMarshal(schemaHelper, "src/test/resources/data/HealthCheckEvent.js");
        
        GenericRecord healthCheckRecord = (Record) schemaHelper.createNewInstance();
        Schema healthCheckRecordSchema = healthCheckRecord.getSchema();
        
        GenericRecord inputRecord = new GenericData.Record(healthCheckRecordSchema.getField("input").schema());
        Schema inputRecordRecordSchema = inputRecord.getSchema();
        
        GenericRecord actionIndentifiersRecord = new GenericData.Record(inputRecordRecordSchema.getField("action_DasH_identifiers").schema());
        
        GenericRecord commonHeaderRecord = new GenericData.Record(inputRecordRecordSchema.getField("common_DasH_header").schema());
        Schema commonHeaderRecordSchema = commonHeaderRecord.getSchema();
        
        GenericRecord commonHeaderFlagsRecord = new GenericData.Record(commonHeaderRecordSchema.getField("flags").schema());
        
        healthCheckRecord.put("input", inputRecord);
        inputRecord.put("action_DasH_identifiers", actionIndentifiersRecord);
        inputRecord.put("common_DasH_header", commonHeaderRecord);
        commonHeaderRecord.put("flags", commonHeaderFlagsRecord);
        
        inputRecord.put("action", "HealthCheck");
        inputRecord.put("payload", "{\"host-ip-address\":\"131.160.203.125\",\"input.url\":\"131.160.203.125/afr\",\"request-action-type\":\"GET\",\"request-action\":\"AFR\"}");
        
        actionIndentifiersRecord.put("vnf_DasH_id", "49414df5-3482-4fd8-9952-c463dff2770b");
        
        commonHeaderRecord.put("request_DasH_id", "afr-request3");
        commonHeaderRecord.put("originator_DasH_id", "AFR");
        commonHeaderRecord.put("api_DasH_ver", "2.15");
        commonHeaderRecord.put("sub_DasH_request_DasH_id", "AFR-subrequest");
        commonHeaderRecord.put("timestamp", "2017-11-06T15:15:18.97Z");
        
        commonHeaderFlagsRecord.put("ttl", "10000");
        commonHeaderFlagsRecord.put("force", "TRUE");
        commonHeaderFlagsRecord.put("mode", "EXCLUSIVE");
        
        String eventString = TextFileUtils.getTextFileAsString("src/test/resources/data/HealthCheckEvent.js");
        String outString = schemaHelper.marshal2Json(healthCheckRecord);
        assertEquals(eventString.toString().replaceAll("\\s+",""), outString.replaceAll("\\s+",""));
    }

    private void testUnmarshalMarshal(final SchemaHelper schemaHelper, final String fileName) throws IOException {
        String inString = TextFileUtils.getTextFileAsString(fileName);
        GenericRecord decodedObject = (GenericRecord)schemaHelper.unmarshal(inString);
        String outString = schemaHelper.marshal2Json(decodedObject);
        assertEquals(inString.replaceAll("\\s+",""), outString.replaceAll("\\s+",""));
    }
}
