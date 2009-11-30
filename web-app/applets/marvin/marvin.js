//////////////////////////////////////////////////////////////////////////
//// Marvin utility functions
//// Copyright (c) 1998-2009 ChemAxon Ltd., Peter Csizmadia,
////                          Ferenc Csizmadia, Tamas Vertse, Gabor Bartha
//////////////////////////////////////////////////////////////////////////

//
// "Public" parameters that can be specified before msketch_begin/mview_begin.
//

// The MAYSCRIPT attribute for MarvinView.
var mview_mayscript = false;

// The MAYSCRIPT attribute for MarvinSketch.
var msketch_mayscript = false;

// The MAYSCRIPT attribute for MarvinSketch.
var mspace_mayscript = false;

// Applet names, unspecified by default
var msketch_name = "";
var mview_name = "";
var mspace_name = "";

var msketch_legacy=!isLeopardSafari();
var mview_legacy=!isLeopardSafari();
var mspace_legacy=!isLeopardSafari();

// Applet can use these additional jar files. If more then one additional
// file are used, files has to be separated by colon. 
// e.g to generate svg from the applet, the batik-core.jar has to be used.
var msketch_usedJars = "";

// Use "builtin" for the browser's default JVM, "plugin" for the Java Plugin.
var marvin_jvm = "";

// GUI used: "awt" or "swing"
var marvin_gui = "";

//
// Internal functions
//

var marvin_jvm0 = "";
var marvin_gui0 = "";
var applet_type; // depends on marvin_jvm, 0=<applet>, 1=<embed>, 2=<object>

// displays an image on the applet's canvas while applet is loading
var loading_image = "img/loading.gif";

// Set marvin_jvm if the URL of the HTML page has a jvm parameter.
if(location.search.lastIndexOf("jvm=plugin") >= 0) {
	marvin_jvm0 = "plugin";
	if(browser_mozilla_version()==5) {//builtin if Netscape 6-
	    marvin_jvm0 = "builtin";
	}
}
if(location.search.lastIndexOf("jvm=builtin") >= 0) {
	marvin_jvm0 = "builtin";
}

// Set marvin_gui if the location string contains the gui parameter.
if(location.search.lastIndexOf("gui=swing") >= 0) {
	marvin_gui0 = "swing";
}
if(location.search.lastIndexOf("gui=awt") >= 0) {
	marvin_gui0 = "awt";
}

var _appletstrbuf = "";

function browser_parse_version0(name) {
	var brz = navigator.userAgent;
	var i = brz.lastIndexOf(name);
	if(i >= 0) {
		var s = brz.substring(i + name.length);
		var j = s.indexOf(".");
		if(j < 0) {
			j = s.indexOf(" ");
		}
		return s.substring(0, j);
	}
	return 0;
}

function browser_parse_version(name) {
	var v = browser_parse_version0(name + "/");
	if(!v) {
		v = browser_parse_version0(name + " ");
	}
	return v;
}

// Returns mozilla version for mozilla and compatible browsers.
function browser_mozilla_version() {
	var s = navigator.userAgent;

	// indexOf is buggy in Netscape 3
	if(s.lastIndexOf("Mozilla/3.") == 0) {
		return 3;
	} else if(s.lastIndexOf("Mozilla/") == 0) {
		return s.substring(8, s.indexOf("."));
	} else {
		return 0;
	}
}

// Returns browser version in Opera, 0 in other browsers.
function browser_Opera() {
	return browser_parse_version("Opera");
}

// Returns mozilla version in Netscape, 0 in other browsers.
function browser_NetscapeMozilla() {
	var brz = navigator.userAgent;
	var compat = brz.toLowerCase().lastIndexOf("compatible") >= 0;
	var opera = browser_Opera();
	if(brz.lastIndexOf("Mozilla/") == 0 && !compat && !opera) {
		return browser_mozilla_version();
	} else {
		return 0;
	}
}

// Returns browser version in MSIE, 0 in other browsers.
function browser_MSIE() {
	var msie = navigator.appName.lastIndexOf("Microsoft Internet Explorer") == 0;
	var opera = browser_Opera();
	if(msie && !opera) {
		return browser_parse_version("MSIE");
	}
	return 0;
}

//Returns the OS version (9 or 10) if it is mac, 0 if isn't.
function macOsVer() {
	var v = navigator.appVersion;
	var vv = navigator.userAgent.toLowerCase();
	var mac = 0;
	if(v.indexOf("Mac") > 0) {
		mac = 9;
		if(vv.indexOf("os x") > 0) {
			mac = 10;
		}
	}
	return mac;
}

