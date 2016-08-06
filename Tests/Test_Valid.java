package Tests;

import GameControl.*;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static junit.framework.TestCase.fail;


/**
 * This class tests the validity of the Game Cluedo
 *
 * Created by Jack on 5/08/2016.
 */
public class Test_Valid {
    private Main_Tests mocks = new Main_Tests();

    /**
     * Test that all cards are in the game by their specified name
     */
    @org.junit.Test
    public void test_CardsAllInTheGame() {
        Game game = new Game();
        for (String name : mocks.getAllRoomNames()) {
            Card card = game.getRoom(name);
            assert Objects.equals(card.getName(), name);
        }
        for (String name : mocks.getAllCharacterNames()) {
            Card card = game.getCharacter(name);
            assert Objects.equals(card.getName(), name);
        }
        for (String name : mocks.getAllWeaponNames()) {
            Card card = game.getWeapon(name);
            assert Objects.equals(card.getName(), name);
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
        Player p1 = mocks.setupMockPlayer(game);
        
        // get the position of the door to the kitchen
        Door kitchenDoor = ((Room)game.getRoom("kitchen")).getDoor(p1.getPosition());
        game.movePlayer(p1,100,kitchenDoor.getRoom());

        assert p1.getRoom().equals(game.getRoom("Kitchen"));
    }

    /**
     * Test there are 3 cards in our solution
     */
    @Test
    public void test_solutionCards(){
        Game game = new Game();

        assert game.getSolution().size() == 3;
    }

    /**
     * Test that we have each a Character, Weapon and Room card in our solution
     */
    @Test
    public void test_solutionValidity(){
        Game game = new Game();
        Collection<Card> solution = game.getSolution();
        boolean character = false;
        boolean weapon = false;
        boolean room = false;

        for (Card c: solution){
            if (c instanceof GameControl.Character)
                character = true;
            else if (c instanceof Weapon)
                weapon = true;
            else if (c instanceof Room)
                room = true;
        }
        assert character == weapon == room == true;
    }

    /**
     * Tests that a player leaves a room
     */
    @Test
    public void test_playerLeavesRoom(){
    	Game game = new Game();

        // first put a player in a room
        Player p1 = mocks.setupMockPlayer(game);

        // get the position of the door to the kitchen
        Door kitchenDoor = ((Room)game.getRoom("kitchen")).getDoor(p1.getPosition());
        game.movePlayer(p1,100,kitchenDoor.getRoom());

        assert p1.getRoom() == kitchenDoor.getRoom();

        // remove the player from the room
        game.movePlayer(p1,2,(Room)game.getRoom("study"));

        assert p1.getRoom() == null;
    }

    /**
     * Test that two players which is an even number contain the same number
     * of cards each
     */
    @Test
    public void test_playersCardsEven() {
        Game game = new Game();
        List<Player> players = mocks.setupTwoMockPlayers(game);
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

        try{
            game.createPlayer("John", Player.Token.MissScarlett,21);

        }catch (IllegalArgumentException e){
            fail();
        }
    }

}
