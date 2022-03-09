package com.monst.pluginconfiguration.validation;

public interface ShortValidation {

    static Bound<Short> absolute() {
        return Bound.requiring(s -> s >= 0, s -> (short) Math.abs(s));
    }

    static Bound<Short> positive() {
        return atLeast((short) 0);
    }

    static Bound<Short> negative() {
        return atMost((short) 0);
    }

    static Bound<Short> atMost(short max) {
        return Bound.requiring(s -> s <= max, s -> max);
    }

    static Bound<Short> atLeast(short min) {
        return Bound.requiring(s -> s >= min, s -> min);
    }

}
