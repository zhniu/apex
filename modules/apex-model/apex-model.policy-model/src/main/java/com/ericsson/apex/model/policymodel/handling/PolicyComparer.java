/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.policymodel.handling;

import com.ericsson.apex.model.basicmodel.concepts.AxArtifactKey;
import com.ericsson.apex.model.policymodel.concepts.AxPolicies;
import com.ericsson.apex.model.policymodel.concepts.AxPolicy;
import com.ericsson.apex.model.utilities.comparison.KeyedMapComparer;
import com.ericsson.apex.model.utilities.comparison.KeyedMapDifference;

/**
 * This class compares the policies in two {@link AxPolicies} objects and returns the differences.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class PolicyComparer {
    /**
     * Compare two {@link AxPolicies} objects, comparing their policy maps one after another.
     *
     * @param left the left policies
     * @param right the right policies
     * @return the difference
     */
    public KeyedMapDifference<AxArtifactKey, AxPolicy> compare(final AxPolicies left, final AxPolicies right) {
        // Find the difference between the AxPolicy objects
        return new KeyedMapComparer<AxArtifactKey, AxPolicy>().compareMaps(left.getPolicyMap(), right.getPolicyMap());
    }
}
