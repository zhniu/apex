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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.apache.avro.generic.GenericData.Fixed;
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
public class TestAvroSchemaFixed {
	private AxKey            testKey = new AxArtifactKey("AvroTest", "0.0.1");
	private AxContextSchemas schemas;
	private String           fixedSchema;

	@Before
	public void initTest() throws IOException {
		schemas = new AxContextSchemas(new AxArtifactKey("AvroSchemas", "0.0.1"));
		ModelService.registerModel(AxContextSchemas.class, schemas);
		new SchemaParameters().getSchemaHelperParameterMap().put("Avro", new AvroSchemaHelperParameters());
		fixedSchema = TextFileUtils.getTextFileAsString("src/test/resources/avsc/FixedSchema.avsc");
	}

	@Test
	public void testFixedInit() throws IOException {
		AxContextSchema avroSchema = new AxContextSchema(new AxArtifactKey("AvroRecord", "0.0.1"), "Avro", fixedSchema);

		schemas.getSchemasMap().put(avroSchema.getKey(), avroSchema);
		SchemaHelper schemaHelper = new SchemaHelperFactory().createSchemaHelper(testKey, avroSchema.getKey());

		try {
			schemaHelper.createNewInstance();
			fail("Test should throw an exception here");
		}
		catch (Exception e) {
			assertEquals("AvroTest:0.0.1: could not create an instance of class \"org.apache.avro.generic.GenericData.Fixed\" using the default constructor \"Fixed()\"", e.getMessage());
		}

		String inString = TextFileUtils.getTextFileAsString("src/test/resources/data/FixedExampleGood.js");
		Fixed newFixedFull = (Fixed)schemaHelper.createNewInstance(inString);
		assertTrue(newFixedFull.toString().startsWith("[48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65"));
		assertTrue(newFixedFull.toString().endsWith  ("53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70]"));
	}

	@Test
	public void testFixedUnmarshalMarshal() throws IOException {
		AxContextSchema avroSchema = new AxContextSchema(new AxArtifactKey("AvroArray", "0.0.1"), "Avro", fixedSchema);

		schemas.getSchemasMap().put(avroSchema.getKey(), avroSchema);
		SchemaHelper schemaHelper = new SchemaHelperFactory().createSchemaHelper(testKey, avroSchema.getKey());

		testUnmarshalMarshal(schemaHelper, "src/test/resources/data/FixedExampleGood.js");

		try {
			testUnmarshalMarshal(schemaHelper, "src/test/resources/data/FixedExampleNull.js");
			fail("This test should throw an exception here");
		}
		catch (Exception e) {
			assertEquals("AvroTest:0.0.1: object \"null\" Avro unmarshalling failed: Expected fixed. Got VALUE_NULL", e.getMessage());
		}
		try {
			testUnmarshalMarshal(schemaHelper, "src/test/resources/data/FixedExampleNull.js");
			fail("This test should throw an exception here");
		}
		catch (Exception e) {
			assertEquals("AvroTest:0.0.1: object \"null\" Avro unmarshalling failed: Expected fixed. Got VALUE_NULL", e.getMessage());
		}
		try {
			testUnmarshalMarshal(schemaHelper, "src/test/resources/data/FixedExampleBad0.js");
			fail("This test should throw an exception here");
		}
		catch (Exception e) {
			assertEquals("AvroTest:0.0.1: object \"\"BADBAD\"\" Avro unmarshalling failed: Expected fixed length 64, but got6", e.getMessage());
		}
		try {
			testUnmarshalMarshal(schemaHelper, "src/test/resources/data/FixedExampleBad1.js");
			fail("This test should throw an exception here");
		}
		catch (Exception e) {
			assertEquals("AvroTest:0.0.1: object \"\"0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0\"\" Avro unmarshalling failed: Expected fixed length 64, but got65", e.getMessage());
		}
	}

	private void testUnmarshalMarshal(final SchemaHelper schemaHelper, final String fileName) throws IOException {
		String inString = TextFileUtils.getTextFileAsString(fileName);
		Fixed decodedObject = (Fixed)schemaHelper.unmarshal(inString);
		String outString = schemaHelper.marshal2Json(decodedObject);
		assertEquals(inString.replaceAll("[\\r?\\n]+", " "), outString.replaceAll("[\\r?\\n]+", " "));
	}
}
