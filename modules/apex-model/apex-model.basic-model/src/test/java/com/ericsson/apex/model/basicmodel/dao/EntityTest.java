/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.basicmodel.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxKeyInfo;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;
import com.ericsson.apex.model.basicmodel.concepts.TestEntity;

/**
 * JUnit test class
 */
public class EntityTest {
    private Connection connection;
    private ApexDao apexDao;

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
    public void testEntityTestSanity() throws ApexException {
        DAOParameters daoParameters = new DAOParameters();

        apexDao = new ApexDaoFactory().createApexDao(daoParameters);

        try {
            apexDao.init(null);
            fail("Test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("Apex persistence unit parameter not set", e.getMessage());
        }

        try {
            apexDao.init(daoParameters);
            fail("Test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("Apex persistence unit parameter not set", e.getMessage());
        }

        daoParameters.setPluginClass("somewhere.over.the.rainbow");
        daoParameters.setPersistenceUnit("Dorothy");
        try {
            apexDao.init(daoParameters);
            fail("Test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("Creation of Apex persistence unit \"Dorothy\" failed", e.getMessage());
        }
        try {
            apexDao.create(new AxArtifactKey());
            fail("Test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("Apex DAO has not been initialized", e.getMessage());
        }
        apexDao.close();
    }

    @Test
    public void testEntityTestAllOpsJPA() throws ApexException {
        DAOParameters daoParameters = new DAOParameters();
        daoParameters.setPluginClass("com.ericsson.apex.model.basicmodel.dao.impl.DefaultApexDao");
        daoParameters.setPersistenceUnit("DAOTest");

        apexDao = new ApexDaoFactory().createApexDao(daoParameters);
        apexDao.init(daoParameters);

        testAllOps();
        apexDao.close();
    }

    @Test
    public void testEntityTestBadVals() throws ApexException {
        DAOParameters daoParameters = new DAOParameters();
        daoParameters.setPluginClass("com.ericsson.apex.model.basicmodel.dao.impl.DefaultApexDao");
        daoParameters.setPersistenceUnit("DAOTest");

        apexDao = new ApexDaoFactory().createApexDao(daoParameters);
        apexDao.init(daoParameters);

        AxArtifactKey nullKey = null;
        AxReferenceKey nullRefKey = null;
        List<AxArtifactKey> nullKeyList = null;
        List<AxArtifactKey> emptyKeyList = new ArrayList<>();
        List<AxReferenceKey> nullRKeyList = null;
        List<AxReferenceKey> emptyRKeyList = new ArrayList<>();

        apexDao.create(nullKey);
        apexDao.create(nullKeyList);
        apexDao.create(emptyKeyList);

        apexDao.delete(nullKey);
        apexDao.delete(nullKeyList);
        apexDao.delete(emptyKeyList);
        apexDao.delete(AxArtifactKey.class, nullKey);
        apexDao.delete(AxReferenceKey.class, nullRefKey);
        apexDao.deleteByArtifactKey(AxArtifactKey.class, nullKeyList);
        apexDao.deleteByArtifactKey(AxArtifactKey.class, emptyKeyList);
        apexDao.deleteByReferenceKey(AxReferenceKey.class, nullRKeyList);
        apexDao.deleteByReferenceKey(AxReferenceKey.class, emptyRKeyList);

        apexDao.get(null, nullKey);
        apexDao.get(null, nullRefKey);
        apexDao.getAll(null);
        apexDao.getAll(null, nullKey);
        apexDao.getArtifact(null, nullKey);
        apexDao.getArtifact(AxArtifactKey.class, nullKey);
        apexDao.getArtifact(null, nullRefKey);
        apexDao.getArtifact(AxReferenceKey.class, nullRefKey);
        apexDao.size(null);

        apexDao.close();
    }

    private void testAllOps(){
        AxArtifactKey  aKey0    = new AxArtifactKey("A-KEY0", "0.0.1");
        AxArtifactKey  aKey1    = new AxArtifactKey("A-KEY1", "0.0.1");
        AxArtifactKey  aKey2    = new AxArtifactKey("A-KEY2", "0.0.1");
        AxKeyInfo      keyInfo0 = new AxKeyInfo(aKey0, UUID.fromString("00000000-0000-0000-0000-000000000000"), "key description 0");
        AxKeyInfo      keyInfo1 = new AxKeyInfo(aKey1, UUID.fromString("00000000-0000-0000-0000-000000000001"), "key description 1");
        AxKeyInfo      keyInfo2 = new AxKeyInfo(aKey2, UUID.fromString("00000000-0000-0000-0000-000000000002"), "key description 2");

        apexDao.create(keyInfo0);

        AxKeyInfo keyInfoBack0 = apexDao.get(AxKeyInfo.class, aKey0);
        assertTrue(keyInfo0.equals(keyInfoBack0));

        AxKeyInfo keyInfoBackNull = apexDao.get(AxKeyInfo.class, AxArtifactKey.getNullKey());
        assertNull(keyInfoBackNull);

        AxKeyInfo keyInfoBack1 = apexDao.getArtifact(AxKeyInfo.class, aKey0);
        assertTrue(keyInfoBack0.equals(keyInfoBack1));

        AxKeyInfo keyInfoBack2 = apexDao.getArtifact(AxKeyInfo.class,  new AxArtifactKey("A-KEY3", "0.0.1"));
        assertNull(keyInfoBack2);

        Set<AxKeyInfo> keyInfoSetIn = new TreeSet<AxKeyInfo>();
        keyInfoSetIn.add(keyInfo1);
        keyInfoSetIn.add(keyInfo2);

        apexDao.create(keyInfoSetIn);

        Set<AxKeyInfo> keyInfoSetOut = new TreeSet<AxKeyInfo>(apexDao.getAll(AxKeyInfo.class));

        keyInfoSetIn.add(keyInfo0);
        assertTrue(keyInfoSetIn.equals(keyInfoSetOut));

        apexDao.delete(keyInfo1);
        keyInfoSetIn.remove(keyInfo1);
        keyInfoSetOut = new TreeSet<AxKeyInfo>(apexDao.getAll(AxKeyInfo.class));
        assertTrue(keyInfoSetIn.equals(keyInfoSetOut));

        apexDao.delete(keyInfoSetIn);
        keyInfoSetOut = new TreeSet<AxKeyInfo>(apexDao.getAll(AxKeyInfo.class));
        assertEquals(0, keyInfoSetOut.size());

        keyInfoSetIn.add(keyInfo0);
        keyInfoSetIn.add(keyInfo1);
        keyInfoSetIn.add(keyInfo0);
        apexDao.create(keyInfoSetIn);
        keyInfoSetOut = new TreeSet<AxKeyInfo>(apexDao.getAll(AxKeyInfo.class));
        assertTrue(keyInfoSetIn.equals(keyInfoSetOut));

        apexDao.delete(AxKeyInfo.class, aKey0);
        keyInfoSetOut = new TreeSet<AxKeyInfo>(apexDao.getAll(AxKeyInfo.class));
        assertEquals(2, keyInfoSetOut.size());
        assertEquals(2, apexDao.size(AxKeyInfo.class));

        Set<AxArtifactKey> keySetIn = new TreeSet<AxArtifactKey>();
        keySetIn.add(aKey1);
        keySetIn.add(aKey2);

        int deletedCount = apexDao.deleteByArtifactKey(AxKeyInfo.class, keySetIn);
        assertEquals(2, deletedCount);

        keyInfoSetOut = new TreeSet<AxKeyInfo>(apexDao.getAll(AxKeyInfo.class));
        assertEquals(0, keyInfoSetOut.size());

        keyInfoSetIn.add(keyInfo0);
        keyInfoSetIn.add(keyInfo1);
        keyInfoSetIn.add(keyInfo0);
        apexDao.create(keyInfoSetIn);
        keyInfoSetOut = new TreeSet<AxKeyInfo>(apexDao.getAll(AxKeyInfo.class));
        assertTrue(keyInfoSetIn.equals(keyInfoSetOut));

        apexDao.deleteAll(AxKeyInfo.class);
        assertEquals(0, apexDao.size(AxKeyInfo.class));

        AxArtifactKey owner0Key = new AxArtifactKey("Owner0", "0.0.1");
        AxArtifactKey owner1Key = new AxArtifactKey("Owner1", "0.0.1");
        AxArtifactKey owner2Key = new AxArtifactKey("Owner2", "0.0.1");
        AxArtifactKey owner3Key = new AxArtifactKey("Owner3", "0.0.1");
        AxArtifactKey owner4Key = new AxArtifactKey("Owner4", "0.0.1");
        AxArtifactKey owner5Key = new AxArtifactKey("Owner5", "0.0.1");

        apexDao.create(new TestEntity(new AxReferenceKey(owner0Key, "Entity0"), 100.0));
        apexDao.create(new TestEntity(new AxReferenceKey(owner0Key, "Entity1"), 101.0));
        apexDao.create(new TestEntity(new AxReferenceKey(owner0Key, "Entity2"), 102.0));
        apexDao.create(new TestEntity(new AxReferenceKey(owner0Key, "Entity3"), 103.0));
        apexDao.create(new TestEntity(new AxReferenceKey(owner0Key, "Entity4"), 104.0));
        apexDao.create(new TestEntity(new AxReferenceKey(owner1Key, "Entity5"), 105.0));
        apexDao.create(new TestEntity(new AxReferenceKey(owner1Key, "Entity6"), 106.0));
        apexDao.create(new TestEntity(new AxReferenceKey(owner1Key, "Entity7"), 107.0));
        apexDao.create(new TestEntity(new AxReferenceKey(owner2Key, "Entity8"), 108.0));
        apexDao.create(new TestEntity(new AxReferenceKey(owner2Key, "Entity9"), 109.0));
        apexDao.create(new TestEntity(new AxReferenceKey(owner3Key, "EntityA"), 110.0));
        apexDao.create(new TestEntity(new AxReferenceKey(owner4Key, "EntityB"), 111.0));
        apexDao.create(new TestEntity(new AxReferenceKey(owner5Key, "EntityC"), 112.0));
        apexDao.create(new TestEntity(new AxReferenceKey(owner5Key, "EntityD"), 113.0));
        apexDao.create(new TestEntity(new AxReferenceKey(owner5Key, "EntityE"), 114.0));
        apexDao.create(new TestEntity(new AxReferenceKey(owner5Key, "EntityF"), 115.0));

        TreeSet<TestEntity> testEntitySetOut = new TreeSet<TestEntity>(apexDao.getAll(TestEntity.class));
        assertEquals(16, testEntitySetOut.size());

        testEntitySetOut = new TreeSet<TestEntity>(apexDao.getAll(TestEntity.class, owner0Key));
        assertEquals(5, testEntitySetOut.size());

        testEntitySetOut = new TreeSet<TestEntity>(apexDao.getAll(TestEntity.class, owner1Key));
        assertEquals(3, testEntitySetOut.size());

        testEntitySetOut = new TreeSet<TestEntity>(apexDao.getAll(TestEntity.class, owner2Key));
        assertEquals(2, testEntitySetOut.size());

        testEntitySetOut = new TreeSet<TestEntity>(apexDao.getAll(TestEntity.class, owner3Key));
        assertEquals(1, testEntitySetOut.size());

        testEntitySetOut = new TreeSet<TestEntity>(apexDao.getAll(TestEntity.class, owner4Key));
        assertEquals(1, testEntitySetOut.size());

        testEntitySetOut = new TreeSet<TestEntity>(apexDao.getAll(TestEntity.class, owner5Key));
        assertEquals(4, testEntitySetOut.size());

        assertNotNull(apexDao.get(TestEntity.class, new AxReferenceKey(owner0Key, "Entity0")));
        assertNotNull(apexDao.getArtifact(TestEntity.class, new AxReferenceKey(owner0Key, "Entity0")));
        assertNull(apexDao.get(TestEntity.class, new AxReferenceKey(owner0Key, "Entity1000")));
        assertNull(apexDao.getArtifact(TestEntity.class, new AxReferenceKey(owner0Key, "Entity1000")));
        apexDao.delete(TestEntity.class, new AxReferenceKey(owner0Key, "Entity0"));

        Set<AxReferenceKey> rKeySetIn = new TreeSet<AxReferenceKey>();
        rKeySetIn.add(new AxReferenceKey(owner4Key, "EntityB"));
        rKeySetIn.add(new AxReferenceKey(owner5Key, "EntityD"));

        int deletedRCount = apexDao.deleteByReferenceKey(TestEntity.class, rKeySetIn);
        assertEquals(2, deletedRCount);

        apexDao.update(new TestEntity(new AxReferenceKey(owner5Key, "EntityF"), 120.0));
    }
}
