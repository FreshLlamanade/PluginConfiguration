package com.monst.pluginconfiguration;

import com.monst.pluginconfiguration.exception.ArgumentParseException;
import com.monst.pluginconfiguration.exception.MissingValueException;
import com.monst.pluginconfiguration.exception.UnreadableValueException;
import com.monst.pluginconfiguration.exception.ValueOutOfBoundsException;
import com.monst.pluginconfiguration.validation.Bound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A configuration value stored in the config.yml file under a certain path.
 * @param <T> the type of the value
 */
public abstract class ConfigurationValue<T> implements Supplier<T> {

    private final JavaPlugin plugin;
    private final String path;
    private final T defaultValue;
    private T loadedValue;

    /**
     * Creates a new configuration value of the specified plugin at the specified path in the plugin's config.yml file.
     * Calling this constructor will immediately load the value from the file, creating it if it doesn't exist.
     * @param plugin the plugin instance
     * @param path the path in the config.yml file (subsections are demarcated with .)
     * @param defaultValue the default value of this configuration value
     */
    public ConfigurationValue(JavaPlugin plugin, String path, T defaultValue) {
        this.plugin = plugin;
        this.path = path;
        this.defaultValue = defaultValue;
        this.reload();
    }

    /**
     * Gets the current value of this configuration value as seen in the config.yml file.
     * @return the current value
     */
    @Override
    public T get() {
        return loadedValue;
    }

    /**
     * Reloads this configuration value.
     */
    public void reload() {
        this.loadedValue = load();
    }

    /**
     * Loads this configuration value from the config.yml.
     * If the value was missing or uninterpretable, the default value will be written to the config.yml and used.
     * If the value was otherwise imperfectly formed or outside its bounds, a replacement will be written to the config.yml and used.
     * @return the loaded value from the config.yml
     */
    private T load() {
        try {
            return read(plugin.getConfig(), path);
        } catch (MissingValueException | UnreadableValueException e) {
            write(defaultValue);
            return defaultValue;
        } catch (ValueOutOfBoundsException e) {
            write(e.getReplacement());
            return e.getReplacement();
        }
    }

    /**
     * Parses a user-entered string to a new value, and sets this configuration value.
     * <p><b>Note:</b></p> it is the responsibility of the developer to call {@link JavaPlugin#reloadConfig()}
     * before using this method, to ensure that changes are written to the most up-to-date version of the config.yml file,
     * as well as {@link JavaPlugin#saveConfig()} after this method to finally persist those changes to the file.
     * This library does not know how often values are going to be set, and so cannot determine the best strategy for
     * when to call these two methods.
     * @param input user input to be parsed
     * @throws ArgumentParseException if the input could not be parsed
     */
    public void parseAndSet(String input) throws ArgumentParseException {
        if (input.isEmpty() && !isOptional())
            reset();
        else
            set(parse(input));
    }

    /**
     * Validates and sets this configuration value to a new value.
     * <p><b>Note:</b></p> it is the responsibility of the developer to call {@link JavaPlugin#reloadConfig()}
     * before using this method, to ensure that changes are written to the most up-to-date version of the config.yml file,
     * as well as {@link JavaPlugin#saveConfig()} after this method to finally persist those changes to the file.
     * This library does not know how often values are going to be set, and so cannot determine the best strategy for
     * when to call these two methods.
     * @param newValue the new value
     */
    public void set(T newValue) {
        newValue = validate(newValue);
        beforeSet();
        loadedValue = newValue;
        write(newValue);
        afterSet();
    }

    /**
     * Resets this configuration value to the default.
     * <p><b>Note:</b></p> it is the responsibility of the developer to call {@link JavaPlugin#reloadConfig()}
     * before using this method, to ensure that changes are written to the most up-to-date version of the config.yml file,
     * as well as {@link JavaPlugin#saveConfig()} after this method to finally persist those changes to the file.
     * This library does not know how often values are going to be set, and so cannot determine the best strategy for
     * when to call these two methods.
     */
    public void reset() {
        beforeSet();
        loadedValue = defaultValue;
        write(defaultValue);
        afterSet();
    }

    /**
     * Validates the provided value against this configuration value's bounds.
     * If the input adheres to every bound, it is returned with no change.
     * Otherwise, a replacement value is returned.
     * @param value the value to validate
     * @return the input, or a validated replacement
     */
    public T validate(T value) {
        try {
            validate(value, getBounds());
            return value;
        } catch (ValueOutOfBoundsException e) {
            return e.getReplacement();
        }
    }

    /**
     * Writes a value to the plugin {@link FileConfiguration} at the corresponding path.
     * @param t the value to be written
     */
    private void write(T t) {
        plugin.getConfig().set(path, convertToFileData(t));
    }

    /**
     * An action to be taken before every time this configuration value is set to a new value.
     */
    protected void beforeSet() {

    }

    /**
     * An action to be taken after every time this configuration value is set to a new value.
     */
    protected void afterSet() {

    }

