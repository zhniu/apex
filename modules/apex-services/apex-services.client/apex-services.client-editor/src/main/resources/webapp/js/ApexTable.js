function createTable(id) {
	var table = document.createElement("table");
	table.setAttribute("id", id);
	table.setAttribute("class", "apexTable ebTable elTablelib-Table-table ebTable_striped");
	return table;
}

function setRowHover(rowElement) {
	rowElement.className = "ebTableRow";
	rowElement.onmouseover = function() {
		this.className = "ebTableRow ebTableRow_hover";
	};
	rowElement.onmouseout = function() {
		this.className = "ebTableRow";
	};
}
