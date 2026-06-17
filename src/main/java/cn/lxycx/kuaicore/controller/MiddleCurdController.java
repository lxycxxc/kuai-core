package cn.lxycx.kuaicore.controller;

import cn.lxycx.kuaicore.bean.InterfaceConf;
import cn.lxycx.kuaicore.conf.ConfStore;
import cn.lxycx.kuaicore.conf.Ret;
import cn.lxycx.kuaicore.conf.RetJson;
import cn.lxycx.kuaicore.service.MiddleService;
import cn.lxycx.kuaicore.util.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("middle")
public class MiddleCurdController {

    @Autowired
    MiddleService middleService;

    @Autowired
    HttpServletRequest request;
    @Autowired
    HttpServletResponse response;

    @Autowired
    Ret ret;

    //所有接口配置
    Map<String, InterfaceConf> interfaceConf = ConfStore.getInterfaceConf();


    //驼峰转下划线
    public <T> Map<String, T> camelToSnake(Map<String, T> camelMap){
        return DataUtil.camelToSnake(camelMap);
    }


//-----------------------------------查询操作-----------------------------------------------------------
    @RequestMapping("/{alias}/getConf")
    public Ret getList(@PathVariable String alias){
        InterfaceConf conf = interfaceConf.get(alias);
        if(conf!=null){
            return ret.by(RetJson.by("0000","查询成功",conf));
        }
        return ret.by(RetJson.by("-0001","查询失败",null));
    }


    /**
     * 查询列表
     * @param alias
     * @param param
     * @return
     */
    @RequestMapping("/{alias}/getList")
    public Ret getList(@PathVariable String alias, String field, @RequestBody Map<String,Map<String,Object>> param){
        Map<String,Object> order_by = param.remove("ORDER_BY");
        RetJson retJson = middleService.getList(request,alias,field,camelToSnake(param),camelToSnake(order_by));
        return ret.by(retJson);
    }

    /**
     * 分页查询
     * @param alias
     * @param page
     * @param limit
     * @param param
     * @return
     */
    @RequestMapping("/{alias}/getPage/{page}/{limit}")
    public Ret getPage(@PathVariable String alias,@PathVariable int page,@PathVariable int limit, String field
            , @RequestBody Map<String,Map<String,Object>> param){
        Map<String,Object> order_by = param.remove("ORDER_BY");
        RetJson retJson = middleService.getPage(request,alias,field,page,limit,camelToSnake(param),camelToSnake(order_by));
        retJson.setPage(page);
        retJson.setLimt(limit);
        return ret.by(retJson);
    }

    /**
     * 查询一条数据
     * @param alias
     * @param param
     * @return
     */
    @RequestMapping("/{alias}/getByOne")
    public Ret getByOne(@PathVariable String alias, String field, @RequestBody Map<String,Map<String,Object>> param){
        log.info("getByOne");
        Map<String,Object> order_by = param.remove("ORDER_BY");
        RetJson retJson = middleService.getByOne(request,alias,field,camelToSnake(param),camelToSnake(order_by));
        return ret.by(retJson);
    }

    /**
     * 查询一条数据
     * @param alias
     * @param param
     * @return
     */
    @RequestMapping("/{alias}/getDataPlus")
    public Ret getDataPlus(@PathVariable String alias, String method, @RequestBody Map<String,Map<String,Object>> param){
        log.info("getDataPlus");
        RetJson retJson = middleService.getDataPlus(request,alias,method,camelToSnake(param));
        return ret.by(retJson);
    }

//-----------------------------------查询操作-----------------------------------------------------------


//-----------------------------------删除操作-----------------------------------------------------------

    /***
     * 删除一条数据
     * @param alias
     * @param param
     * @return
     */
    @RequestMapping("/{alias}/deleteByOne")
    public Ret deleteByOne(@PathVariable String alias, @RequestBody Map<String,Map<String,Object>> param){
        log.info("deleteByOne");
        RetJson retJson = middleService.deleteByOne(request,alias,camelToSnake(param));
        return ret.by(retJson);
    }

    /**
     *  按指定条件删除
     * @param alias
     * @param param
     * @return
     */
    @RequestMapping("/{alias}/delete")
    public Ret delete(@PathVariable String alias, @RequestBody Map<String,Map<String,Object>> param){
        RetJson retJson = middleService.delete(request,alias,camelToSnake(param));
        return ret.by(retJson);
    }

//-----------------------------------删除操作-----------------------------------------------------------



//-----------------------------------添加操作-----------------------------------------------------------

    /**
     * 添加数据操作
     * @param alias 接口别名
     * @param param {"SET_DATA":{ "AAAA":"123" }}
     * @return
     */
    @RequestMapping("/{alias}/insert")
    public Ret insert(@PathVariable String alias, @RequestBody Map<String,Map<String,Object>> param){
        Map<String,Object> set_data = param.remove("SET_DATA");
        RetJson retJson = middleService.insert(request,alias,camelToSnake(set_data));
        return ret.by(retJson);
    }
//-----------------------------------添加操作-----------------------------------------------------------



//-----------------------------------修改操作-----------------------------------------------------------

    /**
     * 修改操作
     * @param alias 接口别名
     * @param param { "SET_DATA":{ "AAAA":"123" }, "AGENCY_ID":{"=":"102001"} } 其中SET_DATA存放修改数据，其余是查询条件
     * @return
     */
    @RequestMapping("/{alias}/update")
    public Ret update(@PathVariable String alias, @RequestBody Map<String,Map<String,Object>> param){
        Map<String,Object> set_data = param.remove("SET_DATA");
        RetJson retJson = middleService.update(request,alias,camelToSnake(set_data),camelToSnake(param));
        return ret.by(retJson);
    }

    @RequestMapping("/{alias}/updatePlus")
    public Ret updatePlus(@PathVariable String alias, @RequestBody Map<String,Map<String,Object>> param){
        Map<String,Object> set_data = param.remove("SET_DATA");
        RetJson retJson = middleService.updatePlus(request,alias, camelToSnake(set_data),camelToSnake(param));
        return ret.by(retJson);
    }
//-----------------------------------修改操作-----------------------------------------------------------

    //-----------------------------------上传操作-----------------------------------------------------------

    /**
     * 文件上传
     * @param path
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping("/upload/{path}")
    public Ret upload(@PathVariable String path, @RequestParam(name = "file") MultipartFile[] file) throws IOException {
        RetJson retJson = middleService.upload(path,file);
        return ret.by(retJson);
    }

    //-----------------------------------上传操作-----------------------------------------------------------
    //-----------------------------------下载操作-----------------------------------------------------------

    /**
     * 文件下载
     * @param uuid
     * @param filename
     * @return
     * @throws IOException
     */
    @RequestMapping("/download")
    public RetJson download(String uuid,String filename) throws IOException {
        RetJson retJson = middleService.download(response,uuid,filename);
        return retJson;
    }

    //-----------------------------------下载操作-----------------------------------------------------------

    /**
     * 文件下载(暂不对外开放)
     * @param uuid
     * @return
     * @throws IOException
     */
    //@RequestMapping("/deleteFile")
    public RetJson deleteFile(String uuid) throws IOException {
        OutputStream out = response.getOutputStream();
        RetJson retJson = middleService.deleteFile(uuid);
        return retJson;
    }

}
