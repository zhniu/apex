function formUtils_generateUUID() { // Public Domain/MIT
	var d = new Date().getTime();
	if (typeof performance !== 'undefined' && typeof performance.now === 'function'){
		d += performance.now(); //use high-precision timer if available
	}
	return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
		var r = (d + Math.random() * 16) % 16 | 0;
		d = Math.floor(d / 16);
		return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
	});
}

function formUtils_generateDescription(name, version, uuid) {
	if (!name) {
		name = "null";
	}
	if (!version) {
		version = "null";
	}
	if (!uuid) {
		uuid = "null";
	}

	var description =
	"Generated description for a concept called \"" +
	name                                            +
	"\" with version \""                            +
	version                                         +
	"\" and UUID \""                                +
	uuid                                            +
	"\"";
	return description;
}

