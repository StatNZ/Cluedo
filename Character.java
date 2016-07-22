/**
 * A Character is a card that is dealt to a player in the game.
 * Each character has a name and a player is assigned a characters name.
 * No player can be the same character.
 * The solution will contain a Character.
 * <p>
 * Created by Jack on 19/07/2016.
 */
public class Character extends Card {

    private String name;

    public Character(String name){
        this.name = name;
    }

    public String toString(){
        return name;
    }
}
