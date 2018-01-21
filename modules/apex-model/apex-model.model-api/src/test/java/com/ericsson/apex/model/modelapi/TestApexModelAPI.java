/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.modelapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ericsson.apex.model.basicmodel.dao.DAOParameters;
import com.ericsson.apex.model.modelapi.impl.ApexModelImpl;
import com.ericsson.apex.model.utilities.TextFileUtils;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestApexModelAPI {
    private Connection connection;

    @Before
    public void setup() throws Exception {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
        connection = DriverManager.getConnection("jdbc:derby:memory:apex_test;create=true");
    }

    @After
    public void teardown() throws Exception {
        connection.close();
        new File("derby.log").delete();
    }

    @Test
    public void testApexModelLoadFromFile() {
        ApexModel apexModel = new ApexModelFactory().createApexModel(null, false);

        ApexAPIResult result = apexModel.loadFromFile("src/main/resources/models/PolicyModel.json");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));

        result = apexModel.loadFromFile("src/test/resources/models/PolicyModel.json");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.deleteModel();
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.loadFromFile("src/test/resources/models/PolicyModel.xml");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.deleteModel();
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.loadFromFile("src/test/resources/models/PolicyModel.junk");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
        assertTrue(result.getMessages().get(0).equals("format of input for Apex concept is neither JSON nor XML"));
    }

    @Test
    public void testApexModelSaveToFile() throws IOException {
        ApexModel apexModel = new ApexModelFactory().createApexModel(null, false);

        ApexAPIResult result = apexModel.loadFromFile("src/test/resources/models/PolicyModel.json");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        File tempJsonModelFile = File.createTempFile("ApexModelTest", ".json");
        result = apexModel.saveToFile(tempJsonModelFile.getCanonicalPath(), false);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        ApexModel jsonApexModel = new ApexModelFactory().createApexModel(null, false);
        result = jsonApexModel.loadFromFile(tempJsonModelFile.getCanonicalPath());
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        tempJsonModelFile.delete();

        File tempXMLModelFile = File.createTempFile("ApexModelTest", ".xml");
        result = apexModel.saveToFile(tempXMLModelFile.getCanonicalPath(), true);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));   

        ApexModel xmlApexModel = new ApexModelFactory().createApexModel(null, false);
        result = xmlApexModel.loadFromFile(tempXMLModelFile.getCanonicalPath());
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));
        tempXMLModelFile.delete();
    }

    @Test
    public void testApexModelDatabase() throws IOException {
        ApexModel apexModel = new ApexModelFactory().createApexModel(null, false);

        ApexAPIResult result = apexModel.loadFromFile("src/test/resources/models/PolicyModel.json");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        DAOParameters daoParameters = new DAOParameters();
        daoParameters.setPluginClass("com.ericsson.apex.model.basicmodel.dao.impl.DefaultApexDao");
        daoParameters.setPersistenceUnit("DAOTest");

        result = apexModel.saveToDatabase(daoParameters);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.deleteModel();
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.loadFromDatabase("PolicyModel", "0.0.1", daoParameters);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.deleteModel();
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.loadFromDatabase("PolicyModel", null, daoParameters);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.deleteModel();
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.loadFromDatabase("VPNPolicyModel", "0.0.1", daoParameters);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
    }

    @Test
    public void testApexModelURL() throws IOException {
        ApexModel apexModel = new ApexModelFactory().createApexModel(null, false);

        ApexAPIResult result = null;

        try {
            result = apexModel.readFromURL(null);
            fail("expecting an IllegalArgumentException");
        }
        catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }

        try {
            result = apexModel.writeToURL(null, true);
            fail("expecting an IllegalArgumentException");
        }
        catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }

        result = apexModel.readFromURL("zooby/looby");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));

        result = apexModel.writeToURL("zooby/looby", true);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));

        result = apexModel.readFromURL("zooby://zooby/looby");
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));

        result = apexModel.writeToURL("zooby://zooby/looby", false);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));

        apexModel = new ApexModelFactory().createApexModel(null, false);

        File tempJsonModelFile = File.createTempFile("ApexModelTest", ".json");
        result = apexModel.saveToFile(tempJsonModelFile.getCanonicalPath(), false);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        String tempFileURLString = tempJsonModelFile.toURI().toString();
        result = apexModel.readFromURL(tempFileURLString);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.SUCCESS));

        result = apexModel.writeToURL(tempFileURLString, false);
        assertTrue(result.getResult().equals(ApexAPIResult.RESULT.FAILED));
        assertTrue(result.getMessages().get(0).equals("protocol doesn't support output"));

        tempJsonModelFile.delete();
    }

    @Test
    public void testApexModelMisc() throws IOException {
        ApexModelImpl apexModelImpl = (ApexModelImpl) new ApexModelFactory().createApexModel(null, false);

        ApexAPIResult result = null;
        
        result = apexModelImpl.getModelKey();
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());
        
        result = apexModelImpl.listModel();
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());
        
        result = apexModelImpl.createModel("ModelName", "0.0.1", null, null);
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());
        
        result = apexModelImpl.updateModel("ModelName", "0.0.1", UUID.randomUUID().toString(), "Model Description");
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());
        
        apexModelImpl.deleteModel();
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());
        
        String modelString = TextFileUtils.getTextFileAsString("src/test/resources/models/PolicyModel.json");
        result = apexModelImpl.loadFromString(modelString);
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());

        File tempFile = File.createTempFile("ApexModel", "json");
        tempFile.deleteOnExit();
        TextFileUtils.putStringAsFile(modelString, tempFile);
        
        apexModelImpl.deleteModel();
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());

        result = apexModelImpl.loadFromFile(tempFile.getCanonicalPath());
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());

        result = apexModelImpl.saveToFile(null, false);
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());
        
        result = apexModelImpl.analyse();
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());
        
        result = apexModelImpl.validate();
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());
        
        result = apexModelImpl.compare(tempFile.getCanonicalPath(), true, true);
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());
        
        result = apexModelImpl.compareWithString(modelString, true, true);
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());
        
        result = apexModelImpl.split("policy");
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());
        
        result = apexModelImpl.split(tempFile.getCanonicalPath(), "policy");
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());
        
        result = apexModelImpl.merge(tempFile.getCanonicalPath(), true);
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());
        
        result = apexModelImpl.mergeWithString(modelString, true);
        System.err.println(result);
        assertEquals(ApexAPIResult.RESULT.SUCCESS, result.getResult());
        
        assertNotEquals(0, apexModelImpl.hashCode());
        assertNotNull(apexModelImpl.clone());
        assertNotNull(apexModelImpl.build());
    }
}
