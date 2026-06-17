package cn.lxycx.kuaicore.conf;

import cn.lxycx.kuaicore.bean.InterfaceConf;
import cn.lxycx.kuaicore.handles.HandleConf;

import java.util.LinkedHashMap;
import java.util.Map;

public class  ConfStore {

    private static Map<String, InterfaceConf> interfaceConf = new LinkedHashMap<>(); //接口配置
    private static Map<String,Map<String, HandleConf>> handleConf = new LinkedHashMap<>();//拦截器配置
    private static Map<String, Map<String,Object>> authorityConf = new LinkedHashMap<>();//权限配置

    public static Map<String, Map<String,Object>> getAuthorityConf() {
        return authorityConf;
    }
    public static void setAuthorityConf(Map<String, Map<String,Object>> authorityConf) {
        ConfStore.authorityConf = authorityConf;
    }


    public static Map<String, InterfaceConf> getInterfaceConf() {
        return interfaceConf;
    }
    public static void setInterfaceConf(Map<String, InterfaceConf> interfaceConf) {
        ConfStore.interfaceConf = interfaceConf;
    }

    public static Map<String,Map<String, HandleConf>> getHandleConf() { return handleConf; }
    public static void setHandleConf(Map<String,Map<String, HandleConf>> handleConf) {
        ConfStore.handleConf = handleConf;
    }
}
