package cn.lxycx.kuaicore.conf;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data@Accessors(chain=true)
public class Ret1<T> implements Ret {
    private String code;
    private String message;
    private T data;
    private Integer count;
    private List<T> list;

    @Override
    public Ret by(RetJson retJson) {
        return new Ret1().setCode(retJson.getCode())
                .setMessage(retJson.getMessage())
                .setData(retJson.getData())
                .setCount(retJson.getCount())
                .setList(retJson.getList());
    }
}
