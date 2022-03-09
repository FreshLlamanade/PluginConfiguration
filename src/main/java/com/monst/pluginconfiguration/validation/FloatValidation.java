package com.monst.pluginconfiguration.validation;

public interface FloatValidation {

    static Bound<Float> absolute() {
        return Bound.requiring(f -> f >= 0, Math::abs);
    }

    static Bound<Float> positive() {
        return atLeast(0f);
    }

    static Bound<Float> negative() {
        return atMost(0f);
    }

    static Bound<Float> atMost(float max) {
        return Bound.requiring(f -> f <= max, f -> max);
    }

    static Bound<Float> atLeast(float min) {
        return Bound.requiring(f -> f >= min, f -> min);
    }

}
