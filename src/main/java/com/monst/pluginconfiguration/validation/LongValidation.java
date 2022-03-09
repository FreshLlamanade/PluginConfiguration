package com.monst.pluginconfiguration.validation;

public interface LongValidation {

    static Bound<Long> absolute() {
        return Bound.requiring(l -> l >= 0, Math::abs);
    }

    static Bound<Long> positive() {
        return atLeast(0L);
    }

    static Bound<Long> negative() {
        return atMost(0L);
    }

    static Bound<Long> atMost(long max) {
        return Bound.requiring(l -> l <= max, l -> max);
    }

    static Bound<Long> atLeast(long min) {
        return Bound.requiring(l -> l >= min, l -> min);
    }

}
