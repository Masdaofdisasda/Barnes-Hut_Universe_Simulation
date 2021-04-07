import java.awt.*;

public class Vector3D {

    private double x;
    private double y;
    private double z;

    // Conctructor
    public Vector3D() {
    }

    public Vector3D(double initX, double initY, double initZ) {

        x = initX;
        y = initY;
        z = initZ;
    }

    public Vector3D(double seed) {

        x = (Math.random() - 0.5) * seed * 10e10;
        y = (Math.random() - 0.5) * seed * 10e10;
        z = 0;
    }

    // Returns the sum of this vector and vector 'v'.
    public Vector3D plus(Vector3D v) {

        double x, y, z;
        x = this.x + v.x;
        y = this.y + v.y;
        z = this.z + v.z;
        return new Vector3D(x, y, z);
    }

    // Returns the product of this vector and 'd'.
    public Vector3D times(double d) {

        double x, y, z;
        x = this.x * d;
        y = this.y * d;
        z = this.z * d;
        return new Vector3D(x, y, z);
    }

    // Returns the sum of this vector and -1*v.
    public Vector3D minus(Vector3D v) {

        double x, y, z;
        x = this.x - v.x;
        y = this.y - v.y;
        z = this.z - v.z;
        return new Vector3D(x, y, z);
    }

    // Returns the Euclidean distance of this vector
    public double distanceTo(Vector3D v) {

        return Math.sqrt((this.x - v.x) * (this.x - v.x) + (this.y - v.y) * (this.y - v.y) + (this.z - v.z) * (this.z - v.z));
    }

    // Returns the length (norm) of this vector.
    public double length() {

        return distanceTo(new Vector3D());
    }

    // Normalizes this vector: changes the length of this vector such that it becomes 1.
    public void normalize() {

        double norm = this.length();
        x /= norm;
        y /= norm;
        z /= norm;
    }

    // Draws a filled circle with a specified radius centered at the (x,y) coordinates of this vector
    // in the existing StdDraw canvas. The z-coordinate is not used.
    public void drawAsDot(double radius, Color color) {

        //TODO: implement method.
        //StdDraw.setPenColor(color);
        //StdDraw.filledCircle(x, y, radius);
    }
}
