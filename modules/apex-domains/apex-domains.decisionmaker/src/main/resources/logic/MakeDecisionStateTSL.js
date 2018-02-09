executor.logger.info(executor.subject.id);
executor.logger.info(executor.inFields);

var returnValue = executor.TRUE;

if (executor.inFields.get("mode").equals("random")) {
	executor.subject.getTaskKey("RandomAnswerTask").copyTo(executor.selectedTask)	;
}
else if (executor.inFields.get("mode").equals("pessimistic")) {
	executor.subject.getTaskKey("PessimisticAnswerTask").copyTo(executor.selectedTask)	;
}
else if (executor.inFields.get("mode").equals("optimistic")) {
	executor.subject.getTaskKey("OptimisticAnswerTask").copyTo(executor.selectedTask)	;
}
else if (executor.inFields.get("mode").equals("dithering")) {
	executor.subject.getTaskKey("DitheringAnswerTask").copyTo(executor.selectedTask)	;
}
//else if (executor.inFields.get("mode").equals("roundrobin")) {
//	executor.subject.getTaskKey("RoundRobinAnswerTask").copyTo(executor.selectedTask)	;
//}

executor.logger.info("Answer Selected Task:" + executor.selectedTask);
