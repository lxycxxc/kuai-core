package cn.lxycx.kuaicore.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.lxycx.kuaicore.bean.FileStoreConf;
import cn.lxycx.kuaicore.bean.InterfaceConf;
import cn.lxycx.kuaicore.bean.KuaiConfig;
import cn.lxycx.kuaicore.bean.KuaiFileStore;
import cn.lxycx.kuaicore.conf.ConfStore;
import cn.lxycx.kuaicore.conf.RetJson;
import cn.lxycx.kuaicore.conf.Validate;
import cn.lxycx.kuaicore.handles.CurdHandleUtils;
import cn.lxycx.kuaicore.mapper.MiddleCurdMapper;
import cn.lxycx.kuaicore.util.FileStroreUtil;
import cn.lxycx.kuaicore.util.KuaiUtil;
import cn.lxycx.kuaicore.util.sql.Record;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
public class MiddleService {
    //所有接口配置
    Map<String, InterfaceConf> interfaceConf = ConfStore.getInterfaceConf();

    @Lazy
    @Autowired
    MiddleCurdMapper middleCurdMapper;
    @Autowired
    CurdHandleUtils curdHandleUtils;

    @Autowired
    KuaiConfig kuaiConfig;

    Map<String, FileStoreConf> fsList;

    @Autowired
    public void init(){
        fsList = kuaiConfig.getFileStores();
    }

    FileStroreUtil fileStroreUtil;
    {
        try {
            fileStroreUtil = SpringUtil.getBean(FileStroreUtil.class);
        }catch (Exception e){
            fileStroreUtil = new FileStroreUtil();
        }
    }


    public String fieldHandle(String field,String confField){
        return confField;
    }


    @Validate(type = "select")
    public RetJson getList(HttpServletRequest request,String alias,String field, Map<String,Map<String,Object>> where,Map<String,Object> order_by){
        InterfaceConf conf = interfaceConf.get(alias);
        try{
            String tablename = conf.getQueryTable();
            if(StringUtils.isEmpty(tablename)){
                tablename = conf.getTables();
            }

            String newField= StrUtil.isBlank(field) ?conf.getField():field;
            List<Map<String,Object>> ret = middleCurdMapper.getList(tablename,newField,where,order_by);

            Class pojo = conf.getPojo();
            List newret = Record.toBeanList(ret,pojo);

            newret = curdHandleUtils.after(conf.getKhAfter().get("select"),null,where,newret);
            return RetJson.by("0000","查询成功",ret.size(),newret);
        }catch (Exception e){
            log.error("getList",e);
            return RetJson.by("-0002",e.getMessage()); //后处理不通过
        }

    }

    @Validate(type = "select")
    public RetJson getPage(HttpServletRequest request,String alias,String field, Integer page, Integer limit, Map<String, Map<String, Object>> where,Map<String,Object> order_by) {
        InterfaceConf conf = interfaceConf.get(alias);
        try{
            String tablename = conf.getQueryTable();
            if(StringUtils.isEmpty(tablename)){
                tablename = conf.getTables();
            }
            String newField= StrUtil.isBlank(field) ?conf.getField():field;
            int cou = middleCurdMapper.getCount(tablename,where);
            List<Map<String,Object>> ret = middleCurdMapper.getPage(tablename,newField,page,limit,where,order_by);

            Class pojo = conf.getPojo();
            List newret = Record.toBeanList(ret,pojo);

            newret = curdHandleUtils.after(conf.getKhAfter().get("select"),null,where,newret);
            return RetJson.by("0000","查询成功",cou,newret);
        }catch (Exception e){
            log.error("getPage",e);
            return RetJson.by("-0002",e.getMessage()); //后处理不通过
        }

    }

