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

import static org.junit.Assert.*;

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

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 * @version 
 */
public class TestAvroSchemaHelperMarshal {
	private AxKey            testKey = new AxArtifactKey("AvroTest", "0.0.1");
	private AxContextSchemas schemas;

	@Before
	public void initTest() {
		schemas = new AxContextSchemas(new AxArtifactKey("AvroSchemas", "0.0.1"));
		ModelService.registerModel(AxContextSchemas.class, schemas);
		new SchemaParameters().getSchemaHelperParameterMap().put("Avro", new AvroSchemaHelperParameters());
	}

	@Test
	public void testNullMarshal() {
		AxContextSchema avroNullSchema = new AxContextSchema(new AxArtifactKey("AvroNull", "0.0.1"), "Avro", "{\"type\": \"null\"}");

		schemas.getSchemasMap().put(avroNullSchema.getKey(), avroNullSchema);
		SchemaHelper schemaHelper0 = new SchemaHelperFactory().createSchemaHelper(testKey, avroNullSchema.getKey());
		
		assertEquals("null", schemaHelper0.marshal2Json(null));
		assertEquals("null", schemaHelper0.marshal2Json(123));
		assertEquals("null", schemaHelper0.marshal2Json("Everything is marshalled to Null, no matter what it is"));
	}

	@Test
	public void testBooleanMarshal() {
		AxContextSchema avroBooleanSchema = new AxContextSchema(new AxArtifactKey("AvroBoolean", "0.0.1"), "Avro", "{\"type\": \"boolean\"}");

		schemas.getSchemasMap().put(avroBooleanSchema.getKey(), avroBooleanSchema);
		SchemaHelper schemaHelper1 = new SchemaHelperFactory().createSchemaHelper(testKey, avroBooleanSchema.getKey());

		assertEquals("true",  schemaHelper1.marshal2Json(true));
		assertEquals("false", schemaHelper1.marshal2Json(false));
		try {
			schemaHelper1.marshal2Json(0);
			fail("Test should throw an exception here");
		}
		catch (Exception e) {
			e.printStackTrace();
			assertEquals("AvroTest:0.0.1: object \"0\" Avro marshalling failed: java.lang.Integer cannot be cast to java.lang.Boolean", e.getMessage());
		}
		try {
			schemaHelper1.marshal2Json("0");
			fail("Test should throw an exception here");
		}
		catch (Exception e) {
			e.printStackTrace();
			assertEquals("AvroTest:0.0.1: object \"0\" Avro marshalling failed: java.lang.String cannot be cast to java.lang.Boolean", e.getMessage());
		}
	}

	@Test
	public void testIntMarshal() {
		AxContextSchema avroIntSchema = new AxContextSchema(new AxArtifactKey("AvroInt", "0.0.1"), "Avro", "{\"type\": \"int\"}");

		schemas.getSchemasMap().put(avroIntSchema.getKey(), avroIntSchema);
		SchemaHelper schemaHelper2 = new SchemaHelperFactory().createSchemaHelper(testKey, avroIntSchema.getKey());

		assertEquals("0",            schemaHelper2.marshal2Json(0));
		assertEquals("1",            schemaHelper2.marshal2Json(1));
		assertEquals("-1",           schemaHelper2.marshal2Json(-1));
		assertEquals("1",            schemaHelper2.marshal2Json(1.23));
		assertEquals("-1",           schemaHelper2.marshal2Json(-1.23));
		assertEquals("2147483647",   schemaHelper2.marshal2Json(2147483647));
		assertEquals("-2147483648",  schemaHelper2.marshal2Json(-2147483648));
		try {
			schemaHelper2.marshal2Json("Hello");
			fail("Test should throw an exception here");
		}
		catch (Exception e) {
			assertTrue(e.getMessage().startsWith("AvroTest:0.0.1: object \"Hello\" Avro marshalling failed: java.lang.String cannot be cast to java.lang.Number"));
		}
		try {
			schemaHelper2.marshal2Json(null);
			fail("Test should throw an exception here");
		}
		catch (Exception e) {
			assertTrue(e.getMessage().startsWith("AvroTest:0.0.1: cannot encode a null object of class \"java.lang.Integer\""));
		}
	}

