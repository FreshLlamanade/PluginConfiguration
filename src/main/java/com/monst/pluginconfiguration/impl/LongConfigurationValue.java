package com.monst.pluginconfiguration.impl;

import com.monst.pluginconfiguration.ConfigurationValue;
import com.monst.pluginconfiguration.exception.ArgumentParseException;
import com.monst.pluginconfiguration.exception.UnreadableValueException;
import com.monst.pluginconfiguration.exception.ValueOutOfBoundsException;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A configuration value of the type {@link Long}.
 */
public class LongConfigurationValue extends ConfigurationValue<Long> {

    public LongConfigurationValue(JavaPlugin plugin, String path, Long defaultValue) {
        super(plugin, path, defaultValue);
    }

    @Override
    protected Long parse(String input) throws ArgumentParseException {
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            throw createArgumentParseException(input);
        }
    }

    protected ArgumentParseException createArgumentParseException(String input) {
        return new ArgumentParseException();
    }

    @Override
    protected Long convert(Object o)
            throws ValueOutOfBoundsException, UnreadableValueException {
        if (o instanceof Integer)
            return ((Integer) o).longValue();
        if (o instanceof Double)
            throw new ValueOutOfBoundsException(((Double) o).longValue());
        try {
            throw new ValueOutOfBoundsException(Long.parseLong(o.toString()));
        } catch (NumberFormatException e) {
            throw new UnreadableValueException();
        }
    }

}
