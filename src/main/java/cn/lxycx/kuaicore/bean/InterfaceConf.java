package cn.lxycx.kuaicore.bean;

import cn.hutool.extra.spring.SpringUtil;
import cn.lxycx.kuaicore.handles.CurdHandle;
import cn.lxycx.kuaicore.handles.CurdHandleAfter;
import cn.lxycx.kuaicore.kapi.AbsKApi;
import cn.lxycx.kuaicore.kapi.bean.KhAfter;
import cn.lxycx.kuaicore.kapi.bean.KhFront;
import cn.lxycx.kuaicore.kapi.bean.SetHandle;
import cn.lxycx.kuaicore.kapi.bean.SetHandleAfter;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class InterfaceConf {

    private Class pojo = Map.class;
    private String id;
    private String tables;
    private String queryTable;
    private String tabletype;
    private String classify;
    private String alias;
    private String remark;
    private String keys;
    private String verify;
    private JSONObject verifyJson;
    private String setvalue;
    private JSONObject setvalueJson;
    private String listconf;
    private String formconf;
    private String roles;
    private String field;
    private JSONObject descs;
    private String wheres;
    private String iscx;
    private String isxg;
    private String issc;
    private String front;
    private JSONObject frontJson;
    private KhFront khFront;
    private String after;
    private JSONObject afterJson;
    private KhAfter khAfter;
    private String state;
    private Date addtime;
    private AbsKApi kApi;

    public void setKhAfterByJson(JSONObject afterJson) {
        KhAfter after = new KhAfter();
        for(String key:afterJson.keySet()){
            JSONArray jsonArray = afterJson.getJSONArray(key);
            for(int i=0;i<jsonArray.size();i++){
                JSONObject json = jsonArray.getJSONObject(i);
                String classname = json.getString("name");
                String param = json.getString("param");

                CurdHandleAfter ch = SpringUtil.getApplicationContext().getBean(classname, CurdHandleAfter.class);
                after.get(key).add(new SetHandleAfter(ch,param));
            }
        }
        this.khAfter = after;
    }

    public void setKhFrontByJson(JSONObject frontJson) {
        KhFront front = new KhFront();
        for(String key:frontJson.keySet()){
            JSONArray jsonArray = frontJson.getJSONArray(key);
            for(int i=0;i<jsonArray.size();i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String classname = json.getString("name");
                String param = json.getString("param");
                CurdHandle ch = SpringUtil.getApplicationContext().getBean(classname, CurdHandle.class);
                front.get(key).add(new SetHandle(ch, param));
            }
        }
        this.khFront = front;
    }
}
