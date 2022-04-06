package com.monst.pluginconfiguration.validation;

public interface IntegerValidation {

    static Bound<Integer> absolute() {
        return Bound.requiring(i -> i >= 0, Math::abs);
    }

    static Bound<Integer> positive() {
        return Bound.atLeast(1);
    }

    static Bound<Integer> positiveOrZero() {
        return Bound.atLeast(0);
    }

    static Bound<Integer> negativeOrZero() {
        return Bound.atMost(0);
    }

    static Bound<Integer> negative() {
        return Bound.atMost(-1);
    }

}
