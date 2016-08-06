package GameControl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * This class is the main access for the user to play the game Cluedo.
 * It controls players actions and contains some of the games logic
 * <p>
 * Created by Jack on 23/07/2016.
 */
public class TextClient {

    private static List<Player> players;
    private static List<Player> excludedPlayers;

    /**
     * Get integer from System.in
     * Author: DJP
     */
    private static int inputNumber(String msg) {
        System.out.print(msg + " ");
        while (true) {
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    System.in));
            try {
                String v = input.readLine();
                int num = Integer.parseInt(v);
                if (num < 2 || num > 6) {
                    System.out.println("Incorrect number of players entered");
                    throw new IOException();
                }
                return Integer.parseInt(v);
            } catch (Exception e) {
                System.out.println("Please enter number!");
            }
        }
    }

    /**
     * Get string from System.in
     * <p>
     * Author: DJP
     */
    private static String inputString(String msg) {
        System.out.print(msg + " ");
        while (true) {
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    System.in));
            try {
                String s = input.readLine();
                checkString(s);
                return s;
            } catch (IOException e) {
                System.out.println("I/O Error ... please try again!");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Create set number of players
     * <p>
     * Author: DJP
     */
    private static List<Player> inputPlayers(int nplayers, Game game) {
        List<Player> players = new ArrayList<>();

        // A player cannot have the same token as another player
        ArrayList<Player.Token> tokens = new ArrayList<>();
        Collections.addAll(tokens, Player.Token.values());

        for (int i = 0; i != nplayers; i++) {
            String name = inputString("Player #" + i + " name?");


            // Print a list of available tokens to the user(s)
            System.out.println("\nList of tokens\n");
            for (Player.Token pt : tokens) {
                System.out.println(pt.toString());
            }
            System.out.println();

            String tokenName = "";
            Player.Token token = null;

            // loop forever until a player chooses a correct token name!!
            while (true) {
                try {
                    tokenName = inputString("Player #" + i + " token?");
                    token = Player.Token.valueOf(tokenName);
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Incorrect name! Please type the name as it is printed above");
                }
            }

            while (!tokens.contains(token)) {
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
            players.add(game.createPlayer(name, token, i+1));
        }
        return players;
    }

    /**
     * Print a detailed list of cards
     *
     * @param type deck/hand/list
     * @param toPrint
     */
    private static void printOptions(String type, String toPrint) {
        System.out.println("\nListing cards in your " + type);
        System.out.println("*********************************");
        System.out.println(toPrint);
        System.out.println("*********************************");
    }

    /**
     * A detailed list of the players options
     */
    private static void displayPlayersOptions() {
        System.out.println("Player options");
        System.out.println("move: Move player on the board toward a room");
        System.out.println("accuse: Accuse a player of the murder...");
        System.out.println("hand: Show cards dealt to you from the beginning");
        System.out.println("notepad: Show all cards in your hand and cards which have been revealed to you");
        System.out.println("more: Shows these options again");
        System.out.println("view: Print the board to the text pane");
        System.out.println("end: End your current turn!\n");
    }

    /**
     * Contains the logic for a player when it is their turn in the game. The method checks whether a player has been
     * moved to a current location or whether the current room they are in (if they are in one) has a secret passage.
     * It then offers the player options on what they would like to do for their turn
     *
     * @param player
     * @param game
     */
    private static void playerOptions(Player player, int nmove, Game game) {

        // check to see if player is in a room at the beginning of their turn
        Room room = player.getRoom();
        if (room != null) {
            // check to see player was moved to this room
            if (player.getWasMoved()) {
                // display a message to the player informing them of a secret passage
                System.out.println("You were placed in the " + room.toString() + " by another palyer");
                String input = inputString("Would you like to stay here? (y/n)");

                if (input.toLowerCase().startsWith("y")) {
                    suggestOptions(player, game, false);
                    return; // end this players turn
                } // else no player can choose other option
            }

            // check if the room contains a secret passage and see if the player would like to use it.
            if (room.hasSecretPassage(game)) {

                // display a message to the player informing them of a secret passage
                System.out.println("The " + room.toString() + " leads to " + room.getSecretPassage().getName());
                String input = inputString("Would you like to go there now? (y/n)");

                if (input.toLowerCase().startsWith("y")) {
                    player.enterRoom(room.getSecretPassage());
                    suggestOptions(player, game, false);
                    return; // end the players turn
                } // else no, so list players options
            }
        }

        // run forever or until a player selects an appropriate option
        while (true) {
            // ask player to make their choice
            String option = inputString("[move/end/accuse/notepad/more]");
            switch (option.toLowerCase()) {
                case "view":
                    System.out.println(game.printBoard(player));
                    break; // loop again

                // move [player can only move less or equal to the dice roll]
                // player must choose a room they wish to move close towards
                case "move":
                    moveCurrentPlayer(player, nmove, game);
                    return;

                // print the cards that are in your hand
                case "hand":
                    printOptions(option, player.printHand());
                    break; // loop again

                // print the cards that have been revealed by other players and that
                // are in your hand
                case "notepad":
                    printOptions(option, player.printHandAndNotepad());
                    break; // loop again

                // accuse [if you unsuccessfully accuse you are eliminated from the game]
                // player can accuse anyone anywhere on the board
                case "accuse":
                    System.out.println("Player " + player.getName() + " accuses");
                    suggestOptions(player, game, true);
                    return;

                // display a detailed list of players options
                case "more":
                    displayPlayersOptions();
                    break; // loop again

                // end this players turn
                case "end":
                    clearScreen();
                    return;

                // let the player retry any number of times!
                default:
                    System.out.println("Incorrect command entered");
                    break;
            }
        }
    }

    private static void checkString(String input) throws IllegalArgumentException {
        if (input == null || input.length() <= 0)
            throw new IllegalArgumentException("Enter a word");
    }


    /**
     * Contains the logic for a player wishing to move. Asks the player for a location/Room to move
     * towards. It then attempts to move the player to the room unless an invalid room name was entered, in
     * which case it will throw an error and ask the player to try again.
     *
     * @param player
     * @param nmove
     * @param game
     */
    private static void moveCurrentPlayer(Player player, int nmove, Game game) {
        while (true) {
            try {
                String inputRoom = inputString("Choose a room to move towards");

                Room room = decodeRoom(game, inputRoom.toLowerCase());
                if (room == null)
                    room = (Room)game.getRoom(inputRoom);

                // check if players chosen room is the same as the current room they are in
                if (room.equals(player.getRoom())) {
                    // essentially the player wants to stay in the room which they cannot
                    System.out.println("You are already in the " + player.getRoom() + ", choose another room or end your turn!");
                    String input = inputString("[move/end]");

                    if (input.contains("move")) {
                        throw new IllegalArgumentException(" "); // rerun this method
                    } else // ending his turn
                        return;

                }

                if (game.movePlayer(player, nmove, room)){
                    // the player has entered a room
                    System.out.println(player.toString()+" has just entered the "+room.getName());
                    suggestOptions(player,game,false);
                } else { // the player did not make it to their chosen room
                    clearScreen();
                    System.out.println("\n" + player.toString() + " has moved towards the " + room.toString());
                }

                return;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.out.println("Please enter a room!");
            }
        }
    }

    private static Room decodeRoom(Game game, String input) {
        String s = input.substring(0, 1);
        switch (s) {
            case "a":
                return game.getRoom("Kitchen");
            case "b":
                return game.getRoom("Ball Room");
            case "c":
                return game.getRoom("Conservatory");
            case "d":
                return game.getRoom("Billiard Room");
            case "e":
                return game.getRoom("Library");
            case "f":
                return game.getRoom("Study");
            case "g":
                return game.getRoom("Hall");
            case "h":
                return game.getRoom("Lounge");
            case "i":
                return game.getRoom("Dining Room");
            default:
                return null;
        }
    }

    /**
     * The logic behind suggesting a killer in a specific room. We cover the basis that a
     * player can suggest or accuse anyone on the board.
     *
     * @param player
     * @param game
     */
    private static void suggestOptions(Player player, Game game, boolean accusing) {
        // player asks question
        while (true) {
            System.out.println("********************");
            System.out.println("Make a suggestion/accusation");
            String person = inputString("Was it character?");
            String tool = inputString("With the weapon");
            try {

                // get the cards corresponding to the input names
                Card room = player.getRoom();
                Card character = game.getCharacter(person);
                Card weapon = game.getWeapon(tool);
                ArrayList<Card> guess = new ArrayList<>();

                // these are instructions for when we accuse
                if (accusing) {
                    try {
                        String accuseRoom = inputString("In the room");
                        Room r = (Room)game.getRoom(accuseRoom);
                        accuseOptions(player, game, character, weapon, r);
                        return;
                    } catch (IllegalArgumentException e) {
                        throw e; // repeat the loop again
                    }
                }

                // moves our accused player into the room, they then remain in the room until there turn
                Player accusedPlayer = getPlayer(Player.Token.valueOf(character.getName().replaceAll("\\s", "")));
                if (accusedPlayer != null && player != accusedPlayer) { // move the players token/position to the corresponding room
                    moveAccused(accusedPlayer, (Room) room);
                }
                // if no player has the token asked, continue with the game as there may be a less then 6 players

                // this is used to determine our check left of play when revealing a card
                guess.add(room);
                guess.add(character);
                guess.add(weapon);

                // it is essentially the end of the players turn, hide their information
                clearScreen();

                // displays the information on the output of what the current player suggested
                System.out.println();
                System.out.println("Player " + player.getName() + " who is " + player.getToken().name() + " asks...");
                System.out.printf("Was it %1s with the %1s in the %1s\n\n", character, weapon, room);

                // now we check each player beginning to the left has a card
                int start = players.indexOf(player);

                // check left direction of player
                if (revealCard(guess, player, start + 1, players.size()))
                    return;
                    // now check the other left of the player
                else if (revealCard(guess, player, 0, start))
                    return;
                else
                    System.out.println("No player revealed a card, put on your poker face :)");

                return;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Creates new lines within our text pane, useful for other players not being able
     * to see a previous players information
     */
    private static void clearScreen() {
        for (int i = 0; i < 100; i++)
            System.out.println();
    }

    /**
     * Moves the player to it's new location room, also updates the players location
     *
     * @param player
     * @param room
     */
    private static void moveAccused(Player player, Room room) {
        // because some rooms have multiple doors, it is best to sit a player at a door of
        // whichever our room returns, (as our rooms are stored in a set)

        Door door = room.getDoors().iterator().next();
        // move the accused player to this doors location
        player.move(door.getPos());
        // update the room the player is currently in
        player.enterRoom(room);
        // set the player was moved
        player.setWasMoved();

    }

    /**
     * Returns the player corresponding to the token otherwise null
     *
     * @param token
     * @return
     */
    private static Player getPlayer(Player.Token token) {
        for (Player p : players) {
            if (p.getToken() == token) {
                return p;
            }
        }
        return null;
    }

    /**
     * Asks every player from start to end to reveal a card if they have it
     *
     * @param guess
     * @param start
     * @param end
     * @return
     */
    private static boolean revealCard(ArrayList<Card> guess, Player player, int start, int end) {
        for (int i = start; i < end; i++) {
            Player leftPlayer = players.get(i);
            if (leftPlayer.checkCards(guess) && leftPlayer != player) {
                // force leftPlayer to reveal a card to current player
                // then add it to the inventory then break method
                Card reveal = leftPlayer.pickRandomCardToReveal(guess);
                player.addCardToInventory(reveal);
                System.out.println(leftPlayer.getName() + " revealed a card to " + player.getName());

                // decide whether to give the other player the option of revealing a certain card
                // or not
                return true; //finished
            } else
                System.out.println(leftPlayer.getName() + " cannot answer");
        }
        return false; // no player in this list has any of the guess cards
    }

    /**
     * The logic for accusing another player. This is where you win the game or loose
     *
     * @param player
     * @param game
     */
    private static void accuseOptions(Player player, Game game, Card character, Card weapon, Card room) {
        // A player can accuse anywhere on the board
        // This is how a player can win the game
        if (game.getSolution().contains(character) &&
                game.getSolution().contains(weapon) &&
                game.getSolution().contains(room)) {
            // the player has won the game
            System.out.println("\n***************************");
            System.out.println("CONGRATULATIONS YOU WON " + player.getName() + "!!!");
            System.out.println("*****************************\n");
            printSolution(game);
            System.out.println("Game Over");
            System.exit(0);// exit the program
        } else { // the guess was incorrect therefor the player is excluded
            // clear the screen
            clearScreen();
            System.out.println("Your accusation was incorrect. You have been eliminated");
            excludedPlayers.add(player);
            if (players.size() - excludedPlayers.size() < 2) {
                System.out.println("There is only one player left in the game, therefore the game is over!");
                printSolution(game);
                System.exit(0);
            }
        }

    }

    /**
     * Prints to the console the winning solution
     *
     * @param game
     */
    private static void printSolution(Game game) {
        System.out.println("The winning solution is:");
        System.out.println(game.printSolution());
    }


    /**
     * Access point to the game
     * <p>
     * I took this method from DJP
     *
     * @param args
     */
    public static void main(String[] args) {
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
        players = inputPlayers(nplayers, game);
        excludedPlayers = new ArrayList<>();

        // deal cards to all the players
        game.dealCards(players);

        // play the game
        int turn = 1;
        Random dice = new Random();
        while (true) {

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
                    turn++;
                }
            }
        }
    }

}
