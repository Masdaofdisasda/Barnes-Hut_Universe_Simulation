import com.sun.source.tree.BreakTree;

import java.awt.*;

public class Vector3 {

    private double x;
    private double y;
    private double z;

    // Constructor
    public Vector3() {
    }

    public Vector3(double initX, double initY, double initZ) {

        x = initX;
        y = initY;
        z = initZ;
    }

    public Vector3(double seed) {

        x = (Math.random() - 0.5) * seed * 10e10;
        y = (Math.random() - 0.5) * seed * 10e10;
        z = (Math.random() - 0.5) * seed * 10e10;
    }

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

        return Math.sqrt((this.x - v.x) * (this.x - v.x) + (this.y - v.y) * (this.y - v.y) + (this.z - v.z) * (this.z - v.z));
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

    public Vector3 weightedPosition(double[] masses, Vector3[] position){
        if (masses == null){ return this; }

        double x = 0;
        double y = 0;
        double z = 0;
        int i = 0;
        double mass = 0;

        while (masses[i] != 0){
            mass += masses[i];
            x += masses[i]*position[i].x;
            y += masses[i]*position[i].y;
            z += masses[i]*position[i].z;
            i++;
        }
        return new Vector3(x/mass, y/mass, z/mass);
    }


    // gibt die Koordinaten des Vektors zurück
    public String toString(){
        return "(" + x + ", " + y + ", " + z + ")";
    }

    // Zeichnet and der x,y Position einen Körper. Die z-Achse skaliert den Körper
    public void drawAsDot(double radius, Color color) {
        radius = radius * ((z-5e10)/2e10);

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

    // überprüft ob der Vektor in den gegebenen bounds liegt
    public boolean outOfBounds(int depth, Vector3 center){
        double bounds = Simulation.bounds / Math.pow(2,depth);

        if (Math.abs(center.x) > bounds || Math.abs(center.y) > bounds || Math.abs(center.z) > bounds) {
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
