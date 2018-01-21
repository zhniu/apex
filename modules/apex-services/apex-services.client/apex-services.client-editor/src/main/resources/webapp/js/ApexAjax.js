function ajax_get(requestURL, callback) {
	$.ajax({
		type: 'GET',
		url: requestURL,
		dataType: "json", // data type of response
		success: function(data, textStatus, jqXHR){
			pageControl_successStatus(data);
			callback(data);
		},
		error: function(jqXHR, textStatus, errorThrown) {
			pageControl_restError(requestURL, jqXHR, textStatus, errorThrown);
		}	
	});
}

function ajax_getOKOrFail(requestURL, callback) {
	$.ajax({
		type: 'GET',
		url: requestURL,
		dataType: "json", // data type of response
		success: function(data, textStatus, jqXHR){
			pageControl_status(data);
			callback(data);
		},
		error: function(jqXHR, textStatus, errorThrown) {
			pageControl_restError(requestURL, jqXHR, textStatus, errorThrown);
		}	
	});
}

function ajax_put(requestURL, requestData, callback) {
	$.ajax({
		type: 'PUT',
		contentType: 'application/json',
		url: requestURL,
		dataType: "json",
		data: requestData,
		success: function(responseData, textStatus, jqXHR){
			pageControl_successStatus(responseData);
			callback(responseData);
		},
		error: function(jqXHR, textStatus, errorThrown) {
			pageControl_restError(requestURL, jqXHR, textStatus, errorThrown);
		}
	});
}

function ajax_post(requestURL, requestData, callback) {
	$.ajax({
		type: 'POST',
		contentType: 'application/json',
		url: requestURL,
		dataType: "json",
		data: requestData,
		success: function(responseData, textStatus, jqXHR){
			pageControl_successStatus(responseData);
			callback(responseData);
		},
		error: function(jqXHR, textStatus, errorThrown) {
			pageControl_restError(requestURL, jqXHR, textStatus, errorThrown);
		}
	});
}

function ajax_delete(requestURL, callback) {
	$.ajax({
		type: 'DELETE',
		url: requestURL,
		dataType: "json", // data type of response
		success: function(data, textStatus, jqXHR){
			pageControl_successStatus(data);
			callback(data);
		},
		error: function(jqXHR, textStatus, errorThrown) {
			pageControl_restError(requestURL, jqXHR, textStatus, errorThrown);
		}	
	});
}
