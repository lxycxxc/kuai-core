package cn.lxycx.kuaicore.util;

import java.util.LinkedHashMap;

public class LinkedHashMapBuilder<K,V> extends LinkedHashMap {
    public LinkedHashMapBuilder<K,V> add(K key, V value) {
        super.put(key, value);
        return this;
    }
}
