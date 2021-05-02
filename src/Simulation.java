import com.sun.source.tree.WhileLoopTree;

import java.awt.*;

public class Simulation {

    // gravitational constant
    public static final double G = 6.6743e-11;

    // one astronomical unit (AU) is the average distance of earth to the sun.
    public static final double AU = 150e9;

    // simulation boundaries
    public static final double bounds = 5e10;

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

        int numOfAstronomicalBodies = 10000;
        for (int i = 0; i < numOfAstronomicalBodies; i++) {
            observableUniverse.addBody(AstroBody.generateRandomBody());
        }

        // Simulation Loop
        while (true) {

            //todo



            observableUniverse.drawSystem();
            StdDraw.show();
        }
    }

}