	@Test
	public void testLongMarshal() {
		AxContextSchema avroLongSchema = new AxContextSchema(new AxArtifactKey("AvroLong", "0.0.1"), "Avro", "{\"type\": \"long\"}");

		schemas.getSchemasMap().put(avroLongSchema.getKey(), avroLongSchema);
		SchemaHelper schemaHelper3 = new SchemaHelperFactory().createSchemaHelper(testKey, avroLongSchema.getKey());
		
		assertEquals("0",                    schemaHelper3.marshal2Json(0L));
		assertEquals("1",                    schemaHelper3.marshal2Json(1L));
		assertEquals("-1",                   schemaHelper3.marshal2Json(-1L));
		assertEquals("9223372036854775807",  schemaHelper3.marshal2Json(9223372036854775807L));
		assertEquals("-9223372036854775808", schemaHelper3.marshal2Json(-9223372036854775808L));
		try {
			schemaHelper3.marshal2Json("Hello");
			fail("Test should throw an exception here");
		}
		catch (Exception e) {
			assertTrue(e.getMessage().startsWith("AvroTest:0.0.1: object \"Hello\" Avro marshalling failed: java.lang.String cannot be cast to java.lang.Long"));
		}
		try {
			schemaHelper3.marshal2Json(null);
			fail("Test should throw an exception here");
		}
		catch (Exception e) {
			assertTrue(e.getMessage().startsWith("AvroTest:0.0.1: cannot encode a null object of class \"java.lang.Long\""));
		}
	}

	@Test
	public void testFloatMarshal() {
		AxContextSchema avroFloatSchema = new AxContextSchema(new AxArtifactKey("AvroFloat", "0.0.1"), "Avro", "{\"type\": \"float\"}");

		schemas.getSchemasMap().put(avroFloatSchema.getKey(), avroFloatSchema);
		SchemaHelper schemaHelper4 = new SchemaHelperFactory().createSchemaHelper(testKey, avroFloatSchema.getKey());

		assertEquals("0.0",          schemaHelper4.marshal2Json(0F));
		assertEquals("1.0",          schemaHelper4.marshal2Json(1F));
		assertEquals("-1.0",         schemaHelper4.marshal2Json(-1F));
		assertEquals("1.23",         schemaHelper4.marshal2Json(1.23F));
		assertEquals("-1.23",        schemaHelper4.marshal2Json(-1.23F));
		assertEquals("9.223372E18",  schemaHelper4.marshal2Json(9.223372E18F));
		assertEquals("-9.223372E18", schemaHelper4.marshal2Json(-9.223372E18F));
		assertEquals("9.223372E18",  schemaHelper4.marshal2Json(9.223372E18F));
		assertEquals("-9.223372E18", schemaHelper4.marshal2Json(-9.223372E18F));
		try {
			schemaHelper4.marshal2Json("Hello");
			fail("Test should throw an exception here");
		}
		catch (Exception e) {
			assertTrue(e.getMessage().startsWith("AvroTest:0.0.1: object \"Hello\" Avro marshalling failed: java.lang.String cannot be cast to java.lang.Float"));
		}
		try {
			schemaHelper4.marshal2Json(null);
			fail("Test should throw an exception here");
		}
		catch (Exception e) {
			assertTrue(e.getMessage().startsWith("AvroTest:0.0.1: cannot encode a null object of class \"java.lang.Float\""));
		}
	}


