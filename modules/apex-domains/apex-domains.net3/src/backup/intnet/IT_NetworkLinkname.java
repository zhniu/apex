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

/**
 * An intend network using simple names for nodes and Point-to-Point DIFs.
 * 
 * @author     Sven van der Meer &lt;sven.van.der.meer@ericsson.com&gt;
 */
public class IT_NetworkLinkname extends IT_Network {

	/** Counter for Point-to-Point DIF names. */
	private int ptpCounter = 1;

	/**
	 * Creates a new intend network.
	 * @param name the network name, must not be blank
	 * @param version the network version, must not be blank
	 * @param nameSpace the network name space, must not be blank
	 * @param description a description, must not be blank
	 */
	protected IT_NetworkLinkname(String name, String version, String nameSpace, String description){
		super(name, version, nameSpace, description);
	}

	/**
	 * Creates a new node for given location and adds it to the network.
	 * @param name the node name, must not be blank
	 * @return new node
	 */
	protected IT_Node createNodeLinkname(String name){
		Validate.notBlank(name);

		IT_Node ret = this.createNode(name);
		ret.getIdentity().setDescription("node <" + name + ">");

		return ret;
	}

	/**
	 * Searches for relevant Point-to-Point DIFs for the node and adds a registration.
	 * @param node the node, must not be null
	 * @param dif the normal DIF to register on the Point-to-Point DIFs, must not be null
	 */
	protected void doNodePtpRegistrationLinkname(IT_Node node, IT_NormalDif dif){
		Validate.notNull(node);
		Validate.notNull(dif);

		Set<IT_PtpDif> links = new HashSet<>();
		for(IT_PtpDif pdif : this.getPtpDifs().values()){
			if(pdif.link.getLeft().equals(node.identity.name) || pdif.link.getRight().equals(node.identity.name)){
				links.add(pdif);
			}
		}
		node.addRegistration(dif, links.toArray(new IT_PtpDif[0]));
	}

	/**
	 * Creates a new Point-to-Point DIF.
	 * @param start the start, must not be blank
	 * @param end the end, must not be blank
	 * @return new Point-to-Point DIF
	 */
	protected IT_PtpDif createPtpDifLinkname(String start, String end){
		Validate.notBlank(start);
		Validate.notBlank(end);
		String name = String.format("%s%03d", "e", ptpCounter++);
		IT_PtpDif ret = this.createPtpDif(
				name,
				"PtP DIF from" + start + " to " + end
		);
		ret.setLink(start, end);
		return ret;
	}

}
