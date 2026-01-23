import java.util.*;
import M1A2.CreditCardValidator;
static Scanner scanner = new Scanner(System.in);

public static void main(String[] args) {

    System.out.print("Enter a credit card number as a long integer: ");
    long creditCardNumber = scanner.nextLong();
    CreditCardValidator validator = new CreditCardValidator();

    if (validator.isValid(creditCardNumber)) System.out.println(creditCardNumber + " is valid");
    else System.out.println(creditCardNumber + " is not valid");

}
