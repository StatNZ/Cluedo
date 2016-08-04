package File_Readers;

import GameControl.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Jack on 1/08/2016.
 */
public class Parser {

    public static List<Position> playersStartPositions = new ArrayList<>();

    public static Board[][] parseFile(Game game) {

        Board[][] board = null;
        List<String> readString = new ArrayList<>();

        try {
            Scanner scan = new Scanner(new File("cluedoBoardDraw.txt"));
            while (scan.hasNext()) {
                readString.add(scan.next());
            }
        } catch (IOException e) {
            System.out.println("File failure " + e);
        }

        // create an appropriate size for our board
        int x = readString.size();
        int y = readString.get(0).length();
        board = new Board[x][y];

        // create objects and add them to our board
        for (int row = 0; row < x; row++) {
            String input = readString.get(row);
            placeString(game, input, board, row);
        }

        return board;
    }

    public static void placeString(Game card, String input, Board[][] board, int row) {

        int index = 0;
        int count = 0;
        while (count < input.length()) {
            char c = input.charAt(index++);
            switch (c) {
                case 'k': //create room kitchen
                    Room kitchen = card.getRoom("Kitchen");
                    addToBoard(card.getRoom("Kitchen"), board, row, count++);
                    break;
                case 'K': //create door kitchen
                    addToBoard(card.getRoom("Kitchen").addDoor(new Position(row, count)), board, row, count++);
                    break;
                case 'b': // create room
                    addToBoard(card.getRoom("Ball Room"), board, row, count++);
                    break;
                case 'B': // create door
                    addToBoard(card.getRoom("Ball Room").addDoor(new Position(row, count)), board, row, count++);
                    break;
                case 'c': // create room
                    addToBoard(card.getRoom("Conservatory"), board, row, count++);
                    break;
                case 'C': // create door
                    addToBoard(card.getRoom("Conservatory").addDoor(new Position(row, count)), board, row, count++);
                    break;
                case 'd':
                    addToBoard(card.getRoom("Dining Room"), board, row, count++);
                    break;
                case 'D':
                    addToBoard(card.getRoom("Dining Room").addDoor(new Position(row, count)), board, row, count++);
                    break;
                case 'r':
                    addToBoard(card.getRoom("Billiard Room"), board, row, count++);
                    break;
                case 'R':
                    addToBoard(card.getRoom("Billiard Room").addDoor(new Position(row, count)), board, row, count++);
                    break;
                case 'l':
                    addToBoard(card.getRoom("Library"), board, row, count++);
                    break;
                case 'L':
                    addToBoard(card.getRoom("Library").addDoor(new Position(row, count)), board, row, count++);
                    break;
                case 'o':
                    addToBoard(card.getRoom("Lounge"), board, row, count++);
                    break;
                case 'O':
                    addToBoard(card.getRoom("Lounge").addDoor(new Position(row, count)), board, row, count++);
                    break;
                case 'h':
                    addToBoard(card.getRoom("Hall"), board, row, count++);
                    break;
                case 'H':
                    addToBoard(card.getRoom("Hall").addDoor(new Position(row, count)), board, row, count++);
                    break;
                case 't':
                    addToBoard(card.getRoom("Study"), board, row, count++);
                    break;
                case 'T':
                    addToBoard(card.getRoom("Study").addDoor(new Position(row, count)), board, row, count++);
                    break;
                case '#':
                    addToBoard(new Room("BLOCKED"), board, row, count++);
                    break;
                case 's':
                    addToBoard(new Room("Solution"), board, row, count++);
                    break;
                case '.': // these are the paths in which we can move
                    addToBoard(null, board, row, count++);
                    break;
                case '*': // NO go zones, they are liked blocked but not
                    addToBoard(new Room("INSIDE"), board, row, count++);
                    break;
                case 'P': // players start position
                    playersStartPositions.add(new Position(row, count++));
                    break;
                default:
                    throw new IllegalArgumentException("Incorrect input read: " + c);

            }
        }
    }

    public static void addToBoard(Board o, Board[][] boards, int row, int col) {
        boards[row][col] = o;
    }

    public static void main(String[] args) {
        Parser.parseFile(new Game());
    }
}
