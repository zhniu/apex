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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Vector;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Utilities for intent networks, for instance topological sort.
 * 
 * @author     Sven van der Meer &lt;sven.van.der.meer@ericsson.com&gt;
 */
public class IT_Utilities {

	/**
	 * Creates a topological sort of the DIF registrations of a node using Kahn's algorithm.
	 * @param node the node to sort
	 * @return null on error (circular dependencies), a vector of strings (node names) otherwise
	 */
	public static Vector<String> topologicalSort(IT_Node node){
		//check node topology using Kahn’s algorithm for Topological Sorting
		int visitedNodes = 0;
		Queue<String> queue = new LinkedList<>();
		Vector<String> topo = new Vector<>();
		Map<String, Pair<List<String>, List<String>>> counts = new HashMap<>();
		counts.putAll(node.getIeCounts());

		for(Entry<String, Pair<List<String>, List<String>>> entry : counts.entrySet()){
			if(entry.getValue().getLeft().size()==0){
				queue.add(entry.getKey());
			}
		}
		while(!queue.isEmpty()){
			String current = queue.poll();
			topo.add(current);
			for(Entry<String, Pair<List<String>, List<String>>> entry : counts.entrySet()){
				if(!entry.getValue().getLeft().isEmpty()){
					entry.getValue().getLeft().remove(current);
					if(entry.getValue().getLeft().isEmpty()){
						queue.add(entry.getKey());
					}
				}
			}
			visitedNodes++;
		}
		if(visitedNodes!=node.getIeCounts().size()){
			//we have an error, circular dependency in the graphs
			return null;
		}
		return topo;
	}
}
