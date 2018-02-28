executor.logger.info(executor.subject.id);
executor.logger.info(executor.inFields);

var vcpeClosedLoopStatus = executor.getContextAlbum("VCPEClosedLoopStatusAlbum").get( executor.inFields.get("vnfID").toString());

executor.logger.info("Logging context information for VNF \"" + executor.inFields.get("vnfID") + "\"");

executor.outFields.put("AAI",                   vcpeClosedLoopStatus.get("AAI"));
executor.outFields.put("closedLoopControlName", vcpeClosedLoopStatus.get("closedLoopControlName"));
executor.outFields.put("closedLoopAlarmStart",  vcpeClosedLoopStatus.get("closedLoopAlarmStart"));
executor.outFields.put("closedLoopAlarmEnd",    vcpeClosedLoopStatus.get("closedLoopAlarmEnd"));
executor.outFields.put("closedLoopEventClient", vcpeClosedLoopStatus.get("closedLoopEventClient"));
executor.outFields.put("closedLoopEventStatus", vcpeClosedLoopStatus.get("closedLoopEventStatus"));
executor.outFields.put("version",               vcpeClosedLoopStatus.get("version"));
executor.outFields.put("requestID",             vcpeClosedLoopStatus.get("requestID"));
executor.outFields.put("target_type",           vcpeClosedLoopStatus.get("target_type"));
executor.outFields.put("target",                vcpeClosedLoopStatus.get("target"));
executor.outFields.put("from",                  vcpeClosedLoopStatus.get("from"));
executor.outFields.put("policyScope",           vcpeClosedLoopStatus.get("policyScope"));
executor.outFields.put("policyName",            vcpeClosedLoopStatus.get("policyName"));
executor.outFields.put("policyVersion",         vcpeClosedLoopStatus.get("policyVersion"));
executor.outFields.put("notification",          vcpeClosedLoopStatus.get("notification"));
executor.outFields.put("notificationTime",      vcpeClosedLoopStatus.get("notificationTime"));

executor.logger.info(executor.outFields);

var returnValue = executor.TRUE;
