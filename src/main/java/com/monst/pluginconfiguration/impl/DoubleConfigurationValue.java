package com.monst.pluginconfiguration.impl;

import com.monst.pluginconfiguration.ConfigurationValue;
import com.monst.pluginconfiguration.exception.ArgumentParseException;
import com.monst.pluginconfiguration.exception.UnreadableValueException;
import com.monst.pluginconfiguration.exception.ValueOutOfBoundsException;
import org.bukkit.plugin.Plugin;

/**
 * A configuration value of the type {@link Double}.
 */
public class DoubleConfigurationValue extends ConfigurationValue<Double> {

    public DoubleConfigurationValue(Plugin plugin, String path, Double defaultValue) {
        super(plugin, path, defaultValue);
    }

    @Override
    protected Double parse(String input) throws ArgumentParseException {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            throw createArgumentParseException(input);
        }
    }

    /**
     * Customize the specific ArgumentParseException that is thrown when an error is encountered while parsing this Double.
     * This method is called by the default implementation of {@link #parse(String) parse}.
     * @param input the input that could not be parsed
     * @return a custom {@link ArgumentParseException}
     */
    protected ArgumentParseException createArgumentParseException(String input) {
        return new ArgumentParseException();
    }

    @Override
    protected Double convert(Object o) throws ValueOutOfBoundsException, UnreadableValueException {
        if (o instanceof Double)
            return (Double) o;
        if (o instanceof Integer)
            return ((Integer) o).doubleValue();
        try {
            throw new ValueOutOfBoundsException(Double.parseDouble(o.toString()));
        } catch (NumberFormatException e) {
            throw new UnreadableValueException();
        }
    }

}
