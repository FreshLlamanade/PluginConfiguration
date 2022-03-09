package com.monst.pluginconfiguration.validation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public interface BigIntegerValidation {

    static Bound<BigDecimal> scale(int scale) {
        return Bound.requiring(i -> i.scale() == scale, i -> i.setScale(2, RoundingMode.HALF_EVEN));
    }

    static Bound<BigInteger> absolute() {
        return Bound.requiring(i -> i.signum() > 0, BigInteger::abs);
    }

    static Bound<BigInteger> positive() {
        return Bound.requiring(i -> i.signum() > 0, i -> BigInteger.ZERO);
    }

    static Bound<BigInteger> negative() {
        return Bound.requiring(i -> i.signum() < 0, i -> BigInteger.ZERO);
    }

    static Bound<BigInteger> atMost(BigInteger max) {
        return Bound.requiring(i -> i.compareTo(max) <= 0, i -> max);
    }

    static Bound<BigInteger> atLeast(BigInteger min) {
        return Bound.requiring(i -> i.compareTo(min) >= 0, i -> min);
    }

}
