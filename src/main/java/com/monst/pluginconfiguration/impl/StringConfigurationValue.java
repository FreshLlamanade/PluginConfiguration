package com.monst.pluginconfiguration.impl;

import com.monst.pluginconfiguration.ConfigurationValue;
import org.bukkit.plugin.Plugin;

/**
 * A configuration value of the type {@link String}.
 */
public class StringConfigurationValue extends ConfigurationValue<String> {

    public StringConfigurationValue(Plugin plugin, String path, String defaultValue) {
        super(plugin, path, defaultValue);
    }

    @Override
    protected String parse(String input) {
        return input;
    }

}
