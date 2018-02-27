executor.logger.info(executor.subject.id);
executor.logger.info(executor.inFields);

var vcpeClosedLoopStatus = executor.getContextAlbum("VCPEClosedLoopStatusAlbum").get(executor.inFields.get("vnfID").toString());

var status = vcpeClosedLoopStatus.get("closedLoopEventStatus");

var returnValue = executor.TRUE;
var onsetFlag;

if (status === "ONSET") {
	executor.subject.getTaskKey("OnsetGuardRequestTask").copyTo(executor.selectedTask);
}
else if (status === "ABATED") {
	executor.subject.getTaskKey("ControlLoopLogTask").copyTo(executor.selectedTask);
	onsetFlag = executor.FALSE;
}
else {
	executor.message = "closedLoopEventStatus must be either \"ONSET\" or \"ABATED\"";
	returnValue = executor.FALSE;
}

executor.outFields.put("onsetFlag", onsetFlag);
executor.logger.info(executor.outFields);
