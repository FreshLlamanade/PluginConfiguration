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
        return Bound.requiring(d -> d.signum() > 0, d -> BigDecimal.ZERO);
    }

    static Bound<BigDecimal> negative() {
        return Bound.requiring(d -> d.signum() < 0, d -> BigDecimal.ZERO);
    }

    static Bound<BigDecimal> atMost(BigDecimal max) {
        return Bound.requiring(d -> d.compareTo(max) <= 0, d -> max);
    }

    static Bound<BigDecimal> atLeast(BigDecimal min) {
        return Bound.requiring(d -> d.compareTo(min) >= 0, d -> min);
    }

}
