public static void main(String[] args) {

    System.out.println("Feet   Meters      Meters   Feet");
    for (int i = 1; i <= 10; i++) System.out.println((double) i + "    " + convertFeetMeters.footToMeter((double) i) +  "      " + (i * 5.0 + 15.0) + "   " + convertFeetMeters.meterToFoot((double) i * 5.0 + 15 ));

}

static class convertFeetMeters {

    public static double meterToFoot(double meters) { return 3.279 * meters; }
    public static double footToMeter(double foot) { return foot * 0.305; }

}