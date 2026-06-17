package cn.lxycx.kuaicore.util;


import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.lxycx.kuaicore.bean.KuaiFileStore;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


public class FileStroreUtil {


    /**
     * @param content  文件输入流
     */
    public KuaiFileStore uploadFile(String rootpath,InputStream content) throws FileNotFoundException {

        String fileguid = UUID.randomUUID().toString(true);
        Map<String,String> retmap = new LinkedHashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/hh");
        String path = sdf.format(new Date());
        if (!StrUtil.isEmpty(rootpath)) {
            path = rootpath + "/" + path+"/";
        }

        byte[] buff = new byte[1024000];
        Long lensum = 0L;
        int len = 0;
        try {
            File f = new File(path);
            if(!f.exists()){  f.mkdirs(); }
            OutputStream fileout = new FileOutputStream(path+fileguid);
            while ((len = content.read(buff))>0){
                lensum+=len;
                fileout.write(buff,0,len);
                fileout.flush();
            }
            fileout.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        KuaiFileStore kfs = new KuaiFileStore();
        kfs.setPath(path);
        kfs.setId(fileguid);
        kfs.setCreatetime(new Date());
        kfs.setFilesize(lensum);

        return kfs;
    }

    public InputStream downloadFiles(String path, String fileguid) {
        try{
            InputStream fileinput = new FileInputStream(path+fileguid);
            return fileinput;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public  void downloadFiles (String path,String fileguid, OutputStream stream,int bufferSize) {
        byte[] buff = new byte[bufferSize * 1024];
        int n = 0;
        try {
            InputStream is = downloadFiles(path,fileguid);
            while ((n = is.read(buff)) != -1) {
                stream.write(buff, 0, n);
                stream.flush();
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public  boolean removeFile(String path, String fileguid) {
        File f = new File(path+fileguid);
        return f.delete();
    }
}
