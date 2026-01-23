import java.util.Scanner;
import M2A3.BinaryFormatException;
static Scanner scanner = new Scanner(System.in);


public static void main(String[] args) {

    System.out.print("Enter a binary number: ");
    String binaryString = scanner.nextLine();

    try {
        int decimalValue = bin2Dec(binaryString);
        System.out.println("Decimal equivalent: " + decimalValue);
    } catch (BinaryFormatException ex) {
        System.out.println("Not a binary number.");
    }

}

public static int bin2Dec(String binaryString) throws BinaryFormatException {
    // Check if string only contains 0s and 1s, or is empty/null
    if (!binaryString.matches("[01]+") || binaryString.isEmpty()) throw new BinaryFormatException("Not a binary number.");

    return Integer.parseInt(binaryString, 2);
}