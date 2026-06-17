package cn.lxycx.kuaicore.util;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

//@Component
public class CookieUtils {

    public static final int COOKIE_MAX_AGE = 7 * 24 * 3600;
    public static final int COOKIE_HALF_HOUR = 24 * 3600; //默认一天


    private  HttpServletRequest request;
    private  HttpServletResponse response;

    public static CookieUtils by(HttpServletRequest request,HttpServletResponse response){
        return new CookieUtils(request,response);
    }

    private CookieUtils(HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
    }

    /*{
        opentype = request.getParameter("opentype");
        openyear = request.getParameter("openyear");
    }*/


    /**
     * 根据Cookie名称得到Cookie对象，不存在该对象则返回Null
     *
     * @param name
     * @return
     */
    public  Cookie getCookie(String name) {
        name = toName(name);
        Cookie[] cookies = request.getCookies();
        if (cookies==null||cookies.length<1) {
            return null;
        }
        Cookie cookie = null;
        for (Cookie c : cookies) {
            if (name.equals(c.getName())) {
                cookie = c;
                break;
            }
        }
        return cookie;
    }

    /**
     * 根据Cookie名称直接得到Cookie值
     *
     * @param name
     * @return
     */
    public  String getCookieValue(String name) {
        Cookie cookie = getCookie(name);
        if(cookie != null){
            return cookie.getValue();
        }
        return null;
    }

    /**
     * 移除cookie
     * @param name 这个是名称，不是值
     */
    public  void removeCookie(String name) {
        name = toName(name);
        if (null == name) {
            return;
        }
        Cookie cookie = getCookie(name);
        if(null != cookie){
            cookie.setPath("/");
            cookie.setValue("");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    /**
     * 添加一条新的Cookie，可以指定过期时间(单位：秒)
     *
     * @param name
     * @param value
     * @param maxValue
     */
    public  void setCookie(String name,
                                 String value, int maxValue) {
        name = toName(name);
        if (StringUtils.isBlank(name)) {
            return;
        }
        if (null == value) {
            value = "";
        }
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        if (maxValue != 0) {
            cookie.setMaxAge(maxValue);
        } else {
            cookie.setMaxAge(COOKIE_HALF_HOUR);
        }
        response.addCookie(cookie);
      /*  try {
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * 添加一条新的Cookie，默认30分钟过期时间
     *
     * @param name
     * @param value
     */
    public  void setCookie(String name,
                                 String value) {
        setCookie(name, value, COOKIE_HALF_HOUR);
    }

    /**
     * 将cookie封装到Map里面
     * @return
     */
    public  Map<String,Cookie> getCookieMap(){
        Map<String,Cookie> cookieMap = new LinkedHashMap<>();
        Cookie[] cookies = request.getCookies();
        if(cookies!=null&&cookies.length>1){
            for(Cookie cookie : cookies){
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }


    private String toName(String name){
        return name;
    }

}
