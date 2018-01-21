executor.logger.debug(executor.subject.id);
var gc = executor.getContextAlbum("GlobalContextAlbum");
executor.logger.debug(gc.name);
executor.logger.debug(executor.inFields);

var caseSelectedType = Java.type("java.lang.Byte");
executor.outFields.put("Test<STATE_NAME>CaseSelected", new caseSelectedType(<RANDOM_BYTE_VALUE>));

var JavaDate = Java.type("java.util.Date");
timeNow = new JavaDate();
executor.outFields.put("Test<STATE_NAME>StateTime", timeNow.getTime());
executor.logger.debug(executor.eo);

var returnValue = executor.TRUE;