	@Test
	public void testDoubleMarshal() {
		AxContextSchema avroDoubleSchema = new AxContextSchema(new AxArtifactKey("AvroDouble", "0.0.1"), "Avro", "{\"type\": \"double\"}");

		schemas.getSchemasMap().put(avroDoubleSchema.getKey(), avroDoubleSchema);
		SchemaHelper schemaHelper5 = new SchemaHelperFactory().createSchemaHelper(testKey, avroDoubleSchema.getKey());
		
		assertEquals("0.0",                   schemaHelper5.marshal2Json(0D));
		assertEquals("1.0",                   schemaHelper5.marshal2Json(1D));
		assertEquals("-1.0",                  schemaHelper5.marshal2Json(-1D));
		assertEquals("1.23",                  schemaHelper5.marshal2Json(1.23));
		assertEquals("-1.23",                 schemaHelper5.marshal2Json(-1.23));
		assertEquals("9.223372036854776E18",  schemaHelper5.marshal2Json(9.223372036854776E18));
		assertEquals("-9.223372036854776E18", schemaHelper5.marshal2Json(-9.223372036854776E18));
		assertEquals("9.223372036854776E18",  schemaHelper5.marshal2Json(9.223372036854776E18));
		assertEquals("-9.223372036854776E18", schemaHelper5.marshal2Json(-9.223372036854776E18));
		try {
			schemaHelper5.marshal2Json("Hello");
			fail("Test should throw an exception here");
		}
		catch (Exception e) {
			assertTrue(e.getMessage().startsWith("AvroTest:0.0.1: object \"Hello\" Avro marshalling failed: java.lang.String cannot be cast to java.lang.Double"));
		}
		try {
			schemaHelper5.marshal2Json(null);
			fail("Test should throw an exception here");
		}
		catch (Exception e) {
			assertTrue(e.getMessage().startsWith("AvroTest:0.0.1: cannot encode a null object of class \"java.lang.Double\""));
		}
	}

	@Test
	public void testStringMarshal() {
		AxContextSchema avroStringSchema = new AxContextSchema(new AxArtifactKey("AvroString", "0.0.1"), "Avro", "{\"type\": \"string\"}");

		schemas.getSchemasMap().put(avroStringSchema.getKey(), avroStringSchema);
		SchemaHelper schemaHelper7 = new SchemaHelperFactory().createSchemaHelper(testKey, avroStringSchema.getKey());

		assertEquals("\"0\"",                    schemaHelper7.marshal2Json("0"));
		assertEquals("\"1\"",                    schemaHelper7.marshal2Json("1"));
		assertEquals("\"-1\"",                   schemaHelper7.marshal2Json("-1"));
		assertEquals("\"1.23\"",                 schemaHelper7.marshal2Json("1.23"));
		assertEquals("\"-1.23\"",                schemaHelper7.marshal2Json("-1.23"));
		assertEquals("\"9223372036854775807\"",  schemaHelper7.marshal2Json("9223372036854775807"));
		assertEquals("\"-9223372036854775808\"", schemaHelper7.marshal2Json("-9223372036854775808"));
		assertEquals("\"9223372036854775808\"",  schemaHelper7.marshal2Json("9223372036854775808"));
		assertEquals("\"-9223372036854775809\"", schemaHelper7.marshal2Json("-9223372036854775809"));
		assertEquals("\"Hello\"",                schemaHelper7.marshal2Json("Hello"));
		try {
			schemaHelper7.marshal2Json(null);
			fail("Test should throw an exception here");
		}
		catch (Exception e) {
			assertTrue(e.getMessage().startsWith("AvroTest:0.0.1: cannot encode a null object of class \"java.lang.String\""));
		}
	}

	@Test
	public void testBytesMarshal() {
		AxContextSchema avroSchema = new AxContextSchema(new AxArtifactKey("AvroString", "0.0.1"), "Avro", "{\"type\": \"bytes\"}");

		schemas.getSchemasMap().put(avroSchema.getKey(), avroSchema);
		SchemaHelper schemaHelper = new SchemaHelperFactory().createSchemaHelper(testKey, avroSchema.getKey());

		byte[] helloBytes = {104, 101, 108, 108, 111};
		String helloOut   = schemaHelper.marshal2Json(helloBytes);
		assertEquals("\"hello\"", helloOut);

		try {
			schemaHelper.marshal2Json(null);
			fail("Test should throw an exception here");
		}
		catch (Exception e) {
			assertTrue(e.getMessage().startsWith("AvroTest:0.0.1: cannot encode a null object of class \"java.lang.Byte[]\""));
		}
	}
}
