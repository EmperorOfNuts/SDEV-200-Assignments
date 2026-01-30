package M3A2;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.Scanner;

public class Rational extends Number implements Comparable<Rational> {
    private BigInteger numerator;
    private BigInteger denominator;

    public Rational() {
        this(BigInteger.ZERO, BigInteger.ONE);
    }

    public Rational(BigInteger numerator, BigInteger denominator) {
        if (denominator.equals(BigInteger.ZERO)) throw new ArithmeticException("Denominator cannot be zero");

        BigInteger gcd = numerator.gcd(denominator);

        this.numerator = numerator.divide(gcd);
        this.denominator = denominator.divide(gcd).abs();
    }

    public Rational(long numerator, long denominator) {
        this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }
    
    public Rational add(Rational secondRational) {
        BigInteger n = numerator.multiply(secondRational.getDenominator())
                .add(denominator.multiply(secondRational.getNumerator()));
        BigInteger d = denominator.multiply(secondRational.getDenominator());

        return new Rational(n, d);
    }

    public Rational subtract(Rational secondRational) {
        BigInteger n = numerator.multiply(secondRational.getDenominator())
                .subtract(denominator.multiply(secondRational.getNumerator()));
        BigInteger d = denominator.multiply(secondRational.getDenominator());

        return new Rational(n, d);
    }

    public Rational multiply(Rational secondRational) {
        BigInteger n = numerator.multiply(secondRational.getNumerator());
        BigInteger d = denominator.multiply(secondRational.getDenominator());

        return new Rational(n, d);
    }

    public Rational divide(Rational secondRational) {
        if (secondRational.getNumerator().equals(BigInteger.ZERO)) throw new ArithmeticException("Cannot divide by zero rational");

        BigInteger n = numerator.multiply(secondRational.getDenominator());
        BigInteger d = denominator.multiply(secondRational.getNumerator());

        return new Rational(n, d);
    }

    public Rational abs() {
        return new Rational(numerator.abs(), denominator);
    }

    public Rational negate() {
        return new Rational(numerator.negate(), denominator);
    }

    public Rational reciprocal() {
        return new Rational(denominator, numerator);
    }

    @Override
    public String toString() {
        if (denominator.equals(BigInteger.ONE)) return numerator.toString();
        return numerator + "/" + denominator;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;

        // Check if other is even a rational
        if (!(other instanceof Rational)) return false;

        Rational r = (Rational) other;
        return numerator.equals(r.numerator) && denominator.equals(r.denominator);
    }

    // Implementation of abstract methods from Number class
    @Override
    public int intValue() { return (int) doubleValue(); }

    @Override
    public long longValue() { return (long) doubleValue(); }

    @Override
    public float floatValue() { return (float) doubleValue(); }

    @Override
    public double doubleValue() { return numerator.doubleValue() / denominator.doubleValue(); }

    @Override
    public int compareTo(Rational otherRational) {
        BigInteger left = numerator.multiply(otherRational.getDenominator());
        BigInteger right = otherRational.getNumerator().multiply(denominator);
        return left.compareTo(right);
    }

    public BigInteger getNumerator() { return numerator; }

    public BigInteger getDenominator() { return denominator; }
}