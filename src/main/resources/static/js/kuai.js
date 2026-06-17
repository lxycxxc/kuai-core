let kuai = {
    c:{
        async:true,  //是否异步
        token:null
    },
    conf:function(cc){
        for(var k in cc){
            this.c[k] = cc[k]
        }
        return this;
    },
    /**
     * 将普通的JSON对象转换成kuai框架的where条件 默认操作符是 = 号
     * @param data
     */
    toWhere(data){
        console.log("toWhere")
        var new_data = {};
        for(var k in data){
            var v = data[k];
            if(v!=null&&String(v)!=""){ //String 解决数字 0
                new_data[k] = {"=":v};
            }
        }
        return new_data;
    },

    /**
     * 修改Kuai框架的Where条件对象的 操作符
     * @param d Where条件对象
     * @param f 需要操作的字段
     * @param c 原来的操作符
     * @param n 新的操作符
     * @param v 新的值（如果为空则用原来的值）
     * @returns {kuai}
     */
    setWhere(d,f,c,n,v){ //设置where 条件中的比较符(链式操作)
        var dd = d[f];
        if(dd!=null){
            var value = v!=null?v:dd[c];
            delete dd[c];
            dd[n] = value;
        }
        return this;
    },
    setOrderBy(w,d){
        w["ORDER_BY"] = d
        return this;
    },
    getList : function(alias,data,callback){
        var url = "middle/"+alias+"/getList";
        return this.sendHttpPost(url,data,callback);
    },
    getPage : function(alias,page,limit,data,callback){
        var url = "middle/"+alias+"/getPage/"+page+"/"+limit;
        return this.sendHttpPost(url,data,callback);
    },
    getByOne : function(alias,data,callback){
        var url = "middle/"+alias+"/getByOne";
        return this.sendHttpPost(url,data,callback);
    },
    update : function(alias,data,callback){
        var url = "middle/"+alias+"/getByOne";
        return this.sendHttpPost(url,data,callback);
    },
    sendHttpPost : function(url,data,callback){
        var that = this;
        var ret;
        var headers = { "Content-Type": "application/json" };
        if(this.token!=null){ headers["k-token"] = this.token }
        $.ajax({
            url: url,
            headers: headers,
            type: 'POST',
            async:that.c.async,
            dataType: 'json',
            data: JSON.stringify(data),
            success: function (d) {
                if(callback!=null) callback(d);
                ret = d;
            },
            error: function (e) {console.log(e)}
        });
        return ret;
    },form_submit(URL, PARAMS) {
        var temp = document.createElement("form");
        temp.action = URL;
        temp.method = "post";
        temp.style.display = "none";

        for (var x in PARAMS) {
            var opt = document.createElement("textarea");
            opt.name = x;
            opt.value = PARAMS[x];
            temp.appendChild(opt);
        }
        document.body.appendChild(temp);
        temp.submit();

        return temp;
    }
}
