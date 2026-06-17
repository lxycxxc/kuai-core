package cn.lxycx.kuaicore.util;

import java.util.regex.Pattern;

/**
 * 常用字符串正则校验
 * @author 邢超
 * 时间：2017-5-31
 */
public  class  RegexPro {

	/**不校验*/
	public  static  String NONE = "";

	/**校验邮箱*/
	public final static  String EMAIL = new String("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");

	/**手机号*/
	public final static  String PHONE = new String("^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$");

	/**固定电话*/
	public final static  String TEL = new String("^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$");

	/**身份证*/
	public final static  String IDCARD = new String("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");

	/**URL*/
	public final static  String URL = new String("http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?");

	/**时间：YYYY-MM-DD HH:mm:ss*/
	public final static  String DATETIME = new String("^(19|20)[0-9][0-9]-((0[1-9])|(1[0-2]))-(([0-2][1-9])|([1-3][0-1])) (([0-2][0-3])|([0-1][0-9])):[0-5][0-9]:[0-5][0-9]$");

	/**日期：YYYY-MM-DD*/
	public final static  String DATE = new String("^(19|20)[0-9][0-9]-((0[1-9])|(1[0-2]))-(([0-2][1-9])|([1-3][0-1]))$");

	/**邮政编码*/
	public final static  String YZCODE = new String ("^\\d{6}$");

	/**字段长度限制*/
	public final static  String VARCHAR_8 = new String ("^\\d{8}$");
	public final static  String VARCHAR_32 = new String ("^\\d{32}$");
	public final static  String VARCHAR_50 = new String ("^\\d{50}$");
	public final static  String VARCHAR_255 = new String ("^\\d{255}$");
	public final static  String VARCHAR_2000 = new String ("^\\d{2000}$");
	public final static  String VARCHAR_4000 = new String ("^\\d{4000}$");

	private String value;
	private String remark;
	private RegexPro(String value){
		this.value = value;
	}
	private RegexPro(String value,String remark){
		this.value = value;
		this.remark = remark;
	}

	public static RegexPro by(String value){
		return new RegexPro(value);
	}
	public static RegexPro by(String value,String remark){
		return new RegexPro(value,remark);
	}


	public String toString(){
		return value;
	}


	/**
	 * 根据指定的正则表达式校验字符串
	 * @param regex 正则表达式
	 * @param acc 需要校验的字符串
	 * @return 校验失败返回null 成功返回 acc
	 * @author 邢超
	 * 创建时间：2017-5-31
	 *
	 */
	public static String pattern(RegexPro regex,String acc){
		if(acc!=null&&regex!=null&&Pattern.matches(regex.toString(), acc)){
			return acc;
		}else{
			return null;
		}
	}

	/**
	 *
	 * 根据指定的正则表达式校验字符串
	 * @param regex 正则表达式
	 * @param acc 需要校验的字符串
	 * @return 校验失败返回null 成功返回 acc
	 * @author 邢超
	 * 创建时间：2017-5-31
	 *
	 */
	public static String pattern(String regex,String acc){
		if(acc!=null&&regex!=null&&Pattern.matches(regex, acc)){
			return acc;
		}else{
			return null;
		}
	}


	public static void main(String[] args) {
		String s = null;
		// (RegexPro.pattern(RegexPro.DATE, "2017-13-10"));

	}

}
