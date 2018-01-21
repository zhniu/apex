/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.basicmodel.handling;

import java.util.ArrayList;
import java.util.List;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.basicmodel.concepts.AxKey;
import com.ericsson.apex.model.basicmodel.concepts.AxModel;
import com.ericsson.apex.model.basicmodel.concepts.AxReferenceKey;

public class AxModelWithReferences extends AxModel {
    private static final long serialVersionUID = -8194956638511120008L;

    private List<AxKey> extraKeyList = new ArrayList<>();
    
    public AxModelWithReferences(final AxArtifactKey key) {
        super(key);
    }
    
    @Override
    public List<AxKey> getKeys() {
        List<AxKey> keys = super.getKeys();
        keys.addAll(extraKeyList);

        return keys;
    }

    public List<AxKey> getExtraKeyList() {
        return extraKeyList;
    }

    public void setReferenceKeyList() {
        List<AxKey> keys = super.getKeys();
        
        for (AxKey key: keys) {
            AxArtifactKey aKey = (AxArtifactKey)key;
            AxReferenceKey keyRef = new AxReferenceKey(aKey, aKey.getName());
            extraKeyList.add(keyRef);
        }
    }
    
    public void addKey(final AxKey aKey) {
        extraKeyList.add(aKey);
    }
    
    public void removeKey(final AxKey aKey) {
        extraKeyList.remove(aKey);
    }
}
