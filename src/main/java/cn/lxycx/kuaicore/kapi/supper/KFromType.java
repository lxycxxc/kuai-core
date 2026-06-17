package cn.lxycx.kuaicore.kapi.supper;

public enum KFromType {

    text("text"),select("select"),sw("switch");
    private String value;

    private KFromType(String value){
        this.value=value;
    }
}
