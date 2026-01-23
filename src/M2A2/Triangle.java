package M2A2;

public class Triangle extends GeometricObject {

    private double side1 = 1.0, side2 = 1.0, side3 = 1.0;

    public Triangle() {}

    public Triangle(double inSide1, double inSide2, double inSide3, String inColor, boolean inFilled) {
        super(inColor, inFilled);
        this.side1 = inSide1; this.side2 = inSide2; this.side3 = inSide3;
    }

    public double getArea() {
        double s = getPerimeter() / 2;
        return Math.sqrt(s * (s - side1) * (s - side2) * (s - side3));
    }

    public double getPerimeter() { return (side1 + side2 + side3); }

    public String toString () { return "Triangle: side1 = " + side1 + " side2 = " + side2 + " side3 = " + side3; }

}
