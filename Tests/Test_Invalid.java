package Tests;

import GameControl.Card;
import GameControl.Game;
import GameControl.Player;
import GameControl.Room;
import org.junit.Test;

import java.util.Collection;
import java.util.Set;

import static junit.framework.TestCase.fail;

/**
 * We test the functionality of our Cluedo game by trying to break it. Here we
 * attempt to sabotage the code by getting it to throw errors and doing illegal
 * logic
 *
 * Created by Jack on 6/08/2016.
 */
public class Test_Invalid {

    private Main_Tests mocks = new Main_Tests();

    /**
     * We attempt to move a player to a room that does not exist
     */
    @Test
    public void test_moveToUnknownRoom(){
        Game game = new Game();
        Player p1 = mocks.setupMockPlayer(game);

        try{
            game.movePlayer(p1,100,new Room("Mock Room"));
            fail("The room does not exist in this game");
        }catch (Exception e){

        }
    }

    /**
     * Attempt to get an incorrect card
     */
    @Test
    public void test_incorrectCard(){
        Game game = new Game();
        Player p1 = mocks.setupMockPlayer(game);
        try{
            game.getCard().getCard("something not right");
            fail();
        }catch (IllegalArgumentException e){

        }
    }

    /**
     * We attemp to make the player travel to an a path that it cannot find
     */
    @Test
    public void test_badPath(){
        Game game = new Game();
        Player p1 = mocks.setupMockPlayer(game);

        try{
            game.movePlayer(p1,100,new Room("Invalid"));
            fail();

        }catch (Exception e){ }
    }

    /**
     * Add a player that does not have a token
     */
    @Test
    public void test_addNoTokenPlayer(){
        Game game = new Game();

        try{
            game.addPlayer("John",null,0);
            fail();

        }catch (Exception e){

        }
    }
}
