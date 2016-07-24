import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * This class is the main access for the user to play the game Cluedo.
 * It controls players actions and contains some of the games logic
 *
 * Created by Jack on 23/07/2016.
 */
public class TextClient {

    private static List<Player> players;

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
    public static String inputString(String msg) {
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
     * Author: DJP
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

            // Print a list of available tokens to the user(s)
            System.out.println("List of tokens");
            for (Player.Token pt: tokens){
                System.out.println(pt.toString());
            }

            String tokenName = inputString("Player #" + i + " token?");
            Player.Token token = Player.Token.valueOf(tokenName);
            while (!tokens.contains(token)){
                System.out.print("Invalid token!  Must be one of: ");
                boolean firstTime = true;
                for (Player.Token t : Player.Token.values()) {
                    if (!firstTime) {
                        System.out.print(", ");
                    }
                    firstTime = false;
                    System.out.print("\"" + t + "\"");
                }
                System.out.println();
                tokenName = inputString("Player #" + i + " token?");
                token = Player.Token.valueOf(tokenName);
            }
            tokens.remove(token);
            players.add(game.addPlayer(name,token));
        }
        return players;
    }

    /**
     * A list of all the players options
     *
     * @param player
     * @param game
     */
    private static void playerOptions(Player player, int nmove, Game game){

        // check to see if player is in a room at the beginning of their turn
        Room room = player.getRoom();
        if (room != null){
            // check if the room contains a secret passage and see if the player
            // would like to use it.
            if (room.hasSecretPassage()){
                String input = inputString("This room leads to "+room.getSecretPassage().getName()+"\n" +
                        "Would you like to go there now? (y/n)");
                if (input.toLowerCase().contains("y")){
                    player.enterRoom(room.getSecretPassage());
                    suggestOptions(player,game);
                }// else no, so list players options
            }
        }

        // list player options
        System.out.println("Player options");
        System.out.println("move: Move player on the board to a room");
        System.out.println("accuse: Accuse a player of the murder:");
        System.out.println("list: Show cards collected so far");

        // ask player to make their choice
        String option = inputString("[move/accuse/list]");
        switch (option.toLowerCase()) {
            // move [player can only move less or equal to the dice roll]
            // player must choose a room they wish to move close towards
            case "move":
                player.leaveRoom();
                moveOptions(player,nmove,game);
                return;

            // accuse [if you unsuccessfully accuse you are eliminated from the game]
            // player can accuse anyone anywhere on the board
            case "accuse":
                //debug
                System.out.println("Player "+player.getName()+" accuses");
                // the problem with accuse is that you will be eliminated from the
                // game if you are unsuccessful
                return;

            // list all the cards that have been collected aswell as your own since
            // the beginning of the game
            case "list":
                System.out.println("*********************************");
                System.out.println("\nListing cards in your inventory");
                System.out.println(player.getHand());
                System.out.println("*********************************");
                playerOptions(player,nmove,game); // loop back until the player chooses a valid option
                return;


            default:
                System.out.println("Incorrect option entered");
                playerOptions(player,nmove,game); // loop again
        }
    }

    /**
     * A more controlled version of moving a player in the game
     * @param player
     * @param nmove
     * @param game
     */
    private static void moveOptions(Player player, int nmove, Game game){
        while (true){
            try {
                String inputRoom = inputString("Choose a room to move towards");
                // Todo: Player has the option to choose a number of steps less then or equal to dice roll
                //int inputSteps = inputNumber("You can choose x amount of steps less than your dice count");
                Room room = game.getRoom(inputRoom);
                game.movePlayer(player, nmove, room);
                if (player.getRoom() != null)
                    suggestOptions(player,game);
                return;
            }catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * The logic behind suggesting a killer in a specific room
     *
     * @param player
     * @param game
     */
    public static void suggestOptions(Player player, Game game){
        // player asks question
        System.out.println("********************");
        System.out.println("Make a suggestion");
        String person = inputString("Choose character");
        String weapon = inputString("Choose weapon");
        Room room = player.getRoom();

        // now each player has to reveal a card if they have it
    }

    private static void accuseOptions(Player player, Game game){

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
        System.out.println("By Jack O'Brien");

        //players
        int nplayers = inputNumber("How many players?");
        players = inputPlayers(nplayers,game);

        // add players to the board
        game.addPlayersToBoard(players);
        // deal cards to all the players
        game.dealCards(players);

        // play the game
        int turn = 1;
        Random dice = new Random();
        while (1 == 1){
            System.out.println("\n********************");
            System.out.println("***** TURN " + turn + " *******");
            System.out.println("********************\n");
            boolean firstTime = true;
            for (Player player : players) {
                if (!firstTime) {
                    System.out.println("\n********************\n");
                }
                firstTime = false;
                int roll = dice.nextInt(10) + 2;
                System.out.println(player.getName() + " rolls a " + roll + ".");
                playerOptions(player,roll,game);
                game.toString();
            }

        }

    }

}
