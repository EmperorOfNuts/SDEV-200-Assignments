import java.util.*;
import M2A2.Triangle;
static Scanner scanner = new Scanner(System.in);

public static void main() {
    double side1, side2, side3;
    String color;
    boolean filled;

    System.out.print("Enter first side: ");
    side1 = scanner.nextDouble();
    System.out.print("Enter second side: ");
    side2 = scanner.nextDouble();
    System.out.print("Enter third side: ");
    side3 = scanner.nextDouble();
    System.out.print("Enter color: ");
    color = scanner.next();
    System.out.print("Is triangle filled? true/false: ");
    filled = scanner.nextBoolean();

    Triangle triangle = new Triangle(side1, side2, side3, color, filled);

    System.out.println(triangle.toString());
    System.out.println("Area: " + triangle.getArea() + ", Perimeter: " + triangle.getPerimeter() + ", Color: " + triangle.getColor() + ", is filled: " + triangle.isFilled());

}
