/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.apps.uservice.test.adapt.sampletypes;

import java.util.LinkedHashMap;
import java.util.Map;

public class FooMap extends LinkedHashMap<String,String>{
    private static final long serialVersionUID = -7125986379378753022L;

    public FooMap() {
        super();
    }

    public FooMap(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }

    public FooMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public FooMap(int initialCapacity) {
        super(initialCapacity);
    }

    public FooMap(Map<? extends String, ? extends String> m) {
        super(m);
    }
}
