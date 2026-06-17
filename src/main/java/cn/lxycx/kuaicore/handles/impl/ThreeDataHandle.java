package cn.lxycx.kuaicore.handles.impl;

import cn.lxycx.kuaicore.handles.CurdHandleAfter;
import cn.lxycx.kuaicore.handles.CurdHandleException;
import cn.lxycx.kuaicore.handles.HandleConf;
import cn.lxycx.kuaicore.util.DataUtil;
import com.alibaba.fastjson.JSONArray;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@HandleConf(name ="返回树形结果(菜单专用)",type = "select",remark = "有四个参数[顶级父级,菜单ID,上级ID,子级List] 例如：0,ID,PID,children")
public class ThreeDataHandle<T> extends CurdHandleAfter<T> {

    @Override
    public  List<T> after(Map<String, Object> set_data,Map<String,Map<String,Object>> wheres, List<T> list,String param) throws CurdHandleException {
        String [] ss = param.split(",");
        if(ss.length==4){
            if(list!=null&&list.size()>0){
                String parentId = ss[0];
                System.out.println("返回树形结构");
                DataUtil d = new DataUtil(ss[1], ss[2], ss[3]);
                JSONArray array = new JSONArray((List<Object>) list);
                T t = list.get(0);
                JSONArray newjsonarray = d.treeMenuList(array,parentId);
                return (List<T>) newjsonarray.toJavaList(t.getClass());
            }
            return list;
        }else{
            throw new  CurdHandleException("缺少必要配置参数：如 0,ID,PID,children");
        }

    }
}
