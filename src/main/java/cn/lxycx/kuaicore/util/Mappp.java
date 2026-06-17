package cn.lxycx.kuaicore.util;


import java.util.LinkedHashMap;

public class Mappp<K,HashMap> extends LinkedHashMap<K, HashMap> {

    public static Mappp by(){return new Mappp();}

    public static Mappp by(Mappp map){
        Mappp m =new Mappp();  m.putAll(map);
        return m;
    }
    public static <K,F,V> Mappp by(K k, V v){
        return by(k, "=", v);
    }
    public static <K,F,V> Mappp by(K k, F f, V v){
        Mappp m2 = new Mappp();
        m2.put(k,Mapp.by(f,v));
        return m2;
    }

    public <F,V> Mappp<K,HashMap> set(K k, V v){
        return set(k,"=", v);
    }
    public <F,V> Mappp<K,HashMap> set(K k, F f, V v){
        HashMap map = (HashMap) Mapp.by(f,v);
        this.put(k, map);
        return this;
    }
}
