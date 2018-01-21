/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.net3.viz

import com.ericsson.apex.domains.net3.intentnetwork.ITDif
import com.ericsson.apex.domains.net3.intentnetwork.ITNode
import java.util.Map.Entry
import org.apache.commons.lang3.NotImplementedException
import org.apache.commons.lang3.Validate
import org.stringtemplate.v4.ST
import org.stringtemplate.v4.STGroup
import org.stringtemplate.v4.STGroupFile

class VizNodeGraph {

	/** The ST group for standard GraphML. */
	protected final STGroup graphML;

	/** The ST group for DIF view GraphML. */
	protected final STGroup nodeGraphMLDif;

	/** The ST group for Node view GraphML. */
	protected final STGroup nodeGraphMLNode;

	public new(){
		graphML = new STGroupFile(VizStgPaths.IT_GRAPHML_STG, '$', '$')
		nodeGraphMLDif = new STGroupFile(VizStgPaths.IT_NODE_GRAPHML_DIF_STG, '$', '$')
		nodeGraphMLNode = new STGroupFile(VizStgPaths.IT_NODE_GRAPHML_NODE_STG, '$', '$')
	}

		/**
	 * Returns the node as a single graph in GraphML notation.
	 * @param node the node to create the graph for
	 * @param view the required view
	 * @return a template with GraphML
	 */
	def ST asGraph(ITNode node, VizGraphType view){
		var ST st = null;
		switch(view){
			case DIF_GRAPH:
				st = this.nodeGraphMLDif.getInstanceOf("graph")
			case NODE_GRAPH:
				st = this.nodeGraphMLNode.getInstanceOf("graph")
			case NETWORK_GRAPH: {
				System.err.println("not implemented yet")
				throw new NotImplementedException("no network graph yet")
			}
		}
		if(st===null){
			return st;//TODO this is a serious error
		}
		this.addToGraph(st, node);
		return st;
	}

	/**
	 * Returns the node as a single graph as a complete GraphML document.
	 * @param node the node to create the graph for
	 * @param view the required view
	 * @return a template with GraphML
	 */
	def ST asGraphML(ITNode node, VizGraphType view){
		val ST st = this.graphML.getInstanceOf("graphMl");
		st.add("graphs", this.asGraph(node, view));
		return st;
	}

	/**
	 * Adds all attributes to a graph.
	 * @param st the template to add to, must not be null
	 * @param node the node to generate a graph for
	 */
	def void addToGraph(ST st, ITNode node){
		Validate.notNull(st);
		st.add("name", node.getName());
		st.add("nodes", node.getAllDifs());

		for(Entry<ITDif, ITDif[]> entry : node.getRegistrations().entrySet()){
			var int i = 0;
			for(ITDif dif : entry.getValue()){
				st.add("edges", new VizGraphEdge(
						node.getName() + "." + entry.getKey().getName() + "." + i,
						entry.getKey().getName(),
						dif.getName()
				));
				i++;
			}
		}
	}
}