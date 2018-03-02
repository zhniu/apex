executor.logger.info(executor.subject.id);
executor.logger.info(executor.inFields);

var vnfID = executor.getContextAlbum("ControlLoopExecutionIDAlbum").remove(executor.executionID.toString());

executor.logger.info("Continuing execution with VNF ID: " + vnfID);

var vcpeClosedLoopStatus = executor.getContextAlbum("VCPEClosedLoopStatusAlbum").get(vnfID.toString());
executor.logger.info(vcpeClosedLoopStatus);

var guardResult = executor.inFields.get("decision");

if (guardResult === "PERMIT") {
	vcpeClosedLoopStatus.put("notification", "OPERATION: GUARD_PERMIT");
}
else if (guardResult === "DENY") {
	vcpeClosedLoopStatus.put("notification", "OPERATION: GUARD_DENY");
}
else {
	executor.message = "guard result must be either \"PERMIT\" or \"DENY\"";
	returnValue = executor.FALSE;
}

var uuidType  = Java.type("java.util.UUID");
var requestID = uuidType.fromString(vcpeClosedLoopStatus.get("requestID"));

executor.outFields.put("requestID", requestID);
executor.outFields.put("vnfID", vnfID);

executor.logger.info(executor.outFields);

var returnValue = executor.TRUE;
