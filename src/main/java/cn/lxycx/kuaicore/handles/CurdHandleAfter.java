package cn.lxycx.kuaicore.handles;

import java.util.List;
import java.util.Map;

public abstract class CurdHandleAfter<T> {

    public <R>  R  after(Map<String, Object> set_data,Map<String,Map<String,Object>> wheres,R ret,String param) throws CurdHandleException{
        if(ret instanceof Integer){
            Integer data = (Integer) ret;
            return (R) after(set_data, wheres, data, param);
        }else if(ret instanceof List){
            List list = (List) ret;
            return (R) after(set_data, wheres, list, param);
        }else{
            return after(set_data, wheres, ret, param);
        }
    }

    //后处理
    public  int  after(Map<String, Object> set_data,Map<String,Map<String,Object>> wheres,int ret,String param) throws CurdHandleException{
        return ret;
    }
    public  T  after(Map<String,Map<String,Object>> wheres,T retmap,String param) throws CurdHandleException{
        return retmap;
    }
    public  List<T> after(Map<String, Object> set_data,Map<String,Map<String,Object>> wheres,List<T> list,String param) throws CurdHandleException {
        for(T map:list){
            map = after(set_data,wheres,map,param);
        }
        return list;
    }

}
