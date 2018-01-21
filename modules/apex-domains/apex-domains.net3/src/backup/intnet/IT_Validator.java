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
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;

/**
 * A validator for an IT networks, nodes, and DIFs.
 * 
 * @author     Sven van der Meer &lt;sven.van.der.meer@ericsson.com&gt;
 */
public final class IT_Validator {

	/** Validation errors. */
	protected final List<String> errors;

	/** Validation warnings. */
	protected final List<String> warnings;

	public IT_Validator(){
		this.errors = new ArrayList<>();
		this.warnings = new ArrayList<>();
	}

	/**
	 * Validates the network and all its nodes and DIFs.
	 * If the validation was successful, it returns 0.
	 * If the validation had warnings, it returns a positive integer with the number of warnings.
	 * If the validation had errors, it returns a negative integer with the number of errors.
	 * @param network the network to validate, must not be null
	 * @return 0 on success, positive integer for warnings, negative integer for errors
	 */
	public int validate(IT_Network network){
		Validate.notNull(network);
		this.errors.clear();
		this.warnings.clear();

		this.validatePtpDifs(network);
		this.validateNormalDifs(network);
		for(IT_Node node : network.getNodes().values()){
			this.validateNode(node);
		}

		if(this.errors.size()>0){
			return (0-this.errors.size());
		}
		else if(this.warnings.size()>0){
			return this.warnings.size();
		}
		return 0;
	}

	/**
	 * Validates normal DIFs against node registrations.
	 * @param network the network, must not be null
	 */
	protected void validateNormalDifs(IT_Network network){
		Validate.notNull(network);

		Set<String> inNodes = new TreeSet<>();
		for(IT_NormalDif ndif : network.getNormalDifs().values()){
			inNodes.clear();
			for(IT_Node node : network.getNodes().values()){
				if(node.getNormalDif(ndif.identity.name)!=null){
					inNodes.add(node.identity.name);
				}
			}
			if(inNodes.size()==0){
				this.warnings.add("N-DIF <" + ndif.identity.name + "> not used at all");
			}
			else if(inNodes.size()==1){
				this.warnings.add("N-DIF <" + ndif.identity.name + "> in only 1 node: " + inNodes);
			}
		}

		for(IT_Node node : network.getNodes().values()){
			for(IT_NormalDif ndif : node.getNormalDifs().values()){
				if(network.getNormalDif(ndif.identity.name)==null){
					this.errors.add("N-DIF <" + ndif.identity.name + "> registered in node <" + node.identity.name + "> not in network N-DIF list (test name)");
				}
				if(!network.getNormalDifs().values().contains(ndif)){
					this.errors.add("N-DIF <" + ndif.identity.name + "> registered in node <" + node.identity.name + "> not in network N-DIF list (test object)");
				}
			}
		}
	}

	/**
	 * Validates Point-to-Point DIFs against node registrations.
	 * @param network the network, must not be null
	 */
	protected void validatePtpDifs(IT_Network network){
		Validate.notNull(network);

		Set<String> inNodes = new TreeSet<>();
		for(IT_PtpDif pdif : network.getPtpDifs().values()){
			inNodes.clear();
			for(IT_Node node : network.getNodes().values()){
				if(node.getPtpDif(pdif.identity.name)!=null){
					inNodes.add(node.identity.name);
				}
			}
			if(inNodes.size()==0){
				this.warnings.add("P-DIF <" + pdif.identity.name + "> not used at all");
			}
			else if(inNodes.size()==1){
				this.warnings.add("P-DIF <" + pdif.identity.name + "> in only 1 node: " + inNodes);
			}
			else if(inNodes.size()>2){
				this.errors.add("P-DIF <" + pdif.identity.name + "> in more than 2 nodes: " + inNodes);
			}
		}

		for(IT_Node node : network.getNodes().values()){
			for(IT_PtpDif pdif : node.getPtpDifs().values()){
				if(network.getPtpDif(pdif.identity.name)==null){
					this.errors.add("P-DIF <" + pdif.identity.name + "> registered in node <" + node.identity.name + "> not in network P-DIF list (test name)");
				}
				if(!network.getPtpDifs().values().contains(pdif)){
					this.errors.add("P-DIF <" + pdif.identity.name + "> registered in node <" + node.identity.name + "> not in network P-DIF list (test object)");
				}
			}
		}
	}

	/**
	 * Validates a single node.
	 * @param node the node to validate, must not be null
	 */
	protected void validateNode(IT_Node node){
		Validate.notNull(node);

		if(node.getPtpDifs().size()==0){
			this.errors.add("node <" + node.identity.name + "> has no point-to-point level P-DIFs");
		}

		// get counts of all DIFs in node
		// in count
		Map<String, Pair<List<String>, List<String>>> ioCount = node.getIeCounts();

		int topDif = 0;
		for(Entry<String, Pair<List<String>, List<String>>> entry : ioCount.entrySet()){
			if(node.getNormalDif(entry.getKey())!=null){
				if(entry.getValue().getLeft().size()==0){
					topDif++;
				}
			}
		}
		if(topDif==0){
			this.errors.add("node <" + node.identity.name + "> has no top level N-DIFs");
		}

		Vector<String> topoNode;
		topoNode = node.getTopologicalOrder();
		if(topoNode.size()==0){
			this.errors.add("node <" + node.identity.name + "> not build (no topological order set");
		}
		if(topoNode.size()!=node.getIeCounts().size()){
			this.errors.add("node <" + node.identity.name + "> size of topological order and i/e count differs");
		}

		Vector<String> topoCalc = IT_Utilities.topologicalSort(node);
		if(topoCalc==null){
			this.errors.add("node <" + node.identity.name + "> has cycled dependency in registrations");
		}

	}

	/**
	 * Returns the errors from the last validation.
	 * @return last validation errors
	 */
	public List<String> getErrors(){
		return this.errors;
	}

	/**
	 * Returns the warnings from the last validation.
	 * @return last validation warnings
	 */
	public List<String> getWarnings(){
		return this.warnings;
	}
}
