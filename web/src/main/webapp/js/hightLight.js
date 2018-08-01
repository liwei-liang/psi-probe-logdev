
var hightMark = 0;

function highlight(keyword) {
	var textbox = $('lineContent');
	var temp = textbox.innerHTML;
	if(hightMark%2==0){
		
		if ("" == keyword) return;
		words = decodeURIComponent(keyword.replace(/\,/g, ' ')).split(/\s+/);
		for (w = 0; w < words.length; w++) {
			var processedKeyword = words[w].replace(/[(){}.+*?^$|\\\[\]]/g, "\\$&")
			var r = new RegExp("(<.+?>.?" + processedKeyword + "[\\s\\S]+?)(<.+>\\[(?!" + processedKeyword.substr(processedKeyword.indexOf('[')+1,processedKeyword.length) + "))", "g");
			temp = temp.replace(r, "<span class='hightRed'>$1</span>$2");
			console.log(r);
		}
		hightMark++;
	}
	else{
		words = decodeURIComponent(keyword.replace(/\,/g, ' ')).split(/\s+/);
		for (w = 0; w < words.length; w++) {
			var r = new RegExp("(<span.+?Red.*?>)", "g");
			temp = temp.replace(r, "");
		}
		hightMark++;
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
function lol(match,keyword) {
	var replaceText = "$1<font class='hightYellow'>$3</font>$4";
	r = getKeywordRegExp(keyword);
	var tag = r.exec(match);
	match =  match.replace(r,replaceText);
	if(tag){
		return lol(match,keyword);
	}
    return match ;
};

var heightWordMark = false;
var insKeyWord = "";
function setHeightKeyWord(keyword) {
	keyword = keyword.trim();
	var textbox = $('lineContent');
	
	var tempHTML = textbox.innerHTML;
	if(heightWordMark && insKeyWord != keyword){
		var r = new RegExp("(<.?font.*?>)", "g");
		tempHTML = tempHTML.replace(r, "");
	}
	if ("" == keyword) {
		textbox.innerHTML = tempHTML;
		return;
	}
    tempHTML = tempHTML.replace(getKeywordRegExp(keyword), function(match){ return lol(match,keyword); });
	textbox.innerHTML = tempHTML;
    heightWordMark = true;
    insKeyWord = keyword;
}

function heightKeyWord() {
    var $keyword = $('highLishtBtn').value;
    setHeightKeyWord($keyword)
}

function heightByKeyWord(keyword) {
	console.log(keyword);
    setHeightKeyWord(keyword)
}