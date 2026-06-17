package cn.lxycx.kuaicore.bean;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class KuaiDb {
    private String table;
    private List<String> field;
    private List<String> where;
    private Map<String,String> verify;
}
