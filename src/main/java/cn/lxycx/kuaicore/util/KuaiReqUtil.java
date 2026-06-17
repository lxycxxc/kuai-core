package cn.lxycx.kuaicore.util;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

public class KuaiReqUtil {

    public static void setWheres (HttpServletRequest request,Map<String, Map<String, String>> where){
        request.setAttribute("kuai_extends_wheres",where);
    }
    public static Map<String, Map<String, Object>> getWheres (HttpServletRequest request){
        Object extends_wheres = request.getAttribute("kuai_extends_wheres");
        if(extends_wheres!=null){
            return (Map<String, Map<String, Object>>)extends_wheres;
        }
        return new LinkedHashMap<>();
    }

}
