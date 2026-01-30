import M3A1.Circle;

public static void main() {

    Circle circle1 = new Circle(1.0);
    Circle circle2 = new Circle(2.0);

    System.out.println("Is circle 1 equal to circle 2? " + (circle1.compareTo(circle2)));
    circle2.setRadius(1.0);
    System.out.println("Is circle 1 equal to circle 2? " + (circle1.compareTo(circle2)));

}