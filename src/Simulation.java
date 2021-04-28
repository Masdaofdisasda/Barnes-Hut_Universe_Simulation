public class Simulation {

    // gravitational constant
    public static final double G = 6.6743e-11;

    // simulation boundaries
    public static final double bounds = 5e10;

    public static void main(String[] args) {
        //TODO: please use this class to run your simulation

        //Create Simulation Data
        UniverseTree observableUniverse = new UniverseTree(new Vector3(0, 0, 0));

        int numOfAstronomicalBodies = 10;
        for (int i = 0; i < numOfAstronomicalBodies; i++) {
            observableUniverse.addBody(AstroBody.generateRandomBody());
        }

    }

}