    @Validate(type = "select")
    public RetJson getByOne(HttpServletRequest request,String alias,String field,Map<String,Map<String,Object>> where,Map<String,Object> order_by){
        InterfaceConf conf = interfaceConf.get(alias);
        try{
            String tablename = conf.getQueryTable();
            if(StringUtils.isEmpty(tablename)){
                tablename = conf.getTables();
            }
            String newField= StrUtil.isBlank(field) ?conf.getField():field;
            Map<String,Object> data = middleCurdMapper.getByOne(tablename,newField,where,order_by);

            Class pojo = conf.getPojo();
            Object newdata = Record.toBean(data,pojo);

            newdata = curdHandleUtils.after(conf.getKhAfter().get("select"),null,where,newdata);
            return RetJson.by("0000","查询成功",newdata);
        }catch (Exception e){
            log.error("getByOne",e);
            return RetJson.by("-0002",e.getMessage()); //后处理不通过
        }
    }


    public RetJson getDataPlus(HttpServletRequest request, String alias, String method, Map<String,Map<String,Object>> param) {
        InterfaceConf conf = interfaceConf.get(alias);
        Class pojo = conf.getPojo();
        //Object newdata = Record.toBean(param,pojo);
        return conf.getKApi().getDataPlus(method,param);
    }


    @Validate(type = "delete")
    public RetJson deleteByOne(HttpServletRequest request,String alias, Map<String, Map<String, Object>> where) {
        InterfaceConf conf = interfaceConf.get(alias);
        String tablename = conf.getTables();
        try{
            int data = middleCurdMapper.deleteByOne(tablename,where);
            data = curdHandleUtils.after(conf.getKhAfter().get("delete"),null,where,data);
            return RetJson.by("0000","删除成功",data);
        }catch (Exception e){
            log.error("deleteByOne",e);
            return RetJson.by("-0002",e.getMessage()); //后处理不通过
        }
    }

    @Validate(type = "delete")
    public RetJson delete(HttpServletRequest request,String alias, Map<String, Map<String, Object>> where) {
        InterfaceConf conf = interfaceConf.get(alias);
        String tablename = conf.getTables();
        try{
            int data = middleCurdMapper.delete(tablename,where);
            data = curdHandleUtils.after(conf.getKhAfter().get("delete"),null,where,data);
            return RetJson.by("0000","删除成功",data);
        }catch (Exception e){
            log.error("delete",e);
            return RetJson.by("-0002",e.getMessage()); //后处理不通过
        }
    }

    @Validate(type = "update")
    public RetJson update(HttpServletRequest request,String alias, Map<String, Object> set_data, Map<String, Map<String, Object>> where) {
        InterfaceConf conf = interfaceConf.get(alias);
        //再校验Set 参数
     /*   RetJson vs = validataSet(conf,"修改",set_data);
        if(vs!= null) return vs;*/
        try{
            String tablename = conf.getTables();

            int data = middleCurdMapper.update(tablename,set_data,where);
            data = curdHandleUtils.after(conf.getKhAfter().get("update"),set_data,where,data);
            return RetJson.by("0000","修改成功",data);
        }catch (Exception e){
            log.error("update",e);
            return RetJson.by("-0002",e.getMessage()); //后处理不通过
        }
    }

    @Validate(type = "update")
    public RetJson updatePlus(HttpServletRequest request,String alias, Map<String, Object> set_data, Map<String, Map<String, Object>> where) {
        InterfaceConf conf = interfaceConf.get(alias);

        //再校验Set 参数
     /*   RetJson vs = validataSet(conf,"修改",set_data);
        if(vs!= null) return vs;*/

        String tablename = conf.getTables();
        try{
            int data = middleCurdMapper.updatePlus(tablename,set_data,where);
            data = curdHandleUtils.after(conf.getKhAfter().get("update"),set_data,where,data);
            return RetJson.by("0000","修改成功",data);
        }catch (Exception e){
            log.error("updatePlus",e);
            return RetJson.by("-0002",e.getMessage()); //后处理不通过
        }
    }

