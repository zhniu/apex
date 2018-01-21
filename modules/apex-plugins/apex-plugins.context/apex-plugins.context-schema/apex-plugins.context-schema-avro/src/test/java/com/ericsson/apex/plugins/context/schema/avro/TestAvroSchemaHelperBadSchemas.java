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
public class TestAvroSchemaHelperBadSchemas {
	private AxKey            testKey = new AxArtifactKey("AvroTest", "0.0.1");
	private AxContextSchemas schemas;

	@Before
	public void initTest() {
		schemas = new AxContextSchemas(new AxArtifactKey("AvroSchemas", "0.0.1"));
		ModelService.registerModel(AxContextSchemas.class, schemas);
		new SchemaParameters().getSchemaHelperParameterMap().put("Avro", new AvroSchemaHelperParameters());
	}

	@Test
	public void badSchemaTest() {
		AxContextSchema avroBadSchema0 = new AxContextSchema(new AxArtifactKey("AvroBad0", "0.0.1"), "Avro", "}");
		schemas.getSchemasMap().put(avroBadSchema0.getKey(), avroBadSchema0);

		try {
			new SchemaHelperFactory().createSchemaHelper(testKey, avroBadSchema0.getKey());
			fail("This test should throw an exception");
		}
		catch (Exception e) {
			assertTrue(e.getMessage().startsWith("AvroTest:0.0.1: avro context schema \"AvroBad0:0.0.1\" schema is invalid"));
		}

		AxContextSchema avroBadSchema1 = new AxContextSchema(new AxArtifactKey("AvroBad1", "0.0.1"), "Avro", "");
		schemas.getSchemasMap().put(avroBadSchema1.getKey(), avroBadSchema1);

		try {
			new SchemaHelperFactory().createSchemaHelper(testKey, avroBadSchema1.getKey());
			fail("This test should throw an exception");
		}
		catch (Exception e) {
			assertTrue(e.getMessage().startsWith("AvroTest:0.0.1: avro context schema \"AvroBad1:0.0.1\" schema is invalid"));
		}
		
		AxContextSchema avroBadSchema2 = new AxContextSchema(new AxArtifactKey("AvroBad2", "0.0.1"), "Avro", "{}");
		schemas.getSchemasMap().put(avroBadSchema2.getKey(), avroBadSchema2);

		try {
			new SchemaHelperFactory().createSchemaHelper(testKey, avroBadSchema2.getKey());
			fail("This test should throw an exception");
		}
		catch (Exception e) {
			assertTrue(e.getMessage().startsWith("AvroTest:0.0.1: avro context schema \"AvroBad2:0.0.1\" schema is invalid"));
		}
		
		AxContextSchema avroBadSchema3 = new AxContextSchema(new AxArtifactKey("AvroBad3", "0.0.1"), "Avro", "{zooby}");
		schemas.getSchemasMap().put(avroBadSchema3.getKey(), avroBadSchema3);

		try {
			new SchemaHelperFactory().createSchemaHelper(testKey, avroBadSchema3.getKey());
			fail("This test should throw an exception");
		}
		catch (Exception e) {
			assertTrue(e.getMessage().startsWith("AvroTest:0.0.1: avro context schema \"AvroBad3:0.0.1\" schema is invalid"));
		}
		
		AxContextSchema avroBadSchema4 = new AxContextSchema(new AxArtifactKey("AvroBad4", "0.0.1"), "Avro", "{\"zooby\"}");
		schemas.getSchemasMap().put(avroBadSchema4.getKey(), avroBadSchema4);

		try {
			new SchemaHelperFactory().createSchemaHelper(testKey, avroBadSchema4.getKey());
			fail("This test should throw an exception");
		}
		catch (Exception e) {
			assertTrue(e.getMessage().startsWith("AvroTest:0.0.1: avro context schema \"AvroBad4:0.0.1\" schema is invalid"));
		}
		
		AxContextSchema avroBadSchema5 = new AxContextSchema(new AxArtifactKey("AvroBad5", "0.0.1"), "Avro", "{\"type\": \"zooby\"}");
		schemas.getSchemasMap().put(avroBadSchema5.getKey(), avroBadSchema5);

		try {
			new SchemaHelperFactory().createSchemaHelper(testKey, avroBadSchema5.getKey());
			fail("This test should throw an exception");
		}
		catch (Exception e) {
			assertTrue(e.getMessage().startsWith("AvroTest:0.0.1: avro context schema \"AvroBad5:0.0.1\" schema is invalid"));
		}
	}
}
