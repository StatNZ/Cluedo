import ecs100.UI;

import java.awt.*;
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

    // the players position on the board
    private Position pos;

    private Set<Card> inventory; //player can never get the same card twice
    private Token token; // the token a player will play during the game
    private String name;

    private Room room; // the current room the player is in

    public Player(String name, Token token, int x, int y){
        this.name = name;
        this.token = token;
        this.pos = new Position(x,y);
        inventory = new HashSet<>();
    }

    /********************
     *      GETTERS     *
     ********************/

    public Room getRoom(){ return this.room; }
    public Position getPos(){ return this.pos; }

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
    @Override
    public void move(Board[][] board, Position p){
        board[pos.x][pos.y] = null;
        this.pos = p;
        // update board
        board[p.x][p.y] = this;
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
        board[pos.x][pos.y] = this;
    }

    @Override
    public void draw() {
        if (this.token == Token.MissScarlett){
            UI.setColor(Color.RED.brighter());
        }if (this.token == Token.ColonelMustard)
            UI.setColor(Color.YELLOW.darker());
        if (this.token == Token.MrGreen)
            UI.setColor(Color.GREEN);
        if (this.token == Token.MrsPeacock)
            UI.setColor(Color.CYAN);
        if (this.token == Token.MrsWhite)
            UI.setColor(Color.GRAY);
        if (this.token == Token.ProfessorPlum)
            UI.setColor(Color.magenta);
        UI.fillRect(pos.x * ratio, pos.y * ratio, ratio, ratio); //.etc
        UI.setColor(Color.black);
    }

    public String toString(){
        return name;
    }
}
