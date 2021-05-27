public class UniverseTree {

    // speichert Baumstruktur
    private AstroBody root; // darf nur belegt sein, wenn der Baum ein Blatt ist
    private UniverseTree[] children; // stellt die 8 Kindsknoten des Octrees dar
    private UniverseTree parent; // übergeordneter Knoten, wird für das Traversieren und Positionsupdaten benötigt

    // speichert Simulationsdaten
    private Vector3 center; // gibt die Koordinaten an, nachdem die 8 Unterknoten eingeteilt werden
    private int depth; // gibt die Tiefe des Knotens an, wird für die bounds Berechnung benötigt
    private Vector3 centerOfMass; // gibt das gewichtete Zentrum des Gesamtgewichts an
    private double totalMass; // gibt das Gesamtgewicht an
    private int  count; // number of bodies in this tree;

    //--------------------------------------------------------------------------------------------------------------//

    // Erzeugt einen neuen, leeren Baum mit dem Ursprung des Koordinatensystems
    public UniverseTree() {
        center = new Vector3(0, 0, 0);
        depth = 0;
        totalMass = 0;
        count = 0;
    }

    // Erzeugt einen neuen Unterbaum bei kind index mit body, neuer Tiefe und neuem center
    private UniverseTree(AstroBody body, int index, UniverseTree parentNode) {
        root = body;
        parent = parentNode;
        depth = parent.depth + 1;
        center = parent.center.split(index, depth);
        parent.children[index] = this;
        totalMass = body.getMass();
        centerOfMass = body.getPosition();
        count = 1;
    }

    //--------------------------------------------------------------------------------------------------------------//

    // Fügt einen neuen body in den Baum ein
    // returns false wenn body null, out of bounds oder schon vorhanden ist
    // returns true wenn der body hinzugefügt wurde
    public boolean addBody(AstroBody body) {

        // body ist leer oder out of bounds
        if (body == null) {
            return false;
        }
        if (body.outOfBounds()) {
            return false;
        }

        //Baum ist leer -> erster Eintrag im Baum
        if (root == null && children == null) {
            root = body;
            totalMass += body.getMass();
            count++;
            centerOfMass = body.getPosition();
            return true;

            // Baum enthält einen body / ist ein Blatt
        } else if (root != null && children == null) {

            // body schon vorhanden
            if (root.equals(body)) {
                return false;
            }


            // 1 - füge neuen body in einem Unterbaum ein
            int index = body.checkIndex(center);
            children = new UniverseTree[8];
            new UniverseTree(body, index, this);
            totalMass += body.getMass();
            count++;


            // 2 - verschiebe root body in einen Unterbaum
            AstroBody removedBody = root;
            root = null;
            totalMass -= removedBody.getMass();
            count--;
            return addBody(removedBody) && updateCenterOfMass();

            // Baum enthält Unterbäume / Nachfolger
        } else if (root == null && children != null) {

            // füge neuen body in einem Unterbaum ein
            int index = body.checkIndex(center);

            // Unterbaum ist schon besetzt
            if (children[index] != null) {
                totalMass += body.getMass();
                count++;
                return children[index].addBody(body) && updateCenterOfMass();

            // Unterbaum ist frei
            } else {
                children[index] = new UniverseTree(body, index, this);
                totalMass += body.getMass();
                updateCenterOfMass();
                count++;
                return true;
            }
        }

        System.out.println("this should not happen");
        return false;
    }


    public boolean updateCenterOfMass(){

        // berechne für diesen Knoten den Schwerpunkt
        if (children != null) {
            int j = 0;

            // zähle Kinder
            for (int i = 0; i < 8; i++) {
                if (children[i] != null) {
                    j++;
                }
            }
            double[] masses = new double[j];
            Vector3[] positions = new Vector3[j];
            j=0;

            // sammle massen und Mittelpunkte
            for (int i = 0; i < 8; i++) {
                if (children[i] != null) {
                    masses[j] = children[i].totalMass;
                    positions[j] = children[i].centerOfMass;
                    j++;
                }
            }
            centerOfMass = Vector3.weightedPosition(masses, positions);
        }

        // rekursiver Aufruf bis zur Wurzel
        if (parent != null){
            return parent.updateCenterOfMass();
        }

        return true;
    }

    public void addSolarSystem(){
        Vector3 center = Vector3.generatePosition(Math.random());
        addBody(AstroBody.generateSunBody(center));
        for (int i = 0; i < 8; i++) {
            addBody(AstroBody.generateOrthoBody(center));
        }
    }

    // Zeichnet root und wenn vorhanden alle children darunter
    public void drawSystem() {

        if (root != null) {
            root.draw();
        }
        if (Simulation.debug) {
            center.drawAsLine(depth);
        }
        if (children != null) {
            for (int i = 0; i < 7; i++) {
                if (children[i] != null) {
                    children[i].drawSystem();
                }
            }
        }
    }

    // gibt die Anzahl an Bodies zurück
    public int getCount(){return count;}

    // Baut den Octree neu auf
    public UniverseTree rebuild(UniverseTree rebuiltTree) {

        if (root != null) {
            rebuiltTree.addBody(root);
        }
        if (children != null) {
            for (int i = 0; i < 7; i++) {
                if (children[i] != null) {
                    children[i].rebuild(rebuiltTree);
                }
            }
        }
        return rebuiltTree;
    }

    //barnes Hut for calculating Force
    public Vector3 updateForce(AstroBody body) {

        //leerer Baum | root == body
        if (root == null && children == null || this.root == body || body == null)
            return null;

        //Blatt
        if (root != null && children == null) {
            return root.gravitationalForce(body);
        }


        //Unterbäume
        else if (children != null && root == null) {
            //s is width of the region represented by this root
            //d is the distance between the body and the node’s center-of-mass
            double s = Simulation.bounds / (depth + 1);
            double d = body.getPosition().distanceTo(centerOfMass);

            if ((s / d) < Simulation.T) {
                Vector3 b1Tob2 = center.minus(body.getPosition());
                double F = Simulation.G * (totalMass * body.getMass()) / (b1Tob2.length() * b1Tob2.length());
                b1Tob2.normalize();
                return b1Tob2.times(F);
            }

            else  {
                for (int i = 0; i < 7; i++) {
                    if (children[i] != null) {
                        return children[i].updateForce(body);
                    }
                }
            }
        }

        //should not happen
        return null;
    }


}



