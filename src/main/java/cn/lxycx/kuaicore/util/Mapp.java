package cn.lxycx.kuaicore.util;

import com.alibaba.fastjson.util.TypeUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class  Mapp<K,V> extends LinkedHashMap<K,V> {

    public static Mapp by(){return new Mapp();}

    public static <K,V> Mapp by(Map<K,V> map){
        Mapp m =new Mapp();  m.putAll(map);
        return m;
    }
    public static <K,V> Mapp by(K k, V v){
        Mapp m = new Mapp(); m.put(k,v);
        return m;
    }

    public Mapp<K,V> set(K k, V v){this.put(k,v);return this;}

    public String getStr(K k){
        if(this.containsKey(k)){
            return get(k).toString();
        }
        return null;
    }

    public int getIntValue(K k){  return getIntValue(k, 0); }
    public int getIntValue(K k,int def){
        Object value = this.get(k);
        Integer intVal = TypeUtils.castToInt(value);
        return intVal == null ? def : intVal;
    }

}
