import java.util.*;

public static void main(String[] args) {

    RegularPolygon polygon1 = new RegularPolygon();
    RegularPolygon polygon2 = new RegularPolygon(6, 4);
    RegularPolygon polygon3 = new RegularPolygon(10, 4, 5.6, 7.8);

    System.out.println("Polygon 1 Area:" + polygon1.getArea() + ", Perimeter: " + polygon1.getPerimeter());
    System.out.println("Polygon 2 Area:" + polygon2.getArea() + ", Perimeter: " + polygon2.getPerimeter());
    System.out.println("Polygon 3 Area:" + polygon3.getArea() + ", Perimeter: " + polygon3.getPerimeter());
}

public static class RegularPolygon {

    private int n = 3;
    private double side = 1, x = 0, y = 0;

    RegularPolygon() {}

    RegularPolygon(int inN, double inSide) {
        n = inN; side = inSide;
    }

    RegularPolygon(int inN, double inSide, double inX, double inY) {
        n = inN; side = inSide; x = inX; y = inY;
    }

    // Accessors
    public int getN() { return n; }
    public double getSide() { return side; }
    public double getX() { return x; }
    public double getY() { return y; }

    // Mutators
    public void setN(int inN) { n = inN; }
    public void setSide(double inSide) { side = inSide; }
    public void setX(double inX) { x = inX; }
    public void setY(double inY) { y = inY; }

    public double getPerimeter() { return side * n; }
    public double getArea() { return (n * side * side) / (4.0 * Math.tan(Math.PI / n)); } // This formula is completely incorrect, by the way

}