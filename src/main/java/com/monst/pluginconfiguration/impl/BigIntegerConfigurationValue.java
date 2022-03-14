package com.monst.pluginconfiguration.impl;

import com.monst.pluginconfiguration.ConfigurationValue;
import com.monst.pluginconfiguration.exception.ArgumentParseException;
import org.bukkit.plugin.Plugin;

import java.math.BigInteger;

/**
 * A configuration value of the type {@link BigInteger}.
 */
public class BigIntegerConfigurationValue extends ConfigurationValue<BigInteger> {

    public BigIntegerConfigurationValue(Plugin plugin, String path, BigInteger defaultValue) {
        super(plugin, path, defaultValue);
    }

    @Override
    protected BigInteger parse(String input) throws ArgumentParseException {
        try {
            return new BigInteger(input);
        } catch (NumberFormatException e) {
            throw createArgumentParseException(input);
        }
    }

    protected ArgumentParseException createArgumentParseException(String input) {
        return new ArgumentParseException();
    }

    @Override
    protected Object convertToFileData(BigInteger bd) {
        return bd.longValue();
    }

}
