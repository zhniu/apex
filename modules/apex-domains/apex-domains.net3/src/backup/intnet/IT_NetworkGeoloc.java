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

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import com.ericsson.apex.domains.net3.netloc.As_Net_Location;
import com.ericsson.apex.domains.net3.netloc.Net_Location;
import com.ericsson.apex.domains.net3.netloc.Net_ObjectLocations;

/**
 * An intend network using geographic locations for nodes and Point-to-Point DIFs.
 * 
 * @author     Sven van der Meer &lt;sven.van.der.meer@ericsson.com&gt;
 */
public class IT_NetworkGeoloc extends IT_Network {

	/** Counter for Point-to-Point DIF names. */
	private int ptpCounter = 1;

	/**
	 * Creates a new intend network.
	 * @param name the network name, must not be blank
	 * @param version the network version, must not be blank
	 * @param nameSpace the network name space, must not be blank
	 * @param description a description, must not be blank
	 */
	protected IT_NetworkGeoloc(String name, String version, String nameSpace, String description){
		super(name, version, nameSpace, description);
	}

	/**
	 * Creates a new node for given location and adds it to the network.
	 * @param location the node location used for name and description, must not be null
	 * @return new node
	 */
	protected IT_Node createNodeGeoloc(As_Net_Location location){
		Validate.notNull(location);
		return this.createNodeGeoloc(location.asNetLocation());
	}

	/**
	 * Creates a new node for given location and adds it to the network.
	 * @param location the node location used for name and description, must not be null
	 * @return new node
	 */
	protected IT_Node createNodeGeoloc(Net_Location location){
		Validate.notNull(location);

		IT_Node ret = this.createNode(location.getName());
		ret.setLocations(new Net_ObjectLocations(location));
		ret.getIdentity().setDescription("Renumbering node for " + location.getCity().getDisplayName() + " in " + location.getCountry().getDisplayName());

		return ret;
	}

	/**
	 * Searches for relevant Point-to-Point DIFs for the node and adds a registration.
	 * @param node the node, must not be null
	 * @param dif the normal DIF to register on the Point-to-Point DIFs, must not be null
	 */
	protected void doNodePtpRegistrationGeoloc(IT_Node node, IT_NormalDif dif){
		Validate.notNull(node);
		Validate.notNull(dif);
		Validate.validState(node.locations.getIntended()!=null, "need an intended location on the node");

		Set<IT_PtpDif> links = new HashSet<>();
		for(IT_PtpDif pdif : this.getPtpDifs().values()){
			if(pdif.getLocation().getStart()==node.locations.getIntended() || pdif.getLocation().getEnd()==node.locations.getIntended()){
				links.add(pdif);
			}
		}
		node.addRegistration(dif, links.toArray(new IT_PtpDif[0]));
	}

	/**
	 * Creates a new Point-to-Point DIF.
	 * @param start the start, must not be null
	 * @param end the end, must not be null
	 * @return new Point-to-Point DIF
	 */
	protected IT_PtpDif createPtpDifGeoloc(As_Net_Location start, As_Net_Location end){
		Validate.notNull(start);
		Validate.notNull(end);
		return this.createPtpDifGeoloc(start.asNetLocation(), end.asNetLocation());
	}

	/**
	 * Creates a new Point-to-Point DIF.
	 * @param start the start, must not be null
	 * @param end the end, must not be null
	 * @return new Point-to-Point DIF
	 */
	protected IT_PtpDif createPtpDifGeoloc(Net_Location start, Net_Location end){
		Validate.notNull(start);
		Validate.notNull(end);
		String name = String.format("%s%03d", "e", ptpCounter++);
		IT_PtpDif ret = this.createPtpDif(
				name,
				"PtP DIF from" + start.getCity().getDisplayName() + " to " + end.getCity().getDisplayName()
		);
		ret.setLink(start, end);
		return ret;
	}

}
