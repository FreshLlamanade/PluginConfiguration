package com.monst.pluginconfiguration.impl;

import com.monst.pluginconfiguration.ConfigurationValue;
import com.monst.pluginconfiguration.exception.ArgumentParseException;
import com.monst.pluginconfiguration.exception.UnreadableValueException;
import com.monst.pluginconfiguration.exception.ValueOutOfBoundsException;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A configuration value of the type {@link Float}.
 */
public class FloatConfigurationValue extends ConfigurationValue<Float> {

    public FloatConfigurationValue(JavaPlugin plugin, String path, Float defaultValue) {
        super(plugin, path, defaultValue);
    }

    @Override
    protected Float parse(String input) throws ArgumentParseException {
        try {
            return Float.parseFloat(input);
        } catch (NumberFormatException e) {
            throw createArgumentParseException(input);
        }
    }

    protected ArgumentParseException createArgumentParseException(String input) {
        return new ArgumentParseException();
    }

    @Override
    protected Float convert(Object o) throws ValueOutOfBoundsException, UnreadableValueException {
        if (o instanceof Number)
            return ((Number) o).floatValue();
        try {
            throw new ValueOutOfBoundsException(Float.parseFloat(o.toString()));
        } catch (NumberFormatException e) {
            throw new UnreadableValueException();
        }
    }

}
