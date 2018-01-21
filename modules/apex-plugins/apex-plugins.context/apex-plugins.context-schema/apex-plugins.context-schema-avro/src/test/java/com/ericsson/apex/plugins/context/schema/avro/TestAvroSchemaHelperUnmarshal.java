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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 * @version 
 */
public class TestAvroSchemaHelperUnmarshal {
	private AxKey            testKey = new AxArtifactKey("AvroTest", "0.0.1");
	private AxContextSchemas schemas;

	@Before
	public void initTest() {
		schemas = new AxContextSchemas(new AxArtifactKey("AvroSchemas", "0.0.1"));
		ModelService.registerModel(AxContextSchemas.class, schemas);
		new SchemaParameters().getSchemaHelperParameterMap().put("Avro", new AvroSchemaHelperParameters());
	}

	//@Test
	public void testNullUnmarshal() {
		AxContextSchema avroNullSchema = new AxContextSchema(new AxArtifactKey("AvroNull", "0.0.1"), "Avro", "{\"type\": \"null\"}");

		schemas.getSchemasMap().put(avroNullSchema.getKey(), avroNullSchema);
		SchemaHelper schemaHelper0 = new SchemaHelperFactory().createSchemaHelper(testKey, avroNullSchema.getKey());
		
		try {
			schemaHelper0.createNewInstance();
			fail("test should throw an exception here");
		}
		catch (Exception e) {
			assertEquals("AvroTest:0.0.1: could not create an instance, schema class for the schema is null", e.getMessage());
		}
		
		assertEquals(null, schemaHelper0.unmarshal("null"));

		try {
			schemaHelper0.unmarshal("123");
			fail("test should throw an exception here");
		}
		catch (Exception e) {
			assertEquals("AvroTest:0.0.1: object \"123\" Avro unmarshalling failed: Expected null. Got VALUE_NUMBER_INT", e.getMessage());
		}
	}

	//@Test
	public void testBooleanUnmarshal() {
		AxContextSchema avroBooleanSchema = new AxContextSchema(new AxArtifactKey("AvroBoolean", "0.0.1"), "Avro", "{\"type\": \"boolean\"}");

		schemas.getSchemasMap().put(avroBooleanSchema.getKey(), avroBooleanSchema);
		SchemaHelper schemaHelper1 = new SchemaHelperFactory().createSchemaHelper(testKey, avroBooleanSchema.getKey());

		try {
			schemaHelper1.createNewInstance();
			fail("test should throw an exception here");
		}
		catch (Exception e) {
			assertEquals("AvroTest:0.0.1: could not create an instance of class \"java.lang.Boolean\" using the default constructor \"Boolean()\"", e.getMessage());
		}
		assertEquals(true, schemaHelper1.createNewInstance("true"));
		
		assertEquals(true,  schemaHelper1.unmarshal("true"));
		assertEquals(false, schemaHelper1.unmarshal("false"));
		try {
			schemaHelper1.unmarshal(0);
			fail("Test should throw an exception here");
		}
		catch (Exception e) {
			assertEquals("AvroTest:0.0.1: object \"0\" Avro unmarshalling failed: Expected boolean. Got VALUE_NUMBER_INT", e.getMessage());
		}
	}

