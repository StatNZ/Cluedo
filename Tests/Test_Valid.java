package Tests;

import GameControl.Card;
import GameControl.Door;
import GameControl.Game;
import GameControl.Player;
import org.junit.Test;

import java.util.List;

/**
 * Created by Jack on 5/08/2016.
 */
public class Test_Valid {
    Main_Tests mainTests = new Main_Tests();

    /**
     * Test that all cards are in the game by their specified name
     */
    @org.junit.Test
    public void test_CardsAllInTheGame() {
        Game game = new Game();
        for (String name : mainTests.getAllRoomNames()) {
            Card card = game.getRoom(name);
            assert card.getName() == name;
        }
        for (String name : mainTests.getAllCharacterNames()) {
            Card card = game.getCharacter(name);
            assert card.getName() == name;
        }
        for (String name : mainTests.getAllWeaponNames()) {
            Card card = game.getWeapon(name);
            assert card.getName() == name;
        }
    }

    /**
     * Sets up our cards and tests if there is the correct amount of cards after
     * initialisation
     */
    @Test
    public void test_numberOfCards() {
        Game game = new Game();
        Card card = game.getCard();
        assert card.getDeck().size() == 21;
    }

    /**
     * Test that a player enters the specified room.
     */
    @Test
    public void test_playerEntersRoom() {
        Game game = new Game();
        Player p1 = game.addPlayer("Jack", Player.Token.ColonelMustard, 0);
        
        // get the position of the door to the kitchen
        Door kitchenDoor = game.getRoom("kitchen").getDoor(p1.getPosition());
        
        // move the player into the kitchen
        p1.move(kitchenDoor.getPos());

        assert p1.getRoom() == game.getRoom("Kitchen");
    }
    
    /**
     * Tests that a player leaves a room
     */
    @Test
    public void test_playerLeavesRoom(){
    	
    }

    /**
     * Test that two players which is an even number contain the same number
     * of cards each
     */
    @Test
    public void test_playersCardsEven() {
        Game game = new Game();
        List<Player> players = mainTests.setupTwoMockPlayers(game);
        game.dealCards(players);

        // there are 18 cards in a deck, each player is dealt a card until there
        // is no more cards in the deck

        for (Player p : players) {
            assert p.getHand().size() == (18 / players.size());
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
