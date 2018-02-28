executor.logger.info(executor.subject.id);
executor.logger.info(executor.inFields);

var uuidType     = Java.type("java.util.UUID");
var integerType  = Java.type("java.lang.Integer");

var requestID = uuidType.fromString(executor.inFields.get("correlation-id"));
var vnfID = executor.getContextAlbum("RequestIDVNFIDAlbum").remove(requestID.toString());

var returnValue = executor.TRUE;

if (vnfID != null) {
	var vcpeClosedLoopStatus = executor.getContextAlbum("VCPEClosedLoopStatusAlbum").get(vnfID.toString());

	var notification = "OPERATION: VNF RESTART WITH RETURN CODE " 
		+ executor.inFields.get("body").get("output").get("status").get("code")
		+ ", "
		+ executor.inFields.get("body").get("output").get("status").get("message");

	vcpeClosedLoopStatus.put("notification", notification);
	vcpeClosedLoopStatus.put("notificationTime", executor.inFields.get("body").get("output").get("common_DasH_header").get("timestamp"));

	executor.outFields.put("requestID", requestID);
	executor.outFields.put("vnfID",     vnfID);
}
else {
	executor.message = "VNF ID not found in context album for request ID " + requestID;
	returnValue = executor.FALSE
}

executor.logger.info(executor.outFields);


