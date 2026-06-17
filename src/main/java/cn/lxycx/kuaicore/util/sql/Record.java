package cn.lxycx.kuaicore.util.sql;

import cn.hutool.core.bean.BeanUtil;
import cn.lxycx.kuaicore.service.MMPlus;
import cn.lxycx.kuaicore.util.KuaiUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 用Pojo 代替 Map
 */
@Component
public class Record {

    @Autowired
    MMPlus mmPlus;

    public static <T> String getTableName(T t){
        TableName handleConf = AnnotationUtils.findAnnotation(t.getClass(), TableName.class);
        return handleConf.value();
    }
    public static <T> String getTableName(Class t){
        TableName handleConf = AnnotationUtils.findAnnotation(t, TableName.class);
        return handleConf.value();
    }


    public <T,W> List<T> getList(T  wheres) {
        String tablename = getTableName(wheres);
        List<Map<String, Object>> list = mmPlus.getList(tablename,  toMap(wheres));
        return toBeanList(list,wheres);
    }

    public <T> List<T> getList(T wheres, Map<String, Object> order_by) {
        String tablename = getTableName(wheres);
        List<Map<String, Object>> list = mmPlus.getList(tablename, "*", KuaiUtil.toWhere(toMap(wheres)), order_by);
        return toBeanList(list,wheres);
    }

    public <T> List<T> getList(Where<T> where,String field,Map<String, Object> order_by) {

        Class pojo = where.pojo;
        String tablename = getTableName(pojo);
        Map<String, Map<String, Object>> wheres  = where.getWheres();

        List<Map<String, Object>> list = mmPlus.getList(tablename, field, wheres, order_by);
        return (List<T>) toBeanList(list,pojo);
    }

    public <T> List<Map<String, Object>> getMapList(Where<T> where,String field) {
        Class pojo = where.pojo;
        String tablename = getTableName(pojo);
        Map<String, Map<String, Object>> wheres  = where.getWheres();
        List<Map<String, Object>> list = mmPlus.getList(tablename, field, wheres, null);
        return list;
    }

    public <T> T  getByOne(T wheres) {
        String tablename = getTableName(wheres);
        Map<String, Object> data = mmPlus.getByOne(tablename, toMap(wheres));
        return toBean(data,wheres);
    }

    public <T> int count(T wheres) {
        String tablename = getTableName(wheres);
        return mmPlus.count(tablename, toMap(wheres));
    }

    public <T> int max(String columnName, Where<T> where) {
        Class pojo = where.pojo;
        String tablename = getTableName(pojo);
        Map<String, Map<String, Object>> wheres  = where.getWheres();
        return mmPlus.max(columnName, tablename, wheres);
    }

    public <T> int min(String columnName, Where<T> where) {
        Class pojo = where.pojo;
        String tablename = getTableName(pojo);
        Map<String, Map<String, Object>> wheres  = where.getWheres();
        return mmPlus.min(columnName, tablename, wheres);
    }

    public <T> T getByOne(Where<T> where,String field, Map<String, Object> order_by) {
        Class pojo = where.pojo;
        String tablename = getTableName(pojo);
        Map<String, Map<String, Object>> wheres  = where.getWheres();
        Map<String, Object> data = mmPlus.getByOne(tablename, field, wheres, order_by);
        return (T) toBean(data,pojo);
    }


    public <T> int deleteByOneSimple(T wheres) {
        String tablename = getTableName(wheres);
        return mmPlus.deleteByOneSimple(tablename, toMap(wheres));
    }

    public int deleteByOne(String tablename, Map<String, Map<String, Object>> wheres) {
        return mmPlus.deleteByOne(tablename, wheres);
    }

    public <T> int deleteSimple(T wheres) {
        String tablename = getTableName(wheres);
        return mmPlus.deleteSimple(tablename, toMap(wheres));
    }

    public int delete(String tablename, Map<String, Map<String, Object>> wheres) {
        return mmPlus.delete(tablename, wheres);
    }

    public <T> int updateSimple(T set_data, T wheres) {
        String tablename = getTableName(set_data);
        return mmPlus.updateSimple(tablename, toMap(set_data), toMap(wheres));
    }

    public <T>int update(T set_data, Map<String, Map<String, Object>> wheres) {
        String tablename = getTableName(set_data);
        return mmPlus.update(tablename, toMap(set_data), wheres);
    }


    public <T> int insert(T set_data) {
        String tablename = getTableName(set_data);
        return mmPlus.insert(tablename, toMap(set_data));
    }

    public <T> int batchInsert(List<T> list) {
        String tablename = getTableName(list.get(0));
        return mmPlus.batchInsert(tablename, toMapList(list));
    }



//-------------------工具类----------------------------
    public static <T> Map<String,Object> toMap(T t){
        Map<String,Object> www = BeanUtil.beanToMap(t,true,true);
        /*Map<String,String> ww2 = new HashMap<>();
        www.forEach(new BiConsumer<String, Object>() {
            @Override
            public void accept(String s, Object o) {
                if(o!=null){
                    ww2.put(s,o.toString());
                }
            }
        });*/
        return www;
    }
    public static <T> List<Map<String,Object>>  toMapList(List<T> data){
        List<Map<String,Object>> list = new ArrayList();
        data.forEach(new Consumer<T>() {
            @Override
            public void accept(T t) {
                Map<String,Object> dd = toMap(t);
                list.add(dd);
            }
        });
        return list;
    }




    public static <T> T toBean(Map<String, Object> data,T t){
        if(!(t == null || t instanceof Map)) {
            Class<T> ttt = (Class<T>) t.getClass();
            return BeanUtil.toBeanIgnoreCase(data, ttt, true);
        }
        return (T) data;
    }

    public static <T> List<T>  toBeanList(List<Map<String, Object>> data,T t){
        if(!(t == null || t instanceof  Map)){
            List<T> list = new ArrayList();
            data.forEach(new Consumer<Map<String, Object>>() {
                @Override
                public void accept(Map<String, Object> stringObjectMap) {
                    T dd = toBean(stringObjectMap,t);
                    list.add(dd);
                }
            });
            return list;
        }
        return (List<T>) data;
    }


    public static <T> T toBean(Map<String, Object> data,Class t){
        if(!(t == null ||  t.isInstance(new HashMap<>()))) {
            return (T) BeanUtil.toBeanIgnoreCase(data, t, true);
        }
        return (T) data;
    }

    public static <T> List<T>  toBeanList(List<Map<String, Object>> data,Class t){
        if(!(t == null || t.isInstance(new HashMap<>()))){
            List<T> list = new ArrayList();
            data.forEach(new Consumer<Map<String, Object>>() {
                @Override
                public void accept(Map<String, Object> stringObjectMap) {
                    T dd = toBean(stringObjectMap,t);
                    list.add(dd);
                }
            });
            return list;
        }
        return (List<T>) data;
    }

}