	//@Test
	public void testIntUnmarshal() {
		AxContextSchema avroIntSchema = new AxContextSchema(new AxArtifactKey("AvroInt", "0.0.1"), "Avro", "{\"type\": \"int\"}");

		schemas.getSchemasMap().put(avroIntSchema.getKey(), avroIntSchema);
		SchemaHelper schemaHelper2 = new SchemaHelperFactory().createSchemaHelper(testKey, avroIntSchema.getKey());

		try {
			schemaHelper2.createNewInstance();
			fail("test should throw an exception here");
		}
		catch (Exception e) {
			assertEquals("AvroTest:0.0.1: could not create an instance of class \"java.lang.Integer\" using the default constructor \"Integer()\"", e.getMessage());
		}
		assertEquals(123, schemaHelper2.createNewInstance("123"));
		
		assertEquals(0,            schemaHelper2.unmarshal("0"));
		assertEquals(1,            schemaHelper2.unmarshal("1"));
		assertEquals(-1,           schemaHelper2.unmarshal("-1"));
		assertEquals(1,            schemaHelper2.unmarshal("1.23"));
		assertEquals(-1,           schemaHelper2.unmarshal("-1.23"));
		assertEquals(2147483647,   schemaHelper2.unmarshal("2147483647"));
		assertEquals(-2147483648,  schemaHelper2.unmarshal("-2147483648"));
		try {
			schemaHelper2.unmarshal("2147483648");
			fail("Test should throw an exception here");
		}
		catch (Exception e) {
			assertTrue(e.getMessage().startsWith("AvroTest:0.0.1: object \"2147483648\" Avro unmarshalling failed: Numeric value (2147483648) out of range of int"));
		}
		try {
			schemaHelper2.unmarshal("-2147483649");
			fail("Test should throw an exception here");
		}
		catch (Exception e) {
			assertTrue(e.getMessage().startsWith("AvroTest:0.0.1: object \"-2147483649\" Avro unmarshalling failed: Numeric value (-2147483649) out of range of int"));
		}
		try {
			schemaHelper2.unmarshal(null);
			fail("Test should throw an exception here");
		}
		catch (Exception e) {
			assertTrue(e.getMessage().equals("AvroTest:0.0.1: object \"null\" Avro unmarshalling failed: String to read from cannot be null!"));
		}
	}

	//@Test
	public void testLongUnmarshal() {
		AxContextSchema avroLongSchema = new AxContextSchema(new AxArtifactKey("AvroLong", "0.0.1"), "Avro", "{\"type\": \"long\"}");

		schemas.getSchemasMap().put(avroLongSchema.getKey(), avroLongSchema);
		SchemaHelper schemaHelper3 = new SchemaHelperFactory().createSchemaHelper(testKey, avroLongSchema.getKey());
		
		try {
			schemaHelper3.createNewInstance();
			fail("test should throw an exception here");
		}
		catch (Exception e) {
			assertEquals("AvroTest:0.0.1: could not create an instance of class \"java.lang.Long\" using the default constructor \"Long()\"", e.getMessage());
		}
		assertEquals(123456789L, schemaHelper3.createNewInstance("123456789"));
		
		assertEquals(0L,                    schemaHelper3.unmarshal("0"));
		assertEquals(1L,                    schemaHelper3.unmarshal("1"));
		assertEquals(-1L,                   schemaHelper3.unmarshal("-1"));
		assertEquals(1L,                    schemaHelper3.unmarshal("1.23"));
		assertEquals(-1L,                   schemaHelper3.unmarshal("-1.23"));
		assertEquals(9223372036854775807L,  schemaHelper3.unmarshal("9223372036854775807"));
		assertEquals(-9223372036854775808L, schemaHelper3.unmarshal("-9223372036854775808"));
		try {
			schemaHelper3.unmarshal("9223372036854775808");
			fail("Test should throw an exception here");
		}
		catch (Exception e) {
			assertTrue(e.getMessage().startsWith("AvroTest:0.0.1: object \"9223372036854775808\" Avro unmarshalling failed: Numeric value (9223372036854775808) out of range of long"));
		}
		try {
			schemaHelper3.unmarshal("-9223372036854775809");
			fail("Test should throw an exception here");
		}
		catch (Exception e) {
			assertTrue(e.getMessage().startsWith("AvroTest:0.0.1: object \"-9223372036854775809\" Avro unmarshalling failed: Numeric value (-9223372036854775809) out of range of long"));
		}
		try {
			schemaHelper3.unmarshal("\"Hello\"");
			fail("Test should throw an exception here");
		}
		catch (Exception e) {
			assertTrue(e.getMessage().equals("AvroTest:0.0.1: object \"\"Hello\"\" Avro unmarshalling failed: Expected long. Got VALUE_STRING"));
		}
		try {
			schemaHelper3.unmarshal(null);
			fail("Test should throw an exception here");
		}
		catch (Exception e) {
			assertTrue(e.getMessage().equals("AvroTest:0.0.1: object \"null\" Avro unmarshalling failed: String to read from cannot be null!"));
		}
	}

