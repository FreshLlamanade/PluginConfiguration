package com.monst.pluginconfiguration.validation;

public interface DoubleValidation {

    static Bound<Double> absolute() {
        return Bound.requiring(d -> d >= 0, Math::abs);
    }

    static Bound<Double> positive() {
        return Bound.greaterThan(0d);
    }

    static Bound<Double> positiveOrZero() {
        return Bound.atLeast(0d);
    }

    static Bound<Double> negativeOrZero() {
        return Bound.atMost(0d);
    }

    static Bound<Double> negative() {
        return Bound.lessThan(0d);
    }

}
