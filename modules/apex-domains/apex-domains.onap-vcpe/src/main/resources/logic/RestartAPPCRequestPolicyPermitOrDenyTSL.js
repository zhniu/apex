executor.logger.info(executor.subject.id);
executor.logger.info(executor.inFields);

var vcpeClosedLoopStatus = executor.getContextAlbum("VCPEClosedLoopStatusAlbum").get(executor.inFields.get("vnfID").toString());

var guardResult = vcpeClosedLoopStatus.get("notification");

if (guardResult === "OPERATION: GUARD_PERMIT") {
	executor.subject.getTaskKey("APPCRestartVNFRequestTask").copyTo(executor.selectedTask);
}
else  {
	executor.subject.getTaskKey("DeniedTask").copyTo(executor.selectedTask);
}

executor.logger.info("RestartAPPCRequestPolicyPermitOrDenyTSL State Selected Task:" + executor.selectedTask);

var returnValue = executor.TRUE;

