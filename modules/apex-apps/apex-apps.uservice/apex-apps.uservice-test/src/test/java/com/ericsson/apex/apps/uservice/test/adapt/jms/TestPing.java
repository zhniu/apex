/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.apps.uservice.test.adapt.jms;

import java.io.Serializable;

import com.ericsson.apex.service.engine.event.ApexEventException;

/**
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class TestPing implements Serializable {
    private static final long serialVersionUID = -3400711508992955886L;

    private String name = "Rose";
    private String description = "A rose by any other name would smell as sweet";
    private long pingTime = System.currentTimeMillis();
    private long pongTime = -1;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public long getPingTime() {
        return pingTime;
    }
    public void setPingTime(long pingTime) {
        this.pingTime = pingTime;
    }
    public long getPongTime() {
        return pongTime;
    }
    public void setPongTime(long pongTime) {
        this.pongTime = pongTime;
    }
    @Override
    public String toString() {
        return "TestPing [name=" + name + ", description=" + description + ", pingTime=" + pingTime + ", pongTime=" + pongTime + "]";
    }
    
    public void verify() throws ApexEventException {
        if (!name.startsWith("Rose")) {
            throw new ApexEventException("TestPing is not valid");
        }
        
        if (name.length() <= 4) {
            throw new ApexEventException("TestPing is not valid");
        }
        
        if (!description.startsWith("A rose by any other name would smell as sweet")) {
            throw new ApexEventException("TestPing is not valid");
        }
        
        if (description.length() <= 44) {
            throw new ApexEventException("TestPing is not valid");
        }
        
        if (pongTime <= pingTime) {
            throw new ApexEventException("TestPing is not valid");
        }
    }
}
