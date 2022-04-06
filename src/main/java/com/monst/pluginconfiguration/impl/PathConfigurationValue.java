package com.monst.pluginconfiguration.impl;

import com.monst.pluginconfiguration.ConfigurationValue;
import com.monst.pluginconfiguration.exception.ArgumentParseException;
import org.bukkit.plugin.Plugin;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A configuration value of the type {@link Path}.
 */
public class PathConfigurationValue extends ConfigurationValue<Path> {

    public PathConfigurationValue(Plugin plugin, String path, Path defaultValue) {
        super(plugin, path, defaultValue);
    }

    @Override
    protected Path parse(String input) throws ArgumentParseException {
        try {
            return Paths.get(input);
        } catch (InvalidPathException e) {
            throw createArgumentParseException(input);
        }
    }

    /**
     * Customize the specific ArgumentParseException that is thrown when an error is encountered while parsing this Path.
     * This method is called by the default implementation of {@link #parse(String) parse}.
     * @param input the input that could not be parsed
     * @return a custom {@link ArgumentParseException}
     */
    protected ArgumentParseException createArgumentParseException(String input) {
        return new ArgumentParseException();
    }

    @Override
    protected Object convertToFileData(Path path) {
        return path.toString();
    }

}
