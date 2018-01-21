function contextSchemaTab_reset() {
	contextSchemaTab_deactivate();
	contextSchemaTab_activate();
}
function contextSchemaTab_activate() {
	contextSchemaTab_create();

	var requestURL = restRootURL + "/ContextSchema/Get?name=&version=";

	ajax_get(requestURL, function(data) {
		$("#contextSchemaTableBody").find("tr:gt(0)").remove();

		for (var i = 0; i < data.messages.message.length; i++) {
			var contextSchema = JSON.parse(data.messages.message[i]).apexContextSchema;

			var contextSchemaRow_tr = document.createElement("tr");
			var contextSchemaid = contextSchema.key.name + ":"  + contextSchema.key.version;

			var contextSchemaRow =
				"<td>"													+
				contextSchemaid 										+
				"</td>"													+
				"<td>"													+
				contextSchema.schemaFlavour								+
				"</td>"													+
				"<td><java>"											+
				apexUtils_escapeHtml(contextSchema.schemaDefinition)	+
				"</java></td>";

			contextSchemaRow_tr.innerHTML = contextSchemaRow; 
			contextSchemaRow_tr.addEventListener('contextmenu', rightClickMenu_scopePreserver("contextSchemaTabContent", "ContextSchema", contextSchema.key.name, contextSchema.key.version));  

			$("#contextSchemaTableBody").append(contextSchemaRow_tr);
		}
	});
}

function contextSchemaTab_deactivate() {
	apexUtils_removeElement("contextSchemaTabContent");
}

function contextSchemaTab_create() {
	var contextSchemaTab = document.getElementById("contextSchemasTab");

	var contextSchemaTabContent = document.getElementById("contextSchemaTabContent");
	if (contextSchemaTabContent != null) {
		return
	}

	var contextSchemaTabContent = document.createElement("contextSchemaTabContent");
	contextSchemaTab.appendChild(contextSchemaTabContent);
	contextSchemaTabContent.setAttribute("id", "contextSchemaTabContent");
	contextSchemaTabContent.addEventListener('contextmenu', rightClickMenu_scopePreserver("contextSchemaTabContent", "ContextSchema",null,null));  

	var contextSchemaTable = createTable("contextSchemaTable");
	contextSchemaTabContent.appendChild(contextSchemaTable);

	var contextSchemaTableHeader = document.createElement("thead");
	contextSchemaTable.appendChild(contextSchemaTableHeader);
	contextSchemaTableHeader.setAttribute("id", "contextSchemaTableHeader");

	var contextSchemaTableHeaderRow = document.createElement("tr");
	contextSchemaTableHeader.appendChild(contextSchemaTableHeaderRow);
	contextSchemaTableHeaderRow.setAttribute("id", "contextSchemaTableHeaderRow");

	var contextSchemaTableKeyHeader = document.createElement("th");
	contextSchemaTableHeaderRow.appendChild(contextSchemaTableKeyHeader);
	contextSchemaTableKeyHeader.setAttribute("id", "contextSchemaTableKeyHeader");
	contextSchemaTableKeyHeader.appendChild(document.createTextNode("Context Item"));

	var contextSchemaTableSchemaFlavourHeader = document.createElement("th");
	contextSchemaTableHeaderRow.appendChild(contextSchemaTableSchemaFlavourHeader);
	contextSchemaTableSchemaFlavourHeader.setAttribute("id", "contextSchemaTableJavatypeHeader");
	contextSchemaTableSchemaFlavourHeader.appendChild(document.createTextNode("Schema Flavour"));

	var contextSchemaTableSchemaDefHeader = document.createElement("th");
	contextSchemaTableHeaderRow.appendChild(contextSchemaTableSchemaDefHeader);
	contextSchemaTableSchemaDefHeader.setAttribute("id", "contextSchemaTableJavatypeHeader");
	contextSchemaTableSchemaDefHeader.appendChild(document.createTextNode("Context Item Schema Definition"));

	var contextSchemaTableBody = document.createElement("tbody");
	contextSchemaTable.appendChild(contextSchemaTableBody);
	contextSchemaTable.setAttribute("id", "contextSchemaTableBody");
}