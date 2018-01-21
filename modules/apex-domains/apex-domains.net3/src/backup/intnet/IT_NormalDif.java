/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/


package com.ericsson.apex.domains.net3.intnet;

/**
 * A normal DIF.
 * 
 * @author     Sven van der Meer &lt;sven.van.der.meer@ericsson.com&gt;
 */
public class IT_NormalDif extends IT_Dif {

	/**
	 * Creates a new DIF.
	 * @param name the DIF name, must not be blank
	 * @param version the DIF version, must not be blank
	 * @param nameSpace the DIF name space, must not be blank
	 * @param description a description, can be null
	 */
	protected IT_NormalDif(String name, String version, String nameSpace, String description){
		super(name, version, nameSpace, description);
	}

}
