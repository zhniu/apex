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
 * Base class for an IT object.
 * 
 * @author     Sven van der Meer &lt;sven.van.der.meer@ericsson.com&gt;
 */
public abstract class IT_BaseObject {

	/** The object's identity. */
	protected final IT_Identity identity;

	/**
	 * Creates a new IT object.
	 * @param name the IT object name, must not be blank
	 * @param version the IT object version, must not be blank
	 * @param nameSpace the IT object name space, must not be blank
	 * @param description a description, can be null
	 */
	protected IT_BaseObject(String name, String version, String nameSpace, String description){
		this.identity = new IT_Identity(name, version, nameSpace, description);
	}

	/**
	 * Returns the IT object identity.
	 * @return IT object identity
	 */
	public IT_Identity getIdentity(){
		return this.identity;
	}

}
