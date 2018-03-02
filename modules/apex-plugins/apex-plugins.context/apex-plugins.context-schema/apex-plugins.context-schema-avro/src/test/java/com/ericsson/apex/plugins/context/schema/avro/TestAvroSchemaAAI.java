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
public class TestAvroSchemaAAI {
	private AxKey            testKey = new AxArtifactKey("AvroTest", "0.0.1");
	private AxContextSchemas schemas;
	private String           aaiInventoryResponseSchema;

	@Before
	public void initTest() throws IOException {
		schemas = new AxContextSchemas(new AxArtifactKey("AvroSchemas", "0.0.1"));
		ModelService.registerModel(AxContextSchemas.class, schemas);
		new SchemaParameters().getSchemaHelperParameterMap().put("Avro", new AvroSchemaHelperParameters());
		aaiInventoryResponseSchema    = TextFileUtils.getTextFileAsString("src/test/resources/avsc/AAIInventoryResponseItemType.avsc");
	}

	@Test
	public void testAAIResponsePolicy() throws IOException {
		AxContextSchema avroSchema = new AxContextSchema(new AxArtifactKey("AvroRecord", "0.0.1"), "Avro", aaiInventoryResponseSchema);

		schemas.getSchemasMap().put(avroSchema.getKey(), avroSchema);
		SchemaHelper schemaHelper = new SchemaHelperFactory().createSchemaHelper(testKey, avroSchema.getKey());

		Array<?> newArrayEmpty = (Array<?>)schemaHelper.createNewInstance();
		assertEquals(0, newArrayEmpty.size());

		String inString = TextFileUtils.getTextFileAsString("src/test/resources/data/AAIResponse4Policy.json");
		Array<?> newArrayFull = (Array<?>)schemaHelper.createNewInstance(inString);
		String vnfName = ((GenericRecord) ((GenericRecord)newArrayFull.get(0)).get("generic_DasH_vnf")).get("vnf_DasH_name").toString();
		assertEquals("ZRDM2MMEX39", vnfName);
	}
}
