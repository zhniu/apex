executor.logger.info(executor.subject.id);
executor.logger.info(executor.inFields);

var size = executor.getContextAlbum("AnswerAlbum").size();
var lastAnswer = executor.getContextAlbum("LastAnswerAlbum").get("lastAnswer");

executor.logger.info(size);
executor.logger.info(lastAnswer);

var answer = ++lastAnswer;
if (answer >= size) {
	answer = 0;
}

executor.getContextAlbum("LastAnswerAlbum").put("lastAnswer", answer)

var selectionA = "a" + answer;

executor.logger.info(selectionA);

executor.outFields.put("decision", executor.getContextAlbum("AnswerAlbum").get(selectionA));

executor.logger.info(executor.outFields);

var returnValue = executor.TRUE;