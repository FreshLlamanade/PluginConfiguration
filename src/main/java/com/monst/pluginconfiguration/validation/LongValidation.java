package com.monst.pluginconfiguration.validation;

public interface LongValidation {

    static Bound<Long> absolute() {
        return Bound.requiring(l -> l >= 0, Math::abs);
    }

    static Bound<Long> positive() {
        return Bound.greaterThan(0L);
    }

    static Bound<Long> positiveOrZero() {
        return Bound.atLeast(0L);
    }

    static Bound<Long> negativeOrZero() {
        return Bound.atMost(0L);
    }

    static Bound<Long> negative() {
        return Bound.lessThan(0L);
    }

}
