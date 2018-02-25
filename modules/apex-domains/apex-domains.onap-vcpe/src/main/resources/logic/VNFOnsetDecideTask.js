executor.logger.info(executor.subject.id);
executor.logger.info(executor.inFields);

var vnf = executor.getContextAlbum("VNFAlbum").get(executor.inFields.get("vnfName"));

executor.outFields.put("vnfName",               executor.inFields.get("vnfName"));
executor.outFields.put("configurationFileName", "manage_threshold_compliance_breached");

executor.logger.info(executor.outFields);

var returnValue = executor.TRUE;
