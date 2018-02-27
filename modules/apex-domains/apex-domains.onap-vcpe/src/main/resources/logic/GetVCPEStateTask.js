executor.logger.info(executor.subject.id);
executor.logger.info(executor.inFields);

var uuidType  = Java.type("java.util.UUID");
var requestID = uuidType.fromString(executor.inFields.get("requestID"));
var vnfID     = uuidType.fromString(executor.inFields.get("AAI").get("generic_DasH_vnf_DoT_vnf_DasH_id"));

var vcpeClosedLoopStatus = executor.getContextAlbum("VCPEClosedLoopStatusAlbum").get(vnfID);

if (vcpeClosedLoopStatus == null) {
	var javaLongType = Java.type("java.lang.Long");

	executor.logger.info("Creating context information for new vCPE VNF \"" + vnfID.toString() + "\"");

	vcpeClosedLoopStatus = executor.getContextAlbum("VCPEClosedLoopStatusAlbum").getSchemaHelper().createNewInstance();

	vcpeClosedLoopStatus.put("AAI",                   executor.inFields.get("AAI"));
	vcpeClosedLoopStatus.put("closedLoopControlName", executor.inFields.get("closedLoopControlName"));
	vcpeClosedLoopStatus.put("closedLoopAlarmStart",  executor.inFields.get("closedLoopAlarmStart"));
	vcpeClosedLoopStatus.put("closedLoopEventClient", executor.inFields.get("closedLoopEventClient"));
	vcpeClosedLoopStatus.put("closedLoopEventStatus", executor.inFields.get("closedLoopEventStatus"));
	vcpeClosedLoopStatus.put("version",               executor.inFields.get("version"));
	vcpeClosedLoopStatus.put("requestID",             executor.inFields.get("requestID"));
	vcpeClosedLoopStatus.put("target_type",           executor.inFields.get("target_type"));
	vcpeClosedLoopStatus.put("target",                executor.inFields.get("target"));
	vcpeClosedLoopStatus.put("from",                  executor.inFields.get("from"));
	vcpeClosedLoopStatus.put("policyScope",           "vCPE");
	vcpeClosedLoopStatus.put("policyName",            "ONAPvCPEPolicyModel");
	vcpeClosedLoopStatus.put("policyVersion",         "0.0.1");
	vcpeClosedLoopStatus.put("notification",          "");

	executor.getContextAlbum("VCPEClosedLoopStatusAlbum").put(vnfID.toString(), vcpeClosedLoopStatus);

	executor.logger.info("Created context information for new vCPE VNF \"" + vnfID.toString() + "\"");
}

executor.outFields.put("requestID", requestID);
executor.outFields.put("vnfID",     vnfID);

executor.logger.info(executor.outFields);

returnValue = executor.TRUE;
