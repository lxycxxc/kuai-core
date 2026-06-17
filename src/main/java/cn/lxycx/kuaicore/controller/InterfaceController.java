package cn.lxycx.kuaicore.controller;

import cn.lxycx.kuaicore.bean.InterfaceConf;
import cn.lxycx.kuaicore.bean.KuaiConfig;
import cn.lxycx.kuaicore.bean.Manage;
import cn.lxycx.kuaicore.conf.ConfStore;
import cn.lxycx.kuaicore.conf.Jdbc;
import cn.lxycx.kuaicore.conf.JdbcOperate;
import cn.lxycx.kuaicore.conf.RetJson;
import cn.lxycx.kuaicore.handles.CurdHandleAfter;
import cn.lxycx.kuaicore.handles.HandleConf;
import cn.lxycx.kuaicore.kapi.AbsKApi;
import cn.lxycx.kuaicore.kapi.RegisterApi;
import cn.lxycx.kuaicore.kapi.supper.KApi;
import cn.lxycx.kuaicore.mapper.InterfaceMapper;
import cn.lxycx.kuaicore.handles.CurdHandle;
import cn.lxycx.kuaicore.mapper.MiddleCurdMapper;
import cn.lxycx.kuaicore.util.CookieUtils;
import cn.lxycx.kuaicore.util.KuaiUtil;
import cn.lxycx.kuaicore.util.LinkedHashMapBuilder;
import cn.lxycx.kuaicore.util.Signer;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * 接口管理控制器
 */
@Slf4j
@Controller
@RequestMapping("interface")
public class InterfaceController {

    @Lazy
    @Autowired
    InterfaceMapper interfaceMapper;
    @Lazy
    @Autowired
    MiddleCurdMapper middleCurdMapper;
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    private Jdbc jdbc;

    @Autowired
    private KuaiConfig kuaiConfig;

    @RequestMapping("admin/{pagename}.html")
    public  String admin(@PathVariable("pagename") String pagename, HttpServletRequest req, HttpServletResponse resp){
        return "kadmin/"+pagename;
    }

    @PostMapping("login")
    @ResponseBody
    public RetJson login(HttpServletRequest req, HttpServletResponse resp,String username,String password){
        Manage manage = kuaiConfig.getManage();
        if(manage.getUsername().equals(username)&&manage.getPassword().equals(password)){
            Calendar date = Calendar.getInstance();
            int day = date.get(Calendar.DATE);
            String authkey = Signer.MD5("login:"+day+":"+manage.getKey());
            CookieUtils cookieUtils = CookieUtils.by(req, resp);
            cookieUtils.setCookie(authkey,Signer.MD5(manage.getUsername()+day+manage.getKey()));
            return RetJson.by("0000","登录成功");
        }
        return RetJson.by("0001","登录失败");
    }

    @PostMapping("logout")
    @ResponseBody
    public RetJson logout(HttpServletRequest req, HttpServletResponse resp){
        Manage manage = kuaiConfig.getManage();
        Calendar date = Calendar.getInstance();
        int day = date.get(Calendar.DATE);
        String authkey = Signer.MD5("login:"+day+":"+manage.getKey());
        CookieUtils cookieUtils = CookieUtils.by(req, resp);
        cookieUtils.removeCookie(authkey);
        return RetJson.by("0000","操作成功");
    }




    @PostMapping("findTables")
    @ResponseBody
    public Map<String,List<Map<String,Object>>> findTables(){
        Map<String,List<Map<String,Object>>> retmap = new LinkedHashMap<>();
        List<Map<String,Object>> list = interfaceMapper.findTables();

        for(Map<String,Object>  mp:list){
            String tabletype = mp.get("TABLE_TYPE")+"";
            List<Map<String,Object>> ll = retmap.get(tabletype);
            if(ll == null){ ll = new ArrayList<>();retmap.put(tabletype,ll); }
            ll.add(mp);
        }

        return retmap;
    }


