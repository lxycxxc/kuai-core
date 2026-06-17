package cn.lxycx.kuaicore.conf;

import cn.lxycx.kuaicore.bean.KuaiConfig;
import cn.lxycx.kuaicore.bean.Module;
import cn.lxycx.kuaicore.controller.InterfaceController;
import cn.lxycx.kuaicore.mapper.InterfaceMapper;
import cn.lxycx.kuaicore.mapper.ModuleMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Component
public class InitTableConf implements CommandLineRunner {
    @Lazy
    @Autowired
    InterfaceMapper interfaceMapper;

    @Lazy
    @Autowired
    ModuleMapper moduleMapper;

    @Autowired
    InterfaceController interfaceController;

    @Autowired
    KuaiConfig kuaiConfig;

    @Lazy
    @Autowired
    SqlSessionFactory sqlSessionFactoryXML;

    @Autowired
    ApplicationContext applicationContext;

    @Value("${server.port}")
    String port="80";
    @Value("${server.servlet.context-path}")
    String path="/";

    //是否启用数据模式
    @Value("${kuai.api.isDataMode: false}")
    boolean isDataMode;

    @Override
    public void run(String... args) throws Exception {

        String  fileStoresRootPath = kuaiConfig.getFileStoresRootPath();
        if(fileStoresRootPath!=null){
           initFileStores();
        }

        //initTables();

        if(isDataMode){//是否启用数据模式
            List<Map<String,Object>> col =  interfaceMapper.findColumns("KUAI_INTERFACE_CONF");
            if(col.isEmpty()){//如果当前表不存在，则创建;
                log.info("当前未初始化基础模块正在初始化...");
                int i = interfaceMapper.createConfTable();
                log.info("基础模块初始化结果："+i);
            }else{
                log.info("基础模块初始化已完成");
            }

            interfaceController.sync();//同步数据到缓存
            interfaceController.refreshCurdHandle();//刷新Handle列表
            log.info("点击访问接口管理页面："+"http://127.0.0.1:"+port+path+"interface/admin/manage.html");
        }


        //初始化KApi接口
        interfaceController.initKApi();
        if(!path.endsWith("/")){  path+="/";  }

    }

    //检测文件管理模块是否初始化完成
    public void initFileStores(){
        List<Map<String,Object>> col =  interfaceMapper.findColumns("KUAI_FILE_STORE");
        if(col.isEmpty()){//如果当前表不存在，则创建;
            log.info("当前未初始化文件上传模块正在初始化...");
            int i = moduleMapper.initFileStore();
            log.info("文件上传模块初始化结果："+i);
        }else{
            log.info("文件上传模块初始化已完成");
        }
    }




    //初始化表
    public void initTables(){
        List<Module> modules = kuaiConfig.getModules();
        if(modules!=null){
            SqlSession sqlSessionXML = sqlSessionFactoryXML.openSession();
            for(Module m:modules){
                boolean init = m.isInit();
                if(init){
                    String name = m.getName();
                    log.info("正在初始化（"+name+"）模块...");
                    List<Module> ts = m.getTables();
                   // String[] ts = table.split(",");
                    for(Module m2:ts){
                        boolean init2 = m2.isInit();
                        if(init2){
                            String t = m2.getName();
                            List<Map<String,Object>> col =  interfaceMapper.findColumns(t);
                            if(col.isEmpty()){//如果当前表不存在，则创建;
                                log.info("正在创建表："+t);
                                try {
                                    int i = sqlSessionXML.update("cn.lxycx.kuaicore.mapper.InitTableMapper." + t);
                                    log.info("创建结果："+i);
                                    //执行初始化语句；
                                    String add = m2.getAdd();
                                    if(add!=null&&!"".equals(add)){
                                        log.info("正在执行初始化SQL："+add);
                                        String[] adds = add.split(",");
                                        for(String a:adds){
                                            int i2 = sqlSessionXML.update("cn.lxycx.kuaicore.mapper.InitTableMapper." + a);
                                            log.info("初始化结果："+i2);
                                        }
                                    }
                                }catch (Exception e){
                                    log.info("执行异常：");
                                    e.printStackTrace();
                                }
                            }else{
                                log.info(t +" 表已存在!");
                            }
                        }

                    }
                    log.info(name +" 模块初始化已完成");
                }
            }
        }
    }


    /**
     * 支持Oracle 和 MySQL切换
     * @return 数据源
     */
    @Bean
    public DatabaseIdProvider getDatabaseIdProvider() {
        DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();
        properties.setProperty("Oracle", "oracle");
        properties.setProperty("MySQL", "mysql");
        databaseIdProvider.setProperties(properties);
        return databaseIdProvider;
    }
}
