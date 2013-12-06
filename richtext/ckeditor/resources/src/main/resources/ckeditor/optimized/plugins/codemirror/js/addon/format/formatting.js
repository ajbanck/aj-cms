﻿(function(){function l(f){for(var c=[/for\s*?\((.*?)\)/g,/&#?[a-z0-9]+;[\s\S]/g,/\"(.*?)((\")|$)/g,/\/\*(.*?)(\*\/|$)/g,/^\/\/.*/g],d=[],a=0;a<c.length;a++)for(var b=0;b<f.length;){var e=f.substr(b).match(c[a]);if(null!=e)d.push({start:b+e.index,end:b+e.index+e[0].length}),b+=e.index+Math.max(1,e[0].length);else break}d.sort(function(a,b){return a.start-b.start});return d}CodeMirror.extendMode("css",{commentStart:"/*",commentEnd:"*/",newlineAfterToken:function(f,c){return/^[;{}]$/.test(c)}});CodeMirror.extendMode("javascript",
{commentStart:"/*",commentEnd:"*/",wordWrapChars:[";","\\{","\\}"],autoFormatLineBreaks:function(f){var c=0,d=this.jsonMode?function(a){return a.replace(/([,{])/g,"$1\n").replace(/}/g,"\n}")}:function(a){return a.replace(/(;|\{|\})([^\r\n;])/g,"$1\n$2")},a=l(f),b="";if(null!=a){for(var e=0;e<a.length;e++)if(a[e].start>c&&(b+=d(f.substring(c,a[e].start)),c=a[e].start),a[e].start<=c&&a[e].end>=c)b+=f.substring(c,a[e].end),c=a[e].end;c<f.length&&(b+=d(f.substr(c)))}else b=d(f);return b.replace(/^\n*|\n*$/,
"")}});CodeMirror.extendMode("xml",{commentStart:"<\!--",commentEnd:"--\>",noBreak:!1,noBreakEmpty:null,tagType:"",tagName:"",isXML:!1,newlineAfterToken:function(f,c,d){var a=!1,a=null,b="";this.isXML="xml"==this.configuration?!0:!1;if("comment"==f||/<\!--/.test(d))return!1;if("tag"==f){0==c.indexOf("<")&&0==!c.indexOf("</")&&(this.tagType="open",a=c.match(/^<\s*?([\w]+?)$/i),this.tagName=null!=a?a[1]:"",b=this.tagName.toLowerCase(),-1!="|label|li|option|textarea|title|a|b|bdi|bdo|big|center|cite|del|em|font|i|img|ins|s|small|span|strike|strong|sub|sup|u|".indexOf("|"+
b+"|")&&(this.noBreak=!0));if(0==c.indexOf(">")&&"open"==this.tagType){this.tagType="";if(RegExp("^"+(this.isXML?"[^<]*?":"")+"</s*?"+this.tagName+"s*?>","i").test(d))return this.noBreak=!1,this.isXML||(this.tagName=""),!1;a=this.noBreak;this.noBreak=!1;return a?!1:!0}0==c.indexOf("</")&&(this.tagType="close",a=c.match(/^<\/\s*?([\w]+?)$/i),null!=a&&(b=a[1].toLowerCase()),-1!="|a|b|bdi|bdo|big|center|cite|del|em|font|i|img|ins|s|small|span|strike|strong|sub|sup|u|".indexOf("|"+b+"|")&&(this.noBreak=
!0));if(0==c.indexOf(">")&&"close"==this.tagType){this.tagType="";if(0==d.indexOf("<")&&(a=d.match(/^<\/?\s*?([\w]+?)(\s|>)/i),b=null!=a?a[1].toLowerCase():"",-1=="|label|li|option|textarea|title|a|b|bdi|bdo|big|center|cite|del|em|font|i|img|ins|s|small|span|strike|strong|sub|sup|u|".indexOf("|"+b+"|")))return this.noBreak=!1,!0;a=this.noBreak;this.noBreak=!1;return a?!1:!0}}if(0==d.indexOf("<")){this.noBreak=!1;if(this.isXML&&""!=this.tagName)return this.tagName="",!1;a=d.match(/^<\/?\s*?([\w]+?)(\s|>)/i);
b=null!=a?a[1].toLowerCase():"";if(-1=="|label|li|option|textarea|title|a|b|bdi|bdo|big|center|cite|del|em|font|i|img|ins|s|small|span|strike|strong|sub|sup|u|".indexOf("|"+b+"|"))return!0}return!1}});CodeMirror.defineExtension("commentRange",function(f,c,d){var a=this,b=CodeMirror.innerMode(a.getMode(),a.getTokenAt(c).state).mode;a.operation(function(){if(f)a.replaceRange(b.commentEnd,d),a.replaceRange(b.commentStart,c),c.line==d.line&&c.ch==d.ch&&a.setCursor(c.line,c.ch+b.commentStart.length);else{var e=
a.getRange(c,d),i=e.indexOf(b.commentStart),g=e.lastIndexOf(b.commentEnd);-1<i&&(-1<g&&g>i)&&(e=e.substr(0,i)+e.substring(i+b.commentStart.length,g)+e.substr(g+b.commentEnd.length));a.replaceRange(e,c,d)}})});CodeMirror.defineExtension("autoIndentRange",function(f,c){var d=this;this.operation(function(){for(var a=f.line;a<=c.line;a++)d.indentLine(a,"smart")})});CodeMirror.defineExtension("autoFormatRange",function(f,c){for(var d=this,a=d.getMode(),b=d.getRange(f,c).split("\n"),e=CodeMirror.copyState(a,
d.getTokenAt(f).state),i=d.getOption("tabSize"),g="",m=0,j=0==f.ch,k=0;k<b.length;++k){for(var h=new CodeMirror.StringStream(b[k],i);!h.eol();){var n=CodeMirror.innerMode(a,e),l=a.token(h,e),o=h.current();h.start=h.pos;if(!j||/\S/.test(o))g+=o,j=!1;if(!j&&n.mode.newlineAfterToken&&n.mode.newlineAfterToken(l,o,h.string.slice(h.pos)||b[k+1]||"",n.state))g+="\n",j=!0,++m}!h.pos&&a.blankLine&&a.blankLine(e);!j&&k<b.length-1&&(g+="\n",j=!0,++m)}d.operation(function(){d.replaceRange(g,f,c);for(var a=f.line+
1,b=f.line+m;a<=b;++a)d.indentLine(a,"smart");d.setSelection(f,d.getCursor(!1))})})})();