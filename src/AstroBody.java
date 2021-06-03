import java.awt.*;

// contains a single astrological body
public class AstroBody {

    private double mass;
    private double radius;
    private Vector3 position; // position of the center.
    private Vector3 movement;
    private Vector3 force;
    private final Color color; // for drawing the body.

    //Constructor
    public AstroBody(double initMass, double initRadius, Vector3 initPosition, Vector3 initMovement, Color initColor) {

        mass = initMass;   // in kg
        radius = initRadius; // in meter
        position = initPosition;
        movement = initMovement;
        force = new Vector3(0,0,0);
        color = initColor;
    }

    public double getMass(){ return  mass; }
    public Vector3 getPosition(){ return position; }
    public Vector3 getForce(){
        return force;
    }
    public void setForce(Vector3 f){ force = f; }

    public String toString(){
        return "mass: " + mass + " " +"pos: " +  position;
    }

    //Returns a vector representing the gravitational force exerted by 'body' on this body.
    //The gravitational Force F is calculated by F = G*(m1*m2)/(r*r), with m1 and m2 being the masses of the objects
    //interacting, r being the distance between the centers of the masses and G being the gravitational constant.
    //To calculate the force exerted on b1, simply multiply the normalized vector pointing from b1 to b2 with the
    //calculated force
    public Vector3 gravitationalForce(AstroBody body) {

        Vector3 b1Tob2 = body.position.minus(this.position);
        double F = Simulation.G * (this.mass * body.mass) / (b1Tob2.length() * b1Tob2.length());
        b1Tob2.normalize();
        return b1Tob2.times(F);
    }

    // Moves this body to a new position, according to the specified force vector 'force' exerted
    // on it, and updates the current movement accordingly.
    // (Movement depends on the mass of this body, its current movement and the exerted force)
    // Hint: see simulation loop in Simulation.java to find out how this is done
    public void move(Vector3 force) {
        if (force == null) return;

        Vector3 newPosition = force.times(1 / mass);
        newPosition = newPosition.plus(position);
        newPosition = newPosition.plus(movement);
        Vector3 newMovement = newPosition.minus(position);

        position = newPosition;
        movement = newMovement;
    }

    // Draws the body to the current StdDraw canvas as a dot using 'color' of this body.
    // The radius of the dot is in relation to the radius of the celestial body
    // (use a conversion based on the logarithm as in 'Simulation.java').
    // Hint: use the method drawAsDot implemented in Vector3 for this
    public void draw() { this.position.drawAsDot(radius, color); }

    // siehe Vector3.java
    public boolean outOfBounds(){
        if (position.outOfBounds()){ return true; } return false; }

    // siehe Vector3.java
    public int checkIndex(Vector3 center){
        return position.checkIndex(center);
    }

    // erzeugt zufällige AstroBody Objekte
    public static AstroBody generateRandomBody() {
        double mass = Math.random()*10e28;
        double radius = (0.5 + Math.random()) *10e4;
        Vector3 position = Vector3.generatePosition();
        Vector3 movement = Vector3.generateMovement();
        Color color = new Color((int) (128 + Math.random() * 128), (int) (128 +Math.random() * 128), (int) (128 +Math.random() * 128));
        return new AstroBody(mass, radius, position, movement, color);
    }

    // erzeugt massive AstroBody Objekte
    public static AstroBody generateBlackHole() {
        double mass = (1+Math.random())*10e37; // 10e37 to 10e39 kg
        double radius = 10e5;
        Vector3 position = Vector3.generatePosition();
        Vector3 movement = new Vector3(Math.random(),Math.random(),Math.random());
        Color color = new Color(100,0,100);
        return new AstroBody(mass, radius, position, movement, color);
    }

    // erzeugt Sonnen
    public static AstroBody generateSunBody(Vector3 center) {
        double mass = 1.989 * 10e30;
        double radius = 4e5; //6.96e6
        Vector3 position = center;
        Vector3 movement = Vector3.generateMovement();
        Color color = new Color(255,255,100);
        return new AstroBody(mass, radius, position, movement, color);
    }
    // erzeugt orthognoale AstroBody Objekte
    public static AstroBody generateOrthoBody(Vector3 center) {
        double mass = Math.random()*10e28;
        double radius = (0.5 + Math.random()) * 10e4;
        Vector3 position = Vector3.generateOrthoPos(center);
        Vector3 movement = Vector3.generateOrthoMov();
        Color color = new Color((int) (128 + Math.random() * 128), (int) (128 +Math.random() * 128), (int) (128 +Math.random() * 128));
        return new AstroBody(mass, radius, position, movement, color);
    }
}
