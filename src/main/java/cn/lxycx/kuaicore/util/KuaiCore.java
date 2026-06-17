package cn.lxycx.kuaicore.util;

import cn.lxycx.kuaicore.bean.KuaiConfig;
import cn.lxycx.kuaicore.bean.Manage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;

@Component
public class KuaiCore {
    
    @Autowired
    private KuaiConfig kuaiConfig;

    public boolean backIsLogin(HttpServletRequest request, HttpServletResponse response){
        Manage manage = kuaiConfig.getManage();
        Calendar date = Calendar.getInstance();
        int day = date.get(Calendar.DATE);
        String authkey = Signer.MD5("login:"+day+":"+manage.getKey());
        CookieUtils cookieUtils = CookieUtils.by(request, response);
        String authvalue = cookieUtils.getCookieValue(authkey);

        if(authvalue!=null){
            String user = Signer.MD5(manage.getUsername()+day+manage.getKey());
            if(user.equals(authvalue)){
                return true;
            }
        }
        return false;
    }

}
