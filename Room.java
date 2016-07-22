import ecs100.*;

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

//   private final String name;
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    // We will only set one door for each room in the meantime
    // but some rooms have multiple doors
    private Door door;

    public Room(String name, int x, int y, int width, int height) {
        super(name);
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }

    public void setDoorLocation(int x, int y) {
        door = new Door(x,y,this);
    }

    public Door getDoor(){return this.door; }

    @Override
    public void setStartPosition(Board[][] board){
        int x = this.x;
        int y = this.y;
        int width = this.width;
        int height = this.height;
       for (int row=x; row<x+width; row++)
           for (int col=y; col<y+height; col++)
               board[row][col] = this;

        // set door locations
        door.setStartPosition(board);
    }

    @Override
    public void draw(){
        UI.drawRect(x*ratio,y*ratio,width*ratio,height*ratio);
        door.draw();
    }

    public boolean equals(Object o){
        if (o instanceof Room){
            Room r = (Room)o;
            return this.name == r.name;
        }
        return false;
    }

    public int hashCode(){
        return x+y+width+height;
    }

    public String toString(){
        return name;
    }
}
