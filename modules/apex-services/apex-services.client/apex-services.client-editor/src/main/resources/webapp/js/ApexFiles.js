function files_fileOpen() {
	$('<input type="file">').on('change', function () {
		var reader = new FileReader();
		modelFileName = this.files[0].name;
		reader.readAsText(this.files[0]);

		reader.onload = function(event) {
			var requestURL = restRootURL + "/Model/Load";
			ajax_put(requestURL, event.target.result, function(resultData) {
				localStorage.setItem("apex_model_loaded", true);
				var requestURL = restRootURL + "/Model/GetKey";
				ajax_get(requestURL, function(data) {
					var modelKey = JSON.parse(data.messages.message[0]).apexArtifactKey;
					pageControl_modelMode(modelKey.name, modelKey.version, modelFileName);
				});
			});
		};
	}).click();
}

function files_fileDownload() {
	var requestURL = restRootURL + "/Model/Download";

	var downloadLink = document.createElement("a");
	document.body.appendChild(downloadLink);
	downloadLink.download = modelFileName;
	downloadLink.href = requestURL;
	downloadLink.click();
	document.body.removeChild(downloadLink);
}

