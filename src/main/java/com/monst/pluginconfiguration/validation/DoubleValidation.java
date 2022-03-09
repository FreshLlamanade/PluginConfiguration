package com.monst.pluginconfiguration.validation;

public interface DoubleValidation {

    static Bound<Double> absolute() {
        return Bound.requiring(d -> d >= 0, Math::abs);
    }

    static Bound<Double> positive() {
        return atLeast(0d);
    }

    static Bound<Double> negative() {
        return atMost(0d);
    }

    static Bound<Double> atMost(double max) {
        return Bound.requiring(d -> d <= max, d -> max);
    }

    static Bound<Double> atLeast(double min) {
        return Bound.requiring(d -> d >= min, d -> min);
    }

}
