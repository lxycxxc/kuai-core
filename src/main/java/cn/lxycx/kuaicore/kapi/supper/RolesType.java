package cn.lxycx.kuaicore.kapi.supper;

public enum RolesType {
    select("select","查询"),insert("insert","添加")
    ,update("select","修改"),delete("delete","删除");
    private String key;
    private String value;

    private RolesType(String key,String value){
        this.key=key;
        this.value=value;
    }
}
