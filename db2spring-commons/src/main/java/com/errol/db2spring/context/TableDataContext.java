package com.errol.db2spring.context;

import com.errol.db2spring.model.TypeOverride;
import com.errol.db2spring.model.table.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TableDataContext {
    private String tableName;
    private Table table;
    private List<TypeOverride> typeOverrides;
}
