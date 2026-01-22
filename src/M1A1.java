public static void main(String[] args) {

    convertFeetMeters converter = new convertFeetMeters();

    System.out.println("Feet   Meters      Meters   Feet");
    for (int i = 1; i <= 10; i++) System.out.println((double) i + "    " + converter.footToMeter((double) i) +  "      " + (i * 5.0 + 15.0) + "   " + converter.meterToFoot((double) i * 5.0 + 15 ));

}

public static class convertFeetMeters {

    public double meterToFoot(double meters) { return 3.279 * meters; }
    public double footToMeter(double foot) { return foot * 0.305; }

}