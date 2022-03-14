package com.monst.pluginconfiguration.validation;

public interface ShortValidation {

    static Bound<Short> absolute() {
        return Bound.requiring(s -> s >= 0, s -> (short) Math.abs(s));
    }

    static Bound<Short> positive() {
        return Bound.greaterThan((short) 0);
    }

    static Bound<Short> positiveOrZero() {
        return Bound.atLeast((short) 0);
    }

    static Bound<Short> negativeOrZero() {
        return Bound.atMost((short) 0);
    }

    static Bound<Short> negative() {
        return Bound.lessThan((short) 0);
    }

}
