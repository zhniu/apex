executor.logger.info(executor.subject.id);
executor.logger.info(executor.inFields);

var vnfID = executor.getContextAlbum("ControlLoopExecutionIDAlbum").remove(executor.executionID);

executor.logger.info(vnfID);

executor.outFields.put("onsetFlag", onsetFlag);
executor.logger.info(executor.outFields);
