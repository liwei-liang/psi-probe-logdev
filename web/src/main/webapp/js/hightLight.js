/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
var hightColorMap = new Map();
var red = "red";
var orange = "orange";
hightColorMap.set(red, false);
hightColorMap.set(orange, false);

function highlight(keyword) {
	var textbox = $('file_content');
	var temp = textbox.innerHTML;
	console.log(hightColorMap);
	if(!hightColorMap.get(red)){
		if ("" == keyword) return;
		words = decodeURIComponent(keyword.replace(/\,/g, ' ')).split(/\s+/);
		for (w = 0; w < words.length; w++) {
			var processedKeyword = words[w].replace(/[(){}.+*?^$|\\\[\]]/g, "\\$&")
			var r = new RegExp("(<div[^>]+>.?" + processedKeyword + ".*?</div>(\\s*<div[^>]+>(?!\\[).*?</div>)*)", "g");
			temp = temp.replace(r, "<span class='hightRed'>$1</span>");
			console.log(r);
		}
		hightColorMap.set(red, true);
	}
	else{
		words = decodeURIComponent(keyword.replace(/\,/g, ' ')).split(/\s+/);
		for (w = 0; w < words.length; w++) {
			var r = new RegExp("(<span.+?Red.*?>)", "g");
			temp = temp.replace(r, "");
		}
		hightColorMap.set(red, false);
	}
	textbox.innerHTML = temp;
}

function highlight2(keyword) {
	var textbox = $('lineContent');
	if ("" == keyword) return;
	var temp = textbox.innerHTML;g
	var htmlReg = new RegExp("\<.*?\>", "i");
	var arr = new Array();
	var tag1 = "</div>"
	var tag2 = '<div class="line">'
	var mark = 0;

	for (var i = 0; true; i++) {
		mark++;
		var tag = htmlReg.exec(temp);
		if (tag) {
			arr[i] = tag;
		} else {
			break;
		}
		temp = temp.replace(tag, "{[(" + i + ")]}");
	}

	words = decodeURIComponent(keyword.replace(/\,/g, ' ')).split(/\s+/);
	for (w = 0; w < words.length; w++) {
		var r = new RegExp("(" + words[w].replace(/[(){}.+*?^$|\\\[\]]/g, "\\$&") + ")", "ig");
		temp = temp.replace(r, "<b style='color:Red;'>$1</b>");
	}

	for (var i = 0; i < arr.length; i++) {
		temp = temp.replace("{[(" + i + ")]}", arr[i]);
	}
	textbox.innerHTML = temp;
}

function getKeywordRegExp(keyword) {
	return new RegExp("(<(?!font)[^>]+>|(>[^<]*[\\W]{1}?))(" + keyword.replace(/[(){}.+*?^$|\\\[\]]/g, "\\$&") + ")(([\\W][^>]*<)|<)", "ig");
}

function getAllKeywordRegExp(keyword) {
	return new RegExp("(<(?!font)[^>]+>[^<]*?)(" + keyword.replace(/[(){}.+*?^$|\\\[\]]/g, "\\$&") + ")([^>]*<)", "ig");
}

function lol(match,keyword,getAllWord) {
	if(getAllWord){
		var replaceText = "$1<font class='hightOrange'>$2</font>$3";
		r = getAllKeywordRegExp(keyword);
	}else{
		var replaceText = "$1<font class='hightOrange'>$3</font>$4";
		r = getKeywordRegExp(keyword);
	}
	var tag = r.exec(match);
	match =  match.replace(r,replaceText);
	if(tag){
		return lol(match,keyword,getAllWord);
	}
    return match ;
};

var insKeyWord = "";
function setHeightKeyWord(keyword,getAllWord) {
	keyword = keyword.trim();
	var textbox = $('file_content');
	
	var tempHTML = textbox.innerHTML;
	if(hightColorMap.get(orange) && insKeyWord != keyword){
		var r = new RegExp("(<.?font.*?>)", "g");
		tempHTML = tempHTML.replace(r, "");
	}
	if ("" == keyword) {
		textbox.innerHTML = tempHTML;
		return;
	}
	if(getAllWord){
	    tempHTML = tempHTML.replace(getAllKeywordRegExp(keyword), function(match){ return lol(match,keyword,getAllWord); });
	}else{
	    tempHTML = tempHTML.replace(getKeywordRegExp(keyword), function(match){ return lol(match,keyword,getAllWord); });
	}
	textbox.innerHTML = tempHTML;
	hightColorMap.set(orange, true);
    insKeyWord = keyword;
}

function heightKeyWord() {
    var $keyword = $('highLishtBtn').value;
    setHeightKeyWord($keyword,true)
}

function heightByKeyWord(keyword) {
	console.log(keyword);
    setHeightKeyWord(keyword,false)
}