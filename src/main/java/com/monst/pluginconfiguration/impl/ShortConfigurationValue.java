package com.monst.pluginconfiguration.impl;

import com.monst.pluginconfiguration.ConfigurationValue;
import com.monst.pluginconfiguration.exception.ArgumentParseException;
import com.monst.pluginconfiguration.exception.UnreadableValueException;
import com.monst.pluginconfiguration.exception.ValueOutOfBoundsException;
import org.bukkit.plugin.Plugin;

/**
 * A configuration value of the type {@link Short}.
 */
public class ShortConfigurationValue extends ConfigurationValue<Short> {

    public ShortConfigurationValue(Plugin plugin, String path, Short defaultValue) {
        super(plugin, path, defaultValue);
    }

    @Override
    protected Short parse(String input) throws ArgumentParseException {
        try {
            return Short.parseShort(input);
        } catch (NumberFormatException e) {
            throw createArgumentParseException(input);
        }
    }

    /**
     * Customize the specific ArgumentParseException that is thrown when an error is encountered while parsing this Short.
     * This method is called by the default implementation of {@link #parse(String) parse}.
     * @param input the input that could not be parsed
     * @return a custom {@link ArgumentParseException}
     */
    protected ArgumentParseException createArgumentParseException(String input) {
        return new ArgumentParseException();
    }

    @Override
    protected Short convert(Object o)
            throws ValueOutOfBoundsException, UnreadableValueException {
        if (o instanceof Integer)
            return ((Integer) o).shortValue();
        if (o instanceof Double)
            throw new ValueOutOfBoundsException(((Double) o).shortValue());
        try {
            throw new ValueOutOfBoundsException(Short.parseShort(o.toString()));
        } catch (NumberFormatException e) {
            throw new UnreadableValueException();
        }
    }

}
