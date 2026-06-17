package cn.lxycx.kuaicore.kapi;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.lxycx.kuaicore.bean.InterfaceConf;
import cn.lxycx.kuaicore.kapi.bean.KhAfter;
import cn.lxycx.kuaicore.kapi.bean.KhFront;
import cn.lxycx.kuaicore.kapi.supper.KField;
import cn.lxycx.kuaicore.kapi.supper.KSetValue;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class RegisterApi<T> {

    private String pkey;
    private String tables;//表名
    private String tabletype;//类型
    private String queryTable; //查询时的表格
    private String classify;//分类
    private String alias;//别名
    private String roles;//权限



    private String iscx = "0";
    private String isxg = "0";
    private String issc = "0";

    /**
     * 绑定实体类
     */
    @Getter@Setter
    private Class<T> pojo;


    @Getter
    private KhAfter after = new KhAfter();
    @Getter
    private KhFront front = new KhFront();


    /**
     * 设置允许无条件操作查询,默认不允许
     * @return
     */
    public RegisterApi yesCx(){ this.iscx = "1"; return this; }
    /**
     * 设置允许无条件操作修改,默认不允许
     * @return
     */
    public RegisterApi yesXg(){ this.isxg = "1"; return this; }
    /**
     * 设置允许无条件操作删除,默认不允许
     * @return
     */
    public RegisterApi yesSc(){ this.issc = "1"; return this; }


    /**
     * 设置操作权限 查询,添加,修改,删除
     * @return
     */
    public RegisterApi setRoles(String... name){
        this.roles = String.join(",", name);
        return this;
    }


    private RegisterApi(String alias, String tabletype, String tables,String queryTable,String roles){
        this.tables = tables;
        this.tabletype = tabletype;
        this.alias = alias;
        this.roles = roles;
        this.queryTable = queryTable;
    }

    public static RegisterApi by(String alias,String tabletype,String tables,String queryTable,String roles){
        return new RegisterApi(alias,tabletype,tables,queryTable,roles);
    }


    /**
     * 获取接口配置
     * @return
     */
    public InterfaceConf getInterfaceConf() throws Exception {
        if(pojo == null) {  throw new Exception("KApi接口未绑定POJO:"+this.alias); }

        //KField kApi = (KField) AnnotatedElementUtils.findAllMergedAnnotations(null,KField.class);
        //String alias = kApi.alias();
        List<String> setField = new ArrayList<>();
        List<String> setWheres = new ArrayList<>();
        List<String> setEdit = new ArrayList<>();

        //设置字段；
        JSONObject verifyJson = new JSONObject();
        JSONObject setvalue = new JSONObject();

        JSONObject descs = new JSONObject(true);

        Annotation[] as = pojo.getDeclaredAnnotations();
        Field[] fields = pojo.getDeclaredFields();
        for(Field field:fields){

            KField kField = field.getAnnotation(KField.class);
            if(kField!=null){
                String name = kField.col();
                String desc = kField.desc();

                descs.put(name, desc);

                if( kField.query()){
                    if(kField.iskey()){
                        setField.add("`"+name+"`");
                    }else{
                        setField.add(name);
                    }
                }

                if( kField.where()) setWheres.add(name);
                if( kField.edit()) setEdit.add(name);
                String verify = kField.verify();
                if(StrUtil.isNotBlank(verify)){
                    verifyJson.put(name, verify);
                }

                KSetValue kSetValue = kField.value();
                if (kSetValue!=null&&StrUtil.isNotBlank(kSetValue.key())){
                    setvalue.put(name,kSetValue);
                }
            }
        }


        InterfaceConf conf = new InterfaceConf();
        //设置基本信息
        conf.setPojo(pojo);
        conf.setTables(tables);
        conf.setTabletype(tabletype);
        conf.setClassify(classify);
        conf.setAlias(alias);
        conf.setRoles(roles);
        conf.setDescs(descs);
        conf.setQueryTable(queryTable);

        //设置查询字段
        conf.setField(ArrayUtil.join(setField.toArray(),","));
        //设置查询条件
        conf.setWheres(ArrayUtil.join(setWheres.toArray(),","));
        if(setWheres.isEmpty()){
            conf.setWheres(pkey);
        }

        //设置无条件操作
        conf.setIscx(iscx);
        conf.setIsxg(isxg);
        conf.setIssc(issc);

        //设置前后拦截
        conf.setKhAfter(after);
        conf.setKhFront(front);


        //设置允许编辑的字段
        conf.setKeys(ArrayUtil.join(setEdit.toArray(),","));
        //设置校验
        conf.setVerify(verifyJson.toJSONString());
        conf.setVerifyJson(verifyJson);
        //设置取值
        conf.setSetvalue(setvalue.toJSONString());
        conf.setSetvalueJson(setvalue);


        return conf;
    }

}
