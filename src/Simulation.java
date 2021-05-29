import com.sun.source.tree.WhileLoopTree;
import java.awt.*;
// http://arborjs.org/docs/barnes-hut

public class Simulation {

    // gravitational constant
    public static final double G = 6.6743e-11;

    // simulation boundaries
    public static final double bounds = 5e10;

    // Number of astronomical bodies
    public static final int n = 10000;

    // Number of supermassive bodies
    public static final int blackHoles = 3;

    // Number of solar system
    public static final int solarSystems = 8;

    // Barnes-Hut Threshold
    public static final double T = 0.5;

    // spawns additional bodies (0 spawns nothing, 1 holds body count)
    public static final double UniverseDecay = 0;

    // Debug mode
    public static final boolean debug = false;

    // Simulation
    public static void main(String[] args) {

        // set visualization parameters
        StdDraw.setCanvasSize(1000, 1000);
        StdDraw.setXscale(-bounds, bounds);
        StdDraw.setYscale(-bounds, bounds);
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.enableDoubleBuffering();

        //Create Simulation Data
        UniverseTree observableUniverse = new UniverseTree();

        for (int i = 0; i < blackHoles; i++) {
            observableUniverse.addBody(AstroBody.generateBlackHole()); }
        for (int i = 0; i < solarSystems; i++) {
            observableUniverse.addSolarSystem(); }
        for (int i = 0; i < n; i++) {
            observableUniverse.addBody(AstroBody.generateRandomBody()); }

        double seconds = 0;

        // Simulation Loop
        while (true) {

            if (seconds % 1 == 0) {
                UniverseTree tree = new UniverseTree();
                observableUniverse = observableUniverse.rebuild(tree);

                // spawn additional bodies if desired
                int lost = (int) ((n - observableUniverse.getCount()) * UniverseDecay);
                for (int i = 0; i < lost; i++) {
                    observableUniverse.addBody(AstroBody.generateRandomBody());
                }

                for (AstroBody body : observableUniverse) {
                    body.setForce(observableUniverse.updateForce(body));
                }

                for (AstroBody body : observableUniverse){
                    body.move(body.getForce());
                }
            }



            // draw new positions
            if (seconds % 1 == 0) {
                // clear old positions (exclude the following line if you want to draw orbits).
                StdDraw.clear(StdDraw.BLACK);
                observableUniverse.drawSystem();
                StdDraw.text(Simulation.bounds * -0.9,Simulation.bounds * -0.9,"Bodies: "+ observableUniverse.getCount());
                StdDraw.show();
            }
            seconds++;
        }
    }
}
