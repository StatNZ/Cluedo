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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;

        Card card = (Card) o;

        return name != null ? name.equals(card.name) : card.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString(){
        return name;
    }
}
