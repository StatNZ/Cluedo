package GameControl;


/**
 * The GameControl.Door class is used to enter a GameControl.Room. A player will walk
 * on top of a door and must choose to enter or pass by.
 * <p>
 * Created by Jack on 19/07/2016.
 */
public class Door implements Board {

    private Position position;
    private Room room;

    public Door(Room room, Position position) {
        this.room = room;
        this.position = position;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            System.out.println("char " + i + " " + (char) (i));
        }
    }

    public Position getPos() {
        return this.position;
    }

    public Room getRoom() {
        return this.room;
    }

    @Override
    public void setStartPosition(Board[][] board) {
        board[position.x][position.y] = this;
    }

    @Override
    public void draw() {
        //UI.fillRect(position.x * ratio, position.y * ratio, ratio, ratio);
    }

    @Override
    public char printArray() {
        return '\37'; //37
    }
}
