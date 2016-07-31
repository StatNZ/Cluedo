
package Cluedo_Tests;


import org.junit.Test;
import GameControl.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Test methods to test the validity of GameControl.Game class
 * Created by Jack on 27/07/2016.
 */
public class Game_Test {



    public List<String> getAllRoomNames(){
        List<String> cardNames = new ArrayList<>();
        cardNames.add("Kitchen");
        cardNames.add("Lounge");
        cardNames.add("Conservatory");
        cardNames.add("Dining Room");
        cardNames.add("Study");
        cardNames.add("Billiard Room");
        cardNames.add("Hall");
        cardNames.add("Library");
        cardNames.add("Ball Room");
        return cardNames;
    }

    public List<String> getAllCharacterNames() {
        List<String> cardNames = new ArrayList<>();
        cardNames.add("Miss Scarlett");
        cardNames.add("Colonel Mustard");
        cardNames.add("Mr. Green");
        cardNames.add("Mrs. White");
        cardNames.add("Mrs. Peacock");
        cardNames.add("Professor Plum");
        return cardNames;
    }

    public List<String> getAllWeaponNames() {
        List<String> cardNames = new ArrayList<>();
        cardNames.add("Dagger");
        cardNames.add("Candlestick");
        cardNames.add("Revolver");
        cardNames.add("Lead Pipe");
        cardNames.add("Spanner");
        cardNames.add("Rope");
        return cardNames;
    }

    public Player setupMockPlayer1(Game game){
        return game.addPlayer("jack", Player.Token.Scarlett);
    }
}
