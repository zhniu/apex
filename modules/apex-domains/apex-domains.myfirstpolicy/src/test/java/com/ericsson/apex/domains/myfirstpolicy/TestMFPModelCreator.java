/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.myfirstpolicy;

import com.ericsson.apex.domains.myfirstpolicy.model.MFPDomainModelFactory;
import com.ericsson.apex.model.basicmodel.test.TestApexModelCreator;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;

/**
 * Create the MyFirstPolicyModel - base class.
 *
 * @author John Keeney (John.Keeney@ericsson.com)
 */
public abstract class TestMFPModelCreator implements TestApexModelCreator<AxPolicyModel> {

    /* (non-Javadoc)
     * @see com.ericsson.apex.model.basicmodel.test.TestApexModelCreator#getMalstructuredModel()
     */
    @Override
    public AxPolicyModel getMalstructuredModel() {
        return null;
    }

    /* (non-Javadoc)
     * @see com.ericsson.apex.model.basicmodel.test.TestApexModelCreator#getObservationModel()
     */
    @Override
    public AxPolicyModel getObservationModel() {
        return null;
    }

    /* (non-Javadoc)
     * @see com.ericsson.apex.model.basicmodel.test.TestApexModelCreator#getWarningModel()
     */
    @Override
    public AxPolicyModel getWarningModel() {
        return getModel();
    }

    /* (non-Javadoc)
     * @see com.ericsson.apex.model.basicmodel.test.TestApexModelCreator#getInvalidModel()
     */
    @Override
    public AxPolicyModel getInvalidModel() {
        return null;
    }

    /**
     *  Create the MyFirstPolicyModel #1.
     */
    public static class TestMFP1ModelCreator extends TestMFPModelCreator {

        /* (non-Javadoc)
         * @see com.ericsson.apex.model.basicmodel.handling.ApexModelCreator#getModel()
         */
        @Override
        public AxPolicyModel getModel() {
            return new MFPDomainModelFactory().getMFP1PolicyModel();
        }
    }

    /**
     * Create the MyFirstPolicyModel#2.
     */
    public static class TestMFP2ModelCreator extends TestMFPModelCreator {

        /* (non-Javadoc)
         * @see com.ericsson.apex.model.basicmodel.handling.ApexModelCreator#getModel()
         */
        @Override
        public AxPolicyModel getModel() {
            return new MFPDomainModelFactory().getMFP2PolicyModel();
        }
    }

}
