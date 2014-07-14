/**
 * Browser-independent [A]JAX call
 * 
 * @param {String} locationURL an URL to call, without parameters
 * @param {String} [parameters=null] a parameters list, in the form 
 *        'param1=value1&param2=value2&param3=value3'
 * @param {Function(XHMLHTTPRequest, Object)} [onComplete=null] a function that
 *        will be called when the response (responseText or responseXML of 
 *        XHMLHTTPRequest) will be received
 * @param {Boolean} [doSynchronous=false] make a synchronous request (onComplete
 *        will /not/ be called)        
 * @param {Boolean} [doPost=false] make a POST request instead of GET        
 * @param {Object} [dataPackage=null] any object to transfer to the onComplete 
 *        listener
 * @return {XHMLHTTPRequest} request object, if no exceptions occured
 */
function makeRequest(locationURL, parameters, onComplete, doSynchronous, doPost, dataPackage) {

    var http_request = false;
    try {
        http_request = new ActiveXObject("Msxml2.XMLHTTP");
    } catch (e1) {
        try {
            http_request= new ActiveXObject("Microsoft.XMLHTTP");
        } catch (e2) {
            http_request = new XMLHttpRequest();
        }
    }
 
    //if (http_request.overrideMimeType) { // optional
    //  http_request.overrideMimeType('text/xml');
    //}
 
    if (!http_request) {
      alert('Cannot create XMLHTTP instance');
      return false;
    }

    if (onComplete && !doSynchronous) {
        completeListener = function() { 
            if (http_request.readyState == 4) {
                if (http_request.status == 200) {
                    onComplete(http_request, dataPackage)
                }
            }
        };
        http_request.onreadystatechange = completeListener;
    }

    //var salt = hex_md5(new Date().toString());
    if (doPost) {
        http_request.open('POST', locationURL, !doSynchronous);
        http_request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        http_request.setRequestHeader("Content-length", parameters.length);
        http_request.setRequestHeader("Connection", "close");
        http_request.send(parameters);
    } else {
        http_request.open('GET', locationURL + (parameters ? ("?" + parameters) : ""), !doSynchronous);
        http_request.send();
    }
 
    return http_request;
 
}

function TryOpen(url) {
    try {
        //var request = makeRequest(url, null, null, true);
	if (url.substr(0,7) == "../http") url = url.substr(3, url.length - 3);
        window.open(url, '_blank');
    } catch(err) {
        alert('Not available yet');
    }
    return false;
}

function TryDownload(url) {
    try {
        var request = makeRequest(url, null, null, true);
        window.open(url,'Download');
    } catch(err) {
        alert('Not available yet');
    }
    return false;
}

/* Client-side access to querystring name=value pairs
	Version 1.3
	28 May 2008
	
	License (Simplified BSD):
	http://adamv.com/dev/javascript/qslicense.txt
*/
function Querystring(qs) { // optionally pass a querystring to parse
	this.params = {};
	
	if (qs == null) qs = location.hash.substring(1, location.hash.length);
	if (qs.length == 0) return;

// Turn <plus> back to <space>
// See: http://www.w3.org/TR/REC-html40/interact/forms.html#h-17.13.4.1
	qs = qs.replace(/\+/g, ' ');
	var args = qs.split('&'); // parse out name/value pairs separated via &
	
// split out each name=value pair
	for (var i = 0; i < args.length; i++) {
		var pair = args[i].split('=');
		var name = decodeURIComponent(pair[0]);
		
		var value = (pair.length==2)
			? decodeURIComponent(pair[1])
			: name;
		
		this.params[name] = value;
	}
}

Querystring.prototype.get = function(key, default_) {
	var value = this.params[key];
	return (value != null) ? value : default_;
}

Querystring.prototype.contains = function(key) {
	var value = this.params[key];
	return (value != null);
}


/**
 * Loads any XML document using ActiveX (for IE) or createDocumentFunction (for 
 * other browsers)
 * @param {String} fileName name of the file to be loaded
 * @return {XMLDocument|Object}
 */
function loadXML(fileName)
{ // http://www.w3schools.com/xsl/xsl_client.asp    
    var xmlFile = null;
 
    if (window.ActiveXObject)
	{ // IE
        xmlFile = new ActiveXObject("Microsoft.XMLDOM");
    }
	else if (document.implementation && document.implementation.createDocument)
	{ // Mozilla, Firefox, Opera, etc.
        xmlFile = document.implementation.createDocument("","",null);
        if (!xmlFile.load)
		{ // Safari lacks on this method, so we make a synchronous XMLHttpRequest
            var request = makeRequest(fileName, null, null, true);
            return request.responseXML;
        }
    }
	else
	{
        alert('Your browser cannot create XML DOM Documents');
    }
    xmlFile.async = false;
    try
	{
        xmlFile.load(fileName);
    }
	catch(e)
	{
        alert('an error occured while loading XML file ' + fileName);
    }
    return(xmlFile);
}


