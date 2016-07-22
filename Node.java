/**
 * Node is used for our BFS
 *
 * Created by Jack on 21/07/2016.
 */
public class Node {

    public final int x;
    public final int y;

    public Node parent;

    public Node(int x, int y, Node parent){
        this.x = x;
        this.y = y;
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;

        Node node = (Node) o;

        if (x != node.x) return false;
        if (y != node.y) return false;
        return parent != null ? parent.equals(node.parent) : node.parent == null;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Node{" +
                "x=" + x +
                ", y=" + y +
                ", parent=" + parent +
                '}';
    }
}
