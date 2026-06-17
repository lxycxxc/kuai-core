package cn.lxycx.kuaicore.handles.impl;

import cn.lxycx.kuaicore.bean.KuaiConfig;
import cn.lxycx.kuaicore.bean.Manage;
import cn.lxycx.kuaicore.handles.CurdHandle;
import cn.lxycx.kuaicore.handles.CurdHandleException;
import cn.lxycx.kuaicore.handles.HandleConf;
import cn.lxycx.kuaicore.util.CookieUtils;
import cn.lxycx.kuaicore.util.Signer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Map;


@Component
@HandleConf(name="认证授权",remark = "用管理后台账号密码登录后才可能使用")
public class AuthorizeHandle extends CurdHandle {
    @Autowired
    HttpServletRequest request;
    @Autowired
    HttpServletResponse response;
    @Autowired
    KuaiConfig kuaiConfig;

    @Override
    public boolean front(Map<String, Object> set_data,Map<String, Map<String, Object>> wheres,String param) throws CurdHandleException {
        Manage manage = kuaiConfig.getManage();
        Calendar date = Calendar.getInstance();
        int day = date.get(Calendar.DATE);
        String authkey = Signer.MD5("login:"+day+":"+manage.getKey());
        CookieUtils cookieUtils = CookieUtils.by(request, response);
        String authvalue = cookieUtils.getCookieValue(authkey);

        if(authvalue!=null){
            String user = Signer.MD5(manage.getUsername()+day+manage.getKey());
            if(user.equals(authvalue)){
                return super.front(set_data,wheres,param);
            }
        }
        throw new CurdHandleException("认证不通过！！");
    }
}
