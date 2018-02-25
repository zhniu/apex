executor.logger.info(executor.subject.id);
executor.logger.info(executor.inFields);

var vnf = executor.getContextAlbum("VNFAlbum").get(executor.inFields.get("genericVNFName"));

if (vnf == null) {
	var javaLongType = Java.type("java.lang.Long");

	executor.logger.info("Creating context information for new VNF \"" + executor.inFields.get("genericVNFName") + "\"");

	vnf = executor.getContextAlbum("VNFAlbum").getSchemaHelper().createNewInstance();

	vnf.put("name",           executor.inFields.get("genericVNFName"));
	vnf.put("ipAddress",      executor.inFields.get("genericVNFName"));
	vnf.put("uuid",           "49414df5-3482-4fd8-9952-c463dff2770b"); // In the demo, we hard code this, it should come from A&AI i a real system
	vnf.put("breached",       false);
	vnf.put("lastOnsetTime",  new javaLongType(0));
	vnf.put("lastAbatedTime", new javaLongType(0));
	
	executor.getContextAlbum("VNFAlbum").put(vnf.get("name").toString(), vnf);
}

var status = executor.inFields.get("status");

var returnValue = executor.TRUE;
var onsetFlag;

if (status === "ONSET") {
	vnf.put("lastOnsetTime",  executor.inFields.get("alarmStartTime"));
	vnf.put("breached",       true);
	onsetFlag = executor.TRUE;
}
else if (status === "ABATED") {
	vnf.put("lastAbatedTime", executor.inFields.get("alarmEndTime"));
	vnf.put("breached",       false);
	onsetFlag = executor.FALSE;
}
else {
	executor.message = "status must be either \"ONSET\" or \"ABATED\"";
	returnValue = executor.FALSE;
}

executor.outFields.put("vnfName",   vnf.get("name"));
executor.outFields.put("onsetFlag", onsetFlag);

executor.logger.info(executor.outFields);
