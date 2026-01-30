import java.math.BigInteger;
import java.util.Scanner;
import M3A2.Rational;

static Scanner input = new Scanner(System.in);

public static void main(String[] args) {

    System.out.print("Enter the first rational number: ");
    long num1 = input.nextLong();
    long den1 = input.nextLong();
    Rational r1 = new Rational(num1, den1);

    System.out.print("Enter the second rational number: ");
    long num2 = input.nextLong();
    long den2 = input.nextLong();
    Rational r2 = new Rational(num2, den2);

    System.out.println(r1 + " + " + r2 + " = " + r1.add(r2));
    System.out.println(r1 + " - " + r2 + " = " + r1.subtract(r2));
    System.out.println(r1 + " * " + r2 + " = " + r1.multiply(r2));
    System.out.println(r1 + " / " + r2 + " = " + r1.divide(r2));
    System.out.println(r2 + " is " + r2.doubleValue());

    int comparison = r1.compareTo(r2);
    if (comparison < 0) System.out.println(r1 + " is smaller than " + r2);
    else if (comparison > 0) System.out.println(r1 + " is bigger than " + r2);
    else System.out.println(r1 + " is equals to " + r2);

    System.out.println(r1 + " is (long) " + r1.longValue());
    System.out.println(r1 + " is (int) " + r1.intValue());
    System.out.println(r1 + " is (double) " + r1.doubleValue());
    System.out.println(r1 + " is (float) " + r1.floatValue());

}