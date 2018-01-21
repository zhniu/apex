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
 * Contains the concepts required to manage events in APEX.
 * It defines the main Apex concepts of events and fields.
 * 
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */

@XmlSchema(namespace = "http://www.ericsson.com/apex", elementFormDefault = XmlNsForm.QUALIFIED, xmlns = {
        @XmlNs(namespaceURI = "http://www.ericsson.com/apex", prefix = "") })

package com.ericsson.apex.model.eventmodel.concepts;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
