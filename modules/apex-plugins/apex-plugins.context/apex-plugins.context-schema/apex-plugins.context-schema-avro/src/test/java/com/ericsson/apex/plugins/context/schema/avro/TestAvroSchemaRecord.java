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
 * @version 
 */
public class TestAvroSchemaRecord {
	private AxKey            testKey = new AxArtifactKey("AvroTest", "0.0.1");
	private AxContextSchemas schemas;
	private String           recordSchema;
	private String           recordSchemaVPN;
	private String           recordSchemaVPNReuse;
    private String           recordSchemaInvalidFields;

	@Before
	public void initTest() throws IOException {
		schemas = new AxContextSchemas(new AxArtifactKey("AvroSchemas", "0.0.1"));
		ModelService.registerModel(AxContextSchemas.class, schemas);
		new SchemaParameters().getSchemaHelperParameterMap().put("Avro", new AvroSchemaHelperParameters());
		recordSchema    = TextFileUtils.getTextFileAsString("src/test/resources/avsc/RecordExample.avsc");
		recordSchemaVPN = TextFileUtils.getTextFileAsString("src/test/resources/avsc/RecordExampleVPN.avsc");
		recordSchemaVPNReuse = TextFileUtils.getTextFileAsString("src/test/resources/avsc/RecordExampleVPNReuse.avsc");
        recordSchemaInvalidFields    = TextFileUtils.getTextFileAsString("src/test/resources/avsc/RecordExampleInvalidFields.avsc");
	}

	@Test
	public void testRecordInit() throws IOException {
		AxContextSchema avroSchema = new AxContextSchema(new AxArtifactKey("AvroRecord", "0.0.1"), "Avro", recordSchema);

		schemas.getSchemasMap().put(avroSchema.getKey(), avroSchema);
		SchemaHelper schemaHelper = new SchemaHelperFactory().createSchemaHelper(testKey, avroSchema.getKey());

		GenericRecord newRecordEmpty = (GenericRecord)schemaHelper.createNewInstance();
		assertEquals(null, newRecordEmpty.get("passwordHash"));

		String inString = TextFileUtils.getTextFileAsString("src/test/resources/data/RecordExampleFull.js");
		GenericRecord newRecordFull = (GenericRecord)schemaHelper.createNewInstance(inString);
		assertEquals("gobbledygook", newRecordFull.get("passwordHash").toString());
	}
    
    @Test
    public void testRecordUnmarshalMarshal() throws IOException {
        AxContextSchema avroSchema = new AxContextSchema(new AxArtifactKey("AvroRecord", "0.0.1"), "Avro", recordSchema);

        schemas.getSchemasMap().put(avroSchema.getKey(), avroSchema);
        SchemaHelper schemaHelper = new SchemaHelperFactory().createSchemaHelper(testKey, avroSchema.getKey());

        testUnmarshalMarshal(schemaHelper, "src/test/resources/data/RecordExampleNull.js");
        testUnmarshalMarshal(schemaHelper, "src/test/resources/data/RecordExampleFull.js");
    }
    
    @Test
    public void testRecordUnmarshalMarshalInvalid() throws IOException {
        AxContextSchema avroSchema = new AxContextSchema(new AxArtifactKey("AvroRecord", "0.0.1"), "Avro", recordSchemaInvalidFields);

        schemas.getSchemasMap().put(avroSchema.getKey(), avroSchema);
        SchemaHelper schemaHelper = new SchemaHelperFactory().createSchemaHelper(testKey, avroSchema.getKey());

        testUnmarshalMarshal(schemaHelper, "src/test/resources/data/RecordExampleInvalidFields.js");
    }

	@Test
	public void testVPNRecordUnmarshalMarshal() throws IOException {
		AxContextSchema avroSchema = new AxContextSchema(new AxArtifactKey("AvroRecord", "0.0.1"), "Avro", recordSchemaVPN);

		schemas.getSchemasMap().put(avroSchema.getKey(), avroSchema);
		SchemaHelper schemaHelper = new SchemaHelperFactory().createSchemaHelper(testKey, avroSchema.getKey());

		testUnmarshalMarshal(schemaHelper, "src/test/resources/data/RecordExampleVPNFull.js");
	}

	@Test
	public void testVPNRecordReuse() throws IOException {
		AxContextSchema avroSchema = new AxContextSchema(new AxArtifactKey("AvroRecord", "0.0.1"), "Avro", recordSchemaVPNReuse);
		schemas.getSchemasMap().put(avroSchema.getKey(), avroSchema);

		schemas.getSchemasMap().put(avroSchema.getKey(), avroSchema);
		new SchemaHelperFactory().createSchemaHelper(testKey, avroSchema.getKey());
	}

	private void testUnmarshalMarshal(final SchemaHelper schemaHelper, final String fileName) throws IOException {
		String inString = TextFileUtils.getTextFileAsString(fileName);
		GenericRecord decodedObject = (GenericRecord)schemaHelper.unmarshal(inString);
		String outString = schemaHelper.marshal2Json(decodedObject);
		assertEquals(inString.replaceAll("\\s+",""), outString.replaceAll("\\s+",""));
	}
}
