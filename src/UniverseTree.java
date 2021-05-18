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

    //--------------------------------------------------------------------------------------------------------------//

    // Erzeugt einen neuen, leeren Baum mit dem Ursprung des Koordinatensystems
    public UniverseTree() {
        center = new Vector3(0, 0, 0);
        depth = 0;
        totalMass = 0;
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


            // 2 - verschiebe root body in einen Unterbaum
            AstroBody removedBody = root;
            root = null;
            totalMass -= removedBody.getMass();
            return addBody(removedBody);

            // Baum enthält Unterbäume / Nachfolger
        } else if (root == null && children != null) {

            // füge neuen body in einem Unterbaum ein
            int index = body.checkIndex(center);

            // Unterbaum ist schon besetzt
            if (children[index] != null) {
                totalMass += body.getMass();
                return children[index].addBody(body);

                // Unterbaum ist frei
            } else {
                children[index] = new UniverseTree(body, index, this);
                totalMass += body.getMass();
                return true;
            }
        }

        System.out.println("this should not happen");
        return false;
    }

    public Vector3 updateCenterOfMass() {

        // Knoten ist ein Blatt
        if (root != null) {
            return centerOfMass = root.getPosition();

            // Knoten hat Kinder
        } else {

            // berechne rekursiv die Schwerpunkte der inneren Knoten
            for (int i = 0; i < 8; i++) {
                if (children[i] == null) {
                    children[i].updateCenterOfMass();
                }
            }

            // berechne für diesen Knoten den Schwerpunkt
            double[] masses = new double[8];
            Vector3[] positions = new Vector3[8];
            int j = 0;

            // sammle Massen und Mittelpunkte
            for (int i = 0; i < 8; i++) {
                if (children[i] != null) {
                    masses[j] = children[i].totalMass;
                    positions[j] = children[i].centerOfMass;
                    j++;
                }
            }

            return centerOfMass = Vector3.weightedPosition(masses, positions);

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


    //barnes Hut for calculating Force
    public Vector3 updateForce(AstroBody body) {

        //first attempt

        //empty body or empty tree
        if (root == null && children == null || this.root == body || body == null)
            return null;

        double s = Simulation.bounds / (depth +1);
        double d = body.getPosition().distanceTo(centerOfMass);

        if ((s/d) < Simulation.T) {
             Vector3 a = center.minus(body.getPosition());
             double l = a.length();
             a.normalize();
             double force = Simulation.G * totalMass * body.getMass() / (l * l);
             return a.times(force);

        } else if (children!= null){
            Vector3 j = new Vector3();

            for (int i = 0; i < 8; i++) {
                if (children[i] != null) {
              j = j.plus(children[i].updateForce(body));

                }
            }
            return j;

            //return children[1].updateForce(body).plus(children[2].updateForce(body)).plus(children[3].updateForce(body)).plus(children[4].updateForce(body)).plus(children[5].updateForce(body)).plus(children[6].updateForce(body)).plus(children[7].updateForce(body));
        }

       /* //leerer Baum | root == body
        if (root == null && children == null || this.root == body)
            return null;

        //Blatt
        if (root != null && children == null) {
            return body.gravitationalForce(root);
        }

        //Unterbäume
        if (children != null && root == null) {

            //s is width of the region represented by this root
            //d is the distance between the body and the node’s center-of-mass
            double s = Simulation.bounds/(depth +1);
            double d = body.getPosition().distanceTo(centerOfMass);

            if ((s / d) < Simulation.T) {
                return body.gravitationalForce(root);
            }

            if ((s / d) > Simulation.T) {
                for (int i = 0; i < 7; i++) {
                    if (children[i] != null) {
                        return children[i].updateForce(body);
                    }
                }
            }
        }


        //should not happen
        return null;*/

        Vector3 v = new Vector3();
        return v;

    }


}

