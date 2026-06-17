package cn.lxycx.kuaicore.kapi.bean;

import cn.lxycx.kuaicore.handles.CurdHandle;
import cn.lxycx.kuaicore.kapi.handle.SimpleInsertCurdHandle;
import cn.lxycx.kuaicore.kapi.handle.SimpleSelectOrDelCurdHandle;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class KhFront {
    List<SetHandle> select = new ArrayList<>();
    List<SetHandle> insert = new ArrayList<>();
    List<SetHandle> update = new ArrayList<>();
    List<SetHandle> delete = new ArrayList<>();

    public List<SetHandle> get(String type){
        if("select".equalsIgnoreCase(type)){
            return select;
        }else if("insert".equalsIgnoreCase(type)){
            return insert;
        }else if("update".equalsIgnoreCase(type)){
            return update;
        }else if("delete".equalsIgnoreCase(type)){
            return delete;
        }else{
            return null;
        }
    }

    public KhFront addSelect(SimpleSelectOrDelCurdHandle front) {
        this.select.add(new SetHandle(front,""));
        return this;
    }

    public KhFront addInsert(SimpleInsertCurdHandle insert) {
        this.insert.add(new SetHandle(insert,""));
        return this;
    }
    public KhFront addUpdate(CurdHandle update) {
        this.update.add(new SetHandle(update,""));
        return this;
    }
    public KhFront addDelete(SimpleSelectOrDelCurdHandle delete) {
        this.delete.add(new SetHandle(delete,""));
        return this;
    }
}
