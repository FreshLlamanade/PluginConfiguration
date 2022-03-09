package com.monst.pluginconfiguration;

import com.monst.pluginconfiguration.exception.UnreadableValueException;
import com.monst.pluginconfiguration.exception.ValueOutOfBoundsException;
import com.monst.pluginconfiguration.validation.Bound;

import java.util.List;

class ExceptionBuffer<T> {

    @FunctionalInterface
    interface Converter<T> {
        T convert(Object o) throws ValueOutOfBoundsException, UnreadableValueException;
    }

    private T t;
    private boolean encounteredException = false;

    ExceptionBuffer(T t) {
        this.t = t;
    }

    private ExceptionBuffer(T t, boolean encounteredException) {
        this.t = t;
        this.encounteredException = encounteredException;
    }

    <R> ExceptionBuffer<R> convert(Converter<R> converter) throws ValueOutOfBoundsException, UnreadableValueException {
        try {
            return new ExceptionBuffer<>(converter.convert(t), encounteredException);
        } catch (ValueOutOfBoundsException e) {
            return new ExceptionBuffer<>(e.getReplacement(), true);
        }
    }

    ExceptionBuffer<T> validate(List<Bound<T>> bounds) {
        for (Bound<T> bound : bounds)
            try {
                bound.check(t);
            } catch (ValueOutOfBoundsException e) {
                t = e.getReplacement();
                encounteredException = true;
            }
        return this;
    }

    T getOrThrow() throws ValueOutOfBoundsException {
        if (encounteredException)
            throw new ValueOutOfBoundsException(t);
        return t;
    }

}
