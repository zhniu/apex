load("nashorn:mozilla_compat.js");

var logger = executor.logger;
logger.trace("start: " + executor.subject.id);
logger.trace("-- infields: " + executor.inFields);

var ifDecision   = executor.inFields["decision"];
var ifMatchStart = executor.inFields["matchStart"];

var albumCustomerMap = executor.getContextAlbum("albumCustomerMap");
var albumProblemMap = executor.getContextAlbum("albumProblemMap");

switch(ifDecision.get("decision").toString()){
	case "NONE":
		executor.outFields["edgeName"] = "";
		executor.outFields["action"] = "";
		break;
	case "IMPEDE":
		for(var i = 0; i < ifDecision.get("customers").size(); i++) {
			customer = albumCustomerMap.get(ifDecision.get("customers").get(i).toString());
			executor.outFields["edgeName"] = customer.get("links").get(0);
			executor.outFields["action"] = "firewall";
		}
		break;
	case "REBUILD":
		//finally solved, remove problem
		albumProblemMap.remove(ifDecision.get("problemID"));
		executor.outFields["edgeName"] = "L10"; //this is ###static###
		executor.outFields["action"] = "rebuild"; //this is ###static###
		break;
	default:
		
}

var returnValueType = Java.type("java.lang.Boolean");
var returnValue = new returnValueType(true);

if(executor.outFields["action"] != ""){
	logger.info("vpnsla: action is to " + executor.outFields["action"] + " " + executor.outFields["edgeName"]);
}
else{
	logger.info("vpnsla: no action required");
}

logger.trace("-- outfields: " + executor.outFields);
logger.trace("finished: " + executor.subject.id);
logger.debug(".a");

var now = new Date().getTime();
logger.info("VPN SLA finished in " + (now - ifMatchStart) + " ms");
