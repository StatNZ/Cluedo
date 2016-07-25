import ecs100.*;

import java.util.Set;

/**
 * A room is a location on the Board as well as a Card. A room
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
    private final int width;
    private final int height;

    // We will only set one door for each room in the meantime
    // but some rooms have multiple doors
    private Door door;

    private Set<Door> doors;

    // some rooms have secret passages
    private Room secretPassage;

    public Room(String name, int x, int y, int width, int height) {
        super(name);
        this.position = new Position(x,y);
        this.height = height;
        this.width = width;
    }

    public void setDoorLocation(int x, int y) {
        door = new Door(new Position(x,y),this);
    }
    public Door getDoor(){return this.door; }

    public boolean hasSecretPassage(){
        return secretPassage != null;
    }
    public Room getSecretPassage(){
        return this.secretPassage;
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
        door.setStartPosition(board);
    }

    /**
     * We don't ever want to use this method
     *
     * @param board
     * @param pos
     */
    @Override
    public void move(Board[][] board, Position pos) {

    }

    @Override
    public void draw(){
        UI.drawRect(position.x*ratio,position.y*ratio,width*ratio,height*ratio);
        door.draw();
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof Room){
            Room r = (Room)o;
            return this.name.equals(r.name) &&
                    this.width == r.width &&
                    this.height == r.height;
        }
        return false;
    }

    public int hashCode(){
        return width+height;
    }

    public String toString(){
        return name;
    }
}
