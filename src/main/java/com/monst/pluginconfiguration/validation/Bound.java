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
