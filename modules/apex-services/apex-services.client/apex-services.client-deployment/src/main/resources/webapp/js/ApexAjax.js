/*
 * Send a GET request
 */
function ajax_get(requestURL, callback, hostName, port, params, errorCallback) {
	var data = {
		hostName: hostName,
		port: port
	};
	for(var p in params) {
		data[p] = params[p];
	}
	return $.ajax({
		type: 'GET',
		url: requestURL,
		dataType: "json",
		data: data,
		success: function(data, textStatus, jqXHR){
			if(callback) {
				callback(data);
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			if(jqXHR.status==500 || jqXHR.status==404) {
				if(jqXHR.responseText.indexOf("cound not handshake with server") !== -1 || jqXHR.status==404) {
					clearEngineURL();
					getEngineURL(jqXHR.responseText);
				} else {
					apexErrorDialog_activate(document.body, jqXHR.responseText);
				}
			}
			if(errorCallback) {
				errorCallback(jqXHR, textStatus, errorThrown);
			}
		}	
	});
}

/*
 * Send a POST request and add a file to its payload
 */
function ajax_upload(requestURL, callback, hostName, port, fileUrl, ignoreConflicts, forceUpdate) {
    var formData = new FormData();
    formData.append("hostName", hostName);
    formData.append("port", port);
    formData.append("file", fileUrl);
    formData.append("ignoreConflicts", ignoreConflicts);
    formData.append("forceUpdate", forceUpdate);
    return $.ajax({
        url: requestURL,
        type: "POST",
        contentType: false,
        dataType: "text",
        processData: false,
        data: formData,
        success: function(data, textStatus, jqXHR){
			callback(data);
		},
        error: function (jqXHR, textStatus, errorThrown) {
        	if(jqXHR.status==500) {
        		apexErrorDialog_activate(document.body, jqXHR.responseText);
			}
        }
    });
}
