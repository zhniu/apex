/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.plugins.executor.testdomain.model;

import com.ericsson.apex.model.basicmodel.concepts.ApexException;
import com.ericsson.apex.model.basicmodel.handling.ApexModelSaver;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;

/**
 * This class saves sample domain models to disk in XML and JSON format.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public final class SampleDomainModelSaver {
    /**
     * Private default constructor to prevent subclassing.
     */
    private SampleDomainModelSaver() {
    }

    /**
     * Write the sample Models to args[0].
     *
     * @param args Not used
     * @throws ApexException the apex exception
     */
    public static void main(final String[] args) throws ApexException {
        if (args.length != 1) {
            System.err.println("usage: " + SampleDomainModelSaver.class.getCanonicalName() + " modelDirectory");
            return;
        }

        // Save Java model
        AxPolicyModel javaPolicyModel = new SampleDomainModelFactory().getSamplePolicyModel("JAVA");
        ApexModelSaver<AxPolicyModel> javaModelSaver = new ApexModelSaver<AxPolicyModel>(AxPolicyModel.class, javaPolicyModel, args[0]);
        javaModelSaver.apexModelWriteJSON();
        javaModelSaver.apexModelWriteXML();

        // Save Javascript model
        AxPolicyModel javascriptPolicyModel = new SampleDomainModelFactory().getSamplePolicyModel("JAVASCRIPT");
        ApexModelSaver<AxPolicyModel> javascriptModelSaver = new ApexModelSaver<AxPolicyModel>(AxPolicyModel.class, javascriptPolicyModel, args[0]);
        javascriptModelSaver.apexModelWriteJSON();
        javascriptModelSaver.apexModelWriteXML();

        // Save JRuby model
        AxPolicyModel jRubyPolicyModel = new SampleDomainModelFactory().getSamplePolicyModel("JRUBY");
        ApexModelSaver<AxPolicyModel> jRubyModelSaver = new ApexModelSaver<AxPolicyModel>(AxPolicyModel.class, jRubyPolicyModel, args[0]);
        jRubyModelSaver.apexModelWriteJSON();
        jRubyModelSaver.apexModelWriteXML();

        // Save Jython model
        AxPolicyModel jythonPolicyModel = new SampleDomainModelFactory().getSamplePolicyModel("JYTHON");
        ApexModelSaver<AxPolicyModel> jythonModelSaver = new ApexModelSaver<AxPolicyModel>(AxPolicyModel.class, jythonPolicyModel, args[0]);
        jythonModelSaver.apexModelWriteJSON();
        jythonModelSaver.apexModelWriteXML();

        // Save MVEL model
        AxPolicyModel mvelPolicyModel = new SampleDomainModelFactory().getSamplePolicyModel("MVEL");
        ApexModelSaver<AxPolicyModel> mvelModelSaver = new ApexModelSaver<AxPolicyModel>(AxPolicyModel.class, mvelPolicyModel, args[0]);
        mvelModelSaver.apexModelWriteJSON();
        mvelModelSaver.apexModelWriteXML();
    }

}
