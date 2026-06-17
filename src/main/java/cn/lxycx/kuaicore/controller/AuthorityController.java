package cn.lxycx.kuaicore.controller;


import cn.hutool.core.lang.UUID;
import cn.lxycx.kuaicore.conf.ConfStore;
import cn.lxycx.kuaicore.conf.RetJson;
import cn.lxycx.kuaicore.service.MMPlus;
import cn.lxycx.kuaicore.util.LinkedHashMapBuilder;
import cn.lxycx.kuaicore.util.Mapp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * 权限管理页面
 */
@Slf4j
@RestController
@RequestMapping("authority")
public class AuthorityController {

    @Autowired
    MMPlus mmPlus;

    String tablename = "KUAI_AUTHORITY_CONF";

    @PostMapping("saveData")
    @ResponseBody
    public RetJson saveData(@RequestBody Map<String,Object> data){

        String[] fields = {"",""};
        Object id = data.remove("id");
        int code = 0;
        try{
            if(id!=null&&id!=""){
                code = mmPlus.updateSimple(tablename,data, Mapp.by("id",id));
            }else{
                data.put("id", UUID.fastUUID().toString(true));
                code = mmPlus.insert(tablename,data);
            }
        }catch (Exception e){
            String err = e.getMessage();
            log.info(e.getMessage());
            if(err.indexOf("ORA-00001")>=0){ //违反唯一约束条件
                return RetJson.by("0001","操作失败:当前别名已存在");
            }else{
                return RetJson.by("0002","操作失败");
            }
        }
        return RetJson.by(String.valueOf(code),code == 1 ?"操作成功":"操作失败");
    }


    @PostMapping("findList")
    @ResponseBody
    public List<Map<String,Object>> findList(){
        Map<String,List<Map<String,Object>>> retmap = new LinkedHashMap<>();
        List<Map<String,Object>> list = mmPlus.getList(tablename,new LinkedHashMap<>());
        return list;
    }


    /**
     * 同步
     * @return
     */
    @PostMapping("sync")
    @ResponseBody
    public Map<String,Object> sync(){
        List<Map<String,Object>> list = mmPlus.getList(tablename,new LinkedHashMap<>());
        Map<String,Map<String,Object>> conf = ConfStore.getAuthorityConf();
        conf.clear();
        for(Map<String,Object> c:list){
            /*JSONObject verifyJson = JSON.parseObject(c.getVerify());
            c.setVerifyJson(verifyJson);// 转JSON
            JSONObject afterJson = JSON.parseObject(c.getAfter());
            c.setAfterJson(afterJson);// 转JSON
            JSONObject frontJson = JSON.parseObject(c.getFront());
            c.setFrontJson(frontJson);// 转JSON
            JSONObject setvalueJson = JSON.parseObject(c.getSetvalue());
            c.setSetvalueJson(setvalueJson);// 转JSON

            conf.put(c.getAlias(),c);

            String tabletype = c.getTabletype();
            if("SQL".equals(tabletype)){ //对复杂的SQL查询语句进行特殊处理
                c.setTables("("+c.getTables()+") t");
            }*/
        }
        int code = 1;
        return new LinkedHashMapBuilder<String,Object>().add("code",code).add("message",code == 1 ?"操作成功":"操作失败");
    }


}