function isLeopardSafari() {
    var agent = navigator.userAgent;
    var isLeopard = agent.lastIndexOf("Intel Mac OS X 10_5") > 0 || agent.lastIndexOf("Intel Mac OS X 10.5") > 0;
    if(isLeopard) {
        return agent.lastIndexOf("Safari/") > 0;
    }
    return false;
}

function marvin_default_jvm()
{
    var osver = macOsVer();
    var mozver = browser_NetscapeMozilla();
    // Mac always use built-in Java
    // Netscape 4 prefers built-in Java
    if(osver >= 9 || mozver == 4) {
        return "builtin";
    } else {
        return "plugin";
    }
}

// Determines default GUI (Swing or AWT) from JVM and browser type.
function marvin_default_gui(jvm)
{
    var osver = macOsVer();
    var mozver = browser_NetscapeMozilla();
    // Only OS 9 and Netscape 4 uses AWT
    if(osver == 9 || mozver == 4) {
	return "awt"
    } else {
	return "swing";
    }
}

var mayscrDefined = false;

function applet_begin(jvm, codebase, archive, code, width, height, name, mayscr)
{
	var netscape = browser_NetscapeMozilla();
	var msie = browser_MSIE();
	var opera = browser_Opera();
	applet_type = 0; // <applet>
	if(jvm == "plugin") {
		if(netscape || opera) {
			applet_type = 1; // <embed> in Netscape and Opera
		} else if(msie) {
			applet_type = 2; // <object> in Microsoft
		}
	}
	var s;
	if(applet_type == 1) {
		s = '<embed TYPE="application/x-java-applet;version=1.5"\n';
		s += ' PLUGINSPAGE="http://java.sun.com/javase/downloads/index.jsp"\n';
	} else if(applet_type == 2) {
		s = '<object CLASSID="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93"\n';
		s += ' CODEBASE="http://java.sun.com/update/1.6.0/jinstall-6u14-windows-i586.cab#Version=1,5,0,0"\n';
	} else {
		s = '<applet';
	}
	if(mayscr) {
		if(applet_type == 0) { // <applet>
			s += ' MAYSCRIPT';
		} else if(applet_type == 1) { // <embed>
			s += ' MAYSCRIPT=true';
		}
                mayscrDefined = true;
	}
	s += ' WIDTH='+width+' HEIGHT='+height;
	if(name) {
		s += ' ID="'+name+'"';
	}
	s += '\n';
	if(msketch_usedJars != "") { 	
		archive += "," + msketch_usedJars;
	}
	if(applet_type != 2) { // <applet> and <embed>
		s += ' CODEBASE="'+codebase+'" ARCHIVE="'+archive+'" CODE="'+code+'"';
	}
	if(applet_type != 1) { // <applet> and <object>
		s += '>\n';
        }
	if(applet_type == 2) { // <object>
		s += '<param NAME="codebase" VALUE="'+codebase+'">\n';
		s += '<param NAME="archive" VALUE="'+archive+'">\n';
		s += '<param NAME="code" VALUE="'+code+'">\n';
		s += '<param NAME="scriptable" VALUE="true">\n';
		if(mayscr) {
			s += '<param NAME="mayscript" VALUE="true">\n';
                        mayscrDefined = true;
		}
	}
	_appletstrbuf = s;
	return s;
}

var skinDefined = false;
var isSetLegacy = false;
function applet_param(name, value)
{
	var s;
        if(name == "skin") {
            // do not overwrite skin later
            skinDefined = true;
        }
        if(name != "legacy_lifecycle") {
            value="@javascript-URI-encoded@"+encodeURIComponent(value);
        } else {
            isSetLegacy=true;
        }
	if(applet_type == 1) { // <embed>
		s = ' '+name+'="'+value+'"\n';
	} else { // <applet> and <object>
		s = '<param NAME="'+name+'" VALUE="'+value+'">\n';
	} 
	_appletstrbuf += s;
	return s;
}

