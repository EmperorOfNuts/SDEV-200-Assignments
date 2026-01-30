package M3A1;

public class Circle extends GeometricObject implements Comparable<Circle> {

    private double radius;

    public Circle() {}

    public Circle(double radius) { this.radius = radius; }

    public void setRadius(double radius) { this.radius = radius; }

    @Override
    public double getArea() { return radius * radius * Math.PI; }

    @Override
    public double getPerimeter() { return 2 * radius * Math.PI; }

    @Override
    public int compareTo (Circle circle) { return Double.compare(radius, circle.radius); }

    public void printCircle() {
        System.out.println("The circle is created " + this.getDateCreated() +
                " and the radius is" + radius);
    }

}
