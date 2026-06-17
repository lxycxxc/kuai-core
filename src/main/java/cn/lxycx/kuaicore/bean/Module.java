package cn.lxycx.kuaicore.bean;

import lombok.Data;

import java.util.List;

@Data
public class Module {
    String name;
    String remark;
    boolean init;
    List<Module> tables;
    String add;
}
