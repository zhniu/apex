executor.logger.info(executor.subject.id);
executor.logger.info(executor.inFields);

var vcpeClosedLoopStatus = executor.getContextAlbum("VCPEClosedLoopStatusAlbum").get(executor.inFields.get("vnfID").toString());

vcpeClosedLoopStatus.put("notification", "VCPE NOTIFICATION HAS BEEN ABATED");
vcpeClosedLoopStatus.put("notificationTime", new Date().toISOString());

executor.logger.info(executor.outFields);

var returnValue = executor.TRUE;
