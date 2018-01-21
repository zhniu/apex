/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.net3.intentnetwork

import java.util.HashMap
import java.util.LinkedList
import java.util.Map
import java.util.Map.Entry
import java.util.Queue
import java.util.Vector

class ITNodeTopologicalSort {

	def public static Vector<String> topologicalSort(ITNode node){
		//check node topology using Kahn’s algorithm for Topological Sorting
		var int visitedNodes = 0
		var Queue<String> queue = new LinkedList
		var Vector<String> topo = new Vector
		var Map<String, ITLinkList> counts = new HashMap
		counts.putAll(node.getIeCount)

		for(Entry<String, ITLinkList> entry : counts.entrySet){
			if(entry.getValue.getLeft.size == 0){
				queue.add(entry.getKey)
			}
		}
		while(!queue.isEmpty){
			val String current = queue.poll
			topo.add(current)
			for(Entry<String, ITLinkList> entry : counts.entrySet){
				if(!entry.getValue.getLeft.isEmpty){
					entry.getValue.getLeft.remove(current)
					if(entry.getValue.getLeft.isEmpty){
						queue.add(entry.getKey)
					}
				}
			}
			visitedNodes++
		}
		if(visitedNodes!=node.getIeCount.size){
			//TODO
			//we have an error, circular dependency in the graphs
			return null
		}
		return topo
	}
}