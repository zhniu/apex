function modelHandling_analyse() {
	var requestURL = restRootURL + "/Model/Analyse";

	ajax_get(requestURL, function(data) {
		resultForm_activate(document.getElementById("mainArea"), "Model Analysis Result", data.messages.message[0]);
	});
}

function modelHandling_validate() {
	var requestURL = restRootURL + "/Model/Validate";

	ajax_getOKOrFail(requestURL, function(data) {
		var validationResultString = "";
		for (var i = 1; i < data.messages.message.length; i++) {
			validationResultString += (data.messages.message[i] + "\n");
		}
		resultForm_activate(document.getElementById("mainArea"), "Model Validation Result", validationResultString);
	});
}