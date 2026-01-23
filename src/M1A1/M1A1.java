import M1A1.ConvertFeetMeters;

public static void main(String[] args) {

    ConvertFeetMeters converter = new ConvertFeetMeters();

    System.out.println("Feet   Meters      Meters   Feet");
    for (int i = 1; i <= 10; i++) System.out.println((double) i + "    " + converter.footToMeter((double) i) +  "      " + (i * 5.0 + 15.0) + "   " + converter.meterToFoot((double) i * 5.0 + 15 ));

}

