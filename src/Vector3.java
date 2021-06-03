import com.sun.source.tree.BreakTree;

import java.awt.*;

public class Vector3 {

    private double x;
    private double y;
    private double z;

    // Constructor
    public Vector3(){}

    public Vector3(double initX, double initY, double initZ) {

        x = initX;
        y = initY;
        z = initZ;
    }

    // creates a random Vector between min & max values
    private Vector3(double min, double max) {
         double seed = Math.random();
         double delta = max-min;

        x = min + (delta * (Math.random() -0.5) * seed);
        y = min + (delta * (Math.random() -0.5) * seed);
        z = min + (delta * (Math.random() -0.5) * seed);
    }

    public static Vector3 generatePosition(){ return new Vector3(Double.MIN_VALUE, 2 * Simulation.bounds); }
    public static Vector3 generateMovement(){
        return new Vector3(5e6, 5e8);
    }
    public static Vector3 generateOrthoPos(Vector3 center) { return new Vector3(center.x + Math.random() * 6e4,center.y,center.z); }
    public static Vector3 generateOrthoMov(){ return new Vector3(0, Math.random() * 5e4, 0); }

    // Returns the sum of this vector and vector 'v'.
    public Vector3 plus(Vector3 v) {

        double x, y, z;
        x = this.x + v.x;
        y = this.y + v.y;
        z = this.z + v.z;
        return new Vector3(x, y, z);
    }

    // Returns the product of this vector and 'd'.
    public Vector3 times(double d) {

        double x, y, z;
        x = this.x * d;
        y = this.y * d;
        z = this.z * d;
        return new Vector3(x, y, z);
    }

    // Returns the sum of this vector and -1*v.
    public Vector3 minus(Vector3 v) {

        double x, y, z;
        x = this.x - v.x;
        y = this.y - v.y;
        z = this.z - v.z;
        return new Vector3(x, y, z);
    }

    // Returns the Euclidean distance of this vector
    public double distanceTo(Vector3 v) {
        //double a = Math.sqrt((this.x - v.x) * (this.x - v.x) + (this.y - v.y) * (this.y - v.y) + (this.z - v.z) * (this.z - v.z));
        return (this.x -(v.x) + this.y - (v.y) + this.z - (v.z));
    }

    // Returns the length (norm) of this vector.
    public double length() {
        return distanceTo(new Vector3());
    }

    // Normalizes this vector: changes the length of this vector such that it becomes 1.
    public void normalize() {
        double norm = this.length();
        x /= norm;
        y /= norm;
        z /= norm;
    }

    public static Vector3 weightedPosition(double[] masses, Vector3[] position){
        if (masses == null){ return null; }
        double x = 0;
        double y = 0;
        double z = 0;
        double mass = 0;

        for (int i = 0; i < masses.length; i++) {
            mass += masses[i];
            x += masses[i]*position[i].x;
            y += masses[i]*position[i].y;
            z += masses[i]*position[i].z;
        }

        return new Vector3(x/mass, y/mass, z/mass);
    }


    // gibt die Koordinaten des Vektors zurück
    public String toString(){
        return "(" + x + ", " + y + ", " + z + ")";
    }

    // Zeichnet and der x,y Position einen Körper. Die z-Achse skaliert den Körper
    public void drawAsDot(double radius, Color color) {
        //double zScale = 1.5e10;   // 1e10-3e10
        //radius = radius * ((z-5e10)/zScale);


        double min = 2e7;
        double max = 2e10;
        double k = (max-min)/(1e11);
        double d = max-k*Simulation.bounds;
        double zScale = k*z+d;
        radius = (radius /10e5) * zScale ; //2e7;

        StdDraw.setPenColor(color);
        StdDraw.filledCircle(x, y, Math.abs(radius));
    }

    // zeichnet im Debug mode die boundaries der Blattknoten
    public void drawAsLine(int depth){
        double bounds = Simulation.bounds / Math.pow(2,depth);

        StdDraw.rectangle(x,y,bounds,bounds);

        //StdDraw.text(x,y,"center:" + this.toString() + " bounds: " + bounds;
    }

    // überprüft ob der Vektor innerhalb der globalen bounds liegt
    public boolean outOfBounds() {
        if (Math.abs(x) > Simulation.bounds || Math.abs(y) > Simulation.bounds || Math.abs(z) > Simulation.bounds) {
            return true;
        }
        return false;
    }


    // liefert einen Index zwischen 0 und 7, der den Untersektor angibt in dem sich center befindet
    public int checkIndex(Vector3 center) {

        double centerX = center.x;
        double centerY = center.y;
        double centerZ = center.z;

        if (z >= centerZ) { //Front
            if (y >= centerY) { // Top
                return x >= centerX ? 1 : 0; // right, left
            } else { // Bottom
                return x >= centerX ? 3 : 2; // right, left
            }
        } else { // Back
            if (y >= centerY) { // Top
                return x >= centerX ? 5 : 4; // right, left
            } else { // Bottom
                return x >= centerX ? 7 : 6; // right, left
            }

        }
    }


    // verschiebt das center in die Richtung die index angibt
    public Vector3 split(int index, int depth) {

        Vector3 newCenter = new Vector3();
        double bounds = Simulation.bounds / Math.pow(2,depth);

        if (index <= 3) {
            newCenter.z = (this.z + bounds) ; // Front
        } else {
            newCenter.z = (this.z - bounds) ; // Back
        }

        if (index == 0 || index == 1 || index == 4 || index == 5) {
            newCenter.y = (this.y + bounds) ; // Top
        } else {
            newCenter.y = (this.y - bounds) ; // Bottom
        }

        if (index % 2 != 0){
            newCenter.x = (this.x + bounds) ; // Right
        }else {
            newCenter.x = (this.x - bounds) ; // Left
        }

        return newCenter;
    }
}
