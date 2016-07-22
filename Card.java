/**
 * A Card is either a Character, Weapon, or Room. Each card has
 * its own characteristic and as players progress through the game,
 * they will inevitably collect cards from other players.
 * <p>
 * Created by Jack on 19/07/2016.
 */
public abstract class Card {

    protected String name;

    public Card(String name){
        this.name = name;
    }

    public String getName(){ return name; }

}
