package com.monst.pluginconfiguration.impl;

import com.monst.pluginconfiguration.ConfigurationValue;
import com.monst.pluginconfiguration.exception.ArgumentParseException;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

/**
 * A configuration value of the type {@link Material}.
 */
public class MaterialConfigurationValue extends ConfigurationValue<Material> {

    public MaterialConfigurationValue(JavaPlugin plugin, String path, Material defaultValue) {
        super(plugin, path, defaultValue);
    }

    @Override
    protected Material parse(String input) throws ArgumentParseException {
        return Optional.ofNullable(Material.matchMaterial(input))
                .orElseThrow(() -> createArgumentParseException(input));
    }

    protected ArgumentParseException createArgumentParseException(String input) {
        return new ArgumentParseException();
    }

    @Override
    protected Object convertToFileData(Material material) {
        return material.toString();
    }

}
