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

interface VizStgPaths {

	/** File for an STG that creates GraphML. */
	final String IT_GRAPHML_STG = "com/ericsson/apex/domains/net3/graphtools/graph-ml.stg";

	/** File for an STG that creates node graphs in DIF view. */
	final String IT_NODE_GRAPHML_DIF_STG = "com/ericsson/apex/domains/net3/graphtools/intnet/graph-ml-dif.stg";

	/** File for an STG that creates node graphs in Node view. */
	final String IT_NODE_GRAPHML_NODE_STG = "com/ericsson/apex/domains/net3/graphtools/intnet/graph-ml-node.stg";

}