    /**
     * 用 compileSQL 代替
     * @param tableName
     * @return
     */
    @Deprecated
    @PostMapping("findColumns")
    @ResponseBody
    public List<Map<String,Object>> findColumns(String tableName){
        return interfaceMapper.findColumns(tableName);
    }



    @PostMapping("compileSQL")
    @ResponseBody
    public RetJson compileSQL(String sql) throws SQLException {
        List<Map<String,Object>> list = new ArrayList<>();
        try {
            Connection con = jdbc.getConn();
            JdbcOperate jdbcOperate = JdbcOperate.by(con);
            String exceSql = "select * from (" + sql + ") a where 1!=1";
            String driver = jdbc.getDriver();
            if(driver.indexOf("mysql")>0&&sql.indexOf("from")<0){
                exceSql = "select * from " + sql + " where 1!=1";
            }
            List<Map<String, Object>> ret = new ArrayList<>();
            List<String> columnLabel = jdbcOperate.findColumnLabel(exceSql);
            log.info("字段列表",columnLabel);
            return RetJson.by("0000","获取成功",columnLabel.size(),columnLabel);

        }catch (Exception e){
            e.printStackTrace();
            return RetJson.by("0001","获取表字段失败");
        }

    }
    /**
     * 同步
     * @return
     */
    @PostMapping("sync")
    @ResponseBody
    public Map<String,Object> sync(){
        List<InterfaceConf> ret = interfaceMapper.findIFConfigList(new LinkedHashMapBuilder<String,Object>().add("state",1));
        Map<String,InterfaceConf> conf =ConfStore.getInterfaceConf();
        conf.clear();
        for(InterfaceConf c:ret){
            JSONObject verifyJson = JSON.parseObject(c.getVerify());
            c.setVerifyJson(verifyJson);// 转JSON

            JSONObject setvalueJson = JSON.parseObject(c.getSetvalue());
            c.setSetvalueJson(setvalueJson);// 转JSON

            JSONObject afterJson = JSON.parseObject(c.getAfter());
            c.setAfterJson(afterJson);// 转JSON
            c.setKhAfterByJson(afterJson);
            JSONObject frontJson = JSON.parseObject(c.getFront());
            c.setFrontJson(frontJson);// 转JSON
            c.setKhFrontByJson(frontJson);

            conf.put(c.getAlias(),c);

            String tabletype = c.getTabletype();
            if("SQL".equals(tabletype)){ //对复杂的SQL查询语句进行特殊处理
                c.setTables("("+c.getTables()+") t");
            }
        }
        int code = 1;
        return new LinkedHashMapBuilder<String,Object>().add("code",code).add("message",code == 1 ?"操作成功":"操作失败");
    }

