package cn.lxycx.kuaicore.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MiddleCurdMapper {

    /**
     * 获取查询数据的数量
     * @param tablename
     * @param wheres
     * @return
     */
    int getCount(String tablename, Map<String, Map<String, Object>> wheres);

    /**
     * 获取查询最大值
     * @param tablename
     * @param wheres
     * @return
     */
    int max(@Param("columnName") String columnName,@Param("tablename") String tablename,@Param("wheres") Map<String, Map<String, Object>> wheres);

    /**
     * 查询最小值
     * @param tablename
     * @param wheres
     * @return
     */
    int min(@Param("columnName")String columnName,@Param("tablename") String tablename,@Param("wheres") Map<String, Map<String, Object>> wheres);

    /**
     * 按条件分页查询
     * @param tablename 表名
     * @param field 自定义显示字段  aa,bb,cc
     * @param page 指定页
     * @param limit 每页显示数
     * @param wheres where条件，格式如 {"ID":{"=":"102"}}
     * @param order_by 排序条件，可以传Null  {"ID":"DESC"}
     * @return
     */
    List<Map<String, Object>> getPage(String tablename, String field, Integer page, Integer limit, Map<String, Map<String, Object>> wheres,Map<String,Object> order_by);

    /**
     * 按条件查询列表
     * @param tablename 表名
     * @param field 自定义显示字段  aa,bb,cc
     * @param wheres where条件，格式如 {"ID":{"=":"102"}}
     * @param order_by 排序条件，可以传Null  {"ID":"DESC"}
     * @return
     */
    List<Map<String,Object>> getList(String tablename, String field, Map<String, Map<String, Object>> wheres,Map<String,Object> order_by);

    /**
     * 查询最靠近的一条数据
     * @param tablename 表名
     * @param field 自定义显示字段  aa,bb,cc
     * @param wheres where条件，格式如 {"ID":{"=":"102"}}
     * @param order_by 排序条件，可以传Null  {"ID":"DESC"}
     * @return
     */
    Map<String,Object> getByOne(String tablename, String field, Map<String, Map<String, Object>> wheres,Map<String,Object> order_by);

    /**
     * 删除最靠近的一条数据
     * @param tablename 表名
     * @param wheres where条件，格式如 {"ID":{"=":"102"}}
     * @return
     */
    int deleteByOne(String tablename, Map<String, Map<String, Object>> wheres);

    /**
     * 删除指定条件的数据
     * @param tablename 表名
     * @param wheres where条件，格式如 {"ID":{"=":"102"}}
     * @return
     */
    int delete(String tablename, Map<String, Map<String, Object>> wheres);

    /**
     * 修改指定条件的数据
     * @param tablename 表名
     * @param set_data 需要修改的数据,格式如 { "NAME":"123" }
     * @param wheres where条件，格式如 {"ID":{"=":"102"}}
     * @return
     */
    int update(String tablename, Map<String, Object> set_data, Map<String, Map<String, Object>> wheres);

    int updatePlus(String tablename, Map<String, Object> set_data, Map<String, Map<String, Object>> wheres);

    /**
     * 插入一条数据
     * @param tablename 表名
     * @param set_data 需要修改的数据,格式如 { "NAME":"123" }
     * @return
     */
    int insert(String tablename, Map<String, Object> set_data);

    /**
     * 里面改插入数据
     * @param tablename
     * @param list 需要修改的数据,格式如 [{ "NAME":"123" },{"NAME":"222","AGE":11}] 需要插入的字段以第一条数据为准,例中的第二条数据，也只会插入NAME字段
     * @return
     */
    int batchInsert(String tablename, List<Map<String, Object>> list);


    /**
     * 执行 查询语句
     * @param sql
     * @param param
     * @return
     */
    List<Map<String,Object>> select(@Param("sql")String sql, @Param("P")Object... param);

    /**
     * 执行查询语句返回单个
     * @param sql
     * @return
     */
    Map<String,Object> selectOne(@Param("sql")String sql,@Param("P")Object... param);

    /**
     * 查询数量
     * @param sql
     * @param param
     * @return
     */
    Integer selectInt(@Param("sql")String sql,@Param("P")Object... param);

    /**
     * 查询数量
     * @param sql
     * @param param
     * @return
     */
    Object selectObj(@Param("sql")String sql,@Param("P")Object... param);

    /**
     * 执行更新语句
     * @param sql
     * @param param
     * @return
     */
    boolean updateSql(@Param("sql")String sql,@Param("P")Object... param);

}
