package com.monst.pluginconfiguration.impl;

import com.monst.pluginconfiguration.ConfigurationValue;
import com.monst.pluginconfiguration.exception.ArgumentParseException;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

/**
 * A configuration value of the type {@link World}.
 */
public class WorldConfigurationValue extends ConfigurationValue<World> {

    public WorldConfigurationValue(Plugin plugin, String path, World defaultValue) {
        super(plugin, path, defaultValue);
    }

    @Override
    protected World parse(String input) throws ArgumentParseException {
        return Optional.ofNullable(Bukkit.getWorld(input)).orElseThrow(() -> createArgumentParseException(input));
    }

    protected ArgumentParseException createArgumentParseException(String input) {
        return new ArgumentParseException();
    }

    @Override
    protected Object convertToFileData(World world) {
        return world.getName();
    }

}
