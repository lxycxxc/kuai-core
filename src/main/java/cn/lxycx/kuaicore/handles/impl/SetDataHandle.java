package cn.lxycx.kuaicore.handles.impl;

import cn.lxycx.kuaicore.handles.CurdHandle;
import cn.lxycx.kuaicore.handles.CurdHandleException;
import cn.lxycx.kuaicore.handles.CurdHandleUtils;
import cn.lxycx.kuaicore.handles.HandleConf;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
@HandleConf(name="设置默认值",type = "insert,update",remark = "{\"CREATE_BY\":{\"Attr\":\"userid\"}}")
public class SetDataHandle extends CurdHandle {

    @Autowired
    HttpServletRequest request;
    @Autowired
    HttpServletResponse response;
    @Autowired
    CurdHandleUtils curdHandleUtils;

    @Override
    public  boolean front(Map<String, Object> set_data,Map<String, Map<String, Object>> wheres, String param) throws CurdHandleException {

        try{
            JSONObject json = JSON.parseObject(param);
            for(String k:json.keySet()){
                JSONObject json2 = json.getJSONObject(k);
                for(String f:json2.keySet()){
                    String value = json2.getString(f);
                    JSONObject newjson = new JSONObject();
                    newjson.put(k,new JSONObject().fluentPut("roles",new JSONArray().fluentAdd("set_data")).fluentPut("isnull",1).fluentPut("type",f).fluentPut("field",value));
                    curdHandleUtils.setValue(request,newjson,set_data,null);
                }
            }
            return true;
        }catch (Exception e){
            throw new CurdHandleException("配置参数格式错误:"+e.getMessage());
           // throw new CurdHandleException(e.getMessage());
        }
    }
}
