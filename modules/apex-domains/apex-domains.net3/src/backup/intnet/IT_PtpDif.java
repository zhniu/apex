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
import org.apache.commons.lang3.tuple.Pair;

import com.ericsson.apex.domains.net3.netloc.NetLoc_LinkLocations;
import com.ericsson.apex.domains.net3.netloc.Net_Location;

/**
 * A Point-to-Point DIF.
 * 
 * @author     Sven van der Meer &lt;sven.van.der.meer@ericsson.com&gt;
 */
public class IT_PtpDif extends IT_Dif {

	/** Start and end points of the DIF. */
	protected Pair<String, String> link;

	/** Start and end points of the DIF. */
	protected NetLoc_LinkLocations location;

	/**
	 * Creates a new DIF.
	 * @param name the DIF name, must not be blank
	 * @param version the DIF version, must not be blank
	 * @param nameSpace the DIF name space, must not be blank
	 * @param description a description, can be null
	 */
	protected IT_PtpDif(String name, String version, String nameSpace, String description){
		super(name, version, nameSpace, description);
	}

	/**
	 * Returns the link ends.
	 * @return link ends, null if not set
	 */
	public Pair<String, String> getLink(){
		return this.link;
	}

	/**
	 * Returns the link locations.
	 * @return link locations, null if none set
	 */
	public NetLoc_LinkLocations getLocation(){
		return this.location;
	}

	/**
	 * Compares two pdifs for similarity.
	 * They are the same (true) if name and nameSpace are the same.
	 * They are similar (also true) if start/end are the same.
	 * @param pdif comparison DIF, must not be null
	 * @return true if similar, false otherwise
	 */
	public boolean sameLink(IT_PtpDif pdif) {
		Validate.notNull(pdif);
		if(this.identity.name.equals(pdif.identity.name) && this.identity.nameSpace.equals(pdif.identity.nameSpace)){
			return true;
		}
		if(this.link!=null && pdif.link!=null){
			if(this.link.getLeft().equals(pdif.link.getLeft()) && this.link.getRight().equals(pdif.link.getRight())){
				return true;
			}
			if(this.link.getLeft().equals(pdif.link.getRight()) && this.link.getRight().equals(pdif.link.getLeft())){
				return true;
			}
		}
		if(this.location!=null && pdif.location!=null){
			return this.location.sameLocation(pdif.location);
		}
		return false;
	}

	/**
	 * Sets the start and end of the link if not already set.
	 * @param start the start location, must not be null
	 * @param end the end location, must not be null
	 */
	public void setLink(Net_Location start, Net_Location end){
		if(this.link!=null || this.location!=null){
			return;
		}
		Validate.notNull(start);
		Validate.notNull(end);
		this.location = new NetLoc_LinkLocations(start, end);
		this.link = Pair.of(start.getName(), end.getName());
	}

	/**
	 * Sets the start and end of the link if not already set.
	 * @param start the start, must not be blank
	 * @param end the end, must not be blank
	 */
	public void setLink(String start, String end){
		if(this.link!=null){
			return;
		}
		Validate.notBlank(start);
		Validate.notBlank(end);
		this.link = Pair.of(start, end);
	}

}
