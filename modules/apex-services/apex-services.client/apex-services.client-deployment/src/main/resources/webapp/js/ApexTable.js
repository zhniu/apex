/*
 * Create a table with given headers
 */
function createEngineTable(parent, id, tableHeaders) {
	var table = createTable(id);

	var tableHead = document.createElement("thead");
	table.appendChild(tableHead);
	tableHead.setAttribute("id", "engineTableHeader");

	var tableHeaderRow = document.createElement("tr");
	tableHead.appendChild(tableHeaderRow);
	tableHeaderRow.setAttribute("id", "engineTableHeaderRow");

	for (var t in tableHeaders) {
		var tableHeader = document.createElement("th");
		tableHeaderRow.appendChild(tableHeader);
		tableHeader.setAttribute("id", "engineTableHeader");
		tableHeader.appendChild(document.createTextNode(tableHeaders[t]));
	}

	var tableBody = document.createElement("tbody");
	tableBody.setAttribute("id", "engineTableBody");
	table.appendChild(tableBody);

	parent.append(table);

	return table;
}

/*
 * Create a table and apply UISDK styles to it
 */
function createTable(id) {
	var table = document.createElement("table");
	table.setAttribute("id", id);
	table.setAttribute("class", "apexTable ebTable elTablelib-Table-table ebTable_striped");
	return table;
}