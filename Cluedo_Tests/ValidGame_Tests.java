package Cluedo_Tests;

import GameControl.Card;
import GameControl.Game;
import org.junit.Test;

/**
 * Created by Jack on 31/07/2016.
 */
public class ValidGame_Tests {

    Game_Test game_test = new Game_Test();

    /**
     * Test that all cards are in the game by their specified name
     */
    @Test
    public void test_CardsAllInTheGame() {
        Game game = new Game();
        for (String name : game_test.getAllRoomNames()) {
            Card card = game.getRoom(name);
            assert card.getName() == name;
        }
        for (String name : game_test.getAllCharacterNames()) {
            Card card = game.getCharacter(name);
            assert card.getName() == name;
        }
        for (String name : game_test.getAllWeaponNames()) {
            Card card = game.getWeapon(name);
            assert card.getName() == name;
        }
    }

    /**
     * Test to see if we can add a player to the game
     *
     * @return
     */
    @Test
    public void test_playerAdded() {
        Game game = new Game();
    }
}
