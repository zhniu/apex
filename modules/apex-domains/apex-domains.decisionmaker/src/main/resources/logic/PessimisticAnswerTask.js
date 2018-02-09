executor.logger.info(executor.subject.id);
executor.logger.info(executor.inFields);

var size = executor.getContextAlbum("AnswerAlbum").size();

var selection = Math.floor(Math.random() * size / 2);

var selectionA = "a" + selection;

executor.logger.info(size);
executor.logger.info(selectionA);

executor.outFields.put("decision", executor.getContextAlbum("AnswerAlbum").get(selectionA));

executor.logger.info(executor.outFields);

var returnValue = executor.TRUE;
