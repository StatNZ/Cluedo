import javax.print.attribute.standard.PrinterLocation;
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
    private static List<Player> excludedPlayers;

    private static int displayCount = 0;

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
                int num = Integer.parseInt(v);
                if (num < 2 || num > 6){
                    System.out.println("Incorrect number of players entered");
                    throw new IOException();
                }
                return Integer.parseInt(v);
            } catch (IOException e) {
                System.out.println("Please enter number!");
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
     * A detailed list of the players options
     */
    private static void displayPlayersOptions(){
        System.out.println("Player options");
        System.out.println("move: Move player on the board toward a room");
        System.out.println("accuse: Accuse a player of the murder:");
        System.out.println("list: Show cards collected so far");
        System.out.println("options: Shows these options again");
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
                    suggestOptions(player,game,false);
                }// else no, so list players options
            }
        }

        // ask player to make their choice
        String option = inputString("[move/accuse/list/options]");
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
                suggestOptions(player,game,true);
                return;

            // list all the cards that have been collected aswell as your own since
            // the beginning of the game
            case "list":
                System.out.println("\nListing cards in your inventory");
                System.out.println("*********************************");
                System.out.println(player.printInventory());
                System.out.println("*********************************");
                playerOptions(player,nmove,game); // loop back until the player chooses a valid option
                return;

            // display a detailed list of players options
            case "options":
                displayPlayersOptions();
                playerOptions(player,nmove,game);
                return;

            // let the player retry any number of times!
            default:
                System.out.println("Incorrect command entered");
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
                //int inputSteps = inputNumber("You can choose x amount of steps less than your dice amount");
                Room room = game.getRoom(inputRoom);
                game.movePlayer(player, nmove, room);
                if (player.getRoom() != null)
                    suggestOptions(player,game,false);
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
    public static void suggestOptions(Player player, Game game, boolean accusing){
        // player asks question
        System.out.println("********************");
        System.out.println("Make a suggestion/accusation");
        String person = inputString("Was it character?");
        String tool = inputString("With the weapon");
        try {
            Card room = player.getRoom();
            Card character = game.getCard(person);
            Card weapon = game.getCard(tool);
            ArrayList<Card> guess = new ArrayList<>();


            if (accusing){
                try{
                    String accuseRoom = inputString("In the room");
                    Room r = game.getRoom(accuseRoom);
                    accuseOptions(player,game,character,weapon,r);
                    return;
                }catch (IllegalArgumentException e){
                    System.out.println(e.getMessage());
                    suggestOptions(player,game,true);
                }
                return;
            }

            guess.add(room);
            guess.add(character);
            guess.add(weapon);

            System.out.println();
            System.out.println("Player "+player.getName()+" who is "+player.getToken().name()+" asks...");
            System.out.printf("Was it %1s with the %1s in the %1s\n\n",character,weapon,room);

            // now we check each player beginning to the left has a card
            int start = players.indexOf(player);
            // check left direction of player
            if (revealCard(guess,player,start+1,players.size()))
                return;
            // now check the other left of the player
            else if (revealCard(guess,player,0,start)){
                return;
            }
            System.out.println("No player revealed a card, put on your poker face :)");

        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            suggestOptions(player,game,false); //repeat until the player spells it right!!
        }
    }

    /**
     * Asks every player from start to end to reveal a card if they have it
     * @param guess
     * @param start
     * @param end
     * @return
     */
    private static boolean revealCard(ArrayList<Card> guess,Player player, int start, int end){
        for (int i=start; i<end; i++){
            Player leftPlayer = players.get(i);
            if (leftPlayer.checkCards(guess) && leftPlayer != player){
                // force leftPlayer to reveal a card to current player
                // then add it to the inventory then break method
                Card reveal = leftPlayer.pickRandomCardToReveal(guess);
                player.addCardToInventory(reveal);
                System.out.println(leftPlayer.getName()+" revealed a card to "+player.getName());

                // decide whether to give the other player the option of revealing a certain card
                // or not
                return true; //finished
            }else
                System.out.println(leftPlayer.getName()+" cannot answer");
        }
        return false; // no player in this list has any of the guess cards
    }

    /**
     * The logic for accusing another player. This is where you win the game or loose
     * @param player
     * @param game
     */
    private static void accuseOptions(Player player, Game game,Card character, Card weapon, Card room){
        // A player can accuse anywhere on the board
        // This is how a player can win the game
        if (game.getSolution().contains(character) &&
                game.getSolution().contains(weapon) &&
                game.getSolution().contains(room)){
            // the player has won the game
            System.out.println("\n***************************");
            System.out.println("CONGRATULATIONS YOU WON "+ player.getName()+"!!!");
            System.out.println("*****************************\n");
            System.out.println("Winning solution:");
            System.out.printf("It was %1s, with the %1s, in the %1s\n",character.getName(),weapon.getName(),room.getName());
            System.out.println("Game Over");
            System.exit(0);// exit the program
        }else { // the guess was incorrect therefor the player is excluded
            System.out.println("Your accusation was incorrect. You have been eliminated");
            excludedPlayers.add(player);
        }

    }

    /**
     * Use command to clear the screen
     */
    public final static void clearConsole()
    {
        try
        {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows"))
                Runtime.getRuntime().exec("cls");
            else
                Runtime.getRuntime().exec("clear");
        }catch (final Exception e) {
            System.out.println("Clear Screen failed");
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
        System.out.println("By Jack O'Brien");

        //players
        int nplayers = inputNumber("How many players? [2-6]");
        players = inputPlayers(nplayers,game);
        excludedPlayers = new ArrayList<>();

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
                // check if player is disqualified from the game
               if (!excludedPlayers.contains(player)) {
                   if (!firstTime) {
                       System.out.println("\n********************\n");
                   }
                   firstTime = false;
                   int roll = dice.nextInt(10) + 2;
                   System.out.println(player.getName() + " rolls a " + roll + ".");
                   playerOptions(player, roll, game);
                   clearConsole();
                   turn++;
               }else //player has been disqualified
                    // check to see if we can still play the game
                    if (excludedPlayers.size() == players.size() ||
                            excludedPlayers.size() == players.size()-1){// second argument says we can't play with only 1 player
                        System.out.println("There is no winner");
                        System.out.println("The solution is:\n"+game.printSolution());
                        System.out.println("Game Over");
                        return;
                    }
            }

        }

    }

}
