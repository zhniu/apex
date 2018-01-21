function contextAlbumTab_reset() {
	contextAlbumTab_deactivate();
	contextAlbumTab_activate();
}

function contextAlbumTab_activate() {
	contextAlbumTab_create();
	
	var requestURL = restRootURL + "/ContextAlbum/Get?name=&version=";

	ajax_get(requestURL, function(data) {
		$("#contextAlbumTableBody").find("tr:gt(0)").remove();

		for (var i = 0; i < data.messages.message.length; i++) {
			var contextAlbum = JSON.parse(data.messages.message[i]).apexContextAlbum;

			var contextAlbumRow_tr = document.createElement("tr");
			var contextAlbumid = contextAlbum.key.name + ":"  + contextAlbum.key.version;

			
			var contextAlbumTableRow = 
				"<td>"                                          	+
				contextAlbum.key.name + ":"  + contextAlbum.key.version +
				"</td>"                                             +
				"<td>"                                              +
				contextAlbum.scope                                  +
				"</td>"                                             +
				"<td>"                                              +
				contextAlbum.isWritable                             +
				"</td>"                                             +
				"<td>"                                              +
				contextAlbum.itemSchema.name + ":"  + contextAlbum.itemSchema.version +
				"</td>"   ;

			contextAlbumRow_tr.innerHTML = contextAlbumTableRow; 
			contextAlbumRow_tr.addEventListener('contextmenu', rightClickMenu_scopePreserver("contextAlbumTabContent", "contextAlbum", contextAlbum.key.name, contextAlbum.key.version));  

			$("#contextAlbumTableBody").append(contextAlbumRow_tr);

		}
	});
}

function contextAlbumTab_deactivate() {
	apexUtils_removeElement("contextAlbumTabContent");
}

function contextAlbumTab_create() {
	var contextAlbumTab = document.getElementById("contextAlbumsTab");

	var contextAlbumTabContent = document.getElementById("contextAlbumTabContent");
	if (contextAlbumTabContent != null) {
		return
	}

	var contextAlbumTabContent = document.createElement("contextAlbumTabContent");
	contextAlbumTab.appendChild(contextAlbumTabContent);
	contextAlbumTabContent.setAttribute("id", "contextAlbumTabContent");
	contextAlbumTabContent.addEventListener('contextmenu', rightClickMenu_scopePreserver("contextAlbumTabContent", "contextAlbum",null, null));  

	var contextAlbumTable = createTable("contextAlbumTable");
	contextAlbumTabContent.appendChild(contextAlbumTable);

	var contextAlbumTableHeader = document.createElement("thead");
	contextAlbumTable.appendChild(contextAlbumTableHeader);
	contextAlbumTableHeader.setAttribute("id", "contextAlbumTableHeader");

	var contextAlbumTableHeaderRow = document.createElement("tr");
	contextAlbumTableHeader.appendChild(contextAlbumTableHeaderRow);
	contextAlbumTableHeaderRow.setAttribute("id", "contextAlbumTableHeaderRow");

	var contextAlbumTableKeyHeader = document.createElement("th");
	contextAlbumTableHeaderRow.appendChild(contextAlbumTableKeyHeader);
	contextAlbumTableKeyHeader.setAttribute("id", "contextAlbumTableKeyHeader");
	contextAlbumTableKeyHeader.appendChild(document.createTextNode("Context Album"));

	var contextAlbumTableScopeHeader = document.createElement("th");
	contextAlbumTableHeaderRow.appendChild(contextAlbumTableScopeHeader);
	contextAlbumTableScopeHeader.setAttribute("id", "contextAlbumTableScopeHeader");
	contextAlbumTableScopeHeader.appendChild(document.createTextNode("Scope"));

	var contextAlbumTableWriteableHeader = document.createElement("th");
	contextAlbumTableHeaderRow.appendChild(contextAlbumTableWriteableHeader);
	contextAlbumTableWriteableHeader.setAttribute("id", "contextAlbumTableWritableHeader");
	contextAlbumTableWriteableHeader.appendChild(document.createTextNode("Writable"));

	var contextAlbumTableItemSchemaHeader = document.createElement("th");
	contextAlbumTableHeaderRow.appendChild(contextAlbumTableItemSchemaHeader);
	contextAlbumTableItemSchemaHeader.setAttribute("id", "contextAlbumTableItemSchemaHeader");
	contextAlbumTableItemSchemaHeader.appendChild(document.createTextNode("Item Schema"));

	var contextAlbumTableBody = document.createElement("tbody");
	contextAlbumTable.appendChild(contextAlbumTableBody);
	contextAlbumTable.setAttribute("id", "contextAlbumTableBody");
}