executor.logger.info(executor.subject.id);
executor.logger.info(executor.inFields);

var vnf = executor.inFields.get("vnf");

executor.logger.info(vnf);

executor.getContextAlbum("VNFAlbum").put(vnf.get("name").toString(), vnf);

executor.outFields.put("vnf", vnf);
executor.outFields.put("message", "VNF " + vnf.get("name").toString() + " initiated");

executor.logger.info(executor.outFields);

var returnValue = executor.TRUE;
