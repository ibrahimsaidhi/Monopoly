package Model;

import java.util.HashMap;

/**
 * Board Class: sets up the board to be played on
 * Date: October 22, 2021
 * @author Hamza Zafar, 101158275
 * @version 1.0
 */

public class Board {

    private HashMap<Integer, Square> board;

    public Board(){
        board = new HashMap<>();
        buildBoard();
    }

    public void buildBoard(){
        Square GO = new Square("Go!");
        Property property1 = new Property("Carleton University", "brown", 200);
        Property property2 = new Property("123 Street", "brown", 200);
        Square empty = new Square("empty");
        Square empty2 = new Square("123");
        Property property3 = new Property("321 Street", "blue", 500);
        Square empty3 = new Square("456");
        Square empty4 = new Square("789");
        Property property4 = new Property("Mansion", "purple", 700);
        Property property5 = new Property("Airport", "purple", 700);
        Square empty5 = new Square("1234");

        board.put(1,GO);
        board.put(2,property1);
        board.put(3,property2);
        board.put(4,empty);
        board.put(5,empty2);
        board.put(6,property3);
        board.put(7,empty3);
        board.put(8,empty4);
        board.put(9,property4);
        board.put(10,property5);
        board.put(11,property5);
        board.put(12,empty5);

    }

    public HashMap<Integer, Square> getBoard() {
        return board;
    }
}