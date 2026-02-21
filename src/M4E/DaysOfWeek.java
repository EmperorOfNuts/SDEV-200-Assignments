package M4E;

public class DaysOfWeek {
	final static String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

	public static String DayOfWeekStr(int NumberOfDay) { return daysOfWeek[NumberOfDay - 1]; }
}
