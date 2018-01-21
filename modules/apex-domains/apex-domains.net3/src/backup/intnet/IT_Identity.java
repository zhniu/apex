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

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.StrBuilder;

/**
 * The identity of an Intent object.
 * 
 * @author     Sven van der Meer &lt;sven.van.der.meer@ericsson.com&gt;
 */
public class IT_Identity {

	/** Object name, unique in the name space. */
	public final String name;

	/* Object version as semantic versioning (major-minor-path). */
	public final String version;

	/** The name space defining name boundaries. */
	public final String nameSpace;

	/** A description of the intent object. */
	public String description;

	/**
	 * Creates a new identity.
	 * @param name the object name, must not be blank
	 * @param version the object version, must not be blank
	 * @param nameSpace the name space, must not be blank
	 */
	protected IT_Identity(String name, String version, String nameSpace){
		this(name, version, nameSpace, null);
	}

	/**
	 * Creates a new identity.
	 * @param name the object name, must not be blank
	 * @param version the object version, must not be blank
	 * @param nameSpace the name space, must not be blank
	 * @param description the description, can be null
	 */
	protected IT_Identity(String name, String version, String nameSpace, String description){
		Validate.notBlank(name, "IT identity name cannot be blank");
		Validate.notBlank(version, "IT identity name cannot be blank");
		Validate.notBlank(nameSpace, "IT identity name cannot be blank");
		this.name = name;
		this.version = version;
		this.nameSpace = nameSpace;
		this.description = description;
	}

	/**
	 * Returns the identity description.
	 * @return identity description, null if not set
	 */
	public String getDescription(){
		return this.description;
	}

	/**
	 * Returns the identity name.
	 * @return identity name
	 */
	public String getName(){
		return this.name;
	}

	/**
	 * Returns the identity name space
	 * @return identity name space
	 */
	public String getNameSpace(){
		return this.nameSpace;
	}

	/**
	 * Returns the identity version
	 * @return identity version
	 */
	public String getVersion(){
		return this.version;
	}

	/**
	 * Sets a description.
	 * @param description the new description, must not be blank
	 * @return self to allow chaining
	 */
	public IT_Identity setDescription(String description){
		Validate.notBlank(nameSpace, "IT identity description explicitely set cannot be blank");
		this.description = description;
		return this;
	}

	/**
	 * Returns a debug string, long information about the object.
	 * @return the debug string
	 */
	public String toDebugString(){
		StrBuilder ret = new StrBuilder();

		ret.append(this.name)
			.append('(')
			.append(this.version)
			.append(", ")
			.append(this.nameSpace)
			.append(", ")
			.append(this.description)
			.append(')')
		;

		return ret.build();
	}

	@Override
	public String toString(){
		return this.name;
	}

}
