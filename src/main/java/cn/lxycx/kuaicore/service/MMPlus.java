package cn.lxycx.kuaicore.service;

import cn.lxycx.kuaicore.mapper.MiddleCurdMapper;
import cn.lxycx.kuaicore.util.KuaiUtil;
import cn.lxycx.kuaicore.util.Mapp;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 操作增删改
 */
@Service
@Log4j2
public class MMPlus implements MiddleCurdMapper {
    @Lazy
    @Autowired
    MiddleCurdMapper middleCurdMapper;


    public int getCountSimple(String tablename, Map<String, Object> wheres) {
        return getCount(tablename, KuaiUtil.toWhere(wheres));
    }
    @Override
    public int getCount(String tablename, Map<String, Map<String, Object>> wheres) {
        return middleCurdMapper.getCount(tablename, wheres);
    }

//----------------------------------------------------------------------------------------

    public List<Map<String, Object>> getPage(String tablename, Integer page, Integer limit, Map<String, Object> wheres) {
        return middleCurdMapper.getPage( tablename,  "*",  page,  limit, KuaiUtil.toWhere(wheres), null) ;
    }
    public List<Map<String, Object>> getPage(String tablename, Integer page, Integer limit, Map<String, Object> wheres, Map<String, Object> order_by) {
        return middleCurdMapper.getPage( tablename,  "*",  page,  limit, KuaiUtil.toWhere(wheres), order_by) ;
    }
    @Override
    public List<Map<String, Object>> getPage(String tablename, String field, Integer page, Integer limit, Map<String, Map<String, Object>> wheres, Map<String, Object> order_by) {
        return middleCurdMapper.getPage( tablename,  field,  page,  limit, wheres, order_by) ;
    }



//---------------------------------------------------------------------------------------------------------


    // 查询列表
    public List<Map<String,Object>> getList(String tablename,  Map<String, Object> wheres){
        return getList(tablename, "*",  KuaiUtil.toWhere(wheres),  null);
    }

    public List<Map<String,Object>> getList(String tablename,  Map<String, Object> wheres, Map<String,Object> order_by){
        return getList(tablename, "*",  KuaiUtil.toWhere(wheres),  order_by);
    }

    @Override
    public List<Map<String,Object>> getList(String tablename, String field, Map<String, Map<String, Object>> wheres, Map<String,Object> order_by){
        List<Map<String,Object>> list = middleCurdMapper.getList( tablename,  field,  wheres,  order_by);
        /*List<Mapp> mappList = new ArrayList<>();
        list.forEach(new Consumer<Map<String, Object>>() {  @Override  public void accept(Map<String, Object> map) {  mappList.add(Mapp.by(map)); }});*/
        return list;
    }


//--------------------------------------------------------------------------------------------------------------

    //查询单个
    public Mapp getByOne(String tablename, Map<String, Object> wheres){
        return getByOne( tablename, "*", KuaiUtil.toWhere(wheres), null);
    }
    //查询单个
    public int count(String tablename, Map<String, Object> wheres){
        return middleCurdMapper.getCount(tablename, KuaiUtil.toWhere(wheres));
    }
    //查询最大值
    public int max(String columnName, String tablename, Map<String, Map<String, Object>> wheres){
        return middleCurdMapper.max(columnName, tablename, wheres);
    }
    //查询最小值
    public int min(String columnName, String tablename, Map<String, Map<String, Object>> wheres){
        return middleCurdMapper.min(columnName, tablename, wheres);
    }

    @Override
    public Mapp getByOne(String tablename, String field, Map<String, Map<String, Object>> wheres, Map<String,Object> order_by){
        Map<String,Object> map =  middleCurdMapper.getByOne( tablename, field,  wheres, order_by);
        if(map!=null){  return Mapp.by(map); }
        return null;
    }


//-------------------------------------------------------------------------------------------------------------

    public int deleteByOneSimple(String tablename, Map<String, Object> wheres) {
        if(wheres.isEmpty()){  whereIsNull(); }
        return middleCurdMapper.deleteByOne(tablename,KuaiUtil.toWhere(wheres));
    }
    @Override
    public int deleteByOne(String tablename, Map<String, Map<String, Object>> wheres) {
        if(wheres.isEmpty()){  whereIsNull(); }
        return middleCurdMapper.deleteByOne(tablename,wheres);
    }




//-------------------------------------------------------------------------------------------------------------


