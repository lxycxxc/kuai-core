package cn.lxycx.kuaicore.conf;

import cn.lxycx.kuaicore.bean.InterfaceConf;
import cn.lxycx.kuaicore.handles.CurdHandleException;
import cn.lxycx.kuaicore.handles.CurdHandleSuccessException;
import cn.lxycx.kuaicore.handles.CurdHandleUtils;
import cn.lxycx.kuaicore.util.KuaiReqUtil;
import cn.lxycx.kuaicore.util.LinkedHashMapBuilder;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class ValidateAspect {
    @Autowired
    CurdHandleUtils curdHandleUtils;
    //所有接口配置
    Map<String, InterfaceConf> interfaceConf = ConfStore.getInterfaceConf();
    Map<String,String> roles = new LinkedHashMapBuilder().add("select","查询").add("update","修改").add("insert","添加").add("delete","删除");


    @Pointcut("execution(public * cn.lxycx.kuaicore.service.*.*(..))")
    public void validate(){}

    @Before("validate()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        log.info("执行：doBefore");
    }

    @After("validate()")
    public void doAfter(JoinPoint joinPoint) throws Throwable {
        log.info("执行：doAfter");
    }

    @Around("validate()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("执行：doAround");
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Validate validate = method.getAnnotation(Validate.class);
        if(validate!=null){
            Map<String,Object> params = new LinkedHashMap<>();
            String [] names = methodSignature.getParameterNames();
            Object[] args = joinPoint.getArgs();
            for(int i=0;i<names.length;i++){
                params.put(names[i],args[i]);
            }
            String alias = params.get("alias").toString();
            HttpServletRequest request = (HttpServletRequest) params.get("request");
            Map<String, Map<String, Object>> where = null;
            Map<String, Object> set_data2 = null;

            if(params.containsKey("where")){
                where = (Map<String, Map<String, Object>>) params.get("where");
            }
            if(params.containsKey("set_data")){
                set_data2 = (Map<String, Object>) params.get("set_data");
            }




            InterfaceConf conf = interfaceConf.get(alias);
            if(conf!=null){
                JSONObject setvalueJson = conf.getSetvalueJson();
                Map<String, Object> set_data = new LinkedHashMap<>();
                if(setvalueJson!=null&&!setvalueJson.isEmpty()){
                    //从其他位置取值
                    RetJson sv = curdHandleUtils.setValue(request,setvalueJson,set_data,where);
                    if(sv!= null) return sv;
                }
                String type = validate.type();
                String typename = roles.get(type);
                //校验 Where 条件
                //前置处理

                if(where!=null){ //如果有where 条件参数
                    RetJson vw = curdHandleUtils.validataWhere(conf,typename,where,false);
                    if(vw!= null) return vw;

                    //扩展操作，方便外部其他拦截器传值
                    where.putAll(KuaiReqUtil.getWheres(request));
                }



                try {
                    boolean  flag = curdHandleUtils.front(conf.getKhFront().get(type),set_data2, where);
                    if(!flag){ return RetJson.by("-0001","前处理不通过");}
                }catch (CurdHandleSuccessException e){//前处理不通过，但是返回成功
                    return RetJson.by("0000",e.getMessage());
                }catch (CurdHandleException e){//前处理不通过
                    return RetJson.by("-0001",e.getMessage());
                }

                if(where!=null){ //如果有where 条件参数
                    RetJson vw = curdHandleUtils.validataWhere(conf,typename,where,true);
                    if(vw!= null) return vw;
                }

                if("update".equals(type)||"insert".equals(type)){ //update 特殊处理

                    set_data2.putAll(set_data);
                    RetJson vs = curdHandleUtils.validataSet(conf,typename,set_data2);
                    if(vs!= null) return vs;
                }else if("select".equals(type)){ //查询语句 order by
                    Map<String, String> order_by = (Map<String, String>) params.get("order_by");
                    RetJson vs = curdHandleUtils.validataOrderBy(conf,order_by);
                    if(vs!= null) return vs;

                    //校验field
                    Object field = params.get("field");
                    if(field!=null){
                        String confField = ","+conf.getField()+",";
                        String[] fs = field.toString().split(",");
                        for(String f:fs){
                            if(confField.indexOf(""+f+"")<0){
                                return RetJson.by("0005","该字段不在查询字段范围内:["+f+"]");
                            }
                        }
                    }
                }

                return joinPoint.proceed();
            }

            return RetJson.by("0001","无效的接口");
        }
        return joinPoint.proceed();
    }



}
