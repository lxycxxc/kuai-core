package cn.lxycx.kuaicore.util;

import javax.servlet.http.HttpServletRequest;

public class RequestPro {
    private HttpServletRequest request;
    private RequestPro(HttpServletRequest request){
        this.request = request;
    }
    public static RequestPro by(HttpServletRequest request){
        RequestPro req = new RequestPro(request);
        return req;
    }

    public <T> T getAttr(String key){
        Object o = request.getAttribute(key);
        return o!=null? (T) o :null;
    }


}
