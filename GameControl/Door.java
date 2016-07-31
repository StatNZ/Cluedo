package GameControl;

import ecs100.*;

/**
 * The GameControl.Door class is used to enter a GameControl.Room. A player will walk
 * on top of a door and must choose to enter or pass by.
 * <p>
 * Created by Jack on 19/07/2016.
 */
public class Door implements Board{

    private Position position;
    private Room room;

    public Door(Position pos, Room room) {
        this.position = pos;
        this.room = room;
    }

    public Position getPos(){ return this.position; }
    public Room getRoom(){ return this.room; }

    @Override
    public void setStartPosition(Board[][] board) {
        board[position.x][position.y] = this;
    }

    @Override
    public void draw() {
        UI.fillRect(position.x*ratio,position.y*ratio,ratio,ratio);
    }
}
