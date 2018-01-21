/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.services.client.editor.rest.bean;

import javax.xml.bind.annotation.XmlType;

/**
 * The Event Bean.
 */
@XmlType
public class BeanFake extends BeanBase {

    private String name = null, version = null, field1 = null;
    private int field2 = 0, field3 = 0;

    public String getName() {
        field1 = name;
        return field1;
    }

    public String getVersion() {
        return version;
    }
    
    public int getField2() {
        field3 = field2;
        return field3;
    }
}
