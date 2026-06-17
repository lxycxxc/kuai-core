package cn.lxycx.kuaicore.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Classify implements Serializable {
    private String id;
    private String name;
    private List<Classify> children;
}
