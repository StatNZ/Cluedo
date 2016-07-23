/**
 * Node is used for our BFS
 *
 * Created by Jack on 21/07/2016.
 */
public class Node {

    private Position position;

    public Node parent;

    public Node(Node parent, Position p){
        this.position = p;
        this.parent = parent;
    }

    public Position getPos(){ return this.position; }

    @Override
    public boolean equals(Object o) {
       if (o instanceof Node){
           Node n = (Node)o;
           return this.position.equals(n.position);
       }
       return false;
    }

    @Override
    public int hashCode() {
        return position.x* position.y;
    }
}
