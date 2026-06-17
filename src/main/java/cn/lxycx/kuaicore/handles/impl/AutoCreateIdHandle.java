package cn.lxycx.kuaicore.handles.impl;

import cn.hutool.core.lang.UUID;
import cn.lxycx.kuaicore.handles.CurdHandle;
import cn.lxycx.kuaicore.handles.CurdHandleException;
import cn.lxycx.kuaicore.handles.HandleConf;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@HandleConf(name="自动生成ID",type = "select,insert,update",remark = "ID,UUID 当指定字段（参数1）值为空时自动按照规则（参数２）［UUID:随机，MAX:数据库当前最大加1］创建。")
public class AutoCreateIdHandle  extends CurdHandle {

    @Value("${spring.datasource.driver-class-name}")
    private  String driver;

    @Override
    public  boolean front(Map<String, Object> set_data,Map<String, Map<String, Object>> wheres, String param) throws CurdHandleException {
        String[] ss = param.split(",");
        if(ss.length == 2){
            String field = ss[0];
            String type = ss[1];
            if(set_data.get(field) == null){
                if("UUID".equals(type)){
                    set_data.put(field, UUID.fastUUID().toString(true));
                }else if("MAX".equals(type)){
                    if(driver.indexOf("mysql")>=0){
                        set_data.put(field, "ifnull(MAX("+field+"),0)+1");
                    }else{
                        set_data.put(field, "nvl(MAX("+field+"),0)+1");
                    }
                }
            }
            return true;
        }
        throw new CurdHandleException("缺少必要配置参数：如 ID,UUID");
    }
}