    @PostMapping("addIFConfig")
    @ResponseBody
    public RetJson addIFConfig( @RequestBody Map<String,Object> data){
        String[] fields = {"",""};
        Object id = data.remove("id");
        int code = 0;
        try{
            if(id!=null&&id!=""){
                data.remove("addtime");
                data.remove("verifyjson");
                data.remove("afterjson");
                data.remove("frontjson");
                data.remove("setvaluejson");
                code = interfaceMapper.updateIFConfig(data,id.toString());
            }else{
                code = interfaceMapper.addIFConfig(data);
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

    @Autowired
    private KuaiConfig KuaiConfig;

    //查询接口基础配置
    @PostMapping("findKuaiConfig")
    @ResponseBody
    public KuaiConfig findKuaiConfig(){
        KuaiConfig ret = KuaiConfig;
        return ret;
    }

    //查询接口配置列表
    @PostMapping("findIFConfigList")
    @ResponseBody
    public List<InterfaceConf> findIFConfigList(@RequestBody Map<String,Object> where){
        List<InterfaceConf> ret = interfaceMapper.findIFConfigList(where);
        return ret;
    }

    @PostMapping("batchDelete")
    @ResponseBody
    public RetJson batchDelete(@RequestBody Map<String,Object> where){
        where.put("state","0");//必须是停用状态
        Map<String, Map<String, Object>> wheres = KuaiUtil.toWhere(where);
        KuaiUtil.setWhere(wheres,"id","=","in", null);
        int len = middleCurdMapper.delete("KUAI_INTERFACE_CONF", wheres);
        int code = len>0?1:0;
        return RetJson.by(String.valueOf(code),code == 1 ?"操作成功":"操作失败",len);
    }

    //导出接口配置列表
    @RequestMapping("downloadConf")
    public void downloadConf(HttpServletResponse response) throws IOException {
        List<InterfaceConf> ret = interfaceMapper.findIFConfigList(new LinkedHashMap<>());
        String str = JSONArray.toJSONString(ret);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=kuaiconfs.json");
        Writer out = response.getWriter();
        out.write(str);
        out.close();
    }

    //导入接口配置列表
    @PostMapping("importConf")
    @ResponseBody
    public RetJson importConf(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {

        byte[] bytes = file.getBytes();
        String text = new String(bytes,"UTF-8");
        JSONArray jsonArray = JSONArray.parseArray(text);
        List<Map> save = jsonArray.toJavaList(Map.class);
        log.info("save:::"+save);

        //先备份，在删除，再批量插入；
        int code = interfaceMapper.backConfTable("KIC_"+System.currentTimeMillis());
        if(code>=0){
            middleCurdMapper.delete("KUAI_INTERFACE_CONF",new LinkedHashMap<>());
            for(Map conf:save){
                code+= interfaceMapper.addIFConfig(conf);
            }

        }
        return RetJson.by(String.valueOf(code),code >= 1 ?"操作成功":"操作失败");
    }


    @PostMapping("getCurdHandle")
    @ResponseBody
    public RetJson getCurdHandle(){
        Map<String,Map<String, HandleConf>> conf =ConfStore.getHandleConf();
        Object json = JSONObject.toJSON(conf);
        return RetJson.by("0000","查询成功",json);
    }

    @PostMapping("refreshCurdHandle")
    @ResponseBody
    public RetJson refreshCurdHandle(){
        Map<String,Map<String, HandleConf>> conf = new LinkedHashMap<>();

        Map<String, HandleConf> front = getHander("front",CurdHandle.class);
        Map<String, HandleConf> after = getHander("after", CurdHandleAfter.class);
        conf.put("front",front);
        conf.put("after",after);

        ConfStore.setHandleConf(conf);
        return RetJson.by("0000","刷新成功",conf);
    }

    public  Map<String, HandleConf> getHander(String str,Class c){
        Map<String, HandleConf> map = new LinkedHashMap<>();
        String[] names = applicationContext.getBeanNamesForType(c);
        for(String name:names){
            Class<?> clazz = applicationContext.getType(name);
            HandleConf handleConf = AnnotationUtils.findAnnotation(clazz,HandleConf.class);
            map.put(name,handleConf);
        }
        return map;
    }

    //初始化接口
    public void initKApi() throws Exception {
        Map<String, InterfaceConf> conf =ConfStore.getInterfaceConf();
        Map<String, HandleConf> map = new LinkedHashMap<>();
        String[] names = applicationContext.getBeanNamesForType(AbsKApi.class);
        for(String name:names){
            Class<?> clazz = applicationContext.getType(name);
            KApi kApi = AnnotationUtils.findAnnotation(clazz, KApi.class);
            String alias = kApi.alias();
            String remark = kApi.remark();

            RegisterApi reg = RegisterApi.by(alias,kApi.type(),kApi.tables(),kApi.queryTable(),kApi.roles());
            AbsKApi absKApi = applicationContext.getBean(name, AbsKApi.class);
            InterfaceConf interfaceConf = absKApi.getApiConf(reg);
            interfaceConf.setKApi(absKApi);
            interfaceConf.setRemark(remark);
            conf.put(alias,interfaceConf);
        }
    }

}
