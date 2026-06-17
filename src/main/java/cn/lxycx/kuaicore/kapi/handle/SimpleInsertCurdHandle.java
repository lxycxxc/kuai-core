package cn.lxycx.kuaicore.kapi.handle;

import cn.lxycx.kuaicore.handles.CurdHandle;
import cn.lxycx.kuaicore.handles.CurdHandleException;

import java.util.Map;

public abstract class SimpleInsertCurdHandle extends CurdHandle {

    @Override
    public  boolean front(Map<String, Object> set_data, Map<String, Map<String, Object>> wheres, String param) throws CurdHandleException {
        return front(set_data);
    }

    /**
     * 用于 KApi 中定义 handle
     * @param set_data
     * @param <T>
     * @return
     */
    public abstract  <T> boolean front(Map<String, Object> set_data) throws CurdHandleException;
}
