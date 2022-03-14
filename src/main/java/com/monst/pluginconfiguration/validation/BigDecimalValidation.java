package com.monst.pluginconfiguration.validation;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface BigDecimalValidation {

    static Bound<BigDecimal> scale(int scale) {
        return Bound.requiring(d -> d.scale() == scale, d -> d.setScale(2, RoundingMode.HALF_EVEN));
    }

    static Bound<BigDecimal> absolute() {
        return Bound.requiring(d -> d.signum() > 0, BigDecimal::abs);
    }

    static Bound<BigDecimal> positive() {
        return Bound.greaterThan(BigDecimal.ZERO);
    }

    static Bound<BigDecimal> positiveOrZero() {
        return Bound.atLeast(BigDecimal.ZERO);
    }

    static Bound<BigDecimal> negativeOrZero() {
        return Bound.atMost(BigDecimal.ZERO);
    }

    static Bound<BigDecimal> negative() {
        return Bound.lessThan(BigDecimal.ZERO);
    }

}
