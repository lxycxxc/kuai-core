package cn.lxycx.kuaicore.kapi.bean;

import cn.lxycx.kuaicore.handles.CurdHandleAfter;
import lombok.Data;

@Data
public class SetHandleAfter {
    CurdHandleAfter handle;
    String param;

    public SetHandleAfter(CurdHandleAfter handle, String param) {
        this.handle = handle;
        this.param = param;
    }
}
