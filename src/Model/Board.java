package Model;

import java.util.HashMap;

public class Board {

    private HashMap<Integer, Square> board = new HashMap<>();
    private String description;

    public Board(String description){
        this.description = description;
        board = new Hashmap<>();
    }

    public void buildBoard(){
        Square GO = new Square;
        Property property1 = new Property;
        Property property2 = new Property;
        Square empty = new Square;
        Square empty2 = new Square;
        Property property3 = new Property;
        Square empty3 = new Square;
        Square empty4 = new Square;
        Property property4 = new Property;
        Property property5 = new Property;
        Square empty5 = new Square;

        board.put(0,GO);
        board.put(1,property1);
        board.put(2,property2);
        board.put(3,empty);
        board.put(4,empty2);
        board.put(5,property3);
        board.put(6,empty3);
        board.put(7,empty4);
        board.put(8,property4);
        board.put(9,property5);

    }
}