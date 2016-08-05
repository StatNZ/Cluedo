package Tests;

import GameControl.Game;
import GameControl.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack on 5/08/2016.
 */
public class Main_Tests {

    public List<String> getAllRoomNames() {
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
        cardNames.add("Mr Green");
        cardNames.add("Mrs White");
        cardNames.add("Mrs Peacock");
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

    /**
     * Helper Method to setup two mock players
     *
     * @param game
     * @return
     */
    public List<Player> setupTwoMockPlayers(Game game) {
        List<Player> players = new ArrayList<>();
        players.add(game.addPlayer("jack", Player.Token.MissScarlett, 0));
        players.add(game.addPlayer("john", Player.Token.MrGreen, 1));
        return players;
    }

}