    public int deleteSimple(String tablename, Map<String, Object> wheres) {
        if(wheres.isEmpty()){  whereIsNull(); }
        return middleCurdMapper.delete(tablename,KuaiUtil.toWhere(wheres));
    }
    @Override
    public int delete(String tablename, Map<String, Map<String, Object>> wheres) {
        if(wheres.isEmpty()){  whereIsNull(); }
        return middleCurdMapper.delete(tablename,wheres);
    }



//-------------------------------------------------------------------------------------------------------------


    public int updateSimple(String tablename, Map<String, Object> set_data, Map<String, Object> wheres) {
        if(wheres.isEmpty()){  whereIsNull(); }
        return middleCurdMapper.update( tablename, set_data,  KuaiUtil.toWhere(wheres));
    }
    @Override
    public int update(String tablename, Map<String, Object> set_data, Map<String, Map<String, Object>> wheres) {
        if(wheres.isEmpty()){  whereIsNull(); }
        return middleCurdMapper.update( tablename, set_data,  wheres);
    }



//-------------------------------------------------------------------------------------------------------------
    @Override
    public int updatePlus(String tablename, Map<String, Object> set_data, Map<String, Map<String, Object>> wheres) {
        if(wheres.isEmpty()){  whereIsNull(); }
        return middleCurdMapper.updatePlus( tablename, set_data,  wheres);
    }
//-------------------------------------------------------------------------------------------------------------
    @Override
    public int insert(String tablename, Map<String, Object> set_data) {
        return middleCurdMapper.insert( tablename, set_data);
    }
    //-------------------------------------------------------------------------------------------------------------
    @Override
    public int batchInsert(String tablename, List<Map<String, Object>> list) {
        return middleCurdMapper.batchInsert( tablename, list);
    }


    /**
     * 执行查询sql
     * @param sql select a,b,c from xxxx where a=? and b=?
     * @param param a,b
     * @return
     */
    @Override
    public List<Map<String, Object>> select(String sql, Object... param) {
        for(int i=0;i<param.length;i++) {
            sql = sql.replaceFirst("\\?", "#{P["+i+"]}");
        }
        log.info("select:"+sql);
        return middleCurdMapper.select(sql, param);
    }

    /**
     * 执行查询sql
     * @param sql select a,b,c from xxxx where a=? and b=?
     * @param param
     * @return
     */
    @Override
    public Map<String, Object> selectOne(String sql, Object... param) {
        for(int i=0;i<param.length;i++) {
            sql = sql.replaceFirst("\\?", "#{P["+i+"]}");
        }
        log.info("selectOne:"+sql);
        return middleCurdMapper.selectOne(sql, param);
    }

    /**
     * 查询返回数量
     * @param sql select count(1) from xxxx where a=? and b=?
     * @param param
     * @return
     */
    @Override
    public Integer selectInt(String sql, Object... param) {
        for(int i=0;i<param.length;i++) {
            sql = sql.replaceFirst("\\?", "#{P["+i+"]}");
        }
        log.info("selectOne:"+sql);
        return middleCurdMapper.selectInt(sql, param);
    }

    /**
     * 查询返回Object
     * @param sql select max(a) from xxxx where a=? and b=?
     * @param param
     * @return
     */
    @Override
    public Object selectObj(String sql, Object... param) {
        for(int i=0;i<param.length;i++) {
            sql = sql.replaceFirst("\\?", "#{P["+i+"]}");
        }
        log.info("selectObj:"+sql);
        return middleCurdMapper.selectObj(sql, param);
    }

    /**
     * 执行修改语句
     * @param sql update xxxx set c=?,z=? where a=? and b=?
     * @param param c,z,a,b
     * @return
     */
    @Override
    public boolean updateSql(String sql, Object... param) {
        for(int i=0;i<param.length;i++) {
            sql = sql.replaceFirst("\\?", "#{P["+i+"]}");
        }
        log.debug("findSql:"+sql);
        return middleCurdMapper.updateSql(sql, param);
    }

    //不允许空条件操作
    public static void whereIsNull(){
        System.out.println("当前操作Where条件不允许为空");
        String str = null;str.toString();
    }

}
