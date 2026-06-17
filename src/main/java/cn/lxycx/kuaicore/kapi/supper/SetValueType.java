package cn.lxycx.kuaicore.kapi.supper;

public enum SetValueType {

    Param("Param"),Attr("Attr"),Sql("Sql")
    ,Header("Header"),Cookie("Cookie");
    private String value;

    private SetValueType(String value){
        this.value=value;
    }
}