function applet_end(type)
{
	var s;
	var msg = "<center><b>YOU CANNOT SEE A JAVA APPLET HERE</b></center>\n";
        var legacy = !isLeopardSafari();
        if(type == 'msketch') {
            legacy = msketch_legacy;
        } else if(type == 'mview') {
            legacy = mview_legacy;
        } else {
            legacy = mspace_legacy;
        }

	if(applet_type == 1) { // <embed>
            if(!isSetLegacy && legacy) {
                s = ' legacy_lifecycle="true"\n';
            } else {
                s = '';                
            }
                s += ' java_arguments="-Djnlp.packEnabled=true"\n';
		s += '>\n<noembed>\n';
		s += msg;
		s += '</noembed>\n';
	} else if(applet_type == 2) { // <object>
                if(!isSetLegacy && legacy) {
                    s = '<param name="legacy_lifecycle" value="true"/>\n';
                } else {
                    s = '';
                }
                s += '<param name="java_arguments" value="-Djnlp.packEnabled=true"/>\n';
		s += msg;
		s += '</object>\n';
	} else { // <applet>
                if(!isSetLegacy && legacy) {
                    s = '<param name="legacy_lifecycle" value="true"/>\n';
                } else {
                    s = '';
                }
                s += '<param name="java_arguments" value="-Djnlp.packEnabled=true"/>\n';
                if(mayscrDefined && !skinDefined && isLeopardSafari()) {
                    s += '<param name="skin" value="javax.swing.plaf.metal.MetalLookAndFeel"/>\n'+msg;
                } else {
		    s += msg;
                }                
		s += '</applet>\n';
	}
	_appletstrbuf += s;
        s = _appletstrbuf;
        _appletstrbuf = "";
	return s;
}


//
// "Public" functions
//


// Determine the JVM.
function marvin_get_jvm() {
	var jvm = marvin_jvm0;
	if(!jvm) {
		jvm = (marvin_jvm != "")? marvin_jvm : marvin_default_jvm();
	}
	jvm = jvm.toLowerCase();
	return jvm;
}

// Determine GUI type ("awt" or "swing").
function marvin_get_gui() {
	var gui = marvin_gui0;
	if(!gui) {
		gui = (marvin_gui != "")? marvin_gui : marvin_default_gui(marvin_get_jvm());
	}
	return gui;
}

// If msketch is able to generate image returns 1 else 0. It is depends on 
// the browser.
function msketch_detect() 
{
	var netscape = browser_NetscapeMozilla();
	var msie = browser_MSIE();
	if(msie > 0) {
	    marvin_jvm = "plugin";
	} else if(netscape > 0) {
	    if(netscape > 4) {
	        marvin_jvm = "builtin";
	    } else {
	    	alert("Image generation can be run only in SWING mode.\n"+
			"Your browser does not support SWING.");
		return 0;
	    }
	}
	return 1;
}

function msketch_begin(codebase, width, height){
	msketch_begin(codebase, width, height, isLeopardSafari());
}

function msketch_begin(codebase, width, height, oldbehaviour)
{
        if(oldbehaviour == undefined) {
            oldbehaviour = isLeopardSafari();
        }
	var archive, code;
	var jvm = marvin_get_jvm();
	var gui = marvin_get_gui();
	if (oldbehaviour){
		code = "JMSketch";
	} else {
		code = "JMSketchLaunch";
	}
	if(gui.toLowerCase() == "swing") {
		if (oldbehaviour){
			archive = "jmarvin.jar";
		} else {
			archive = "appletlaunch.jar"
		}
	} else {
		archive = "marvin.jar";
		code = "MSketch";
	}
	applet_begin(jvm, codebase, archive, code, width, height, msketch_name,
		     msketch_mayscript);
}


function msketch_param(name, value)
{
	return applet_param(name, value);
}

function msketch_end()
{
	s0 = msketch_end_to_string();
	document.write(s0);
}

function msketch_end_to_string() {
	s0 = applet_end("msketch");
	msketch_name = "";
	return s0;
}

function mview_begin(codebase, width, height){
	mview_begin(codebase, width, height, isLeopardSafari());
}

function mview_begin(codebase, width, height, oldbehaviour)
{
        if(oldbehaviour == undefined) {
            oldbehaviour = isLeopardSafari();
        }
	var archive, code;
	var jvm = marvin_get_jvm();
	var gui = marvin_get_gui();
	if(gui.toLowerCase() == "swing") {
		if (oldbehaviour){
	    	archive = "jmarvin.jar";
			code = "JMView";
		} else {
			archive = "appletlaunch.jar";
			code = "JMViewLaunch";
		}
	} else {
		archive = "marvin.jar";
		code = "MView";
	}
	applet_begin(jvm, codebase, archive, code, width, height, mview_name,
		     mview_mayscript);
}

function mview_param(name, value)
{
	return applet_param(name, value);
}

