package cn.lxycx.kuaicore.kapi.bean;

import cn.lxycx.kuaicore.kapi.handle.SimpleDeleteCurdHandleAfter;
import cn.lxycx.kuaicore.kapi.handle.SimpleInsertCurdHandleAfter;
import cn.lxycx.kuaicore.kapi.handle.SimpleSelectCurdHandleAfter;
import cn.lxycx.kuaicore.kapi.handle.SimpleUpdateCurdHandleAfter;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class KhAfter {
    List<SetHandleAfter> select = new ArrayList<>();
    List<SetHandleAfter> insert = new ArrayList<>();
    List<SetHandleAfter> update = new ArrayList<>();
    List<SetHandleAfter> delete = new ArrayList<>();

    public List<SetHandleAfter> get(String type){
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

    public KhAfter addSelect(SimpleSelectCurdHandleAfter select) {
        this.select.add(new SetHandleAfter(select,""));
        return this;
    }

    public KhAfter addInsert(SimpleInsertCurdHandleAfter insert) {
        this.insert.add(new SetHandleAfter(insert,""));
        return this;
    }
    public KhAfter addUpdate(SimpleUpdateCurdHandleAfter update) {
        this.update.add(new SetHandleAfter(update,""));
        return this;
    }
    public KhAfter addDelete(SimpleDeleteCurdHandleAfter delete) {
        this.delete.add(new SetHandleAfter(delete,""));
        return this;
    }
}
