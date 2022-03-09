package com.monst.pluginconfiguration.validation;

public interface IntegerValidation {

    static Bound<Integer> absolute() {
        return Bound.requiring(i -> i >= 0, Math::abs);
    }

    static Bound<Integer> positive() {
        return atLeast(0);
    }

    static Bound<Integer> negative() {
        return atMost(0);
    }

    static Bound<Integer> atMost(int max) {
        return Bound.requiring(i -> i <= max, i -> max);
    }

    static Bound<Integer> atLeast(int min) {
        return Bound.requiring(i -> i >= min, i -> min);
    }

}
