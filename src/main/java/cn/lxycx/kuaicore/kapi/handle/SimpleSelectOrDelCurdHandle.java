package cn.lxycx.kuaicore.kapi.handle;

import cn.lxycx.kuaicore.handles.CurdHandle;
import cn.lxycx.kuaicore.handles.CurdHandleException;

import java.util.Map;

public abstract class SimpleSelectOrDelCurdHandle<T> extends CurdHandle {

    @Override
    public  boolean front(Map<String, Object> set_data, Map<String, Map<String, Object>> wheres, String param) throws CurdHandleException {
        return front(wheres);
    }

    /**
     * 用于 KApi 中定义 handle
     * @param wheres
     * @return
     */
    public abstract  boolean front(Map<String, Map<String, Object>> wheres) throws CurdHandleException;
}
