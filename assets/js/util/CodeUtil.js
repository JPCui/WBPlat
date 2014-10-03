
var str2Unicode = function(str) {
    var res=[];
    for(var i=0;i &lt; str.length;i++)
        res[i]=("00"+str.charCodeAt(i).toString(16)).slice(-4);
    return "\\u"+res.join("\\u");
}

var unicode2Str = function(str) {
    str=str.replace(/\\/g,"%");
    return unescape(str);
}