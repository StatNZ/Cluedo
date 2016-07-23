import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * This class is the main access for the user to play the game Cluedo.
 * It controls players actions and contains some of the games logic
 *
 * Created by Jack on 23/07/2016.
 */
public class TextClient {

    /**
     * Get integer from System.in
     *
     * Author: DJP
     */
    private static int inputNumber(String msg) {
        System.out.print(msg + " ");
        while (1 == 1) {
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    System.in));
            try {
                String v = input.readLine();
                return Integer.parseInt(v);
            } catch (IOException e) {
                System.out.println("Please enter a number!");
            }
        }
    }

    /**
     * Get string from System.in
     *
     * Author: DJP
     */
    private static String inputString(String msg) {
        System.out.print(msg + " ");
        while (1 == 1) {
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    System.in));
            try {
                return input.readLine();
            } catch (IOException e) {
                System.out.println("I/O Error ... please try again!");
            }
        }
    }



    /**
     * Create set number of players
     *
     */
    private static List<Player> inputPlayers(int nplayers, Game game){
        List<Player> players = new ArrayList<>();

        // A player cannot have the same token as another player
        ArrayList<Player.Token> tokens = new ArrayList<Player.Token>();
        for(Player.Token t : Player.Token.values()) {
            tokens.add(t);
        }

        for (int i=0; i != nplayers; i++){
            String name = inputString("Player #" + i + " name?");
            String tokenName = inputString("Player #" + i + " token?");
            Player.Token token = Player.Token.valueOf(tokenName);
            while (!tokens.contains(token)){
                
            }
        }
    }

    /**
     * Access point to the game
     * @param args
     */
    public static void main(String[] args){
        Game game = new Game();

        System.out.println("*********************************");
        String art = "   ________               __    \n" +
                "  / ____/ /_  _____  ____/ /___ \n" +
                " / /   / / / / / _ \\/ __  / __ \\\n" +
                "/ /___/ / /_/ /  __/ /_/ / /_/ /\n" +
                "\\____/_/\\__,_/\\___/\\__,_/\\____/ \n" +
                "                                ";
        System.out.println(art);
        System.out.println("*********************************");

        // intro to the game
        System.out.println("Welcome to Cluedo!");
        System.out.println("By by Jack O'Brien");

        //players
        int nplayers = inputNumber("How many players? ");
        List<Player> players = inputPlayers(nplayers,game);

    }

}
