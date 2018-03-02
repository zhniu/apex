executor.logger.info(executor.subject.id);
executor.logger.info(executor.inFields);

var vcpeClosedLoopStatus = executor.getContextAlbum("VCPEClosedLoopStatusAlbum").get(executor.inFields.get("vnfID").toString());

var guardDecisionAttributes = executor.subject.getOutFieldSchemaHelper("decisionAttributes").createNewInstance();

guardDecisionAttributes.put("actor", "APPC");
guardDecisionAttributes.put("recipe", "Restart");
guardDecisionAttributes.put("target", executor.inFields.get("vnfID").toString());
guardDecisionAttributes.put("clname", "APEXvCPEImplementation");

executor.logger.info(guardDecisionAttributes);

executor.outFields.put("decisionAttributes", guardDecisionAttributes);
executor.outFields.put("onapName", "APEX");

executor.getContextAlbum("ControlLoopExecutionIDAlbum").put(executor.executionID.toString(), executor.inFields.get("vnfID"));

executor.logger.info(executor.outFields);

var returnValue = executor.TRUE;