
function showHideElement_display(showhide, element, _showstyle,
		_hidestyle, _buttonshowstyle, _buttonhidestyle) {
	var checkbox = $(showhide).find('input:checkbox:first');
	if (checkbox) {
		checkbox.change(function(event) {
			$(element).toggle("fast");
		});
	} else {
		if (_buttonshowstyle) {
			showhide.classList.remove(_buttonshowstyle);
		}
		if (_buttonhidestyle) {
			showhide.classList.add(_buttonhidestyle);
		}
		showhide.onclick = function(event) {
			$(element).toggle("fast");
		};
	}
}


function showHideElement(id_prefix, element, _initialhide, _showText, _hideText, _showstyle, _hidestyle, _buttonshowstyle, _buttonhidestyle) {
	var retdiv = document.createElement("div");
	var divname = id_prefix;
	retdiv.setAttribute("id", divname); 
	retdiv.setAttribute("class","showHideElement");
	var showhide = document.createElement("div");
	retdiv.appendChild(showhide);
	showhide.setAttribute("id", divname+"_showhide");
	showhide.innerHTML = '<label class="ebSwitcher"><input type="checkbox" class="ebSwitcher-checkbox" /><div class="ebSwitcher-body"><div class="ebSwitcher-onLabel">Show</div><div class="ebSwitcher-switch"></div><div class="ebSwitcher-offLabel">Hide</div></div></label>';
		
	retdiv.appendChild(element);
	if(_initialhide != null && _initialhide === true){
		element.style.display="none";
	}
	else{
		element.style.display="block";
	}
	showHideElement_display(showhide, element, _showstyle, _hidestyle, undefined, undefined);
	return retdiv;
};

