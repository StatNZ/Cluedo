package GameControl;

import ecs100.*;

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
public class Room extends Card implements Board{

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

    public boolean hasSecretPassage(Game game){
        switch (getName()){
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
    public Room getSecretPassage(){
        return this.secretPassage;
    }

    /**
     * Creates a door at the specified position and returns a new door object
     * @param p
     * @return
     */
    public Door addDoor(Position p){
        Door d = new Door(this,p);
        doors.add(d);
        return d;
    }

    /**
     * Return the closest door to the current position
     */
    public Door getDoor(Position p){
        double closer = Double.MAX_VALUE;
        Door door = null;
        for (Door d: doors){
            if (d.getPos().distance(p) < closer){
                closer = d.getPos().distance(p);
                door = d;
            }
        }
        return door;
    }

    @Override
    public void setStartPosition(Board[][] board){
        int x = position.x;
        int y = position.y;
        int width = this.width;
        int height = this.height;
       for (int row=x; row<x+width; row++)
           for (int col=y; col<y+height; col++)
               board[row][col] = this;

        // set door locations
       for (Door d: this.doors){
           d.setStartPosition(board);
       }
    }

    @Override
    public void draw(){
        UI.drawRect(position.x*ratio,position.y*ratio,width*ratio,height*ratio);
        for (Door d: this.doors){
            d.draw();
        }
    }

    @Override
    public char printArray() {
        switch (getName()) {
            case "Kitchen":
                return 'K';
            case "Dining Room":
                return 'D';
            case "Hall":
                return 'H';
            case "Billiard Room":
                return 'P';
            case "Ball Room":
                return 'B';
            case "Lounge":
                return 'O';
            case "Library":
                return 'L';
            case "Study":
                return 'S';
            case "BLOCKED"://blocked toilet return poop
            case "Solution":
                return '#';
            case "Conservatory":
                return 'C';
        }
        return ' ';
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof Room){
            Room r = (Room)o;
            return this.getName().equals(r.getName()) &&
                    this.width == r.width &&
                    this.height == r.height;
        }
        return false;
    }

    public int hashCode(){
        return width+height;
    }

    public String toString(){
        return this.getName();
    }
}
