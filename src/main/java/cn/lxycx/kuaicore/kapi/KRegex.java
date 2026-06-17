package cn.lxycx.kuaicore.kapi;

public interface KRegex {
    /**不校验*/
    public String NONE = "";

    /**校验邮箱*/
    public String EMAIL = "^\\s*\\w+(?:\\.{0,1}[\\w-]+*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+*\\.[a-zA-Z]+\\s*$";

    /**手机号*/
    public String PHONE = "^((13[0-9]|(14[5|7]|(15([0-3]|[5-9]|(18[0,5-9]\\d{8}$";

    /**固定电话*/
    public String TEL = "^(0\\d{2}-\\d{8}(-\\d{1,4}?|(0\\d{3}-\\d{7,8}(-\\d{1,4}?$";

    /**身份证*/
    public String IDCARD = "(\\d{14}[0-9a-zA-Z]|(\\d{17}[0-9a-zA-Z]";

    /**URL*/
    public String URL = "http(s?://([\\w-]+\\.+[\\w-]+(/[\\w- ./?%&=]*?";

    /**时间：YYYY-MM-DD HH:mm:ss*/
    public String DATETIME = "^(19|20[0-9][0-9]-((0[1-9]|(1[0-2]-(([0-2][1-9]|([1-3][0-1] (([0-2][0-3]|([0-1][0-9]:[0-5][0-9]:[0-5][0-9]$";

    /**日期：YYYY-MM-DD*/
    public String DATE = "^(19|20[0-9][0-9]-((0[1-9]|(1[0-2]-(([0-2][1-9]|([1-3][0-1]$";

    /**邮政编码*/
    public String YZCODE = "^\\d{6}$";

    /**字段长度限制*/
    public String VARCHAR_8 = "^\\d{8}$";
    public String VARCHAR_32 = "^\\d{32}$";
    public String VARCHAR_50 = "^\\d{50}$";
    public String VARCHAR_255 = "^\\d{255}$";
    public String VARCHAR_2000 = "^\\d{2000}$";
    public String VARCHAR_4000 = "^\\d{4000}$";
}
