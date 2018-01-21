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

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.Validate;

/**
 * An intend network.
 * 
 * @author     Sven van der Meer &lt;sven.van.der.meer@ericsson.com&gt;
 */
public class IT_Network extends IT_HasDifs {

	/** The nodes in the network as mapping from node name to node object. */
	protected final Map<String, IT_Node> nodes;

	/**
	 * Creates a new intend network.
	 * @param name the network name, must not be blank
	 * @param version the network version, must not be blank
	 * @param nameSpace the network name space, must not be blank
	 * @param description a description, must not be blank
	 */
	protected IT_Network(String name, String version, String nameSpace, String description){
		super(name, version, nameSpace, description);
		Validate.notBlank(description);

		this.nodes = new TreeMap<>();
	}

	/**
	 * Creates a new node checking name and existing nodes.
	 * @param name the name for the new node, must not be blank
	 * @return a new node if name is not blank and no node of name exists
	 */
	public IT_Node createNode(String name){
		Validate.notBlank(name, "IT network <" + this.identity.name + "> createNode(): cannot add node with blank name");
		Validate.validState(!this.nodes.containsKey(name), "IT network <" + this.identity.name + "> createNode(): node <" + name + "> already exists");

		IT_Node node = new IT_Node(name, this.identity.version, this.identity.nameSpace, "Auto-generated description: node " + name + " in intend network " + this.identity.name);
		this.nodes.put(name, node);
		return node;
	}

	/**
	 * Creates a new normal DIF if does not already exist.
	 * @param name the DIF name, must not be blank
	 * @param description the DIF description, must not be blank
	 * @return new normal DIF on success
	 */
	public IT_NormalDif createNormalDif(String name, String description){
		Validate.notBlank(name, "IT network <" + this.identity.name + "> createNormalDif(): cannot add DIF with blank name");
		Validate.notBlank(description, "IT network <" + this.identity.name + "> createNormalDif(): cannot add DIF with blank description");
		IT_NormalDif dif = new IT_NormalDif(name, this.identity.version, this.identity.nameSpace, description);
		this.addDif(dif, false);
		return dif;
	}

	/**
	 * Creates a new Point-to-Point DIF if does not already exist.
	 * @param name the DIF name, must not be blank
	 * @param description the DIF description, must not be blank
	 * @return new Point-to-Point DIF on success
	 */
	public IT_PtpDif createPtpDif(String name, String description){
		Validate.notBlank(name, "IT network <" + this.identity.name + "> createPtpDif(): cannot add DIF with blank name");
		Validate.notBlank(description, "IT network <" + this.identity.name + "> createPtpDif(): cannot add DIF with blank description");
		IT_PtpDif dif = new IT_PtpDif(name, this.identity.version, this.identity.nameSpace, description);
		this.addDif(dif, false);
		return dif;
	}

	/**
	 * Returns a node.
	 * @param name the node name
	 * @return node if found, null otherwise
	 */
	public IT_Node getNode(String name){
		return this.nodes.get(name);
	}

	/**
	 * Returns all nodes in the network.
	 * @return all nodes
	 */
	public Map<String, IT_Node> getNodes(){
		return this.nodes;
	}

	/**
	 * Builds all network artifacts, for instance the topological sort of nodes.
	 * @return self to allow chaining
	 */
	public IT_Network build(){
		for(IT_Node node : this.nodes.values()){
			node.setTopologicalOrder(IT_Utilities.topologicalSort(node));
		}

		return this;
	}

}
