executor.logger.info(executor.subject.id);
executor.logger.info(executor.inFields);

var returnValue = executor.TRUE;

if (executor.inFields.get("onsetFlag")) {
	executor.subject.getTaskKey("VNFOnsetDecideTask").copyTo(executor.selectedTask)	;
}
else {
	executor.subject.getTaskKey("VNFAbatedDecideTask").copyTo(executor.selectedTask)	;
}

executor.logger.info("Decide State Selected Task:" + executor.selectedTask);
