package com.monst.pluginconfiguration.impl;

import com.monst.pluginconfiguration.ConfigurationValue;
import com.monst.pluginconfiguration.exception.ArgumentParseException;
import com.monst.pluginconfiguration.exception.UnreadableValueException;
import com.monst.pluginconfiguration.exception.ValueOutOfBoundsException;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A configuration value of the type {@link Integer}.
 */
public class IntegerConfigurationValue extends ConfigurationValue<Integer> {

    public IntegerConfigurationValue(JavaPlugin plugin, String path, Integer defaultValue) {
        super(plugin, path, defaultValue);
    }

    @Override
    protected Integer parse(String input) throws ArgumentParseException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw createArgumentParseException(input);
        }
    }

    protected ArgumentParseException createArgumentParseException(String input) {
        return new ArgumentParseException();
    }

    @Override
    protected Integer convert(Object o)
            throws ValueOutOfBoundsException, UnreadableValueException {
        if (o instanceof Integer)
            return (Integer) o;
        if (o instanceof Double)
            throw new ValueOutOfBoundsException(((Double) o).intValue());
        try {
            throw new ValueOutOfBoundsException(Integer.parseInt(o.toString()));
        } catch (NumberFormatException e) {
            throw new UnreadableValueException();
        }
    }

}
