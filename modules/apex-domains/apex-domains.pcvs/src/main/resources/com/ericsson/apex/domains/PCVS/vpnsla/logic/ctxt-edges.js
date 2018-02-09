load("nashorn:mozilla_compat.js");

var logger = executor.logger;
logger.trace("start: " + executor.subject.id);
logger.trace("-- infields: " + executor.inFields);

var ifEdgeName = executor.inFields["edgeName"];
var ifEdgeStatus = executor.inFields["status"];

var albumTopoEdges = executor.getContextAlbum("albumTopoEdges");

logger.trace("-- got infields, testing existing edge");

var ctxtEdge = albumTopoEdges.get(ifEdgeName);
if (ctxtEdge != null){
	albumTopoEdges.remove(ifEdgeName);
	logger.trace("-- removed edge: <" + ifEdgeName + ">");
}

logger.trace("-- creating edge: <" + ifEdgeName + ">");
ctxtEdge = "{name:" + ifEdgeName + ", start:" + executor.inFields["start"] + ", end:" + executor.inFields["end"] + ", active:" + ifEdgeStatus + "}";
albumTopoEdges.put(ifEdgeName, ctxtEdge);

if(logger.isTraceEnabled()){
	logger.trace("   >> *** Edges ***");
	if (albumTopoEdges != null) {
		for(var i = 0; i < albumTopoEdges.values().size(); i++) {
			logger.trace("   >> >> " + albumTopoEdges.values().get(i).get("name") + " \t " + albumTopoEdges.values().get(i).get("start") + " --> " + albumTopoEdges.values().get(i).get("end") + " \t " + albumTopoEdges.values().get(i).get("active"));
		}
	}
	else{
		logger.trace("   >> >> edge album is null");
	}
}

executor.outFields["report"] = "edge ctxt :: added edge " + ifEdgeName;

logger.info("vpnsla: ctxt added edge " + ifEdgeName);

var returnValueType = Java.type("java.lang.Boolean");
var returnValue = new returnValueType(true);
logger.trace("finished: " + executor.subject.id);
logger.debug(".");
