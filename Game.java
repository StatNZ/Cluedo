

import com.sun.xml.internal.bind.v2.TODO;
import ecs100.UI;

import java.util.*;

/**
 * This class is where we control all the functionality that has to do with
 * the game "Cluedo" itself. Cluedo and all it's rules and player options will
 * be controlled through this class.
 * <p>
 * The Game class will contain collections of all the deck and players in the game
 * <p>
 * Created by Jack on 19/07/2016.
 */
public class Game {

    private Board[][] board = new Board[26][26];

    private List<Card> deck = new ArrayList<>();
    private List<Player> players = new ArrayList<>();

    private Set<Card> solution = new HashSet<>(); // contains cards to win the game
    private List<Card> allCards = new ArrayList<>();


    public Game() {
        createRooms();
        createCards();
        allCards = deck;
        //draw();
    }

    public void draw(){
        UI.clearGraphics();
        for (int i=0; i<26; i++){
            for (int j=0; j<26; j++){
                if (board[i][j] != null){
                    board[i][j].draw();
                }
            }
        }
    }

    /**
     * Here we set up the locations of the rooms on the board, we also
     * set the door locations. A room is a location on the board as well
     * as a Card
     */
    public void createRooms(){
        Room kitchen = new Room("Kitchen", 1, 1, 7, 4);
        Room ball = new Room("Ball Room", 10, 1, 7, 7);
        Room conservatory = new Room("Conservatory", 19, 1, 7, 4);
        Room lounge = new Room("Lounge", 1, 19, 5, 7);
        Room hall = new Room("Hall", 8, 17, 9, 9);
        Room study = new Room("Study", 19, 21, 7, 5);
        Room dining = new Room("Dining Room", 1, 7, 5, 9);
        Room library = new Room("Library", 20, 14, 6, 5);
        Room billiard = new Room("Billiard Room", 21, 6, 5, 6);

        // Set the door locations for each room
        kitchen.setDoorLocation(6,5);
        ball.setDoorLocation(11,8);
        conservatory.setDoorLocation(19,5);
        lounge.setDoorLocation(5,18);
        hall.setDoorLocation(12,16);
        study.setDoorLocation(19,20);
        dining.setDoorLocation(6,8);
        library.setDoorLocation(19,16);
        billiard.setDoorLocation(20,8);//20/8

        // Assign the location for each room to the board
        kitchen.setStartPosition(board);
        ball.setStartPosition(board);
        conservatory.setStartPosition(board);
        lounge.setStartPosition(board);
        hall.setStartPosition(board);
        study.setStartPosition(board);
        dining.setStartPosition(board);
        library.setStartPosition(board);
        billiard.setStartPosition(board);

        // add the rooms to the deck (as they are cards as well
        deck.add(kitchen);
        deck.add(ball);
        deck.add(conservatory);
        deck.add(lounge);
        deck.add(hall);
        deck.add(study);
        deck.add(dining);
        deck.add(library);
        deck.add(billiard);
    }

    public void addPlayersToBoard(){
        for (Player p: players){
            p.setStartPosition(board);
        }
    }

    /**
     * Here we set up our characters and weapons for our deck of playing
     * deck which our players will keep in their inventory
     */
    public void createCards(){
        // characters
        Character scarlett = new Character("Miss Scarlett");
        Character mustard = new Character("Colonel Mustard");
        Character green = new Character("Mr Green");
        Character peacock = new Character("Mrs Peacock");
        Character white = new Character("Mrs White");
        Character plum = new Character("Professor Plum");

        // weapons
        Weapon rope = new Weapon("Rope");
        Weapon dagger = new Weapon("Dagger");
        Weapon candle = new Weapon("Candlestick");
        Weapon pipe = new Weapon("Lead Pipe");
        Weapon spanner = new Weapon("Spanner");
        Weapon gun = new Weapon("Revolver");

        // add the cards to the deck
        deck.add(scarlett);
        deck.add(mustard);
        deck.add(green);
        deck.add(peacock);
        deck.add(white);
        deck.add(plum);
        deck.add(rope);
        deck.add(dagger);
        deck.add(candle);
        deck.add(pipe);
        deck.add(spanner);
        deck.add(gun);
    }

    /**
     * Get the room by string name;
     * @param roomName
     * @return
     */
    public Room getRoom(String roomName){
        for (Card c: allCards){
            if (c.getName().equals(roomName) && c instanceof Room)
                return (Room)c;
        }
        throw new IllegalArgumentException("Room name is incorrect");
    }

    /**
     * Get the room the player is currently in
     * @param player
     * @return
     */
    public Room getRoom(Player player){
        if (player.getRoom() != null)
            return player.getRoom();
        throw new IllegalArgumentException("Player is not in a room");
    }

    /**
     * Define our solution cards which are randomly chosen and stored in a set.
     * The solution contains 1 Character, 1 Weapon, and 1 Room
     */
    public void setSolution(){
        // probably not the most ideal way to do this
        Collections.shuffle(deck);
        for (int i=0; i<deck.size(); i++)
            if (deck.get(i) instanceof Room) {
                solution.add(deck.get(i));
                deck.remove(i);
                break;
            }
        for (int i=0; i<deck.size(); i++)
            if (deck.get(i) instanceof Character) {
                solution.add(deck.get(i));
                deck.remove(i);
                break;
            }
        for (int i=0; i<deck.size(); i++)
            if (deck.get(i) instanceof Weapon) {
                solution.add(deck.get(i));
                deck.remove(i);
                break;
            }

    }

