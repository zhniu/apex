executor.logger.info(executor.subject.id);
executor.logger.info(executor.inFields);

var eventList = executor.subject.getOutFieldSchemaHelper("EventList").createNewInstance();

var eventType = Java.type("com.ericsson.apex.service.engine.event.ApexEvent");

for (i = 0; i < 5; i++) {
	var event = new eventType("InputEvent", "0.0.1", "com.ericsson.apex.periodic", "APEX", "APEX");

	var par0 = "Hello: " + i;
	var par1 = "Goodbye: " + i;
	
	event.put("Par0", par0);
	event.put("Par1", par1);

	eventList.add(event);
}

executor.outFields.put("EventList", eventList);

executor.logger.info(executor.outFields);

var returnValue = executor.TRUE;
