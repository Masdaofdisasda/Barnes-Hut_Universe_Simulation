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

        StdDraw.setCanvasSize(1000, 1000);
        StdDraw.setXscale(-bounds, bounds);
        StdDraw.setYscale(-bounds, bounds);

        StdDraw.clear(StdDraw.BLACK);

        StdDraw.enableDoubleBuffering();

        //Create Simulation Data
        AstroBody[] bodies = new AstroBody[n];
        for (int i = 0; i < n; i++) {
            bodies[i] = AstroBody.generateRandomBody();
        }

        Vector3[] forceOnBody = new Vector3[n];
        for (int i = 0; i < n; i++) {
            forceOnBody[i] = bodies[i].getForce();
        }

        // Simulation Loop
        while (true) {

            UniverseTree tree = new UniverseTree();
            for (int i = 0; i < n; i++) {
                tree.addBody(bodies[i]);
            }

            for (int i = 0; i < n; i++) {
                forceOnBody[i] = tree.updateForce(bodies[i]);
            }

            for (int i = 0; i < n; i++) {
                bodies[i].move(forceOnBody[i]);
            }

            // clear old positions (exclude the following line if you want to draw orbits).
            StdDraw.clear(StdDraw.BLACK);

            // draw new positions
            tree.drawSystem();
            StdDraw.show();
        }
    }
}
