/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2014-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.context.schema.avro;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.apache.avro.generic.GenericData.EnumSymbol;
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
public class TestAvroSchemaEnum {
	private AxKey            testKey = new AxArtifactKey("AvroTest", "0.0.1");
	private AxContextSchemas schemas;
	private String           enumSchema;

	@Before
	public void initTest() throws IOException {
		schemas = new AxContextSchemas(new AxArtifactKey("AvroSchemas", "0.0.1"));
		ModelService.registerModel(AxContextSchemas.class, schemas);
		new SchemaParameters().getSchemaHelperParameterMap().put("Avro", new AvroSchemaHelperParameters());
		enumSchema = TextFileUtils.getTextFileAsString("src/test/resources/avsc/EnumSchema.avsc");
	}

	@Test
	public void testEnumInit() throws IOException {
		AxContextSchema avroSchema = new AxContextSchema(new AxArtifactKey("AvroRecord", "0.0.1"), "Avro", enumSchema);

		schemas.getSchemasMap().put(avroSchema.getKey(), avroSchema);
		SchemaHelper schemaHelper = new SchemaHelperFactory().createSchemaHelper(testKey, avroSchema.getKey());

		EnumSymbol newEnumEmpty = (EnumSymbol)schemaHelper.createNewInstance();
		assertEquals("SPADES", newEnumEmpty.toString());

		EnumSymbol newEnumFull = (EnumSymbol)schemaHelper.createNewInstance("\"HEARTS\"");
		assertEquals("HEARTS", newEnumFull.toString());
	}

	@Test
	public void testEnumUnmarshalMarshal() throws IOException {
		AxContextSchema avroSchema = new AxContextSchema(new AxArtifactKey("AvroArray", "0.0.1"), "Avro", enumSchema);

		schemas.getSchemasMap().put(avroSchema.getKey(), avroSchema);
		SchemaHelper schemaHelper = new SchemaHelperFactory().createSchemaHelper(testKey, avroSchema.getKey());

		testUnmarshalMarshal(schemaHelper, "src/test/resources/data/EnumExampleHearts.js");

		try {
			testUnmarshalMarshal(schemaHelper, "src/test/resources/data/EnumExampleNull.js");
			fail("This test should throw an exception here");
		}
		catch (Exception e) {
			assertEquals("AvroTest:0.0.1: object \"null\" Avro unmarshalling failed: Expected fixed. Got VALUE_NULL", e.getMessage());
		}
		try {
			testUnmarshalMarshal(schemaHelper, "src/test/resources/data/EnumExampleNull.js");
			fail("This test should throw an exception here");
		}
		catch (Exception e) {
			assertEquals("AvroTest:0.0.1: object \"null\" Avro unmarshalling failed: Expected fixed. Got VALUE_NULL", e.getMessage());
		}
		try {
			testUnmarshalMarshal(schemaHelper, "src/test/resources/data/EnumExampleBad0.js");
			fail("This test should throw an exception here");
		}
		catch (Exception e) {
			assertEquals("AvroTest:0.0.1: object \"\"TWEED\"\" Avro unmarshalling failed: Unknown symbol in enum TWEED", e.getMessage());
		}
		try {
			testUnmarshalMarshal(schemaHelper, "src/test/resources/data/EnumExampleBad1.js");
			fail("This test should throw an exception here");
		}
		catch (Exception e) {
			assertEquals("AvroTest:0.0.1: object \"\"Hearts\"\" Avro unmarshalling failed: Unknown symbol in enum Hearts", e.getMessage());
		}
	}

	private void testUnmarshalMarshal(final SchemaHelper schemaHelper, final String fileName) throws IOException {
		String inString = TextFileUtils.getTextFileAsString(fileName);
		EnumSymbol decodedObject = (EnumSymbol)schemaHelper.unmarshal(inString);
		String outString = schemaHelper.marshal2Json(decodedObject);
		assertEquals(inString.replaceAll("[\\r?\\n]+", " "), outString.replaceAll("[\\r?\\n]+", " "));
	}
}
