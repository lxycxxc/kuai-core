package cn.lxycx.kuaicore.bean;

import lombok.Data;

import java.util.Date;

@Data
public class KuaiFileStore {
    private String id;
    private String path;
    private String name;
    private int isdelete;
    private Long filesize;
    private Date createtime;
    private Date deltime;
}
