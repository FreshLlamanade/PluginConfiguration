package com.monst.pluginconfiguration.impl;

import com.monst.pluginconfiguration.ConfigurationValue;
import com.monst.pluginconfiguration.exception.ArgumentParseException;
import com.monst.pluginconfiguration.exception.UnreadableValueException;
import com.monst.pluginconfiguration.exception.ValueOutOfBoundsException;
import org.bukkit.plugin.Plugin;

/**
 * A configuration value of the type {@link Character}.
 */
public class CharacterConfigurationValue extends ConfigurationValue<Character> {

    public CharacterConfigurationValue(Plugin plugin, String path, Character defaultValue) {
        super(plugin, path, defaultValue);
    }

    @Override
    protected Character parse(String input) throws ArgumentParseException {
        if (input.length() != 1)
            throw createArgumentParseException(input);
        return input.charAt(0);
    }

    /**
     * Customize the specific ArgumentParseException that is thrown when an error is encountered while parsing this Character.
     * This method is called by the default implementation of {@link #parse(String) parse}.
     * @param input the input that could not be parsed
     * @return a custom {@link ArgumentParseException}
     */
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