function mview_end_to_string()
{
	s0 = applet_end("mview");
	mview_name = "";
	return s0;
}

function mview_end() {
	s0 = mview_end_to_string();
	document.write(s0);
}

function mspace_begin(name,codebase, width, height)
{
    var jvm = marvin_get_jvm();
    var archive = "mspace.jar,jmarvin.jar,jogl.jar,jextexp.jar,gluegen-rt.jar";
    var code = "chemaxon/marvin/space/gui/MSpaceApplet";
    var netscape = browser_NetscapeMozilla();
    var mayscr = mspace_mayscript;
    var msie = browser_MSIE();
    var opera = browser_Opera();
    applet_type = 0; // <applet>
    if(jvm == "plugin") {
	if(netscape || opera) {
			applet_type = 1; // <embed> in Netscape and Opera
	} else if(msie) {
			applet_type = 2; // <object> in Microsoft
	}
    }
    var s;
    if(applet_type == 1) {
	s = '<embed TYPE="application/x-java-applet;version=1.5"\n';
	s += ' PLUGINSPAGE="http://java.sun.com/javase/downloads/index.jsp"\n';
    } else if(applet_type == 2) {
	s = '<object CLASSID="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93"\n';
	s += ' CODEBASE="http://java.sun.com/update/1.6.0/jinstall-6u14-windows-i586.cab#Version=1,5,0,0"\n';
    } else {
	s = '<applet';
    }
    if(mayscr) {
	if(applet_type == 0) { // <applet>
	    s += ' MAYSCRIPT';
	} else if(applet_type == 1) { // <embed>
	    s += ' MAYSCRIPT=true';
	}
    }
    s += ' WIDTH='+width+' HEIGHT='+height;
    if(name) {
	s += ' ID="'+name+'"';
    }
    if(applet_type != 2) { // <applet> and <embed>
	s += ' CODEBASE="'+codebase+'" ARCHIVE="'+archive+'" CODE="'+code+'"';
    }
    if(applet_type != 1) { // <applet> and <object>
	s += '>\n';
//        s += '<param NAME="image" VALUE="'+loading_image+'">\n';
    } else {
//        s += ' IMAGE="'+loading_image+'"\n';
    }
    if(applet_type == 2) { // <object>
	s += '<param NAME="codebase" VALUE="'+codebase+'">\n';
	s += '<param NAME="archive" VALUE="'+archive+'">\n';
	s += '<param NAME="code" VALUE="'+code+'">\n';
	s += '<param NAME="scriptable" VALUE="true">\n';
        if(mayscr) {
	    s += '<param NAME="mayscript" VALUE="true">\n';
	}
    }
	_appletstrbuf = s + "\n";
	return s;
}

function mspace_param(name, value) {
    var s;
    if(applet_type == 1) { // <embed>
		s = name+'="'+value+'"\n';
    } else { // <applet> and <object>
	s = '<param NAME="'+name+'" VALUE="'+value+'">\n';
    }
	_appletstrbuf += s;
    return s;
}

function mspace_end_to_string() {
    s0 = applet_end("mspace");
    mspace_name = "";
	return s0;
}

function mspace_end() {
	s0 = mspace_end_to_string();
	document.write(s0);
}

function links_set_search(s) {
	for(i = 0; i < document.links.length; ++i) {
		var p = document.links[i].pathname;
		if(p.lastIndexOf(".html") > 0 || p.lastIndexOf(".jsp") > 0) {
			var href = document.links[i].href;
			var k = href.indexOf('?');
			if(k > 0) {
				href = href.substring(0, k);
			}
			document.links[i].href = href + s;
		}
	}
}

function unix2local(s) {
	var strvalue = "" + s;
	var v = navigator.appVersion;
	if(v.indexOf("Win") > 0) {
		strvalue = strvalue.split("\r\n").join("\n"); // To avoid "\r\r\n"
		return strvalue.split("\n").join("\r\n");
	} else if(v.indexOf("Mac") > 0) { // Macintosh
		return strvalue.split("\n").join("\r");
	} else { // Unix
		return strvalue;
	}
}

function local2unix(s) {
	var strvalue = "" + s;
	var v = navigator.appVersion;
	if(v.indexOf("Win") > 0) {
		return strvalue.split("\r").join("");
	} else if(v.indexOf("Mac") > 0) { // Macintosh
		return strvalue.split("\r").join("\n");
	} else { // Unix
		return strvalue;
	}
}

