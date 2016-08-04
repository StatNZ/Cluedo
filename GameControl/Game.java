package GameControl;

import File_Readers.Parser;

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
        String output = "";
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {

                if (x == p.getPosition().x && y == p.getPosition().y)
                    output += p.playerNumber+" "; // draw our player on the board

                else if (board[x][y] instanceof Room )
                    output += board[x][y].printArray() + " "; // draw our rooms on the board

                else
                    output += ". "; // draw the rest of our squares

            }
            output += "\n";
        }
        output += "\nThe key:\nA = Kitchen,\nB = Ball Room\n" +
                "C = Conservatory,\nD = Billiard Room,\nE = Library,\n," +
                "F = Study,\nG = Hall,\nH = Lounge,\nI = Dining room\n" +
                "Number = Current Player\n";
        return output;
    }

//    public void draw(){
//        UI.clearGraphics();
//        for (int i=0; i<board.length; i++){
//            for (int j=0; j<board[0].length; j++){
//                if (board[i][j] != null){
//                    board[i][j]
//                }
//            }
//        }
//    }

    /**
     * Here we set up our player with their initial position on where they will be
     * starting on the board, each player is assigned a character in which they will
     * play throughout the game. This does not necessarily mean that they will have
     * that character card in their deck.
     *
     * @param name
     * @param token
     */
    public Player addPlayer(String name, Player.Token token, int playerNum) {
        switch (token) {
            case MissScarlett:
                return new Player(name, token, Parser.playersStartPositions.get(0),playerNum);
            case ProfessorPlum:
                return new Player(name, token, Parser.playersStartPositions.get(1),playerNum);
            case MrsWhite:
                return new Player(name, token, Parser.playersStartPositions.get(2),playerNum);
            case MrsPeacock:
                return new Player(name, token, Parser.playersStartPositions.get(3),playerNum);
            case MrGreen:
                return new Player(name, token, Parser.playersStartPositions.get(4),playerNum);
            case ColonelMustard:
                return new Player(name, token, Parser.playersStartPositions.get(5),playerNum);
            default:
                throw new IllegalArgumentException("Player selection was abnormally exited");
        }
    }

    /**
     * Checks if a door is adjacent to our Position then return
     * a door
     *
     * @param pos
     * @return
     */
    public Door isDoor(Position pos) {
        // check north
        if (board[pos.x][pos.y - 1] instanceof Door)
            return (Door) board[pos.x][pos.y - 1];
        // south
        if (board[pos.x][pos.y + 1] instanceof Door)
            return (Door) board[pos.x][pos.y + 1];
        // east
        if (board[pos.x + 1][pos.y] instanceof Door)
            return (Door) board[pos.x + 1][pos.y];
        // west
        if (board[pos.x - 1][pos.y] instanceof Door)
            return (Door) board[pos.x - 1][pos.y];
        return null;
    }

    /**
     * Return the specified card using a string which will be the name of the card
     *
     * @return
     */
    public Card getCard(String name) {
        return card.getCard(name);
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
     * @return
     */
    public Card getWeapon(String weaponName) {
        return card.getWeapon(weaponName);
    }

    /**
     * Get the room by string name;
     *
     * @return
     */
    public Room getRoom(String roomName) {
        return card.getRoom(roomName);
    }

    /**
     * Checks if current position is a Room
     *
     * @return
     */
    public boolean isRoom(Position p) {
        return !(board[p.x][p.y] instanceof Room);
    }

    /**
     * Here we attempt to move the player n amount of steps (according to the dice roll),
     * In the future we will implement this move function with more logic. We will attempt
     * to move the player in a A* fashion towards a room/door of the players choosing
     *
     * @param player - the current player who's turn it is
     * @param nmoves - the number of steps we can take (either determined by dice roll, or if player selects < dice roll)
     * @param room   - this is the destination we want to get to
     */
    public void movePlayer(Player player, int nmoves, Room room) {

        // choose the best door to start from
        if (player.getRoom() != null){
            Door startingDoor = player.getRoom().selectBestDoorToDoor(room);
            player.move(startingDoor.getPos());
        }

        // make the player leave the room
        player.leaveRoom();

        // setup A* instructions
        Node start = new Node(null, player.getPosition());
        Node end = new Node(null, room.getDoor(player.getPosition()).getPos());
        start.setGoal(end);
        end.setGoal(end);

        // compute A* algorithm
        Node path = Astar(start, end);
        List<Position> pathToFollow = new ArrayList<>();
        addPath(pathToFollow, path);

        // make the player follow the A* path
        int i = pathToFollow.size() - 1;
        while (nmoves != 0) {

            if (i < 0) break; //safe guard against out-of-bounds

            // Check if there is a door in our surrounding position
            // check if new position is a door
            Door door = isDoor(pathToFollow.get(i));
            if (door != null) {
                Room r = door.getRoom();

                // give client the option to enter room
                String input = TextClient.inputString("Would you like to enter room " + r.getName() + ": yes/no?");

                if (input.contains("y")) {
                    // put the player in the room and update position
                    player.enterRoom(r);
                    player.move(pathToFollow.get(i));
                    return;
                } else {
                    // player said no, so if room we want to travel to
                    // is equal to the room we are at, we end our turn
                    if (room.equals(r)) {
                        player.move(pathToFollow.get(i));
                        return;
                    }//else carry on with the program
                }
            }

            // move the player
            player.move(pathToFollow.get(i));
            i--;    nmoves--;
        }
    }

    /**
     * Helper method for adding our shortest path to the list in
     * which we will use to move our player
     */
    public void addPath(List<Position> positions, Node path) {
        while (path.parent != null) {
            positions.add(new Position(path.getPos().x, path.getPos().y));
            path = path.parent;
        }
    }

    /**
     * Algorithm for Astar search which will return a node containing
     * the shortest path to our destination. Should never return null.
     *
     * @param start
     * @param end
     * @return
     */
    public Node Astar(Node start, Node end) {
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
