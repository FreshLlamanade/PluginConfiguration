package com.monst.pluginconfiguration.validation;

public interface FloatValidation {

    static Bound<Float> absolute() {
        return Bound.requiring(f -> f >= 0, Math::abs);
    }

    static Bound<Float> positiveOrZero() {
        return Bound.atLeast(0f);
    }

    static Bound<Float> negativeOrZero() {
        return Bound.atMost(0f);
    }

}
