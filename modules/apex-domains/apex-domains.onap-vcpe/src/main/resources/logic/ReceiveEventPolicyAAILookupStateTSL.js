executor.logger.info(executor.subject.id);
executor.logger.info(executor.inFields);

var returnValue = executor.TRUE;

var vcpeClosedLoopStatus = executor.getContextAlbum("VCPEClosedLoopStatusAlbum").get(executor.inFields.get("vnfID").toString());
var aaiInfo = vcpeClosedLoopStatus.get("AAI");

executor.logger.info(aaiInfo);

if (aaiInfo.get("generic_DasH_vnf.resource_DasH_version")                         != null
		&& aaiInfo.get("generic_DasH_vnf.vnf_DasH_name")                          != null
		&& aaiInfo.get("generic_DasH_vnf.prov_DasH_status")                       != null
		&& aaiInfo.get("generic_DasH_vnf.is_DasH_closed_DasH_loop_DasH_disabled") != null
		&& aaiInfo.get("generic_DasH_vnf.orchestration_DasH_status")              != null
		&& aaiInfo.get("generic_DasH_vnf.vnf_DasH_type")                          != null
		&& aaiInfo.get("generic_DasH_vnf.in_DasH_maint")                          != null
		&& aaiInfo.get("generic_DasH_vnf.service_DasH_id")                        != null
		&& aaiInfo.get("generic_DasH_vnf.vnf_DasH_id")                            != null) {
	executor.subject.getTaskKey("AAILookupRequestTask").copyTo(executor.selectedTask);
}
else {
	executor.subject.getTaskKey("NoAAILookupTask").copyTo(executor.selectedTask);
}

executor.logger.info("ReceiveEventPolicyOnsetOrAbatedStateTSL State Selected Task:" + executor.selectedTask);
