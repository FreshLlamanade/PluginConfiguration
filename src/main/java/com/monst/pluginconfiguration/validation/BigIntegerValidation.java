package com.monst.pluginconfiguration.validation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public interface BigIntegerValidation {

    static Bound<BigDecimal> scale(int scale) {
        return Bound.requiring(i -> i.scale() == scale, i -> i.setScale(scale, RoundingMode.HALF_EVEN));
    }

    static Bound<BigInteger> absolute() {
        return Bound.requiring(i -> i.signum() >= 0, BigInteger::abs);
    }

    static Bound<BigInteger> positive() {
        return Bound.greaterThan(BigInteger.ZERO);
    }

    static Bound<BigInteger> positiveOrZero() {
        return Bound.atLeast(BigInteger.ZERO);
    }

    static Bound<BigInteger> negativeOrZero() {
        return Bound.atMost(BigInteger.ZERO);
    }

    static Bound<BigInteger> negative() {
        return Bound.lessThan(BigInteger.ZERO);
    }

}
