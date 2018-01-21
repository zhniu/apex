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

enum VizGraphType {

	/** DIF grapg, all DIFs and their connections. */
	DIF_GRAPH,

	/** Node graph, a single node with all DIFs. */
	NODE_GRAPH,

	/** Network graph, DIFs and their IPCPs. */
	NETWORK_GRAPH

}