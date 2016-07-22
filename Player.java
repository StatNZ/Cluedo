import ecs100.UI;

import java.util.HashSet;
import java.util.Set;

/**
 * Each player is a character in the game of cluedo. A player will
 * be assigned a character and will remain that character for the
 * duration of the game. Each player will start at their respective
 * character position.
 * <p>
 * Created by Jack on 19/07/2016.
 */
public class Player implements Board{

    // Could possibly do enums for Tokens
    public enum Token{
        MissScarlett,
        ProfessorPlum,
        MrGreen,
        MrsWhite,
        MrsPeacock,
        ColonelMustard
    }

    // Our current position on the board
    // thinking about replacing this with location
    private int x;
    private int y;

    private Set<Card> inventory; //player can never get the same card twice
    private Token token; // the token a player will play during the game
    private String name;

    private Room room; // the current room the player is in

    public Player(String name, Token token, int x, int y){
        this.name = name;
        this.token = token;
        this.x = x;
        this.y = y;
        inventory = new HashSet<>();
    }

    /********************
     *      GETTERS     *
     ********************/
    public int getX(){ return this.x; }
    public int getY(){ return this.y; }
    public Room getRoom(){ return this.room; }

    /**
     * A player enters a room
     * @param r
     */
    public void enterRoom(Room r){
        this.room = r;
    }

    /**
     * A player leaves the room
     */
    public void leaveRoom(){
        this.room = null;
    }

    /**
     * Move the player to a new position
     *
     * @param p the updated position
     */
    public void move(Position p){
        this.x = p.x;
        this.y = p.y;
    }

    /**
     * A player is dealt deck in the beginning of the game. As the game
     * progresses a player will add deck to their inventory
     */
    public void addCardToInventory(Card card){
        inventory.add(card);
    }

    /**
     * Check our inventory for the argument
     *
     * @param card
     * @return
     */
    public boolean checkCards(Card card){
        if (inventory.contains(card))
            return true;
        else
            return false;
    }

    @Override
    public void setStartPosition(Board[][] board) {
        int x = this.x;
        int y = this.y;
        board[x][y] = this;
    }

    @Override
    public void draw() {
        if (this.token == Token.MissScarlett)
            UI.drawString("S",x*ratio,y*ratio); //.etc
    }

    public String toString(){
        return name;
    }
}
