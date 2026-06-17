package cn.lxycx.kuaicore.kapi.bean;

import cn.lxycx.kuaicore.handles.CurdHandle;
import lombok.Data;

@Data
public class SetHandle {
    CurdHandle handle;
    String param;
    public SetHandle(CurdHandle handle, String param) {
        this.handle = handle;
        this.param = param;
    }

}
