package GameControl;

import java.util.*;

/**
 * A GameControl.Card is either a GameControl.Character, GameControl.Weapon, or GameControl.Room. Each card has
 * its own characteristic and as players progress through the game,
 * they will inevitably collect cards from other players.
 * <p>
 * Created by Jack on 19/07/2016.
 */
public class Card {

    // list of all our playing cards
    private List<Card> deck = new ArrayList<>();

    // List of Cards in their respective categories
    private List<Room> rooms = new ArrayList<>();
    private List<Character> characters = new ArrayList<>();
    private List<Weapon> weapons = new ArrayList<>();

    // Our solution to win the game of Cluedo
    private Set<Card> solution = new HashSet<>();

    private int deckCount = 0;

    // the name of our card
    private String name;

    public Card(Board[][] board){
        setupCards(board);
        setupSolution();
        deck.addAll(characters);
        deck.addAll(weapons);
        deck.addAll(rooms);

        // shuffle our deck
        Collections.shuffle(deck);
    }

    protected Card(String name){
        this.name = name;
    }

    /**
     * Here we set up the locations of the rooms on the board, we also
     * set the door locations. A room is a location on the board as well
     * as a Card
     *
     * Here we set up our characters and weapons for our deck of playing
     * deck which our players will keep in their inventory
     */
    private void setupCards(Board[][] board){
        // set up the rooms and their special cases
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
        kitchen.setDoorLocation(6,4);
        ball.setDoorLocation(11,7);
        conservatory.setDoorLocation(19,4);
        lounge.setDoorLocation(5,19);
        hall.setDoorLocation(12,17);
        study.setDoorLocation(19,21);
        dining.setDoorLocation(5,8);
        library.setDoorLocation(20,16);
        billiard.setDoorLocation(21,8);//20/8

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
        rooms.add(kitchen);
        rooms.add(ball);
        rooms.add(conservatory);
        rooms.add(lounge);
        rooms.add(hall);
        rooms.add(study);
        rooms.add(dining);
        rooms.add(library);
        rooms.add(billiard);

        // set up our character and weapon cards
        // characters
        Character scarlett = new Character("Miss Scarlett");
        Character mustard = new Character("Colonel Mustard");
        Character green = new Character("Mr. Green");
        Character peacock = new Character("Mrs. Peacock");
        Character white = new Character("Mrs. White");
        Character plum = new Character("Professor Plum");

        // weapons
        Weapon rope = new Weapon("Rope");
        Weapon dagger = new Weapon("Dagger");
        Weapon candle = new Weapon("Candlestick");
        Weapon pipe = new Weapon("Lead Pipe");
        Weapon spanner = new Weapon("Spanner");
        Weapon gun = new Weapon("Revolver");

        // add the cards to the deck
        characters.add(scarlett);
        characters.add(mustard);
        characters.add(green);
        characters.add(peacock);
        characters.add(white);
        characters.add(plum);

        weapons.add(rope);
        weapons.add(dagger);
        weapons.add(candle);
        weapons.add(pipe);
        weapons.add(spanner);
        weapons.add(gun);
    }

    /*********************************/
    /*          GETTERS              */
    /*********************************/

    /** Return the name of the card */
    public String getName(){ return name; }

    /**
     * Gets a card from the deck with the specified name
     * @param name
     * @return
     */
    public Card getCard(String name){
        for (Card c: deck){
            if (c.getName().contains(name))
                return c;
        }
        throw new IllegalArgumentException("Card: "+name+" is not contained in the deck");
    }

    /**
     * Return the list of rooms
     */
    public List<Room> getRooms(){
        return this.rooms;
    }

    public Room getRoom(String name){
        for (Room r: rooms){
            if (r.getName().toLowerCase().contains(name.toLowerCase()))
                return r;
        }
        throw new IllegalArgumentException("Name: "+name+" was not located in the room category!");
    }

    public Character getCharacter(String name){
        for (Character c: characters){
            if (c.getName().toLowerCase().contains(name.toLowerCase()))
                return c;
        }
        throw new IllegalArgumentException("Name: "+name+" was not located in the character category!");
    }

    public Weapon getWeapon(String name){
        for (Weapon w: weapons){
            if (w.getName().toLowerCase().contains(name.toLowerCase()))
                return w;
        }
        throw new IllegalArgumentException("Name: "+name+" was not located in the weapon category!");
    }

    /**
     * Define our solution cards which are randomly chosen and stored in a set.
     * The solution contains 1 Character, 1 Weapon, and 1 Room
     */
    public void setupSolution(){
        Collections.shuffle(rooms);
        Collections.shuffle(characters);
        Collections.shuffle(weapons);

        solution.add(characters.get(0));
        solution.add(weapons.get(0));
        solution.add(rooms.get(0));
    }

    public Set<Card> getSolution(){
        return this.solution;
    }

    public String printSolution(){
        Card character = null;
        Card weapon = null;
        Card room = null;
        for (Card c: solution){
            if (c instanceof Room)
                room = c;
            else if (c instanceof Weapon)
                weapon = c;
            else
                character = c;
        }
        return String.format("It was %1s with the %1s in the %1s!",character,weapon,room);
    }

    /**
     * Deals cards to all players in the game
     */
    public void dealCards(List<Player> players){
        List<Card> modifiedDeck = removeSolutionFromDeck();
        while (deckCount < modifiedDeck.size())
            for (Player p: players)
                p.addCardToHand(modifiedDeck.get(deckCount++));
    }

    /**
     * Helper method for dealCards.
     * Removes the solution from the deck and returns
     * a new array to deal the appropriate cards
     * @return
     */
    private List<Card> removeSolutionFromDeck(){
        List<Card> modified = new ArrayList<>();
        for (Card c: deck){
            if (!solution.contains(c))
                modified.add(c);
        }
        return modified;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;

        Card card = (Card) o;

        return name != null ? name.equals(card.name) : card.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString(){
        return name;
    }
}
