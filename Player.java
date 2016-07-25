import com.sun.istack.internal.NotNull;
import ecs100.UI;

import java.awt.*;
import java.util.*;
import java.util.List;

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
        Scarlett,
        Plum,
        Green,
        White,
        Peacock,
        Mustard
    }

    // the players position on the board
    private Position pos;

    private List<Card> hand; // our given cards at the beginning the ones we show to other players
    private Set<Card> inventory; //all cards seen and owned by current player
    private Token token; // the token a player will play during the game
    private String name;

    private Room room; // the current room the player is in

    public Player(String name, Token token, int x, int y){
        this.name = name;
        this.token = token;
        this.pos = new Position(x,y);
        hand = new ArrayList<>();
    }

    /********************
     *      GETTERS     *
     ********************/

    public Room getRoom(){ return this.room; }
    public Position getPos(){ return this.pos; }
    public String getName(){ return this.name; }
    public Player.Token getToken(){ return this.token; }

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
     * List all cards currently in your hand in String format
     * @return
     */
    public String printInventory(){
        String hand = "";
        for (Card c: inventory){
            hand = hand + c.getName()+"\n";
        }
        return hand;
    }

    /**
     * These are the cards that we collect from other players during the game
     */
    public void addCardToInventory(Card card){
        inventory.add(card);
    }

    /**
     * These are the cards that are dealt to us in the beginning of the game
     * @param card
     */
    public void addCardToStart(Card card){
        hand.add(card);
        // setup inventory
        inventory = new HashSet<>(hand);
    }

    /**
     * Check our inventory for at least one card within the given list
     * @param guess = list of cards
     * @return
     */
    public boolean checkCards(ArrayList<Card> guess){
        for (Card c: guess){
            if (hand.contains(c))
                return true;
        }
        return false;
    }

    /**
     * This method will return the first instance of a card in their hand that is
     * the same as a card in the guess
     * @return
     */
    public Card pickRandomCardToReveal(List<Card> guess){
        for (Card pC: this.hand){
            if (guess.contains(pC))
                return pC;
        }
        throw new IllegalArgumentException("Revealing card has returned null. Bad behaviour");
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

    @Override
    public void setStartPosition(Board[][] board) {
        board[pos.x][pos.y] = this;
    }

    @Override
    public void draw() {
        if (this.token == Token.Scarlett){
            UI.setColor(Color.RED.brighter());
        }if (this.token == Token.Mustard)
            UI.setColor(Color.YELLOW.darker());
        if (this.token == Token.Green)
            UI.setColor(Color.GREEN);
        if (this.token == Token.Peacock)
            UI.setColor(Color.CYAN);
        if (this.token == Token.White)
            UI.setColor(Color.GRAY);
        if (this.token == Token.Plum)
            UI.setColor(Color.magenta);
        UI.fillRect(pos.x * ratio, pos.y * ratio, ratio, ratio); //.etc
        UI.setColor(Color.black);
    }

    public String toString(){
        return name;
    }
}
