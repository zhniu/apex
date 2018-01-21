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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Properties;

import org.junit.Test;

import com.ericsson.apex.model.basicmodel.dao.converters.CDATAConditioner;
import com.ericsson.apex.model.basicmodel.dao.converters.UUID2String;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestDaoMisc {

    @Test
    public void testUUID2StringMopUp() {
        UUID2String uuid2String = new UUID2String();
        assertEquals("", uuid2String.convertToDatabaseColumn(null));
    }

    @Test
    public void testCDataConditionerMopUp() {
        assertNull(CDATAConditioner.clean(null));
    }
    
    @Test
    public void testDaoFactory() {
        DAOParameters daoParameters = new DAOParameters();

        daoParameters.setPluginClass("somewhere.over.the.rainbow");
        try {
            new ApexDaoFactory().createApexDao(daoParameters);
            fail("test shold throw an exception here");
        }
        catch (Exception e) {
            assertEquals("Apex DAO class not found for DAO plugin \"somewhere.over.the.rainbow\"", e.getMessage());
        }
        
        daoParameters.setPluginClass("java.lang.String");
        try {
            new ApexDaoFactory().createApexDao(daoParameters);
            fail("test shold throw an exception here");
        }
        catch (Exception e) {
            assertEquals("Specified Apex DAO plugin class \"java.lang.String\" does not implement the ApexDao interface", e.getMessage());
        }
    }
    
    @Test
    public void testDaoParameters() {
        DAOParameters pars = new DAOParameters();
        pars.setJdbcProperties(new Properties());
        assertEquals(0, pars.getJdbcProperties().size());
        
        pars.setJdbcProperty("name", "Dorothy");
        assertEquals("Dorothy", pars.getJdbcProperty("name"));
        
        pars.setPersistenceUnit("Kansas");
        assertEquals("Kansas", pars.getPersistenceUnit());
        
        pars.setPluginClass("somewhere.over.the.rainbow");
        assertEquals("somewhere.over.the.rainbow", pars.getPluginClass());
        
        assertEquals("DAOParameters [pluginClass=somewhere.over.the.rainbow, persistenceUnit=Kansas, jdbcProperties={name=Dorothy}]", pars.toString());
    }
}
