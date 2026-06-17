package cn.lxycx.kuaicore.conf;


import cn.lxycx.kuaicore.util.KuaiCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private static Logger log = LoggerFactory.getLogger(LoginInterceptor.class);

    @Autowired
    private KuaiCore kuaiCore;



    /**
     * 前处理
     * @return  true 请求继续，false 请求结束
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!kuaiCore.backIsLogin(request,response)){
            request.getRequestDispatcher("/interface/admin/login.html").forward(request, response);
            return false;
        }
        return true;
    }

    //请求后视图渲染前
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "text/html;charset=UTF-8");
    }

    //视图渲染之后处理
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }


}
