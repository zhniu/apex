executor.logger.debug(executor.subject.id);
executor.logger.debug(executor.inFields);

var JavaDate = Java.type("java.util.Date");
timeNow = new JavaDate();
executor.outFields.put("State<STATE_NUMBER>Timestamp", timeNow.getTime());
executor.logger.debug(executor.outFields);

var returnValue = executor.TRUE;
			
