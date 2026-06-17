package cn.lxycx.kuaicore.kapi.handle;

import cn.lxycx.kuaicore.handles.CurdHandleAfter;
import cn.lxycx.kuaicore.handles.CurdHandleException;

import java.util.List;
import java.util.Map;

public abstract class SimpleSelectCurdHandleAfter<T> extends CurdHandleAfter<T> {

    @Override
    public <R> R after(Map<String, Object> set_data, Map<String, Map<String, Object>> wheres, R ret, String param) throws CurdHandleException {
        if(ret instanceof List){
            List list =  (List)ret;
            return (R) afterList(wheres, list);
        }else {
            return (R)afterOne(wheres, (T)ret);
        }
    }

    /**
     * 查询单条数据接口后处理(getByOne)
     * @param wheres 查询时的where条件
     * @param retmap 查询的结果
     * @return
     */
    public abstract  T afterOne( Map<String, Map<String, Object>> wheres, T retmap);

    /**
     * 查询多条数据接口后处理(getList,getPage)
     * @param wheres 查询时的where条件
     * @param retlist 查询的结果
     * @return
     */
    public abstract  List<T> afterList(Map<String, Map<String, Object>> wheres, List<T> retlist);
}
