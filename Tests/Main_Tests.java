package Tests;

import GameControl.Game;
import GameControl.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Mainly just a helper method for setting up mock players and cards
 *
 * Created by Jack on 5/08/2016.
 */
class Main_Tests {

    List<String> getAllRoomNames() {
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

    List<String> getAllCharacterNames() {
        List<String> cardNames = new ArrayList<>();
        cardNames.add("Miss Scarlett");
        cardNames.add("Colonel Mustard");
        cardNames.add("Mr Green");
        cardNames.add("Mrs White");
        cardNames.add("Mrs Peacock");
        cardNames.add("Professor Plum");
        return cardNames;
    }

    List<String> getAllWeaponNames() {
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
     * Sets up a mock player within our game.
     */
    Player setupMockPlayer(Game game){
        return game.createPlayer("Jack", Player.Token.MrGreen,1);
    }

    /**
     * Helper Method to setup two mock players
     */
    List<Player> setupTwoMockPlayers(Game game) {
        List<Player> players = new ArrayList<>();
        players.add(game.createPlayer("jack", Player.Token.MissScarlett, 0));
        players.add(game.createPlayer("john", Player.Token.MrGreen, 1));
        return players;
    }

}
