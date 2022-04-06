package com.monst.pluginconfiguration.impl;

import com.monst.pluginconfiguration.ConfigurationValue;
import com.monst.pluginconfiguration.exception.ArgumentParseException;
import com.monst.pluginconfiguration.exception.UnreadableValueException;
import com.monst.pluginconfiguration.exception.ValueOutOfBoundsException;
import org.bukkit.plugin.Plugin;

/**
 * A configuration value of the type {@link Boolean}.
 * This class avoids the use of {@link Boolean#parseBoolean(String)} in favor of a more strict parser.
 */
public class BooleanConfigurationValue extends ConfigurationValue<Boolean> {

    public BooleanConfigurationValue(Plugin plugin, String path, Boolean defaultValue) {
        super(plugin, path, defaultValue);
    }

    @Override
    protected Boolean parse(String input) throws ArgumentParseException {
        if (input.equalsIgnoreCase("true"))
            return true;
        if (input.equalsIgnoreCase("false"))
            return false;
        throw createArgumentParseException(input);
    }

    /**
     * Customize the specific ArgumentParseException that is thrown when an error is encountered while parsing this Boolean.
     * This method is called by the default implementation of {@link #parse(String) parse}.
     * @param input the input that could not be parsed
     * @return a custom {@link ArgumentParseException}
     */
    protected ArgumentParseException createArgumentParseException(String input) {
        return new ArgumentParseException();
    }

    @Override
    protected Boolean convert(Object o) throws ValueOutOfBoundsException, UnreadableValueException {
        if (o instanceof Boolean)
            return (Boolean) o;
        try {
            throw new ValueOutOfBoundsException(parse(o.toString()));
        } catch (ArgumentParseException e) {
            throw new UnreadableValueException();
        }
    }

}