    /**
     * Returns true if it is possible to leave this configuration value blank.
     * This will enable {@link #parse(String)} to receive an empty string as an input.
     * It is highly recommended in these cases to wrap the type of the configuration value with {@link java.util.Optional}.
     * @return true if this configuration value is optional.
     */
    protected boolean isOptional() {
        return false;
    }

    /**
     * Returns true if this configuration value can be changed and take effect at runtime, without a server restart.
     * This method may be overridden at will; it is not used by the rest of this library.
     * @return true if this value can be hot-swapped at runtime, false if it requires a restart.
     */
    @SuppressWarnings("unused")
    public boolean isHotSwappable() {
        return true;
    }

    /**
     * Reads and reconstructs the value currently stored in the {@link FileConfiguration} under the given path.
     * @param config the plugin {@link FileConfiguration}, findable with {@link JavaPlugin#getConfig()}
     * @param path the path where the value is located
     * @return the reconstructed value currently stored in the config.yml file
     * @throws MissingValueException if the value is missing from the file
     * @throws ValueOutOfBoundsException if the value is not within its bounds
     * @throws UnreadableValueException if the value is uninterpretable
     */
    T read(FileConfiguration config, String path)
            throws MissingValueException, ValueOutOfBoundsException, UnreadableValueException {
        Object o = config.get(path, null); // preserve nullability
        if (o == null)
            throw new MissingValueException(); // value is not present in the file
        return new ExceptionBuffer<>(o)
                .convert(this::convert)
                .validate(getBounds())
                .getOrThrow();
    }

    /**
     * Parses a value from a user-entered string.
     * @param input a string representation of the desired object
     * @return the parsed object
     * @throws ArgumentParseException if the input could not be parsed
     */
    protected abstract T parse(String input) throws ArgumentParseException;

    /**
     * Takes the {@link Object} that was fetched from the configuration file, and attempts to convert it into the correct type.
     * This method should be overridden in cases where a more appropriate procedure exists.
     * @throws UnreadableValueException if the object is of an unrelated type and could not be converted.
     * @throws ValueOutOfBoundsException if the object was readable but should have been in a different format.
     */
    protected T convert(Object o) throws ValueOutOfBoundsException, UnreadableValueException {
        try {
            return parse(o.toString());
        } catch (ArgumentParseException e) {
            throw new UnreadableValueException();
        }
    }

    /**
     * Throws an {@link ValueOutOfBoundsException} if the provided value does not adhere to one or more of the provided bounds.
     * The exception will contain a replacement value to use instead.
     * @param t the value to validate
     * @throws ValueOutOfBoundsException if the value is outside its bounds
     */
    static <T> void validate(T t, List<Bound<T>> bounds) throws ValueOutOfBoundsException {
        new ExceptionBuffer<>(t).validate(bounds).getOrThrow();
    }

    /**
     * Gets a list of {@link Bound}s that this configuration value is required to conform to.
     * If the value is found to be outside one or more of these bounds, it will be replaced.
     * By default, this method returns a singleton list of the Bound provided by {@link #getBound()}.
     * Therefore, if the configuration value only requires a single Bound, override that method instead.
     * @return a list of Bounds for this configuration value
     */
    protected List<Bound<T>> getBounds() {
        return Collections.singletonList(getBound());
    }

    /**
     * Gets a single {@link Bound} that this configuration value is required to conform to.
     * If the value is found to be outside this bound, it will be replaced.
     * By default, this method returns a Bound that allows all values to pass through.
     * If more than one bound is needed, override {@link #getBounds()} instead.
     * @return a Bound for this configuration value
     */
    protected Bound<T> getBound() {
        return Bound.alwaysPasses();
    }

    /**
     * Converts the configuration value into a type that can be stored in the config.yml file.
     * By default, this method returns the value itself.
     * @param t the value
     * @return a file-storable object
     */
    protected Object convertToFileData(T t) {
        return t;
    }

    /**
     * Gets the path of this configuration value where it can be found in the config.yml file.
     * @return the path of this configuration value
     */
    public String getPath() {
        return path;
    }

    /**
     * Gets the default value of this configuration value as defined in the constructor.
     * @return the default value of this configuration value
     */
    public T getDefaultValue() {
        return defaultValue;
    }

    /**
     * Gets a list of tab-completions to be shown to a player typing in a command.
     * By default, this returns a formatted list of the current value and the default value of this configuration value.
     * This method may be overridden at will; it is not used by the rest of this library.
     * @param player the player typing in the command
     * @param args the arguments the player has typed so far
     * @return a list of tab-completions
     */
    @SuppressWarnings("unused")
    public List<String> getTabCompletions(Player player, String[] args) {
        return Arrays.asList(toString(), format(defaultValue));
    }

    /**
     * Formats this configuration value.
     * @param t the value
     * @return a formatted string representing the state of this configuration value
     */
    protected String format(T t) {
        return String.valueOf(t);
    }

    /**
     * @return the formatted current state of this configuration value
     */
    @Override
    public String toString() {
        return format(get());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ConfigurationValue<?> other = (ConfigurationValue<?>) o;
        return Objects.equals(path, other.path) && Objects.equals(defaultValue, other.defaultValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, defaultValue);
    }

}
