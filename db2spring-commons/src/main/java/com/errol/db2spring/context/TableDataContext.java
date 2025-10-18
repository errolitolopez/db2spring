package com.errol.db2spring.context;

import com.errol.db2spring.model.TypeOverride;
import com.errol.db2spring.model.table.Table;

import java.util.List;

public record TableDataContext(
        String tableName,
        List<Table> tables,
        List<TypeOverride> typeOverrides) {
}
