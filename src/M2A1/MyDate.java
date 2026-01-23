package M2A1;

import java.time.*;

public class MyDate {

    private int day, month, year;
    // private static final int  epochDay = 1, epochMonth = 1, epochYear = 1970, daysInYear = 365;

    public MyDate() {
        LocalDate currentDate = LocalDate.now();
        year = currentDate.getYear();
        month = currentDate.getMonthValue();
        day = currentDate.getDayOfMonth();
    }

    public MyDate(long elapsedMilliseconds) {
        setDate(elapsedMilliseconds);
    }

    public MyDate(int inDay, int inMonth, int inYear) {
        if (inDay > 28) {
            int dayOfMonth = switch (inMonth) {
                case 2 -> (isLeapYear(inYear) ? 29 : 28);
                case 4, 6, 9, 11 -> 30;
                default -> 31;
            };

            if (inDay > dayOfMonth) {
                if (inDay == 29) throw new DateTimeException("Invalid date " + inYear + " isn't a leap year.");
                else throw new DateTimeException("Invalid date.");
            }

        }

        day = inDay; month = inMonth; year = inYear;

    }

    public void setDate (long elapsedMilliseconds) {

        int daysSinceEpoch = (int) (elapsedMilliseconds / (1000L * 60 * 60 * 24)); // Calculate total days from given ms
        int year = 1970, daysInYear = 365;

        while (daysSinceEpoch >= daysInYear) {
            daysSinceEpoch -= daysInYear;
            year++;
            daysInYear = isLeapYear(year) ? 366 : 365;
        }

        this.year = year;

        int[] monthDays = getMonthDays(this.year);
        int currentMonth = 0;

        while (daysSinceEpoch >= monthDays[currentMonth]) {
            daysSinceEpoch -= monthDays[currentMonth];
            currentMonth++;
        }

        this.month = currentMonth;
        this.day = daysSinceEpoch + 1; // convert from 0 based to 1 for days

    }

    public int getDay() { return day; }
    public int getMonth() { return month; }
    public int getYear() { return year; }

    public void getDate() {
        System.out.println(year + "-" + (month) + "-" + day);
    }

    private int[] getMonthDays(int inYear) {
        return new int[] {
                31, isLeapYear(inYear) ? 29 : 28, 31, 30, 31, 30,
                31, 31, 30, 31, 30, 31
        };
    }

    /* & 15 checks if year is divisible by 16 (bit should be ... 0000)
       & 3 checks if it is divisible by 4 (bit should be ... xx00)
       Cannot check the bit for 100 since it's not power of 2
       This follows ISO-8601 Rules for leap years */
    private boolean isLeapYear(int inYear) { return (inYear & 15) == 0 ? (inYear & 3) == 0 : (inYear & 3) == 0 && inYear % 100 != 0; }

}
