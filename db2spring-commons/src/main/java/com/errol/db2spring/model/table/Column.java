package com.errol.db2spring.model.table;

import com.errol.db2spring.model.SmartString;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Column {
    private String columnName;
    private String sqlType;
    private String javaType;

    private Integer size;
    private boolean nullable;
    private boolean primaryKey;
    private boolean date;

    public SmartString getSmartColumnName() {
        return new SmartString(columnName).toCamelCase();
    }
}
