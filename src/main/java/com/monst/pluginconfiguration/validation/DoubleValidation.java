package com.monst.pluginconfiguration.validation;

public interface DoubleValidation {

    static Bound<Double> absolute() {
        return Bound.requiring(d -> d >= 0, Math::abs);
    }

    static Bound<Double> positiveOrZero() {
        return Bound.atLeast(0d);
    }

    static Bound<Double> negativeOrZero() {
        return Bound.atMost(0d);
    }

}
