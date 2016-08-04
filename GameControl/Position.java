package GameControl;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to define a position on the board.
 * <p>
 * Created by Jack on 20/07/2016.
 */
public class Position {

    public final int x;
    public final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Create a List of positions in all the possible directions
     * we can travel
     *
     * @return List of positions
     */
    public List<Position> getNeighbours() {
        List<Position> children = new ArrayList<>();
        Position up = new Position(x, y - 1);
        Position down = new Position(x, y + 1);
        Position left = new Position(x - 1, y);
        Position right = new Position(x + 1, y);

        if (up.isFree()) children.add(up);
        if (down.isFree()) children.add(down);
        if (left.isFree()) children.add(left);
        if (right.isFree()) children.add(right);

        return children;
    }

    /**
     * The hypotenuse between two positions on our board
     *
     * @param p
     * @return
     */
    public double distance(Position p) {
        return Math.hypot(this.x - p.x, this.y - p.y);
    }

    /**
     * Check if we are within our bounding box
     *
     * @return
     */
    public boolean isFree() {
        return !(this.x < 0 || this.y > 24 || this.x > 24 || this.y < 0);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Position) {
            Position p = (Position) o;
            return this.x == p.x && this.y == p.y;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
