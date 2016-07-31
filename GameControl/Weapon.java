package GameControl;

/**
 * A GameControl.Weapon is a GameControl.Card that is dealt to a player.
 * The solution will contain a GameControl.Weapon.
 * Each weapon has a name that a player can refer to.
 *
 * A weapon is not specified at any location and just referred to as
 * a playing card.
 * <p>
 * Created by Jack on 19/07/2016.
 */
public class Weapon extends Card {

    public Weapon(String name){
        super(name);
    }

    public String toString(){
        return this.getName();
    }

}