	//@Test
	public void testFloatUnmarshal() {
		AxContextSchema avroFloatSchema = new AxContextSchema(new AxArtifactKey("AvroFloat", "0.0.1"), "Avro", "{\"type\": \"float\"}");

		schemas.getSchemasMap().put(avroFloatSchema  .getKey(), avroFloatSchema);
		SchemaHelper schemaHelper4 = new SchemaHelperFactory().createSchemaHelper(testKey, avroFloatSchema.getKey());

		try {
			schemaHelper4.createNewInstance();
			fail("test should throw an exception here");
		}
		catch (Exception e) {
			assertEquals("AvroTest:0.0.1: could not create an instance of class \"java.lang.Float\" using the default constructor \"Float()\"", e.getMessage());
		}
		assertEquals(1.2345F, schemaHelper4.createNewInstance("1.2345"));
		
		assertEquals(0.0F,          schemaHelper4.unmarshal("0"));
		assertEquals(1.0F,          schemaHelper4.unmarshal("1"));
		assertEquals(-1.0F,         schemaHelper4.unmarshal("-1"));
		assertEquals(1.23F,         schemaHelper4.unmarshal("1.23"));
		assertEquals(-1.23F,        schemaHelper4.unmarshal("-1.23"));
		assertEquals(9.223372E18F,  schemaHelper4.unmarshal("9223372036854775807"));
		assertEquals(-9.223372E18F, schemaHelper4.unmarshal("-9223372036854775808"));
		assertEquals(9.223372E18F,  schemaHelper4.unmarshal("9223372036854775808"));
		assertEquals(-9.223372E18F, schemaHelper4.unmarshal("-9223372036854775809"));
		try {
			schemaHelper4.unmarshal("\"Hello\"");
			fail("Test should throw an exception here");
		}
		catch (Exception e) {
			assertTrue(e.getMessage().equals("AvroTest:0.0.1: object \"\"Hello\"\" Avro unmarshalling failed: Expected float. Got VALUE_STRING"));
		}
		try {
			schemaHelper4.unmarshal(null);
			fail("Test should throw an exception here");
		}
		catch (Exception e) {
			assertTrue(e.getMessage().equals("AvroTest:0.0.1: object \"null\" Avro unmarshalling failed: String to read from cannot be null!"));
		}
	}

	//@Test
	public void testDoubleUnmarshal() {
		AxContextSchema avroDoubleSchema = new AxContextSchema(new AxArtifactKey("AvroDouble", "0.0.1"), "Avro", "{\"type\": \"double\"}");

		schemas.getSchemasMap().put(avroDoubleSchema .getKey(), avroDoubleSchema);
		SchemaHelper schemaHelper5 = new SchemaHelperFactory().createSchemaHelper(testKey, avroDoubleSchema.getKey());
		
		try {
			schemaHelper5.createNewInstance();
			fail("test should throw an exception here");
		}
		catch (Exception e) {
			assertEquals("AvroTest:0.0.1: could not create an instance of class \"java.lang.Double\" using the default constructor \"Double()\"", e.getMessage());
		}
		assertEquals(1.2345E06, schemaHelper5.createNewInstance("1.2345E06"));
		
		assertEquals(0.0,                   schemaHelper5.unmarshal("0"));
		assertEquals(1.0,                   schemaHelper5.unmarshal("1"));
		assertEquals(-1.0,                  schemaHelper5.unmarshal("-1"));
		assertEquals(1.23,                  schemaHelper5.unmarshal("1.23"));
		assertEquals(-1.23,                 schemaHelper5.unmarshal("-1.23"));
		assertEquals(9.223372036854776E18,  schemaHelper5.unmarshal("9223372036854775807"));
		assertEquals(-9.223372036854776E18, schemaHelper5.unmarshal("-9223372036854775808"));
		assertEquals(9.223372036854776E18,  schemaHelper5.unmarshal("9223372036854775808"));
		assertEquals(-9.223372036854776E18, schemaHelper5.unmarshal("-9223372036854775809"));
		try {
			schemaHelper5.unmarshal("\"Hello\"");
			fail("Test should throw an exception here");
		}
		catch (Exception e) {
			assertTrue(e.getMessage().equals("AvroTest:0.0.1: object \"\"Hello\"\" Avro unmarshalling failed: Expected double. Got VALUE_STRING"));
		}
		try {
			schemaHelper5.unmarshal(null);
			fail("Test should throw an exception here");
		}
		catch (Exception e) {
			assertTrue(e.getMessage().equals("AvroTest:0.0.1: object \"null\" Avro unmarshalling failed: String to read from cannot be null!"));
		}
	}

