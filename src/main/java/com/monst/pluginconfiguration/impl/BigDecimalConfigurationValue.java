package com.monst.pluginconfiguration.impl;

import com.monst.pluginconfiguration.ConfigurationValue;
import com.monst.pluginconfiguration.exception.ArgumentParseException;
import org.bukkit.plugin.Plugin;

import java.math.BigDecimal;

/**
 * A configuration value of the type {@link BigDecimal}.
 */
public class BigDecimalConfigurationValue extends ConfigurationValue<BigDecimal> {

    public BigDecimalConfigurationValue(Plugin plugin, String path, BigDecimal defaultValue) {
        super(plugin, path, defaultValue);
    }

    @Override
    protected BigDecimal parse(String input) throws ArgumentParseException {
        try {
            return new BigDecimal(input);
        } catch (NumberFormatException e) {
            throw createArgumentParseException(input);
        }
    }

    protected ArgumentParseException createArgumentParseException(String input) {
        return new ArgumentParseException();
    }

    @Override
    protected Object convertToFileData(BigDecimal bd) {
        return bd.doubleValue();
    }

}
