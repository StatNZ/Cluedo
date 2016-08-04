package GameControl;

/**
 * A GameControl.Character is a card that is dealt to a player in the game.
 * Each character has a name and a player is assigned a characters name.
 * No player can be the same character.
 * The solution will contain a GameControl.Character.
 * <p>
 * A character is not specified at any location
 * <p>
 * Created by Jack on 19/07/2016.
 */
public class Character extends Card {

    public Character(String name) {
        super(name);
    }

    public String toString() {
        return this.getName();
    }
}