	@Test
	public void testStringUnmarshal() {
		AxContextSchema avroStringSchema = new AxContextSchema(new AxArtifactKey("AvroString", "0.0.1"), "Avro", "{\"type\": \"string\"}");

		schemas.getSchemasMap().put(avroStringSchema .getKey(), avroStringSchema);
		SchemaHelper schemaHelper7 = new SchemaHelperFactory().createSchemaHelper(testKey, avroStringSchema.getKey());

		assertEquals("",          schemaHelper7.createNewInstance(""));
		assertEquals("1.2345E06", schemaHelper7.createNewInstance("1.2345E06"));
		
		assertEquals("0",                    schemaHelper7.unmarshal("0"));
		assertEquals("1",                    schemaHelper7.unmarshal("1"));
		assertEquals("-1",                   schemaHelper7.unmarshal("-1"));
		assertEquals("1.23",                 schemaHelper7.unmarshal("1.23"));
		assertEquals("-1.23",                schemaHelper7.unmarshal("-1.23"));
		assertEquals("9223372036854775807",  schemaHelper7.unmarshal("9223372036854775807"));
		assertEquals("-9223372036854775808", schemaHelper7.unmarshal("-9223372036854775808"));
		assertEquals("9223372036854775808",  schemaHelper7.unmarshal("9223372036854775808"));
		assertEquals("-9223372036854775809", schemaHelper7.unmarshal("-9223372036854775809"));
		assertEquals("Hello",                schemaHelper7.unmarshal("Hello"));
		assertEquals("Hello",                schemaHelper7.unmarshal(new Utf8("Hello")));
		try {
			schemaHelper7.unmarshal(null);
			fail("Test should throw an exception here");
		}
		catch (Exception e) {
			assertTrue(e.getMessage().equals("AvroTest:0.0.1: object \"null\" Avro unmarshalling failed: String to read from cannot be null!"));
		}
	}

	@Test
	public void testBytesUnmarshal() {
		AxContextSchema avroSchema = new AxContextSchema(new AxArtifactKey("AvroString", "0.0.1"), "Avro", "{\"type\": \"bytes\"}");

		schemas.getSchemasMap().put(avroSchema .getKey(), avroSchema);
		SchemaHelper schemaHelper = new SchemaHelperFactory().createSchemaHelper(testKey, avroSchema.getKey());

		try {
			schemaHelper.createNewInstance();
			fail("test should throw an exception here");
		}
		catch (Exception e) {
			assertEquals("AvroTest:0.0.1: could not create an instance of class \"java.lang.Byte[]\" using the default constructor \"Byte[]()\"", e.getMessage());
		}
		byte[] newBytes = (byte[])schemaHelper.createNewInstance("\"hello\"");
		assertEquals(5, newBytes.length);
		assertEquals(104, newBytes[0]);
		assertEquals(101, newBytes[1]);
		assertEquals(108, newBytes[2]);
		assertEquals(108, newBytes[3]);
		assertEquals(111, newBytes[4]);

		try {
			schemaHelper.unmarshal(null);
			fail("Test should throw an exception here");
		}
		catch (Exception e) {
			assertTrue(e.getMessage().equals("AvroTest:0.0.1: object \"null\" Avro unmarshalling failed: String to read from cannot be null!"));
		}
	}
}
