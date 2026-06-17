package cn.lxycx.kuaicore.mapper;

import cn.lxycx.kuaicore.bean.InterfaceConf;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface InterfaceMapper {

    int createConfTable();
    int backConfTable(String table);

    List<Map<String,Object>> findTables();
    List<Map<String,Object>> findColumns(String tableName);

    /**
     * 添加一个简单的接口
     * @param data
     * @return
     */
    int addIFConfig(Map<String,Object> data);
    int updateIFConfig(@Param("maps") Map<String,Object> maps, String id);
    List<InterfaceConf> findIFConfigList(Map<String,Object> where);

}
