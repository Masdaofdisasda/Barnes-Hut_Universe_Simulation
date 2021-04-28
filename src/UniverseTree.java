public class UniverseTree {

    private AstroBody root;
    private UniverseTree[] children;

    private Vector3 center;
    // todo center und bounds müssen für jeden knoten gespeichert werden, da sonst immer center - bounds gerechent wird

    public UniverseTree( AstroBody body){ root = body; }

    public UniverseTree(Vector3 centerPosition){ center = centerPosition; }

    public boolean addBody(AstroBody body){

        // body ist leer oder out of bounds
        if (body == null){ return false;}
        if (body.outOfBounds()){ return false;}

        //Baum ist leer
        if (root == null && children == null){
            root = body;
            return true;

        // Baum enthält einen body / ist ein Blatt
        }else if (root != null && children == null){

            // body schon vorhanden
            if (root == body){ return false; }

            // füge neuen body in einem Unterbaum ein
            int index = body.checkIndex(center);
            children = new UniverseTree[8];
            children[index] = new UniverseTree(body);
            children[index].center = center.split(index);

            // verschiebe root body in einen Unterbaum
            index = root.checkIndex(center);
            if (children[index] == null) {
                children[index] = new UniverseTree(root);
                children[index].center = center.split(index);
            }else { children[index].addBody(root);}

            root = null;
            return true;

        // Baum enthält Unterbäume / Nachfolger
        }else if (root == null && children != null){
            int index = body.checkIndex(center);
            if (children[index] != null) {
                return children[index].addBody(body);
            }else {
                children[index] = new UniverseTree(body);
                children[index].center = center.split(index);
            }
        }

        return false;
    }
}

