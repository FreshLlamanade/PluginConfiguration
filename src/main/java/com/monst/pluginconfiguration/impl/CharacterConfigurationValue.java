package com.monst.pluginconfiguration.impl;

import com.monst.pluginconfiguration.ConfigurationValue;
import com.monst.pluginconfiguration.exception.ArgumentParseException;
import com.monst.pluginconfiguration.exception.UnreadableValueException;
import com.monst.pluginconfiguration.exception.ValueOutOfBoundsException;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A configuration value of the type {@link Character}.
 */
public class CharacterConfigurationValue extends ConfigurationValue<Character> {

    public CharacterConfigurationValue(JavaPlugin plugin, String path, Character defaultValue) {
        super(plugin, path, defaultValue);
    }

    @Override
    protected Character parse(String input) throws ArgumentParseException {
        if (input.length() != 1)
            throw createArgumentParseException(input);
        return input.charAt(0);
    }

    protected ArgumentParseException createArgumentParseException(String input) {
        return new ArgumentParseException();
    }

    @Override
    protected Character convert(Object o) throws ValueOutOfBoundsException, UnreadableValueException {
        String s = o.toString();
        if (s.length() < 1)
            throw new UnreadableValueException();
        if (s.length() > 1)
            throw new ValueOutOfBoundsException(s.charAt(0));
        return s.charAt(0);
    }

}
