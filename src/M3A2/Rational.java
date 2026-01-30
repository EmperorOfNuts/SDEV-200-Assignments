package M3A2;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.Scanner;

public class Rational extends Number implements Comparable<Rational> {
    private BigInteger numerator;
    private BigInteger denominator;

    /** Construct a rational with default properties (0/1) */
    public Rational() {
        this(BigInteger.ZERO, BigInteger.ONE);
    }

    /** Construct a rational with specified numerator and denominator */
    public Rational(BigInteger numerator, BigInteger denominator) {
        if (denominator.equals(BigInteger.ZERO)) throw new ArithmeticException("Denominator cannot be zero");

        BigInteger gcd = numerator.gcd(denominator);

        this.numerator = numerator.divide(gcd);
        this.denominator = denominator.divide(gcd).abs();
    }

    /** Construct a rational from long values */
    public Rational(long numerator, long denominator) {
        this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    /** Return numerator */
    public BigInteger getNumerator() { return numerator; }

    /** Return denominator */
    public BigInteger getDenominator() { return denominator; }

    /** Add a rational number to this rational */
    public Rational add(Rational secondRational) {
        BigInteger n = numerator.multiply(secondRational.getDenominator())
                .add(denominator.multiply(secondRational.getNumerator()));
        BigInteger d = denominator.multiply(secondRational.getDenominator());

        return new Rational(n, d);
    }

    /** Subtract a rational number from this rational */
    public Rational subtract(Rational secondRational) {
        BigInteger n = numerator.multiply(secondRational.getDenominator())
                .subtract(denominator.multiply(secondRational.getNumerator()));
        BigInteger d = denominator.multiply(secondRational.getDenominator());

        return new Rational(n, d);
    }

    /** Multiply a rational number with this rational */
    public Rational multiply(Rational secondRational) {
        BigInteger n = numerator.multiply(secondRational.getNumerator());
        BigInteger d = denominator.multiply(secondRational.getDenominator());

        return new Rational(n, d);
    }

    /** Divide this rational by another rational */
    public Rational divide(Rational secondRational) {
        if (secondRational.getNumerator().equals(BigInteger.ZERO)) throw new ArithmeticException("Cannot divide by zero rational");

        BigInteger n = numerator.multiply(secondRational.getDenominator());
        BigInteger d = denominator.multiply(secondRational.getNumerator());

        return new Rational(n, d);
    }

    /** Return the absolute value of this rational */
    public Rational abs() {
        return new Rational(numerator.abs(), denominator);
    }

    /** Return the negation of this rational */
    public Rational negate() {
        return new Rational(numerator.negate(), denominator);
    }

    /** Return the reciprocal of this rational */
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
}