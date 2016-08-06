package GameControl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import File_Readers.Parser;

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

    private Board[][] board;

    // Card class containing our collections of cards
    private Card card;

    public Game() {

        // create our cards for the game
        this.card = new Card();

        // read our board in from a text file and populate our game board
        board = Parser.parseFile(this);
    }

    public String printBoard(Player p) {
        if (p == null) throw new NullPointerException("Player is null in printBoard");
        String output = "";
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {

                if (x == p.getPosition().x && y == p.getPosition().y)
                    output += p.playerNumber + " "; // draw our player on the board

                else if (board[x][y] instanceof Room)
                    output += board[x][y].printArray() + " "; // draw our rooms on the board

                else
                    output += ". "; // draw the rest of our squares

            }
            output += "\n";
        }
        output += "\nThe key:\nA = Kitchen,\nB = Ball Room\n" +
                "C = Conservatory,\nD = Billiard Room,\nE = Library\n," +
                "F = Study,\nG = Hall,\nH = Lounge,\nI = Dining room\n" +
                "Number = Current Player\n";
        return output;
    }

    /**
     * Here we set up our player with their initial position on where they will be
     * starting on the board, each player is assigned a character in which they will
     * play throughout the game. This does not necessarily mean that they will have
     * that character card in their deck.
     *
     * @param name the name the player will take on for the duration of the game
     * @param token the Character assigned token
     */
    public Player addPlayer(String name, Player.Token token, int playerNum) {
        switch (token) {
            case MissScarlett:
                return new Player(name, token, Parser.playersStartPositions.get(0), playerNum);
            case ProfessorPlum:
                return new Player(name, token, Parser.playersStartPositions.get(1), playerNum);
            case MrsWhite:
                return new Player(name, token, Parser.playersStartPositions.get(2), playerNum);
            case MrsPeacock:
                return new Player(name, token, Parser.playersStartPositions.get(3), playerNum);
            case MrGreen:
                return new Player(name, token, Parser.playersStartPositions.get(4), playerNum);
            case ColonelMustard:
                return new Player(name, token, Parser.playersStartPositions.get(5), playerNum);
            default:
                throw new IllegalArgumentException("Player selection was abnormally exited");
        }
    }

    /**
     * Checks if a door is adjacent to our Position then return
     * a door
     *
     * @param position the position to check whehter there is a door there or not
     * @return door object - if there is one near the specified position
     */
    public Door isDoor(Position position) {
        // check north
        if (board[position.x][position.y - 1] instanceof Door)
            return (Door) board[position.x][position.y - 1];
        // south
        if (board[position.x][position.y + 1] instanceof Door)
            return (Door) board[position.x][position.y + 1];
        // east
        if (board[position.x + 1][position.y] instanceof Door)
            return (Door) board[position.x + 1][position.y];
        // west
        if (board[position.x - 1][position.y] instanceof Door)
            return (Door) board[position.x - 1][position.y];
        return null;
    }

    /**
     * Returns the card object class
     *
     * @return Card class
     */
    public Card getCard() {
        return this.card;
    }

    /**
     * Return a character card that contains the string name
     */
    public Card getCharacter(String charName) {
        return card.getCharacter(charName);
    }

    /**
     * Return a weapon card that contains the string name
     *
     * @return Weapon card
     */
    public Card getWeapon(String weaponName) {
        return card.getWeapon(weaponName);
    }

    /**
     * Get the room by string name;
     *
     * @return Room
     */
    public Room getRoom(String roomName) {
        return card.getRoom(roomName);
    }

    /**
     * Checks if current position is a Room
     *
     * @return
     */
    private boolean isRoom(Position p) {
        return !(board[p.x][p.y] instanceof Room);
    }

    /**
     * Here we attempt to move the player n amount of steps (according to the dice roll),
     * In the future we will implement this move function with more logic. We will attempt
     * to move the player in a A* fashion towards a room/door of the players choosing
     *
     *
     *
     * @param player - the current player who's turn it is
     * @param nmoves - the number of steps we can take (either determined by dice roll, or if player selects < dice roll)
     * @param room   - this is the destination we want to get to
     * @return boolean - true if player has entered a room
     */
    public boolean movePlayer(Player player, int nmoves, Room room) {

        // choose the best door to start from
        if (player.getRoom() != null) {
            Door startingDoor = player.getRoom().selectBestDoorToDoor(room);
            player.move(startingDoor.getPos());
        }

        // setup A* instructions
        Door closestDoor = room.getDoor(player.getPosition());
        Node start = new Node(null, player.getPosition());
        Node end = new Node(null, closestDoor.getPos());
        start.setGoal(end);
        end.setGoal(end);

        // compute A* algorithm
        Node path = Astar(start, end);
        List<Position> pathToFollow = new ArrayList<>();
        addPath(pathToFollow, path);

        // remove player from the current room as they have chosen to move elsewhere
        player.leaveRoom();

        // make the player follow the A* path
        int i = pathToFollow.size()-1;
        while (nmoves != 0) {

            // we must ensure the player enters the correct room, once they do, we can then update
            // their new position.
            if (player.getPosition().equals(closestDoor.getPos())){
                player.enterRoom(closestDoor.getRoom());
                return true;
            }

            //safe guard against out-of-bounds
            if (i < 0) break;

            // move the player
            player.move(pathToFollow.get(i));
            i--;
            nmoves--;
        }
        return false;
    }

    /**
     * Helper method for adding our shortest path to the list in
     * which we will use to move our player
     */
    private void addPath(List<Position> positions, Node path) {
        while (path.parent != null) {
            positions.add(new Position(path.getPos().x, path.getPos().y));
            path = path.parent;
        }
    }

    /**
     * Algorithm for Astar search which will return a node containing
     * the shortest path to our destination. Should never return null.
     *
     * @param start Source node
     * @param end Sink node
     * @return Astar node containing our selected path
     */
    private Node Astar(Node start, Node end) {
        Queue<Node> order = new PriorityQueue<>();
        Set<Node> visited = new HashSet<>();

        order.add(start); // add our start node to the queue

        while (!order.isEmpty()) {
            // do Astar LOGIC
            Node n = order.poll();
            visited.add(n);
            if (n.equals(end)) return n; // found our path
            else {
                for (Position p : n.getPos().getNeighbours()) {
                    Node neighbour = new Node(n, p);
                    neighbour.setGoal(end);
                    if (!visited.contains(neighbour) && isRoom(p))
                        order.add(neighbour);
                }
            }
        }
        throw new IllegalArgumentException("Path finder has failed");
    }

    /**
     * Each player is dealt a set of cards. Each card dealt to a player
     * will come from a shuffled deck
     */
    public void dealCards(List<Player> players) {
        card.dealCards(players);
    }

    /**
     * Return the solution list
     */
    public Set<Card> getSolution() {
        return card.getSolution();
    }

    public String printSolution() {
        return card.printSolution();
    }

    public String toString() {
        return board.toString();
    }

}
