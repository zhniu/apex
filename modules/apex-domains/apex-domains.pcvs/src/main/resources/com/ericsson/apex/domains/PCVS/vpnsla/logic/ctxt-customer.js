load("nashorn:mozilla_compat.js");

var logger = executor.logger;
logger.trace("start: " + executor.subject.id);
logger.trace("-- infields: " + executor.inFields);

var ifCustomerName = executor.inFields["customerName"];
var ifLinks = executor.inFields["links"];

logger.trace("-- got infields, testing existing customer");
var ctxtCustomer = executor.getContextAlbum("albumCustomerMap").get(ifCustomerName);
if (ctxtCustomer != null){
	executor.getContextAlbum("albumCustomerMap").remove(ifCustomerName);
	logger.trace("-- removed customer: <" + ifCustomerName + ">");
}

logger.trace("-- creating customer: <" + ifCustomerName + ">");
var links = new Array();
for(var i = 0; i <  ifLinks.split(" ").length; i++) {
	var link = executor.getContextAlbum("albumTopoEdges").get(ifLinks.split(" ")[i]);
	if (link != null) {
		logger.trace("-- link: <" + ifLinks.split(" ")[i] + ">");
		links.push(ifLinks.split(" ")[i]);
	}
	else{
		logger.trace("-- unknown link: <" + ifLinks.split(" ")[i] + "> for customer <" + ifCustomerName + ">");
	}
}
logger.trace("-- links: <" + links + ">");
ctxtCustomer = "{customerName:" + ifCustomerName + ", dtSLA:" + executor.inFields["dtSLA"] + ", dtYTD:" + executor.inFields["dtYTD"] + ", priority:" + executor.inFields["priority"]  + ", satisfaction:" + executor.inFields["satisfaction"] + ", links:[" + links + "]}";

executor.getContextAlbum("albumCustomerMap").put(ifCustomerName, ctxtCustomer);

if(logger.isTraceEnabled()){
	logger.trace("   >> *** Customers ***");
	if (executor.getContextAlbum("albumCustomerMap") != null) {
		for(var i = 0; i < executor.getContextAlbum("albumCustomerMap").values().size(); i++) {
			logger.trace("   >> >> " +
				executor.getContextAlbum("albumCustomerMap").values().get(i).get("customerName") +" : " +
				"dtSLA=" + executor.getContextAlbum("albumCustomerMap").values().get(i).get("dtSLA") +" : " +
				"dtYTD=" + executor.getContextAlbum("albumCustomerMap").values().get(i).get("dtYTD") +" : " +
				"links=" + executor.getContextAlbum("albumCustomerMap").values().get(i).get("links") +" : " +
				"priority=" + executor.getContextAlbum("albumCustomerMap").values().get(i).get("priority") +" : " +
				"satisfaction=" + executor.getContextAlbum("albumCustomerMap").values().get(i).get("satisfaction")
			);
		}
	}
	else{
		logger.trace("   >> >> customer album is null");
	}
}

executor.outFields["report"] = "customer ctxt :: added customer: " + ifCustomerName;

logger.info("vpnsla: ctxt added customer " + ifCustomerName);

var returnValueType = Java.type("java.lang.Boolean");
var returnValue = new returnValueType(true);
logger.trace("finished: " + executor.subject.id);
logger.debug(".");
