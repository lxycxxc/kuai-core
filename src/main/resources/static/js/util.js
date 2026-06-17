/**
 * 获取url参数值
 **/
function GetQueryValue(queryName) {
    var href = window.location.href;
    // console.log("href:"+href);
    var query = decodeURI(window.location.search.substring(1));
    var vars = query.split("&");
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair[0] == queryName) { return pair[1]; }
    }
    return null;
}
function GetQueryMap() {
    var href = window.location.href;
    // console.log("href:"+href);v
    var map = {};
    var query = decodeURI(window.location.search.substring(1));
    if(query !=null&&query!=""){
        var vars = query.split("&");
        for (var i = 0; i < vars.length; i++) {
            var pair = vars[i].split("=");
            map[pair[0]] = pair[1];
        }
    }
    return map;
}
