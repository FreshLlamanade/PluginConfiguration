package com.monst.pluginconfiguration.validation;

import com.monst.pluginconfiguration.exception.ValueOutOfBoundsException;

import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface Bound<T> {

    void check(T t) throws ValueOutOfBoundsException;

    static <T> Bound<T> alwaysPasses() {
        return requiring(any -> true, any -> any);
    }

    static <T> Bound<T> alwaysFails() {
        return disallowing(any -> true, any -> any);
    }

    static <T extends Comparable<T>> Bound<T> atLeast(T min) {
        return requiring(t -> t.compareTo(min) >= 0, t -> min);
    }

    static <T extends Comparable<T>> Bound<T> greaterThan(T min) {
        return requiring(t -> t.compareTo(min) > 0, t -> min);
    }

    static <T extends Comparable<T>> Bound<T> lessThan(T max) {
        return requiring(t -> t.compareTo(max) < 0, t -> max);
    }

    static <T extends Comparable<T>> Bound<T> atMost(T max) {
        return requiring(t -> t.compareTo(max) <= 0, t -> max);
    }

    static <T> Bound<T> requiring(Predicate<? super T> shouldBe, Function<T, T> replacementMapper) {
        return t -> {
            if (!shouldBe.test(t))
                throw new ValueOutOfBoundsException(replacementMapper.apply(t));
        };
    }

    static <T> Bound<T> disallowing(Predicate<? super T> shouldNotBe, Function<T, T> replacementMapper) {
        return t -> {
            if (shouldNotBe.test(t))
                throw new ValueOutOfBoundsException(replacementMapper.apply(t));
        };
    }

}
