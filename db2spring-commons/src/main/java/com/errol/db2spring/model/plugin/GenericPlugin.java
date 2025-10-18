package com.errol.db2spring.model.plugin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GenericPlugin implements Plugin {
    private String pluginName;
    private boolean enabled = true;
    private String value;

    public GenericPlugin(String pluginName) {
        this.pluginName = pluginName;
    }

    public GenericPlugin(String pluginName, boolean enabled) {
        this.pluginName = pluginName;
        this.enabled = enabled;
    }

    public GenericPlugin(String pluginName, String value) {
        this.value = value;
        this.pluginName = pluginName;
    }
}
