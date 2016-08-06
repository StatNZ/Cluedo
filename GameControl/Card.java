package GameControl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public Card() {
        setupCards();
        setupSolution();
        deck.addAll(characters);
        deck.addAll(weapons);
        deck.addAll(rooms);

        // shuffle our deck
        Collections.shuffle(deck);
    }

    protected Card(String name) {
        this.name = name;
    }

    /**
     * Here we set up the locations of the rooms on the board, we also
     * set the door locations. A room is a location on the board as well
     * as a Card
     * <p>
     * Here we set up our characters and weapons for our deck of playing
     * deck which our players will keep in their inventory
     */
    private void setupCards() {

        // create room cards
        rooms.add(new Room("Kitchen"));
        rooms.add(new Room("Lounge"));
        rooms.add(new Room("Study"));
        rooms.add(new Room("Conservatory"));
        rooms.add(new Room("Billiard Room"));
        rooms.add(new Room("Hall"));
        rooms.add(new Room("Library"));
        rooms.add(new Room("Ball Room"));
        rooms.add(new Room("Dining Room"));

        // set up our character and weapon cards
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

    public Collection<Card> getDeck() {
        return this.deck;
    }

    /**
     * Return the name of the card
     */
    public String getName() {
        return name;
    }

    /**
     * Gets a card from the deck with the specified name
     *
     * @param name the name of the card we are searching for
     * @return Card if it is found
     */
    public Card getCard(String name) {
        for (Card c : deck) {
            if (c.getName().contains(name))
                return c;
        }
        throw new IllegalArgumentException("Card: " + name + " is not contained in the deck");
    }

    /**
     * Return the list of rooms
     */
    public List<Room> getRooms() {
        return this.rooms;
    }

    public Room getRoom(String name) {
        for (Room r : rooms) {
            if (r.getName().toLowerCase().contains(name.toLowerCase()))
                return r;
        }
        throw new IllegalArgumentException("Name: " + name + " is not a valid Room card");
    }

    public Character getCharacter(String name) {
        for (Character c : characters) {
            if (c.getName().toLowerCase().contains(name.toLowerCase()))
                return c;
        }
        throw new IllegalArgumentException("Name: " + name + " is not a valid Character card");
    }

    public Weapon getWeapon(String name) {
        for (Weapon w : weapons) {
            if (w.getName().toLowerCase().contains(name.toLowerCase()))
                return w;
        }
        throw new IllegalArgumentException("Name: " + name + " is not a valid Weapon card");
    }

    /**
     * Define our solution cards which are randomly chosen and stored in a set.
     * The solution contains 1 Character, 1 Weapon, and 1 Room
     */
    private void setupSolution() {
        Collections.shuffle(rooms);
        Collections.shuffle(characters);
        Collections.shuffle(weapons);

        solution.add(characters.get(0));
        solution.add(weapons.get(0));
        solution.add(rooms.get(0));
    }

    public Set<Card> getSolution() {
        return this.solution;
    }

    public String printSolution() {
        Card character = null;
        Card weapon = null;
        Card room = null;
        for (Card c : solution) {
            if (c instanceof Room)
                room = c;
            else if (c instanceof Weapon)
                weapon = c;
            else
                character = c;
        }
        return String.format("It was %1s with the %1s in the %1s!", character, weapon, room);
    }

    /**
     * Deals cards to all players in the game
     */
    public void dealCards(List<Player> players) {
        List<Card> modifiedDeck = removeSolutionFromDeck();
        while (true)
            for (Player p : players) {
                if (this.deckCount >= modifiedDeck.size())
                    return;
                p.addCardToHand(modifiedDeck.get(this.deckCount++));
            }

    }

    /**
     * Helper method for dealCards.
     * Removes the solution from the deck and returns
     * a new array to deal the appropriate cards
     *
     * @return A modified list of cards that do not contain the solution cards (21 cards - 3 cards)
     */
    private List<Card> removeSolutionFromDeck() {
        List<Card> modified = new ArrayList<>();
        for (Card c : this.deck) {
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
    public String toString() {
        return name;
    }

}
