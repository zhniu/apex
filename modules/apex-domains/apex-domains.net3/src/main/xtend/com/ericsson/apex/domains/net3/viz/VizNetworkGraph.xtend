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

import com.ericsson.apex.domains.net3.intentnetwork.ITNetwork
import com.ericsson.apex.domains.net3.intentnetwork.ITNode
import org.apache.commons.lang3.Validate
import org.stringtemplate.v4.ST
import org.stringtemplate.v4.STGroup
import org.stringtemplate.v4.STGroupFile

class VizNetworkGraph {

	/** The intent network to create graphs for. */
	protected final ITNetwork network;

	/** The ST Group file to use. */
	protected final STGroup stg;

	/** The graph tool for handling individual nodes. */
	protected final VizNodeGraph nodeTool;

	public new(ITNetwork net){
		Validate.notNull(net)
		network = net
		stg = new STGroupFile(VizStgPaths.IT_GRAPHML_STG, '$', '$')
		nodeTool = VizNodeGraph.newInstance
	}

	/**
	 * Returns the network as a template with GraphML statements.
	 * @param view the required view
	 * @return a template with GraphML statements
	 */
	def ST asGraphML(VizGraphType view){
		var ST st = this.stg.getInstanceOf("graphMl");
		for(ITNode node : network.getNodes().values()){
			st.add("graphs", this.nodeTool.asGraph(node, view));
		}
		return st;
	}
}