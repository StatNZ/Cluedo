import ecs100.UI;

/**
 * The Door class is used to enter a Room. A player will walk
 * on top of a door and must choose to enter or pass by.
 * <p>
 * Created by Jack on 19/07/2016.
 */
public class Door implements Board{

    // because they are final we can make them public
    public final int x;
    public final int y;

    private Room room;

    public Door(int x, int y, Room room) {
        this.x = x*ratio;
        this.y = y*ratio;
        this.room = room;
    }

    @Override
    public void setStartPosition(Board[][] board) {
        int x = this.x/ratio;
        int y = this.y/ratio;
        board[x][y] = this;
    }

    @Override
    public void draw() {
        UI.fillRect(x,y,ratio,ratio);
    }
}