function transformXMLDoc(xml_name, xsl_name, anchor_id)
{
	xml=loadXML(xml_name);
	xsl=loadXML(xsl_name);
	var node = document.getElementById(anchor_id);
	// code for IE
	if (window.ActiveXObject)
	  {
	  resultDocument=xml.transformNode(xsl);
	  var html = node.innerHTML;
	  node.innerHTML = html + resultDocument;
	  }
	// code for Mozilla, Firefox, Opera, etc.
	else if (document.implementation 
	&& document.implementation.createDocument)
	  {
	  xsltProcessor=new XSLTProcessor();
	  xsltProcessor.importStylesheet(xsl);
	  resultDocument = xsltProcessor.transformToFragment(xml,document);
	  node.appendChild(resultDocument);
	  }
}

function display(content_name, anchor_id, append)
{
	//remove old items
	if (!append) {
		var doc = document.getElementById(anchor_id);
		while ( doc.childNodes.length >= 1 ) {
			doc.removeChild( doc.lastChild );       
		}
	}
	resetHeight();
	
	// Get the menu xml document
	var xmlDoc = loadXML("xml/menu.xml");
	var num = 0;
	// Get all "item" elements from the xml document
	var items = xmlDoc.getElementsByTagName("item");
	if(items.length > 0) {
		// Loop through these elements. Each one contains a row of the table. 
		for(var i=0; i < items.length; i++) {
		// Now get the element that contains our attribute name 
			if (items[i].getAttribute("name") == content_name) {
				var transformation = items[i].getAttribute("transformation");
				// Get all resources for category 
				var resources = items[i].getElementsByTagName("resource");
				num += resources.length;
				// Transform the resources
				for(var j=0; j < resources.length; j++) {
					transformXMLDoc(resources[j].childNodes[0].nodeValue, transformation, anchor_id);	//alert(resources[j].childNodes[0].nodeValue +" " + transformation + " " + anchor_id);					
				}		
			} 		
		} 
	}
	
	fitContent();
	initalizetooltip();
	return num;
}

function displayContent(content_name)
{
	document.location.hash = currentAnchor = "#content=" + content_name;	
	if(0 == display(content_name, "content", false)) {
		display("Null", "content", false);
	}
}

 var currentAnchor = null; 
 
function Load()
{
	document.onmousemove = positiontip;
	// Parse the current page's querystring
	var qs = new Querystring();
	var v = (qs != null) ? qs.get("content", "Agenda") : "Agenda"; 	
	displayContent(v);
	display("Menu", "menu", false);
	//On load page, init the timer which check if the there are anchor changes each 300 ms
	setInterval("checkAnchor()", 200);
}

function LoadAll()
{
	display("Research", "content", true);
	display("Industrial", "content", true);
	display("Demos", "content", true);
	display("Tutorials", "content", true);
	display("Keynotes", "content", true);
	display("Panels", "content", true);
}

function LoadFull()
{
    	if (location.hash.indexOf("#print") >= 0) {
	    transformXMLDoc("../FullProgram.xml", "./PrintProgram.xsl", "content");
	} else {
	    transformXMLDoc("../FullProgram.xml", "./FullProgram.xsl", "content");
	}
	if (location.hash != "") {
		var el = document.getElementById(location.hash.substring(1,location.hash.length-1)); //alert(el);
		if (el != null) {
			var new_position = el.offset();
             		window.scrollTo(new_position.left,new_position.top);
		}
	}
}

 //Function which checks if there are anchor changes, if there are, reloads 
 function checkAnchor(){  
     //Check if it has changes  
     if(currentAnchor != decodeURIComponent(document.location.hash)){
		// alert(currentAnchor + " | " + document.location.hash);
	     var qs = new Querystring();
		var v = (qs != null) ? qs.get("content", "Home") : "Home";
		//if there is not anchor, the loads the default section
		displayContent(v); 
     }
 }
 
 function foldContent(obj){
	 if(obj.parentNode.className.match(/_folded$/)){
		obj.parentNode.className = obj.parentNode.className.substring(0,obj.parentNode.className.length-7);
	 }else{
		obj.parentNode.className = obj.parentNode.className + "_folded";
	 }
	 resetHeight();
	 fitContent();
 }

 function resetHeight() {
	 //reset the height
	var sidebar = document.getElementById("sidebar");
	var content = document.getElementById("content");
	if (sidebar != null) sidebar.style.height = 'auto';
	if (content != null) content.style.height = 'auto';
 }

 function fitContent(){
	var sidebar = document.getElementById("sidebar");
	var content = document.getElementById("content");
	if (sidebar == null | content == null) return;

	 //Stretch content and sidebar height
	 var sidebarHeight = sidebar.offsetHeight;
	 var contentHeight = content.offsetHeight;
	 
	var height = Math.max(contentHeight, sidebarHeight);
	
	content.style.height = height + 'px';
	if (sidebarHeight < height) {
		height = height - 12;
	}
	sidebar.style.height = height + 'px';
 }
