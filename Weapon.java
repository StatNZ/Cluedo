/**
 * A Weapon is a Card that is dealt to a player.
 * The solution will contain a Weapon.
 * Each weapon has a name that a player can refer to.
 * <p>
 * Created by Jack on 19/07/2016.
 */
public class Weapon extends Card {

    private String name;

    public Weapon(String name){
        this.name = name;
    }

    public String toString(){
        return name;
    }

}
