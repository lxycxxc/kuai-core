package cn.lxycx.kuaicore.conf;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;

import java.util.List;

@Data
public class RetJson<T> {
    private String code;
    private String message;
    private T data;
    private Integer count;
    private List<T> list;
    private Integer page;
    private Integer limt;


    private RetJson(String code,String msg,T data){
        this.code = code;
        this.message = msg;
        this.data = data;
    }
    private RetJson(String code,String msg,int count,List<T> list1){
        this.code = code;
        this.message = msg;
        this.count = count;
        this.list = list1;
    }

    public static RetJson by(String code,String msg){
        return new RetJson( code, msg, null);
    }

    public static RetJson by(String code,String msg,Object data){
        return new RetJson( code, msg, data);
    }

    public  static <T> RetJson by(String code,String msg,int count,List<T> list){
        return new RetJson( code, msg, count,list);
    }

    public String toJson(){
        return JSONObject.toJSONStringWithDateFormat(this,"yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
    }

}
