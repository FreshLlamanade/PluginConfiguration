package com.monst.pluginconfiguration.impl;

import com.monst.pluginconfiguration.ConfigurationValue;
import com.monst.pluginconfiguration.exception.ArgumentParseException;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

/**
 * A configuration value of the type {@link Material}.
 */
public class MaterialConfigurationValue extends ConfigurationValue<Material> {

    public MaterialConfigurationValue(Plugin plugin, String path, Material defaultValue) {
        super(plugin, path, defaultValue);
    }

    @Override
    protected Material parse(String input) throws ArgumentParseException {
        return Optional.ofNullable(Material.matchMaterial(input)).orElseThrow(() -> createArgumentParseException(input));
    }

    /**
     * Customize the specific ArgumentParseException that is thrown when an error is encountered while parsing this Material.
     * This method is called by the default implementation of {@link #parse(String) parse}.
     * @param input the input that could not be parsed
     * @return a custom {@link ArgumentParseException}
     */
    protected ArgumentParseException createArgumentParseException(String input) {
        return new ArgumentParseException();
    }

    @Override
    protected Object convertToFileData(Material material) {
        return material.toString();
    }

}
