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

/**
 * Base class for a DIF.
 * 
 * @author     Sven van der Meer &lt;sven.van.der.meer@ericsson.com&gt;
 */
public abstract class IT_Dif extends IT_BaseObject implements Comparable<IT_Dif> {

	/**
	 * Creates a new DIF.
	 * @param name the DIF name, must not be blank
	 * @param version the DIF version, must not be blank
	 * @param nameSpace the DIF name space, must not be blank
	 * @param description a description, can be null
	 */
	protected IT_Dif(String name, String version, String nameSpace, String description){
		super(name, version, nameSpace, description);
	}

	@Override
	public int compareTo(IT_Dif dif) {
		Validate.notNull(dif);
		String me = this.identity.nameSpace + "." + this.identity.name;
		String it = dif.identity.nameSpace + "." + dif.identity.name;
		return me.compareTo(it);
	}

	@Override
	public String toString(){
		return this.identity.getName();
	}
}
