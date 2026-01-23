package M1A2;

public class CreditCardValidator {

    public boolean isValid(long number) {

        return (prefixMatched(number, 4) || prefixMatched(number, 5) || prefixMatched(number, 6) || prefixMatched(number, 37)) && ((sumOfDoubleEvenPlace(number) + sumOfOddPlace(number)) % 10 == 0) && (getSize(number) <= 16 && getSize(number) >= 13);
    }

    public int sumOfDoubleEvenPlace(long number) {
        int sum = 0;
        String tempNumber = String.valueOf(number);

        for (int i = tempNumber.length() - 2; i >= 0; i -= 2) sum += getDigit(Character.getNumericValue(tempNumber.charAt(i)) * 2);

        System.out.println(sum + " even");
        return sum;
    }

    public int getDigit(int number) {
        if (number < 10) return number;
        else return Character.getNumericValue(String.valueOf(number).charAt(0)) + Character.getNumericValue(String.valueOf(number).charAt(1));
    }

    public int sumOfOddPlace(long number) {
        int sum = 0;
        String tempNumber = String.valueOf(number);

        for (int i = tempNumber.length() - 1; i > 0; i -= 2) sum += Character.getNumericValue(tempNumber.charAt(i));

        System.out.println(sum + " odd");
        return sum;
    }

    public boolean prefixMatched(long number, int d) {
        if (d > 10) return getPrefix(number, 2) == d;
        else return getPrefix(number, 1) == d;
    }

    public int getSize(long d) { return Long.toString(d).length(); }

    public long getPrefix(long number, int k) {
        if (k >= Long.toString(number).length()) return number;
        else return Long.parseLong(String.valueOf(number).substring(0, k));
    }
}
