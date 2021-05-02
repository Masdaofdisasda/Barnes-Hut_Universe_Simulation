public class UniverseTree {

    // speichert Baumstruktur
    private AstroBody root; // darf nur belegt sein, wenn der Baum ein Blatt ist
    private UniverseTree[] children; // stellt die 8 Kindsknoten des Octrees dar
    private UniverseTree parent; // übergeordneter Knoten, wird für das Traversieren und Positionsupdaten benötigt

    // speichert räumliche lage
    private Vector3 center; // gibt die Koordinaten an, nachdem die 8 Unterknoten eingeteilt werden
    private int depth; // gibt die Tiefe des Knotens an, wird für die bounds Berechnung benötigt

    //--------------------------------------------------------------------------------------------------------------//

    // Erzeugt einen neuen Unterbaum mit Knoten body
    private UniverseTree( AstroBody body){ root = body; }

    // Erzeugt einen neuen Unterbaum mit Knoten body
    private UniverseTree( AstroBody body, int index,  int depthOfParent, UniverseTree parentNode){
        root = body;
        center = parentNode.center.split(index, depth + 1);
        depth = depthOfParent + 1;
        parent = parentNode;
    }

    // Erzeugt einen neuen, leeren Baum mit dem Ursprung des Koordinatensystems
    public UniverseTree(){
        center = new Vector3(0,0,0);
        depth = 0;
    }

    // Fügt einen neuen body in den Baum ein
    // returns false wenn body null, out of bounds oder schon vorhanden ist
    // returns true wenn der body hinzugefügt wurde
    public boolean addBody(AstroBody body){

        // body ist leer oder out of bounds
        if (body == null){ return false;}
        if (body.outOfBounds()){ return false;}

        //Baum ist leer -> erster Eintrag im Baum
        if (root == null && children == null){
            root = body;
            return true;

        // Baum enthält einen body / ist ein Blatt
        }else if (root != null && children == null){

            // body schon vorhanden
            if (root.equals(body)){ return false; }


            // 1 - füge neuen body in einem Unterbaum ein
            int index = body.checkIndex(center);
            children = new UniverseTree[8];
            createSubtreeAtChild(index, body);
            //children[index] = new UniverseTree(body, index, depth, this);

            // 2 - verschiebe root body in einen Unterbaum
            AstroBody removedBody = root;
            root = null;
            return addBody(removedBody);

        // Baum enthält Unterbäume / Nachfolger
        }else if (root == null && children != null){

            // füge neuen body in einem Unterbaum ein
            int index = body.checkIndex(center);

            // Unterbaum ist schon besetzt
            if (children[index] != null) {
                return children[index].addBody(body);

            // Unterbaum ist frei
            }else { createSubtreeAtChild(index, body);
                //children[index] = new UniverseTree(body, index, depth, this);
                return true;
            }
        }

        System.out.println("this should not happen");
        return false;
    }

    // legt einen neuen Unterbaum bei kind index mit body, neuer Tiefe und neuem center an
     public void createSubtreeAtChild(int index, AstroBody body){
         children[index] = new UniverseTree(body);
         children[index].depth = depth + 1;
         children[index].center = center.split(index, depth + 1);
         children[index].parent = this;
     }

     // überprüft body auf neue Postion
    public void updatePosition(){
        // todo

    }

    public void drawSystem(){

        if (root != null){
            root.draw();
        }
        if ( Simulation.debug ){
            center.drawAsLine(depth);
        }
        if (children != null){
            for (int i = 0; i < 7; i++) {
                if (children[i] != null){
                    children[i].drawSystem();
                }
            }
        }

    }
}