    /**
     * Here we attempt to move the player n amount of steps (according to the dice roll),
     * In the future we will implement this move function with more logic. We will attempt
     * to move the player in a BFS fashion towards a room/door of the players choosing
     * @param player
     * @param nmoves
     * @param room - this is the destination we want to get to
     */
    public void movePlayer(Player player, int nmoves, Room room){

        /** notes:
         * because we want to move in a bfs fashion towards a room, there will be no need
         * to record the direction in which we travel.
         * The user can specify a certain amount of steps they wish the player to move,
         * dependant on the dice roll.
         */

        // compute BFS

        // current position of player
        Node start = new Node(null,player.getPos());
        Node end = new Node(null,room.getDoor().getPos());

        Node path = BFS(start,end);
        if (path == null) //break out and report an error
            throw new IllegalArgumentException("Path finder has failed");
        List<Position> pathToFollow = new ArrayList<>();
        addPath(pathToFollow, path);

        int i = pathToFollow.size()-1;
        while (nmoves != 0){
            if (i < 0) break;
            player.move(board,pathToFollow.get(i));
            i--;
            nmoves--;
            UI.sleep(500);
            draw();
        }
        // first determine in which direction we want to travel
        // we will move our player using a BFS

        // second account for the process of when we are on a door
    }

    /**
     * Helper method for adding our shortest path to the list in
     * which we will use to move our player
     */
    public void addPath(List<Position> positions, Node path){
        while (path.parent != null){
            positions.add(new Position(path.getPos().x,path.getPos().y));
            path = path.parent;
        }
    }

    /**
     * Algorithm for BFS search which will return a node containing
     * the shortest path to our destination. Should never return null.
     * @param start
     * @param end
     * @return
     */
    public Node BFS(Node start, Node end){
        Queue<Node> order = new ArrayDeque<>();
        Set<Node> visited = new HashSet<>();

        order.add(start); // add our start node to the queue

        while (!order.isEmpty()){
            // do BFS LOGIC
            Node n = order.poll();
            visited.add(n);
            if (n.equals(end)) return n; // found our path
            else {
                //addNeighbours(n,order,visited);
                for (Position p: n.getPos().getNeighbours()){
                    Node neighbour = new Node(n,p);
                    if (!visited.contains(neighbour) && isFree(p))
                        order.add(neighbour);
                }
            }
        }
        return null; // we haven't found a path
                    // we should never get here but we will account for it
    }

    /**
     * Helper method for our BFS. It determines if the space in which we
     * want to travel is free at the specified position
     * @return
     */
    public boolean isFree(Position p){
        if (board[p.x][p.y] instanceof Room)
            return false;
        return true;
    }

    /**
     * Here we set up our player with their initial position on where they will be
     * starting on the board, each player is assigned a character in which they will
     * play throughout the game. This does not necessarily mean that they will have
     * that character card in their deck.
     *
     * @param name
     * @param token
     */
    public Player addPlayer(String name, Player.Token token){
        switch (token){
            case MissScarlett://1,17
                Player p1 = new Player(name,token,1,17);
                players.add(p1);
                return p1;
            case ProfessorPlum://6,25
                Player p2 = new Player(name, token,6,25);
                players.add(p2);
                return p2;
            case MrsWhite://25/19
                Player p3 = new Player(name,token,24,20);
                players.add(p3);
                return p3;
            case MrsPeacock:
                Player p4 = new Player(name,token,24,6);
                players.add(p4);
                return p4;
            case MrGreen:
                Player p5 = new Player(name,token,17,1);
                players.add(p5);
                return p5;
            case ColonelMustard:
                Player p6 = new Player(name,token,8,1);
                players.add(p6);
                return p6;
            default:
                throw new IllegalArgumentException("Player selection was abnormally exited");
        }
    }

    /**
     * Return the specified card using a string
     * @param name
     * @return
     */
    public Card getCard(String name){
        for (Card c: allCards){
            if (c.getName().equals(name))
                return c;
        }
        return null; // does not contain the card
    }

    /**
     * Each player is dealt a set of cards. Each card dealt to a player
     * will come from a shuffled deck
     */
    public void dealCards(){
        while (!deck.isEmpty())
            for (Player p : players) {
                if (deck.isEmpty()) break; // this ensures our random doesn't throw an illegalArg.
                Card c = randomCard();
                p.addCardToInventory(c);
                // removes a card from the deck, ensuring that we cannot select the same card again
                deck.remove(c);

                // Todo: remove this code, it is for debugging
                System.out.println("Card removed = " + c.toString()+" belongs to "+p.toString());
            }
    }

    /**
     * Helper method for getting a random card from the deck
     * @return
     */
    public Card randomCard(){
        int rnd = new Random().nextInt(deck.size());
        return deck.get(rnd);
    }

    /**
     * A method used for testing, Prints all the cards that are
     * currently in the deck. However once the game has commenced, there
     * should not be a card left in the deck
     */
    public void printCards(){
        for (Card c: deck)
            System.out.println(c.toString());
    }

    public static void main(String[] args) {
        Game game = new Game();
        Player p = game.addPlayer("jack", Player.Token.MissScarlett);
        Player p1 = game.addPlayer("john", Player.Token.ProfessorPlum);
        game.addPlayersToBoard();
        game.draw();
        game.movePlayer(p1,100,game.getRoom("Kitchen"));
        game.movePlayer(p,100,game.getRoom("Kitchen"));
    }
}