    @Validate(type = "insert")
    public RetJson insert(HttpServletRequest request,String alias, Map<String, Object> set_data) {
        InterfaceConf conf = interfaceConf.get(alias);
        try{
            String tablename = conf.getTables();
            int data = middleCurdMapper.insert(tablename,set_data);
            data = curdHandleUtils.after(conf.getKhAfter().get("insert"),set_data,null,data);
            return RetJson.by("0000","添加成功",data);
        }catch (Exception e){
            log.error("insert",e);
            return RetJson.by("-0002",e.getMessage()); //后处理不通过
        }
    }

    public RetJson upload(String path,MultipartFile[] file) throws IOException {
        String  fileStoresRootPath = kuaiConfig.getFileStoresRootPath();
        if(fileStoresRootPath!=null){
            FileStoreConf fs = fsList.get(path);
            if(fs!=null){
                String suffix = fs.getSuffix();
                Long size = fs.getSize();
                for(int i=0;i<file.length;i++){
                    MultipartFile f =file[i];
                    String name = f.getOriginalFilename();
                    if(!name.matches(suffix)){ return RetJson.by("0002","第"+(i+1)+"个文件名称不合规！",null); }
                    if(f.getSize()>size){ return RetJson.by("0003","第"+(i+1)+"个文件超过规定大小！",null); }
                }

                List<KuaiFileStore> kfss = new ArrayList<>();
                List<Map<String,Object>> kfssmap = new ArrayList<>();

                String newpath = fileStoresRootPath+"/"+path;
                for(int i=0;i<file.length;i++){
                    MultipartFile f =file[i];
                    KuaiFileStore kfs = fileStroreUtil.uploadFile(newpath,f.getInputStream());
                    String name = f.getOriginalFilename();
                    kfs.setName(name);
                    kfss.add(kfs);
                    kfssmap.add(BeanUtil.beanToMap(kfs,false,false));
                }
                middleCurdMapper.batchInsert("KUAI_FILE_STORE",kfssmap);
                return RetJson.by("0000","上传成功",kfss);
            }else{
                return RetJson.by("0001","该路径不可上传",null);
            }
        }else{
            return RetJson.by("0001","该功能未启用",null);
        }
    }

    //上传文件记录上传信息；
    public RetJson upload(String path,String filename,InputStream stream) throws IOException {
        String  fileStoresRootPath = kuaiConfig.getFileStoresRootPath();
        if(fileStoresRootPath!=null){
            FileStoreConf fs = fsList.get(path);
            if(fs!=null){
                String suffix = fs.getSuffix();
                Long size = fs.getSize();
                String name = filename;

                if(!name.matches(suffix)){ return RetJson.by("0002","文件名称不合规！",null); }
                if(stream.available()>size){ return RetJson.by("0003","文件超过规定大小！",null); }


                List<KuaiFileStore> kfss = new ArrayList<>();
                List<Map<String,Object>> kfssmap = new ArrayList<>();

                String newpath = fileStoresRootPath+"/"+path;
                KuaiFileStore kfs = fileStroreUtil.uploadFile(newpath,stream);

                kfs.setName(name);
                kfss.add(kfs);
                kfssmap.add(BeanUtil.beanToMap(kfs,false,false));

                middleCurdMapper.batchInsert("KUAI_FILE_STORE",kfssmap);
                return RetJson.by("0000","上传成功",kfss);
            }else{
                return RetJson.by("0001","该路径不可上传",null);
            }
        }else{
            return RetJson.by("0001","该功能未启用",null);
        }
    }

    /**
     * 文件下载。执行完会关闭流
     * @param out
     * @param uuid
     * @param filename
     * @return
     * @throws IOException
     */
    public RetJson download(OutputStream out, String uuid,String filename) throws IOException {
        Map<String,Object> wheres = new LinkedHashMap<>();
        wheres.put("ID",uuid);
        Map<String,Object> obj = middleCurdMapper.getByOne("KUAI_FILE_STORE","*", KuaiUtil.toWhere(wheres),null);
        if(obj  == null){
            return RetJson.by("0001","文件不存在");
        }

        KuaiFileStore kfs = BeanUtil.toBeanIgnoreCase(obj,KuaiFileStore.class,false);
        if(StringUtils.isEmpty(filename)){
            filename = kfs.getName();
        }else{
            String nnn = kfs.getName();
            filename+=nnn.substring(nnn.indexOf("."));
        }


        fileStroreUtil.downloadFiles(kfs.getPath(),kfs.getId(),out,5);
        return RetJson.by("0000","下载成功",filename);
    }

