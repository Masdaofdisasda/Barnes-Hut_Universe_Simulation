import java.awt.*;

public class AstroBody {

    private double mass;
    private double radius;
    private Vector3D position; // position of the center.
    private Vector3D movement;
    private final Color color; // for drawing the body.

    //Constructor
    public AstroBody(double initMass, double initRadius, Vector3D initPosition, Vector3D initMovement, Color initColor) {

        mass = initMass;   // in kg
        radius = initRadius; // in meter
        position = initPosition;
        movement = initMovement;
        color = initColor;
    }

    // Returns the distance between this body and the specified 'body'.
    public double distanceTo(AstroBody body) {

        return this.position.distanceTo(body.position);     //Achtung, es gibt 2 distanceTo Methoden!
    }

    //Returns a vector representing the gravitational force exerted by 'body' on this body.
    //The gravitational Force F is calculated by F = G*(m1*m2)/(r*r), with m1 and m2 being the masses of the objects
    //interacting, r being the distance between the centers of the masses and G being the gravitational constant.
    //To calculate the force exerted on b1, simply multiply the normalized vector pointing from b1 to b2 with the
    //calculated force
    public Vector3D gravitationalForce(AstroBody body) {

        Vector3D b1Tob2 = body.position.minus(this.position);
        double F = Simulation.G * (this.mass * body.mass) / (b1Tob2.length() * b1Tob2.length());
        b1Tob2.normalize();
        return b1Tob2.times(F);
    }

    // Moves this body to a new position, according to the specified force vector 'force' exerted
    // on it, and updates the current movement accordingly.
    // (Movement depends on the mass of this body, its current movement and the exerted force)
    // Hint: see simulation loop in Simulation.java to find out how this is done
    public void move(Vector3D force) {

        Vector3D newPosition = force.times(1 / mass);
        newPosition = newPosition.plus(position);
        newPosition = newPosition.plus(movement);
        Vector3D newMovement = newPosition.minus(position);

        position = newPosition;
        movement = newMovement;
    }

    // Draws the body to the current StdDraw canvas as a dot using 'color' of this body.
    // The radius of the dot is in relation to the radius of the celestial body
    // (use a conversion based on the logarithm as in 'Simulation.java').
    // Hint: use the method drawAsDot implemented in Vector3 for this
    public void draw() {

        double logRadius = 1e9 * Math.log10(radius);
        this.position.drawAsDot(logRadius, color);
    }

    // generates random astronomical bodies up to the size of a black hole
    public AstroBody generateRandomBody() {
        double mass = Math.random()*10e32;
        double radius = Math.random()*10e4;
        Vector3D position = new Vector3D(Math.random());
        Vector3D movement = new Vector3D(Math.random());
        Color color = new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
        return new AstroBody(mass, radius, position, movement, color);
    }
}