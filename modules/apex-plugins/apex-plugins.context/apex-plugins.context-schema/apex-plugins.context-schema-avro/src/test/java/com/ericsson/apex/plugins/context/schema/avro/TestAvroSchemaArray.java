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

import org.apache.avro.generic.GenericData.Array;
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
public class TestAvroSchemaArray {
	private AxKey            testKey = new AxArtifactKey("AvroTest", "0.0.1");
	private AxContextSchemas schemas;
	private String           longArraySchema;
	private String           addressArraySchema;

	@Before
	public void initTest() throws IOException {
		schemas = new AxContextSchemas(new AxArtifactKey("AvroSchemas", "0.0.1"));
		ModelService.registerModel(AxContextSchemas.class, schemas);
		new SchemaParameters().getSchemaHelperParameterMap().put("Avro", new AvroSchemaHelperParameters());
		longArraySchema = TextFileUtils.getTextFileAsString("src/test/resources/avsc/ArrayExampleLong.avsc");
		addressArraySchema = TextFileUtils.getTextFileAsString("src/test/resources/avsc/ArrayExampleAddress.avsc");
	}

	@Test
	public void testArrayInit() throws IOException {
		AxContextSchema avroSchema = new AxContextSchema(new AxArtifactKey("AvroRecord", "0.0.1"), "Avro", addressArraySchema);

		schemas.getSchemasMap().put(avroSchema.getKey(), avroSchema);
		SchemaHelper schemaHelper = new SchemaHelperFactory().createSchemaHelper(testKey, avroSchema.getKey());

		Array<?> newArrayEmpty = (Array<?>)schemaHelper.createNewInstance();
		assertEquals(0, newArrayEmpty.size());

		String inString = TextFileUtils.getTextFileAsString("src/test/resources/data/ArrayExampleAddressFull.js");
		Array<?> newArrayFull = (Array<?>)schemaHelper.createNewInstance(inString);
		assertEquals("{\"streetaddress\": \"1600 Pennsylvania Avenue\", \"city\": \"Washington DC\"}", newArrayFull.get(0).toString());
	}
	
	@Test
	public void testLongArrayUnmarshalMarshal() throws IOException {
		AxContextSchema avroSchema = new AxContextSchema(new AxArtifactKey("AvroArray", "0.0.1"), "Avro", longArraySchema);

		schemas.getSchemasMap().put(avroSchema.getKey(), avroSchema);
		SchemaHelper schemaHelper = new SchemaHelperFactory().createSchemaHelper(testKey, avroSchema.getKey());

		testUnmarshalMarshal(schemaHelper, "src/test/resources/data/ArrayExampleLongNull.js");
		testUnmarshalMarshal(schemaHelper, "src/test/resources/data/ArrayExampleLongFull.js");
	}

	@Test
	public void testAddressArrayUnmarshalMarshal() throws IOException {
		AxContextSchema avroSchema = new AxContextSchema(new AxArtifactKey("AvroArray", "0.0.1"), "Avro", addressArraySchema);

		schemas.getSchemasMap().put(avroSchema.getKey(), avroSchema);
		SchemaHelper schemaHelper = new SchemaHelperFactory().createSchemaHelper(testKey, avroSchema.getKey());

		testUnmarshalMarshal(schemaHelper, "src/test/resources/data/ArrayExampleAddressNull.js");
		testUnmarshalMarshal(schemaHelper, "src/test/resources/data/ArrayExampleAddressFull.js");
	}

	private void testUnmarshalMarshal(final SchemaHelper schemaHelper, final String fileName) throws IOException {
		String inString = TextFileUtils.getTextFileAsString(fileName);
		Array<?> schemaObject = (Array<?>)schemaHelper.unmarshal(inString);
		String outString = schemaHelper.marshal2Json(schemaObject);
        assertEquals(inString.replaceAll("\\s+",""), outString.replaceAll("\\s+",""));
	}
}
