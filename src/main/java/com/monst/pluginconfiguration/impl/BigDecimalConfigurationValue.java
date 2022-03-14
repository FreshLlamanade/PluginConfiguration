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

    /**
     * Attempts to perform a narrowing conversion to a Java Double to store in the file. If this conversion cannot be made,
     * e.g. because this BigDecimal value cannot fit inside a Double, the string representation is returned.
     * @param bd the value
     * @return a Double or String representing this value
     */
    @Override
    protected Object convertToFileData(BigDecimal bd) {
        Double d = bd.doubleValue();
        if (d.isInfinite() || d.isNaN())
            return bd.toString();
        return d;
    }

}
