/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.core.engine.engine.impl;

import com.ericsson.apex.core.engine.engine.ApexEngine;
import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;

/**
 * A factory class to create APEX engines of a given type. As there is only a single type of Apex engine in existence, this class is trivial.
 *
 * @author Liam Fallon
 */
public class ApexEngineFactory {

    /**
     * Create an Apex engine implementation.
     *
     * @param key the key
     * @return the apex engine
     */
    public ApexEngine createApexEngine(final AxArtifactKey key) {
        return new ApexEngineImpl(key);
    }
}
