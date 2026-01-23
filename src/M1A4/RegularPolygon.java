package M1A4;

public class RegularPolygon {

    private int n = 3;
    private double side = 1, x = 0, y = 0;

    public RegularPolygon() {}

    public RegularPolygon(int inN, double inSide) {
        n = inN; side = inSide;
    }

    public RegularPolygon(int inN, double inSide, double inX, double inY) {
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
