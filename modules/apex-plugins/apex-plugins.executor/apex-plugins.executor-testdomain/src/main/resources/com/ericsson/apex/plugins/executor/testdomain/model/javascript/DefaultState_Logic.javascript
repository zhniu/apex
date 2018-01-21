executor.logger.debug(executor.subject.id);
var gc = executor.getContextAlbum("GlobalContextAlbum");
executor.logger.debug(gc.name);
executor.subject.defaultTaskKey.copyTo(executor.selectedTask)

var returnValue = executor.TRUE;

