import com.sun.source.tree.WhileLoopTree;

import java.awt.*;

// http://arborjs.org/docs/barnes-hut

public class Simulation {

    // gravitational constant
    public static final double G = 6.6743e-11;

    // one astronomical unit (AU) is the average distance of earth to the sun.
    public static final double AU = 150e9;

    // simulation boundaries
    public static final double bounds = 5e10;

    // Number of astronomical bodies
    public static final int n = 10000;

    // Barnes-Hut Threshold
    public static final int T = 1;

    // Debug mode
    public static final boolean debug = false;



    public static void main(String[] args) {
        //TODO: please use this class to run your simulation

        StdDraw.setCanvasSize(1000,1000);
        StdDraw.setXscale(-bounds, bounds);
        StdDraw.setYscale(-bounds, bounds);
        StdDraw.enableDoubleBuffering();
        StdDraw.clear(StdDraw.BLACK);

        //Create Simulation Data
        UniverseTree observableUniverse = new UniverseTree();

        for (int i = 0; i < n; i++) {
            observableUniverse.addBody(AstroBody.generateRandomBody());
        }
        observableUniverse.updateCenterOfMass();


        double seconds = 0;

        // Simulation Loop
        while (true) {

            //todo

            seconds++; // each iteration computes the movement of the celestial bodies within one second.

            // for each body (with index i): compute the total force exerted on it.
            /*for (int i = 0; i < SolarSystem.size(); i++) {
                SolarSystem.get(i).MyResetForce();
                for (int j = 0; j < SolarSystem.size(); j++) {
                    if (i == j) continue;
                    Vector3 forceToAdd = SolarSystem.get(i).gravitationalForce(SolarSystem.get(j));
                    SolarSystem.get(i).MyUpdateForce(forceToAdd);
                }
            }*/
            // now SolarSystem.get(i).force holds the force vector exerted on body with index i.

            // for each body (with index i): move it according to the total force exerted on it.
            /*for (int i = 0; i < SolarSystem.size(); i++) {
                SolarSystem.get(i).move();
            }*/

            // show all movements in StdDraw canvas only every 3 hours (to speed up the simulation)
            if (seconds % (3 * 3600) == 0) {
                // clear old positions (exclude the following line if you want to draw orbits).
                StdDraw.clear(StdDraw.BLACK);

                // draw new positions
                observableUniverse.drawSystem();

                StdDraw.show();
            }
        }
    }

}
