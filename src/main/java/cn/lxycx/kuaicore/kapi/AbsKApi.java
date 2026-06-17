package cn.lxycx.kuaicore.kapi;


import cn.lxycx.kuaicore.bean.InterfaceConf;
import cn.lxycx.kuaicore.conf.RetJson;
import cn.lxycx.kuaicore.kapi.bean.KhAfter;
import cn.lxycx.kuaicore.kapi.bean.KhFront;

import java.util.Map;

public abstract class AbsKApi<T> {

    public InterfaceConf getApiConf(RegisterApi<T> reg) throws Exception {
        setting(reg);
        //setField(reg.getFields());
        front(reg.getFront()).after(reg.getAfter());
        return reg.getInterfaceConf();
    }

    /**
     * 接口设置
     * @param reg
     */
    public abstract void setting (RegisterApi<T> reg);
 /*   {
        //设置允许无条件操作，默认不允许
        reg.cxYes()
        //设置查询显示的字段，默认*全部
        .setQueryField("")
        //设置允许我作为条件的字段，默认没有
        .setWheres("")
        //
        .setKeys("");
    }*/

    /**
     * 设置允许修改的字段，默认没有
     * @param fields
     */
   // public abstract  void setField(List<KField> fields);

    /**
     * 添加前置拦截
     * @return
     */
    public AbsKApi front(KhFront front){
        return this;
    }

    /**
     * 添加后置拦截
     * @return
     */
    public AbsKApi after(KhAfter after){
        return this;
    }


    /**
     * 扩展查询
     * @return
     */
    public RetJson getDataPlus(String method,Map<String, Map<String,Object>> param){
        return RetJson.by("1001","当前方法未定义");
    }


}
