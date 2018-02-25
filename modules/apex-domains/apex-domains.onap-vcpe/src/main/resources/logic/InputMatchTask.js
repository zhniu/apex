executor.logger.info(executor.subject.id);
executor.logger.info(executor.inFields);

executor.outFields.put("genericVNFName", executor.inFields.get("AAI").get("generic_DasH_vnf_DoT_vnf_DasH_name"));
executor.outFields.put("alarmStartTime", executor.inFields.get("closedLoopAlarmStart"));
executor.outFields.put("alarmEndTime"  , executor.inFields.get("closedLoopAlarmEnd"));
executor.outFields.put("status"        , executor.inFields.get("closedLoopEventStatus"));

executor.logger.info(executor.outFields);

var returnValue = executor.TRUE;
