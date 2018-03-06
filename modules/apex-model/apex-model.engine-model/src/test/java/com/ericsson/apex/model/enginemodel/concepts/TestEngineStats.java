/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.enginemodel.concepts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult.ValidationResult;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestEngineStats {

    @Test
    public void testEngineStats() {
        assertNotNull(new AxEngineStats());
        assertNotNull(new AxEngineStats(new AxReferenceKey()));
        
        AxReferenceKey statsKey = new AxReferenceKey("EngineKey", "0.0.1", "EngineStats");
        AxEngineStats stats = new AxEngineStats(statsKey);
        
        try {
            stats.setKey(null);
            fail("test should throw an exception here");
        }
        catch (Exception e) {
            assertEquals("key may not be null", e.getMessage());
        }

        stats.setKey(statsKey);
        assertEquals("EngineKey:0.0.1:NULL:EngineStats", stats.getKey().getID());
        assertEquals("EngineKey:0.0.1:NULL:EngineStats", stats.getKeys().get(0).getID());

        stats.setAverageExecutionTime(123.45);
        assertEquals(new Double(123.45), new Double(stats.getAverageExecutionTime()));
        
        stats.setEventCount(987);
        assertEquals(987, stats.getEventCount());
        
        long lastExecutionTime = System.currentTimeMillis();
        stats.setLastExecutionTime(lastExecutionTime);
        assertEquals(lastExecutionTime, stats.getLastExecutionTime());
        
        long timestamp = System.currentTimeMillis();
        stats.setTimeStamp(timestamp);
        assertEquals(timestamp, stats.getTimeStamp());
        assertNotNull(stats.getTimeStampString());
        
        long upTime = System.currentTimeMillis() - timestamp;
        stats.setUpTime(upTime);
        assertEquals(upTime, stats.getUpTime());
        
        stats.engineStart();
        assertTrue(stats.getUpTime() > -1);
        stats.engineStop();
        assertTrue(stats.getUpTime() >= 0);

        stats.engineStop();

        stats.reset();
        
        stats.setEventCount(-2);
        stats.executionEnter(new AxArtifactKey());
        assertEquals(2, stats.getEventCount());
        
        stats.setEventCount(10);
        stats.executionEnter(new AxArtifactKey());
        assertEquals(11, stats.getEventCount());
        
        stats.reset();
        stats.engineStart();
        stats.setEventCount(4);
        stats.executionEnter(new AxArtifactKey());
        try {
            Thread.sleep(10);
        }
        catch (Exception e) {
            fail("test should not throw an exeption");
        }
        stats.executionExit();
        double avExecutionTime = stats.getAverageExecutionTime();
        System.err.println(avExecutionTime);
        assertTrue(avExecutionTime >= 2.0 && avExecutionTime < 3.0);
        stats.engineStop();
        
        AxValidationResult result = new AxValidationResult();
        result = stats.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());
        
        stats.setKey(new AxReferenceKey());
        result = new AxValidationResult();
        result = stats.validate(result);
        assertEquals(ValidationResult.INVALID, result.getValidationResult());
        
        stats.setKey(statsKey);
        result = new AxValidationResult();
        result = stats.validate(result);
        assertEquals(ValidationResult.VALID, result.getValidationResult());
        
        stats.clean();
        stats.reset();

        AxEngineStats clonedStats = new AxEngineStats(stats);
        assertEquals("AxEngineStats:(engineKey=AxReferenceKey:(parentKey", clonedStats.toString().substring(0, 50));

        assertNotNull(stats.getKeys());

        assertFalse(stats.hashCode() == 0);

        assertTrue(stats.equals(stats));
        assertTrue(stats.equals(clonedStats));
        assertFalse(stats.equals(null));
        assertFalse(stats.equals("Hello"));
        assertFalse(stats.equals(new AxEngineStats(new AxReferenceKey())));

        assertEquals(0, stats.compareTo(stats));
        assertEquals(0, stats.compareTo(clonedStats));
        assertNotEquals(0, stats.compareTo(new AxArtifactKey()));
        assertNotEquals(0, stats.compareTo(null));
        assertNotEquals(0, stats.compareTo(new AxEngineStats(new AxReferenceKey())));

        stats.setTimeStamp(1);
        assertFalse(stats.equals(new AxEngineStats(statsKey)));
        assertNotEquals(0, stats.compareTo(new AxEngineStats(statsKey)));
        stats.setTimeStamp(0);
        assertTrue(stats.equals(new AxEngineStats(statsKey)));
        assertEquals(0, stats.compareTo(new AxEngineStats(statsKey)));

        stats.setEventCount(1);
        assertFalse(stats.equals(new AxEngineStats(statsKey)));
        assertNotEquals(0, stats.compareTo(new AxEngineStats(statsKey)));
        stats.setEventCount(0);
        assertTrue(stats.equals(new AxEngineStats(statsKey)));
        assertEquals(0, stats.compareTo(new AxEngineStats(statsKey)));

        stats.setLastExecutionTime(1);
        assertFalse(stats.equals(new AxEngineStats(statsKey)));
        assertNotEquals(0, stats.compareTo(new AxEngineStats(statsKey)));
        stats.setLastExecutionTime(0);
        assertTrue(stats.equals(new AxEngineStats(statsKey)));
        assertEquals(0, stats.compareTo(new AxEngineStats(statsKey)));

        stats.setAverageExecutionTime(1);
        assertFalse(stats.equals(new AxEngineStats(statsKey)));
        assertNotEquals(0, stats.compareTo(new AxEngineStats(statsKey)));
        stats.setAverageExecutionTime(0);
        assertTrue(stats.equals(new AxEngineStats(statsKey)));
        assertEquals(0, stats.compareTo(new AxEngineStats(statsKey)));

        stats.setUpTime(1);
        assertFalse(stats.equals(new AxEngineStats(statsKey)));
        assertNotEquals(0, stats.compareTo(new AxEngineStats(statsKey)));
        stats.setUpTime(0);
        assertTrue(stats.equals(new AxEngineStats(statsKey)));
        assertEquals(0, stats.compareTo(new AxEngineStats(statsKey)));

        stats.engineStart();
        assertFalse(stats.equals(new AxEngineStats(statsKey)));
        AxEngineStats newStats = new AxEngineStats(statsKey);
        newStats.setTimeStamp(stats.getTimeStamp());
        assertFalse(stats.equals(newStats));
        assertNotEquals(0, stats.compareTo(newStats));
        stats.engineStop();
        stats.reset();
        assertTrue(stats.equals(new AxEngineStats(statsKey)));
        assertEquals(0, stats.compareTo(new AxEngineStats(statsKey)));
    }
}
