package cn.lxycx.kuaicore.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class KuaiUtil {

    Map<String,Map<String,String>> wheres = new LinkedHashMap<>();

    private KuaiUtil(){};

    public static KuaiUtil by(){
        return new KuaiUtil();
    }

    public KuaiUtil put(String k,String k2,String v){
        Map<String,String> m = new HashMap<>();
        m.put(k2,v);
        wheres.put(k,m);
        return this;
    }

    public Map<String,Map<String,String>> getWheres(){
        return wheres;
    }



    /**
     * 将普通的JSON对象转换成kuai框架的where条件 默认操作符是 = 号
     * @param data
     */
    public static Map<String,Map<String,Object>> toWhere(Map<String,Object> data){
        Map<String,Map<String,Object>> new_data = new LinkedHashMap<>();
        for(String k:data.keySet()){
            Object v = data.get(k);
            new_data.put(k,new LinkedHashMapBuilder<>().add("=",v));
        }
        return new_data;
    }

    /**
     * 修改Kuai框架的Where条件对象的 操作符
     * @param d Where条件对象
     * @param f 需要操作的字段
     * @param c 原来的操作符
     * @param n 新的操作符
     * @param v 新的值（如果为空则用原来的值）
     * @return 修改结果
     */
    public static boolean setWhere(Map<String,Map<String,Object>> d,String f,String c,String n,Object v){ //设置where 条件中的比较符(链式操作)
        Map<String,Object> dd = d.get(f);
        if(dd!=null){
            Object value = v!=null?v:dd.get(c);
            dd.remove(c);
            dd.put(n,value);
            return true;
        }
        return false;
    }


    public static Object parseWheres(Map<String, Map<String, Object>> wheres, String key){
        LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) wheres.get(key);
        return map.values().toArray()[0];
    }

    public static Object getObj(Map<String, Map<String,Object>> data,String key){
        Map<String, Object> map = data.get(key);
        Object[] values =map.values().toArray();
        if(values.length>=1){
            return values[0];
        }
        return null;
    }
    public static Object getObj(Map<String, Map<String,Object>> data,String key,String oper){
        Map<String,Object> data2 = data.get(key);
        if(data2!=null){
            return data2.get(oper);
        }
        return null;
    }
}
