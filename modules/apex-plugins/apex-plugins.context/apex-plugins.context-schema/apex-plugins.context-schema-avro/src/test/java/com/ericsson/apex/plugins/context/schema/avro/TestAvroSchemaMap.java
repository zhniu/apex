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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.avro.util.Utf8;
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
 * @version 
 */
public class TestAvroSchemaMap {
	private AxKey            testKey = new AxArtifactKey("AvroTest", "0.0.1");
	private AxContextSchemas schemas;
	private String           longMapSchema;
	private String           addressMapSchema;
	private String           addressMapSchemaInvalidFields;

	@Before
	public void initTest() throws IOException {
		schemas = new AxContextSchemas(new AxArtifactKey("AvroSchemas", "0.0.1"));
		ModelService.registerModel(AxContextSchemas.class, schemas);
		new SchemaParameters().getSchemaHelperParameterMap().put("Avro", new AvroSchemaHelperParameters());
		longMapSchema = TextFileUtils.getTextFileAsString("src/test/resources/avsc/MapExampleLong.avsc");
        addressMapSchema = TextFileUtils.getTextFileAsString("src/test/resources/avsc/MapExampleAddress.avsc");
        addressMapSchemaInvalidFields = TextFileUtils.getTextFileAsString("src/test/resources/avsc/MapExampleAddressInvalidFields.avsc");
	}

	@Test
	public void testMapInit() throws IOException {
		AxContextSchema avroSchema = new AxContextSchema(new AxArtifactKey("AvroRecord", "0.0.1"), "Avro", addressMapSchema);

		schemas.getSchemasMap().put(avroSchema.getKey(), avroSchema);
		SchemaHelper schemaHelper = new SchemaHelperFactory().createSchemaHelper(testKey, avroSchema.getKey());

		HashMap<?, ?> newMapEmpty = (HashMap<?, ?>)schemaHelper.createNewInstance();
		assertEquals(0, newMapEmpty.size());

		String inString = TextFileUtils.getTextFileAsString("src/test/resources/data/MapExampleAddressFull.js");
		HashMap<?, ?> newMapFull = (HashMap<?, ?>)schemaHelper.createNewInstance(inString);
		
		assertEquals("{\"streetaddress\": \"221 B Baker St.\", \"city\": \"London\"}", newMapFull.get(new Utf8("address2")).toString());
	}

	@Test
	public void testLongMapUnmarshalMarshal() throws IOException {
		AxContextSchema avroSchema = new AxContextSchema(new AxArtifactKey("AvroMap", "0.0.1"), "Avro", longMapSchema);

		schemas.getSchemasMap().put(avroSchema.getKey(), avroSchema);
		SchemaHelper schemaHelper = new SchemaHelperFactory().createSchemaHelper(testKey, avroSchema.getKey());

		testUnmarshalMarshal(schemaHelper, "src/test/resources/data/MapExampleLongNull.js");
		testUnmarshalMarshal(schemaHelper, "src/test/resources/data/MapExampleLongFull.js");
	}

    @Test
    public void testAddressMapUnmarshalMarshal() throws IOException {
        AxContextSchema avroSchema = new AxContextSchema(new AxArtifactKey("AvroMap", "0.0.1"), "Avro", addressMapSchema);

        schemas.getSchemasMap().put(avroSchema.getKey(), avroSchema);
        SchemaHelper schemaHelper = new SchemaHelperFactory().createSchemaHelper(testKey, avroSchema.getKey());

        testUnmarshalMarshal(schemaHelper, "src/test/resources/data/MapExampleAddressNull.js");
        testUnmarshalMarshal(schemaHelper, "src/test/resources/data/MapExampleAddressFull.js");
    }

    @Test
    public void testAddressMapUnmarshalMarshalInvalidFields() throws IOException {
        AxContextSchema avroSchema = new AxContextSchema(new AxArtifactKey("AvroMap", "0.0.1"), "Avro", addressMapSchemaInvalidFields);

        schemas.getSchemasMap().put(avroSchema.getKey(), avroSchema);
        SchemaHelper schemaHelper = new SchemaHelperFactory().createSchemaHelper(testKey, avroSchema.getKey());

        testUnmarshalMarshal(schemaHelper, "src/test/resources/data/MapExampleAddressInvalidFields.js");
    }

	private void testUnmarshalMarshal(final SchemaHelper schemaHelper, final String fileName) throws IOException {
		String originalInString = TextFileUtils.getTextFileAsString(fileName);
		HashMap<?, ?> firstDecodedMap = (HashMap<?, ?>)schemaHelper.unmarshal(originalInString);
		
		String outString = schemaHelper.marshal2Json(firstDecodedMap);
		
		File tempOutFile = File.createTempFile("ApexAvro", ".js");
		TextFileUtils.putStringAsFile(outString, tempOutFile);

		String decodeEncodeInString = TextFileUtils.getTextFileAsString(fileName);
		tempOutFile.delete();
		
		HashMap<?, ?> secondDecodedMap = (HashMap<?, ?>)schemaHelper.unmarshal(decodeEncodeInString);

		// Now check that our doubly encoded map equals the first decoded map, Java map equals checks values and keys
		assertEquals(firstDecodedMap, secondDecodedMap);
	}
}
