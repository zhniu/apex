load("nashorn:mozilla_compat.js");
importClass(org.slf4j.LoggerFactory);

importClass(java.util.ArrayList);

importClass(org.apache.avro.generic.GenericData.Array);
importClass(org.apache.avro.generic.GenericRecord);
importClass(org.apache.avro.Schema);

var logger = executor.logger;
logger.trace("start: " + executor.subject.id);
logger.trace("-- infields: " + executor.inFields);

var rootLogger = LoggerFactory.getLogger(logger.ROOT_LOGGER_NAME);

var ifSituation = executor.inFields["situation"];

var albumProblemMap = executor.getContextAlbum("albumProblemMap");

//create outfiled for decision
var decision = executor.subject.getOutFieldSchemaHelper("decision").createNewInstance();
decision.put("description", "None, everything is ok");
decision.put("decision", "REBUILD");
decision.put("customers", new ArrayList());
decision.put("problemID", ifSituation.get("problemID"));

var returnValueType = Java.type("java.lang.Boolean");
if (albumProblemMap.get(ifSituation.get("problemID")).get("status") == "SOLVED"){
	logger.trace("-- problem solved");
	var returnValue = new returnValueType(true);
}
else{
	logger.trace("-- wrong problemID <" + problemID + "> for SOLVED task, we should not be here");
	rootLogger.error(executor.subject.id + " " + "-- wrong problemID <" + problemID + "> for SOLVED task, we should not be here");
	var returnValue = new returnValueType(false);
}

executor.outFields["decision"] = decision;

logger.info("vpnsla: sla solved, problem solved");

logger.trace("finished: " + executor.subject.id);
logger.debug(".d-non");
