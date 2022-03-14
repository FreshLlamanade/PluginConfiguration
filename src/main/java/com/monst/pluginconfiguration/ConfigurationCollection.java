package com.monst.pluginconfiguration;

import com.monst.pluginconfiguration.exception.ArgumentParseException;
import com.monst.pluginconfiguration.exception.UnreadableValueException;
import com.monst.pluginconfiguration.exception.ValueOutOfBoundsException;
import com.monst.pluginconfiguration.validation.Bound;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A configuration value with multiple elements, always stored as a list in the config.yml.
 * @param <T> the type of each element
 * @param <C> the type of the container collection
 */
public abstract class ConfigurationCollection<T, C extends Collection<T>> extends ConfigurationValue<C> {

    public ConfigurationCollection(Plugin plugin, String path, C defaultValue) {
        super(plugin, path, defaultValue);
    }

    /**
     * Parses a collection of values from a user-entered string.
     * The individual elements may be separated by spaces, commas, or both.
     * @param input a string representation of the desired object
     * @return the parsed elements in a new collection
     * @throws ArgumentParseException if an element could not be parsed
     */
    @Override
    protected C parse(String input) throws ArgumentParseException {
        C collection = createCollection();
        for (String s : input.split("\\s*(,|\\s)\\s*")) // the entered list may be separated by commas or spaces or both
            collection.add(parseOne(s));
        return collection;
    }

    /**
     * Parses a collection of objects from a list of objects in the configuration file.
     * Each stored object is parsed into an object, validated, and added to a collection.
     * In contrast to {@link #parse(String)}, {@link ArgumentParseException}s will not interrupt the process.
     * If any problems are found while parsing, validating, and adding the objects to the collection, this will be
     * recorded in {@code problemFound} and dealt with later.
     * If a problem was found, an exception will be thrown.
     * If anything of use was reconstructed, this exception will be an {@link ValueOutOfBoundsException} with the
     * collection as a replacement, and the configuration file will be updated to match this collection.
     * If nothing of use could be reconstructed, a {@link UnreadableValueException} will be thrown and the value
     * in the configuration file will be reset to default.
     * @param o a list of the stored type from the configuration file
     * @return a collection of parsed and validated values
     * @throws ValueOutOfBoundsException if there was a problem with at least one value in the configuration file.
     */
    protected C convert(Object o) throws ValueOutOfBoundsException, UnreadableValueException {
        boolean problemFound = false;
        List<?> list = (o instanceof List) ? (List<?>) o : Collections.singletonList(o);
        C collection = createCollection();
        for (Object element : list) {
            try {
                T t = new ExceptionBuffer<>(element)
                        .convert(this::convertOne)
                        .validate(getElementBounds())
                        .getOrThrow();
                if (!collection.add(t))
                    problemFound = true;
            } catch (ValueOutOfBoundsException e) {
                collection.add(e.getReplacement());
                problemFound = true;
            } catch (UnreadableValueException e) {
                problemFound = true;
            }
        }
        if (problemFound)
            throw new ValueOutOfBoundsException(collection);
        return collection;
    }

    /**
     * Validates every element of the provided collection against this configuration value's element bounds,
     * as well as the collection itself against this configuration value's bounds.
     * If the input adheres to every bound, it is returned with no change.
     * Otherwise, a replacement value is returned.
     * @param collection the value to validate
     * @return the input, or a validated replacement
     */
    @Override
    public C validate(C collection) {
        C validated = createCollection();
        for (T element : collection) {
            try {
                validate(element, getElementBounds());
                validated.add(element);
            } catch (ValueOutOfBoundsException e) {
                validated.add(e.getReplacement());
            }
        }
        try {
            validate(validated, getBounds());
            return validated;
        } catch (ValueOutOfBoundsException e) {
            return e.getReplacement();
        }
    }

    /**
     * Creates a new instance of the specific collection implementation to wrap the elements in.
     * This collection must implement the {@link Collection#add(Object)} method.
     * The implementation-specific rules of this collection will be observed when adding elements;
     * that is, if not all elements could be added, then the config.yml will be updated with only
     * those elements that were successfully added.
     * @return a new instance of the desired collection
     */
    protected abstract C createCollection();

    /**
     * Parses an element from a user-entered string.
     * @param input a string representation of the desired object
     * @return the parsed object
     * @throws ArgumentParseException if the input could not be parsed
     */
    protected abstract T parseOne(String input) throws ArgumentParseException;

    /**
     * Takes an {@link Object} element from the list that was fetched from the configuration file, and attempts to
     * convert it into the correct type.
     * This method should be overridden in cases where a more appropriate procedure exists.
     * @throws UnreadableValueException if the object is of an unrelated type and could not be converted.
     * @throws ValueOutOfBoundsException if the object was readable but should have been in a different format.
     */
    protected T convertOne(Object o)
            throws ValueOutOfBoundsException, UnreadableValueException {
        try {
            return parseOne(o.toString());
        } catch (ArgumentParseException e) {
            throw new UnreadableValueException();
        }
    }

    /**
     * Gets a list of {@link Bound}s that every element of this configuration collection is required to conform to.
     * If an element is found to be outside one or more of these bounds, it will be replaced.
     * By default, this method returns a singleton list of the Bound provided by {@link #getElementBound()}.
     * Therefore, if the collection elements only require a single Bound, override that method instead.
     * @return a list of Bounds for each collection element
     */
    protected List<Bound<T>> getElementBounds() {
        return Collections.singletonList(getElementBound());
    }

    /**
     * Gets a single {@link Bound} that every element of this configuration collection is required to conform to.
     * If an element is found to be outside this bound, it will be replaced.
     * By default, this method returns a Bound that allows all values to pass through.
     * If more than one bound is needed for the collection elements, override {@link #getElementBounds()} instead.
     * @return a Bound for all collection elements
     */
    protected Bound<T> getElementBound() {
        return Bound.alwaysPasses();
    }

    @Override
    protected String format(C collection) {
        if (collection.isEmpty())
            return "[]";
        return collection.stream().map(this::formatOne).collect(Collectors.joining(", ")); // do not include [ ]
    }

    protected String formatOne(T t) {
        return String.valueOf(t);
    }

}
