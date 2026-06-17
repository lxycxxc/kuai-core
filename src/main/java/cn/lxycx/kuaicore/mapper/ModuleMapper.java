package cn.lxycx.kuaicore.mapper;

import org.apache.ibatis.annotations.Mapper;

@Deprecated
@Mapper
public interface ModuleMapper {

    /**
     * 初始化文件上传下载模块模块
     * @return
     */
    int initFileStore();

}
