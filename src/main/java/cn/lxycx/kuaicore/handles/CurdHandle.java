package cn.lxycx.kuaicore.handles;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public abstract class CurdHandle {

    /**
     * 数据前处理
     * @param set_data 需要修改或插入的数据
     * @param wheres 查询条件
     * @param param 页面针对不通接口设置的附加参数
     * @return 如果返回true则通过，进入下一步，否则不通过，接口返回结果
     * @throws CurdHandleException
     */
    public  boolean  front(Map<String, Object> set_data,Map<String,Map<String,Object>> wheres,String param) throws CurdHandleException{
        return true;
    }


}
