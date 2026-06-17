package cn.lxycx.kuaicore.handles;

import cn.lxycx.kuaicore.bean.InterfaceConf;
import cn.lxycx.kuaicore.conf.RetJson;
import cn.lxycx.kuaicore.kapi.bean.SetHandle;
import cn.lxycx.kuaicore.kapi.bean.SetHandleAfter;
import cn.lxycx.kuaicore.util.CookieUtils;
import cn.lxycx.kuaicore.util.LinkedHashMapBuilder;
import cn.lxycx.kuaicore.util.RegexPro;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CurdHandleUtils {
    @Autowired
    ApplicationContext applicationContext;

    public boolean front(List<SetHandle> front, Map<String, Object> set_data, Map<String,Map<String,Object>> where) throws CurdHandleException {
        if(front!= null){
            int size = front.size();
            for(int i=0;i<size;i++){
                SetHandle hh = front.get(i);
                //String classname = hh.getString("name");
                String param = hh.getParam();
                //log.info("front.classname:"+classname);
                CurdHandle ch = hh.getHandle();//applicationContext.getBean(classname, CurdHandle.class);
                if(!ch.front(set_data,where,param)){
                    return false;
                }
            }
        }
        return true;
    }

    public <R> R  after(List<SetHandleAfter> after, Map<String, Object> set_data, Map<String,Map<String,Object>> wheres, R data) throws CurdHandleException{
        if(after!= null){
            int size = after.size();
            for(int i=0;i<size;i++){
                SetHandleAfter hh = after.get(i);
                //String classname = hh.getString("name");
                String param = hh.getParam();
              //  log.info("after.classname:"+classname);
                CurdHandleAfter ch = hh.getHandle();//applicationContext.getBean(classname, CurdHandleAfter.class);
                data = (R) ch.after(set_data,wheres,data,param);
            }
        }
        return data;
    }

/*    public Map<String,Object> after(JSONArray after,Map<String, String> set_data, Map<String,Map<String,String>> wheres, Map<String,Object> data) throws CurdHandleException{
        if(after!= null){
            int size = after.size();
            for(int i=0;i<size;i++){
                JSONObject hh = after.getJSONObject(i);
                String classname = hh.getString("name");
                String param = hh.getString("param");
                log.info("after.classname:"+classname);
                CurdHandleAfter ch = applicationContext.getBean(classname, CurdHandleAfter.class);
                data = ch.after(set_data,wheres,data,param);
            }
        }
        return data;
    }
    public Integer after(JSONArray after,Map<String, String> set_data, Map<String,Map<String,String>> wheres, int data) throws CurdHandleException{
        if(after!= null){
            int size = after.size();
            for(int i=0;i<size;i++){
                JSONObject hh = after.getJSONObject(i);
                String classname = hh.getString("name");
                String param = hh.getString("param");
                log.info("after.classname:"+classname);
                CurdHandleAfter ch = applicationContext.getBean(classname, CurdHandleAfter.class);
                data = ch.afterInt(set_data,wheres,data,param);
            }
        }
        return data;
    }*/




    private List<String> fs = Arrays.asList("=",">",">=","<","<=","!=","<>","in","like","not in","is");
    private List<String> fs_nb = Arrays.asList("=",">",">=","<","<=","!=","<>","in","like","not in","in_select","notin_select","is");
    private String add(String str,String f){
        return  f+str+f;
    }

    /**
     * 校验Where条件
     * @param conf
     * @param role
     * @param where
     * @param isnb 是否内部校验
     * @return
     */
    public RetJson validataWhere(InterfaceConf conf, String role, Map<String,Map<String,Object>> where,boolean isnb){
        if(conf!=null){

            String roles = conf.getRoles();
            //判断是否支持该类型权限
            if(roles.indexOf(role)<0){return RetJson.by("0002","不支持当前操作:"+role);}

            String iswu  = conf.getIscx();
            switch (role){
                case "添加":
                    iswu = "1";
                case "修改":
                    iswu = conf.getIsxg();
                case "删除":
                    iswu = conf.getIssc();
            }

            //判断是否允许空条件操作
            if(where.isEmpty()&&!"1".equals(iswu)){return RetJson.by("0003","不支持空条件操作");}

            JSONObject verifyJson = conf.getVerifyJson();
            String wheresss = add(conf.getWheres(),",");
            for (String k: where.keySet()) {
                if(wheresss.indexOf(add(k,","))<0){
                    return RetJson.by("0004","该查询条件不存在:"+k);
                }else{
                    String regx = verifyJson.getString(k);
                    Map<String,Object> vmap =  where.get(k);
                    if(vmap!=null&&vmap.size()==1){
                        String f = vmap.keySet().iterator().next();

                        if((isnb?fs_nb:fs).indexOf(f)>=0){//内部允许 in_select
                        /*  String value = vmap.get(f);
                             if(regx!=null){  // where 暂不校验输入值
                                if(RegexPro.pattern(regx, value)==null){
                                    return RetJson.by("0004","该字段值不符合规范:"+k+" ["+regx+"]");
                                }
                            }*/
                        }else{
                            return RetJson.by("0004","该字段操作符不符合规范:"+k+" ["+f+"]");
                        }


                    }else{
                        return RetJson.by("0004","该字段值格式有误:"+k);
                    }

                }
            }
            return null;
        }
        return RetJson.by("0001","无效的接口");
    }

    /**
     * 校验Order by
     * @param conf
     * @param order_by
     * @return
     */
    public RetJson validataOrderBy(InterfaceConf conf, Map<String, String> order_by) {
        JSONObject verifyJson = conf.getVerifyJson();
        String wheresss = add(conf.getWheres(),",");
        if(order_by!=null){
            for (String o: order_by.keySet()) {
                if(wheresss.indexOf(add(o,","))<0){
                    return RetJson.by("0004","该排序字段不被允许:"+o);
                }else{
                    String regx = verifyJson.getString(o);
                    String v =  order_by.get(o);
                    if(v == null||"".equals(v)){
                        order_by.put(o,"");
                    }else{
                        if(!v.matches("ASC|DESC|asc|desc")){
                            return RetJson.by("0004","该字段排序符不符合规范:"+o+" ["+v+"]");
                        }
                    }

                }
            }
        }
        return null;
    }

    /**
     * 校验插入或修改的数据
     * @param conf
     * @param role
     * @param set_data
     * @return
     */
    public RetJson validataSet(InterfaceConf conf, String role, Map<String, Object> set_data) {
        if(conf!=null){

            String roles = conf.getRoles();
            //判断是否支持该类型权限
            if(roles.indexOf(role)<0){return RetJson.by("0002","不支持当前操作:"+role);}

            if(set_data != null&&!set_data.isEmpty()){
                JSONObject verifyJson = conf.getVerifyJson();
                String keysss = add(conf.getKeys(),",");
                for (String k: set_data.keySet()) {
                    if(keysss.indexOf(add(k,","))<0){
                        return RetJson.by("0004","该字段不允许修改:"+k);
                    }else{
                        String regx = verifyJson.getString(k);
                        Object value = set_data.get(k);
                        if(!StringUtils.isEmpty(regx)){
                            String newvlaue = value!=null?value.toString():null;
                            if(RegexPro.pattern(regx, newvlaue)==null){
                                return RetJson.by("0004","该字段值不符合规范:"+k+" ["+regx+"]");
                            }
                        }
                    }
                }
            }else{
                return RetJson.by("0100","更新失败，缺少 SET_DATA 参数");
            }
            return null;
        }
        return RetJson.by("0001","无效的接口");
    }

    /**
     * 设置取值方式
     * @param request
     * @param setvalueJson
     * @param set_data
     * @param where
     * @return
     */
    public RetJson setValue(HttpServletRequest request, JSONObject setvalueJson, Map<String, Object> set_data, Map<String, Map<String, Object>> where) {

        for(String key:setvalueJson.keySet()){
            JSONObject json = setvalueJson.getJSONObject(key);
            String field = json.getString("field");
            String isnull = json.getString("isnull");
            String type = json.getString("type");
            JSONArray roles = json.getJSONArray("roles");
            String value = null;
            Map<String,String[]> ppp = request.getParameterMap();
            switch (type){
                case "Param":
                    value = request.getParameter(field);
                    break;
                case "Attr":
                    Object vvv = request.getAttribute(field);
                    value = vvv!=null?vvv.toString():null;
                    break;
                case "Header":
                    value = request.getHeader(field);
                    break;
                case "Cookie":
                    value = CookieUtils.by(request,null).getCookieValue(field);
                    break;
                case "Sql":
                    value = "("+field+")";
                    break;
                default:
                    break;
            }
            if(value!=null){ //取值不为空
                if(where!=null&&roles.contains("where")){
                    where.put(key,new LinkedHashMapBuilder().add("=",value));
                }
                if(roles.contains("set_data")){
                    set_data.put(key,value);
                }
            }else if("1".equals(isnull)){//设置允许为空的情况下不做处理
                //不做处理
            }else{
                return RetJson.by("0001",type+"中无"+field+"字段");
            }

        }
        return null;
    }


}
