/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.policymodel.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.ericsson.apex.model.basicmodel.concepts.AxValidationResult;
import com.ericsson.apex.model.basicmodel.handling.ApexModelException;
import com.ericsson.apex.model.basicmodel.handling.ApexModelReader;
import com.ericsson.apex.model.policymodel.concepts.AxPolicyModel;

public class PolicyModelValidator {
    public static void main (String[] args) throws ApexModelException, FileNotFoundException {
        ApexModelReader<AxPolicyModel> policyModelReader = new ApexModelReader<AxPolicyModel
                >(AxPolicyModel.class);


        AxPolicyModel policyModel =  policyModelReader.read(new FileInputStream(args[0]));
        AxValidationResult result = policyModel.validate(new AxValidationResult());
        System.out.println(result);
    }
}
