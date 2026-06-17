package cn.lxycx.kuaicore.bean;

import lombok.Data;

@Data
public class TableColumns {
    String tableName;
    String columnName;
    String dataType;
    Long dataLength;
}
