package GameControl;

import java.util.HashSet;
import java.util.Set;

/**
 * A room is a location on the GameControl.Board as well as a GameControl.Card. A room
 * can be dealt to any player in the game. The solution will contain
 * a room.
 * Each room has a name that a player can refer to.
 * The location that a room is set to will be final as the room cannot
 * change positions throughout the game
 * <p>
 * Created by Jack on 19/07/2016.
 */
public class Room extends Card implements Board {

    private Position position;
    private int width;
    private int height;

    // Setting multiple doors for a room
    private Set<Door> doors = new HashSet<>();

    // some rooms have secret passages
    private Room secretPassage;

    public Room(String name) {
        super(name);
    }

    public boolean hasSecretPassage(Game game) {
        switch (getName()) {
            case "Kitchen":
                secretPassage = game.getRoom("Study");
                break;
            case "Lounge":
                secretPassage = game.getRoom("Conservatory");
                break;
            case "Study":
                secretPassage = game.getRoom("Kitchen");
                break;
            case "Conservatory":
                secretPassage = game.getRoom("Lounge");
                break;
        }
        return secretPassage != null;
    }

    public Room getSecretPassage() {
        return this.secretPassage;
    }

    public Set<Door> getDoors() {
        return this.doors;
    }

    /**
     * Creates a door at the specified position and returns a new door object
     *
     * @param p the position on where to put the door
     * @return door at the specified position
     */
    public Door createDoor(Position p) {
        Door d = new Door(this, p);
        doors.add(d);
        return d;
    }

    /**
     * Choose the closest door relevant to the other door
     *
     * @return this will return 'this' closest door to the other door
     */
    public Door selectBestDoorToDoor(Room room) {
        // iterate through both sets finding which door of myDoor is closes to the other door
        double closest = Double.MAX_VALUE;
        Door door = null;
        for (Door myDoor : doors)
            for (Door other : room.doors) {
                double check = myDoor.getPos().distance(other.getPos());
                if (check < closest) {
                    closest = check;
                    door = myDoor;
                }
            }
        if (door != null)
            return door;
        else
            throw new IllegalArgumentException("Selecting Best Door To Door failed :(!");
    }

    /**
     * Return the closest door to the current position
     */
    public Door getDoor(Position p) {
        double closer = Double.MAX_VALUE;
        Door door = null;
        for (Door d : doors) {
            if (d.getPos().distance(p) < closer) {
                closer = d.getPos().distance(p);
                door = d;
            }
        }
        return door;
    }

    @Override
    public char printArray() {
        switch (getName()) {
            case "Kitchen":
                return 'A';
            case "Dining Room":
                return 'I';
            case "Hall":
                return 'G';
            case "Billiard Room":
                return 'D';
            case "Ball Room":
                return 'B';
            case "Lounge":
                return 'H';
            case "Library":
                return 'E';
            case "Study":
                return 'F';
            case "Conservatory":
                return 'C';
            case "INSIDE":
                return ' ';
            case "BLOCKED":
            case "Solution":
                return '#';

        }
        return ' ';
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Room) {
            Room r = (Room) o;
            return this.getName().equals(r.getName()) &&
                    this.width == r.width &&
                    this.height == r.height;
        }
        return false;
    }

    public int hashCode() {
        return width + height;
    }

    public String toString() {
        return this.getName();
    }
}
