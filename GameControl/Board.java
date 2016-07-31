package GameControl;

/**
 * The GameControl.Board is a 25x25 playing field which has 9 rooms in certain
 * locations. Players and Rooms are dispersed upon the board in certain
 * positions and are able to freely move along the board.
 * <p>
 * The board will be drawn to a canvas so the players can physically
 * see their steps they take.
 * <p>
 * Created by Jack on 19/07/2016.
 */
public interface Board {

    /**
     * Used for scaling the board
     */
    public int ratio = 20;

    /**
     * Here we set our initial locations for players and rooms
     * on the board
     * @param board
     */
    public void setStartPosition(Board[][] board);

    /**
     * This allows us to draw our board to the canvas or text pane
     */
    public void draw();

}
