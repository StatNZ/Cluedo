/**
 * Node is used for our Astar
 *
 * Created by Jack on 21/07/2016.
 */
public class Node implements Comparable<Node>{

    private Position position;

    public Node parent;

    public Node goal;

    public double hCost; //heuristic

    public Node(Node parent, Position p){
        this.position = p;
        this.parent = parent;
    }

    public Position getPos(){ return this.position; }

    /**
     * Used to determine the distance from this.position to our
     * goal node.
     * @param node
     */
    public void setGoal(Node node){
        this.goal = node;
        hCost = this.position.distance(goal.position);
    }

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

    @Override
    public int compareTo(Node o) {
        if (this.hCost > o.hCost)
            return 1;
        else if (this.hCost < o.hCost)
            return -1;
        return 0;
    }
}
