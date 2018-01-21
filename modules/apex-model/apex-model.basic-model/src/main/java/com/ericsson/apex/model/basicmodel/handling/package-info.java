/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/


/**
 * Contains a number of utility classes for handling APEX {@link com.ericsson.apex.model.basicmodel.concepts.AxModel} models and
 * {@link com.ericsson.apex.model.basicmodel.concepts.AxConcept} concepts.
 * Classes to read and write models to files, strings, and databases are included, as
 * well as classes to generate XML schemas for models.
 * 
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */

@XmlSchema(namespace = "http://www.ericsson.com/apex", elementFormDefault = XmlNsForm.QUALIFIED, xmlns = {
        @XmlNs(namespaceURI = "http://www.ericsson.com/apex", prefix = "") })

package com.ericsson.apex.model.basicmodel.handling;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
