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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;

import com.ericsson.apex.domains.net3.netloc.Net_ObjectLocations;

/**
 * A Node in an intent network.
 * 
 * @author     Sven van der Meer &lt;sven.van.der.meer@ericsson.com&gt;
 */
public class IT_Node extends IT_HasDifs {

	/** The locations of the node, null if none set or used. */
	protected Net_ObjectLocations locations;

	/** Registrations as mapping from DIF to set of registrations for that DIF. */
	protected final Map<IT_Dif, IT_Dif[]> registrations;

	/** Ingress and egress links for each DIF in a node. */
	protected final Map<String, Pair<List<String>, List<String>>> ieCounts;

	/** The DIFs of the node in topological order, set by a validator or other external component. */
	protected final Vector<String> topoSort;

	/**
	 * Creates a new node.
	 * @param name the node name, must not be blank
	 * @param version the node version, must not be blank
	 * @param nameSpace the node name space, must not be blank
	 * @param description a description, can be null
	 */
	protected IT_Node(String name, String version, String nameSpace, String description){
		super(name, version, nameSpace, description);

		this.registrations = new TreeMap<>();
		this.ieCounts = new TreeMap<>();
		this.topoSort = new Vector<>();
	}

	/**
	 * Adds a new vertical registration to the node.
	 * @param dif the DIF to add registrations for
	 * @param difs the DIFs to register the DIF for
	 * @return self to allow chaining
	 */
	public IT_Node addRegistration(IT_Dif dif, IT_Dif ...difs){
		Validate.notNull(dif);
		Validate.noNullElements(difs);

		this.registrations.put(dif, difs);
		this.addDif(dif, true);
		this.addAllDifs(difs, true);

		// add ingress/egress links to local count map
		if(!this.ieCounts.containsKey(dif.toString())){
			this.ieCounts.put(dif.toString(), Pair.of(new ArrayList<>(), new ArrayList<>()));
		}
		for(IT_Dif regdif : difs){
			if(!this.ieCounts.containsKey(regdif.toString())){
				this.ieCounts.put(regdif.toString(), Pair.of(new ArrayList<>(), new ArrayList<>()));
			}
			// ingress counts
			this.ieCounts.get(regdif.toString()).getLeft().add(dif.toString());
			// egress counts
			this.ieCounts.get(dif.toString()).getRight().add(regdif.toString());
		}

		return this;
	}

	/**
	 * Returns the node locations.
	 * @return node locations, null if none set
	 */
	public Net_ObjectLocations getLocations(){
		return this.locations;
	}

	/**
	 * Returns the current vertical registrations.
	 * @return current vertical registrations
	 */
	public Map<IT_Dif, IT_Dif[]> getRegistrations(){
		return this.registrations;
	}

	/**
	 * Sets the node locations.
	 * @param locations new locations, must not be null, ignored if already set
	 * @return self to allow chaining
	 */
	public IT_Node setLocations(Net_ObjectLocations locations){
		Validate.notNull(locations);
		if(this.locations==null){
			this.locations = locations;
		}
		return this;
	}

	/**
	 * Returns a map with ingress/egress lists for each DIF registered in the node.
	 * The mapping is of DIF name to a pair.
	 * The pair's left hand side has the ingress counts as a list of DIF names.
	 * If the size of the list is 0, the DIf has no incoming connection.
	 * The pair's right hand side has the egress counts as a list of DIF names.
	 * If the size of the list is 0, the DIF has no outgoing connection.
	 * @return ingress/egress count map
	 */
	public Map<String, Pair<List<String>, List<String>>> getIeCounts(){
		return this.ieCounts;
	}

	/**
	 * Sets the topological order for the node's DIFs.
	 * @param topo new topological order, ignored if null
	 * @return self to allow chaining
	 */
	protected IT_Node setTopologicalOrder(final Vector<String> topo){
		if(topo!=null){
			this.topoSort.clear();
			this.topoSort.addAll(topo);
		}
		return this;
	}

	/**
	 * Returns the topological order of the DIFs in the node.
	 * @return topological order, empty if none set
	 */
	public Vector<String> getTopologicalOrder(){
		return this.topoSort;
	}
}