    /**
     * 文件下载。执行完会关闭流
     * @param response
     * @param uuid
     * @param filename
     * @return
     * @throws IOException
     */
    public RetJson download(HttpServletResponse response, String uuid,String filename) throws IOException {
        Map<String,Object> wheres = new LinkedHashMap<>();
        wheres.put("ID",uuid);
        Map<String,Object> obj = middleCurdMapper.getByOne("KUAI_FILE_STORE","*", KuaiUtil.toWhere(wheres),null);
        if(obj  == null){
            return RetJson.by("0001","文件不存在");
        }

        KuaiFileStore kfs = BeanUtil.toBeanIgnoreCase(obj,KuaiFileStore.class,false);
        if(StringUtils.isEmpty(filename)){
            filename = kfs.getName();
        }else{
            String nnn = kfs.getName();
            filename+=nnn.substring(nnn.indexOf("."));
        }
        if (String.valueOf(obj.get("name")).contains("mp4")) {
            response.setContentType("video/mp4");
            response.setContentLength(Integer.valueOf(obj.get("filesize").toString()));
            response.setHeader("Content-Range", String.valueOf(obj.get("filesize")));
            response.setHeader("Accept-Ranges", "bytes");
        }
        response.setHeader("Content-Disposition", "attachment; filename="+new String(filename.getBytes("UTF-8"), "iso-8859-1"));
        fileStroreUtil.downloadFiles(kfs.getPath(),kfs.getId(),response.getOutputStream(),5);
        return null;
    }

    /**
     * 复制一个文件创建一条新记录，不存在则复制失败
     * @param uuid
     * @return
     * @throws IOException
     */
    public RetJson copyFile(String uuid,String path,String filename) throws IOException {

        Map<String,Object> wheres = new LinkedHashMap<>();
        wheres.put("ID",uuid);
        Map<String,Object> obj = middleCurdMapper.getByOne("KUAI_FILE_STORE","*", KuaiUtil.toWhere(wheres),null);
        if(obj  == null){
            return RetJson.by("0001","文件不存在");
        }

        KuaiFileStore kfs = BeanUtil.toBeanIgnoreCase(obj,KuaiFileStore.class,false);
        if(StrUtil.isBlank(filename)){
            filename = kfs.getName();
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        fileStroreUtil.downloadFiles(kfs.getPath(),kfs.getId(),outputStream,5);

        return  upload(path,filename,new ByteArrayInputStream(outputStream.toByteArray()));
    }
    /**
     * 文件删除。执行完会关闭流
     * @param uuid
     * @return
     * @throws IOException
     */
    public RetJson deleteFile(String uuid) throws IOException {
        Map<String,Object> wheres = new LinkedHashMap<>();
        wheres.put("ID",uuid);
        Map<String,Object> obj = middleCurdMapper.getByOne("KUAI_FILE_STORE","*", KuaiUtil.toWhere(wheres),null);
        if(obj  == null){
            return RetJson.by("0001","文件不存在");
        }else{
            Map<String,Object> save_data = new LinkedHashMap<>();
            save_data.put("isdelete",1);
            middleCurdMapper.update("KUAI_FILE_STORE",save_data,KuaiUtil.toWhere(wheres));
        }

        KuaiFileStore kfs = BeanUtil.toBeanIgnoreCase(obj,KuaiFileStore.class,false);

        boolean isflag = fileStroreUtil.removeFile(kfs.getPath(),kfs.getId());
        if(isflag){
            return RetJson.by("0000","操作成功");
        }else {
            return RetJson.by("0000","操作失败");
        }

    }



}
