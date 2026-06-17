package cn.lxycx.kuaicore.util.sql;

import cn.lxycx.kuaicore.util.Mapp;
import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 将POJO 构建成 where 条件
 * @param <T>
 */
public class Where<T> {
    @Data@AllArgsConstructor
    class W{
        String fh;
        Object o;
    }

    Class<T> pojo;
    List<W> fields = new ArrayList<>();
    public Where(Class<T> t){ this.pojo = t; }

    public  T set(String f){
        try {
            T t1 = pojo.newInstance();
            fields.add(new W(f, t1));
            return t1;
        }catch (Exception e){
            return null;
        }
    }
    public  Where set(String f,T pojo){
        fields.add(new W(f, pojo));
        return this;
    }

    public Where setIsNull(String field){
        fields.add(new W("is", new Mapp<>().set(field,"null")));
        return this;
    }

    public Where setIsNotNull(String field){
        fields.add(new W("is", new Mapp<>().set(field,"not null")));
        return this;
    }



    public Map<String, Map<String, Object>> getWheres(){
        Map<String, Map<String, Object>> where = new LinkedHashMap<>();
        for(W w:fields){
            Map<String, Object> data =Record.toMap(w.getO());
            String fh = w.getFh();
            data.forEach(new BiConsumer<String, Object>() {
                @Override
                public void accept(String k, Object v) {
                    Map<String, Object> dd = where.get(k);
                    if(dd == null){
                        dd = new LinkedHashMap<>();
                        where.put(k,dd);
                    }
                    dd.put(fh,v);
                }
            });
        }
        System.out.println(JSON.toJSONString(where));
        return where;
    }

}
