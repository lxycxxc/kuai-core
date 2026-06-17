package cn.lxycx.kuaicore.kapi.handle;

import cn.lxycx.kuaicore.handles.CurdHandleAfter;
import cn.lxycx.kuaicore.handles.CurdHandleException;

import java.util.Map;

public abstract class SimpleInsertCurdHandleAfter<T> extends CurdHandleAfter<T> {

    @Override
    public <R> R after(Map<String, Object> set_data, Map<String, Map<String, Object>> wheres, R ret, String param) throws CurdHandleException {
        if(ret instanceof Integer){
            Integer data = (Integer) ret;
            return (R) after(set_data, data);
        }
        return ret;
    }

    /**
     * 新增接口后置拦截
     * @param set_data 新增的内容
     * @param ret 执行的结果
     * @return
     */
    public abstract Integer after( Map<String, Object> set_data, int ret);

}
