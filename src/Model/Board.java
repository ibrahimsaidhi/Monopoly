package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Board Class: sets up the board to be played on
 * Date: November 5th, 2021
 * @author Hamza Zafar, 101158275
 * @version 2.0
 */

public class Board {

    private ArrayList<Square> board;
    private List<Player> players;

    public Board(){
        board = new ArrayList<>();
        buildBoard();
    }

    public void buildBoard(){
        board.add(0, new Square("Go!"));
        board.add(1, new Property("Mediterranean Ave", "brown", 60));
        board.add(2, new Square("empty"));
        board.add(3, new Property("Baltic Ave", "brown", 60));
        board.add(4, new Property("Oriental Ave", "light blue", 100));
        board.add(5, new Square("empty"));
        board.add(6, new Property("Vermont Ave", "light blue", 100));
        board.add(7, new Property("Connecticut Ave", "light blue", 120));
        board.add(8, new Property("St. Charles Place", "purple", 140));
        board.add(9, new Property("States Ave", "purple", 140));
        board.add(10, new Property("Virginia Ave", "purple", 160));
        board.add(11, new Property("St. James Place", "orange", 180));
        board.add(12, new Square("empty"));
        board.add(13, new Property("Tennessee Ave", "orange", 180));
        board.add(14, new Property("New York Ave", "orange", 200));
        board.add(15, new Property("Kentucky Ave", "red", 220));
        board.add(16, new Property("Indiana Ave", "red", 220));
        board.add(17, new Property("Illinois Ave", "red", 240));
        board.add(18, new Property("Atlantic Ave", "yellow", 260));
        board.add(19, new Property("Ventnor Ave", "yellow", 260));
        board.add(20, new Property("Marvin Gardens", "yellow", 280));
        board.add(21, new Property("Pacific Ave", "green", 300));
        board.add(22, new Property("North Carolina Ave", "green", 300));
        board.add(23, new Property("Pennsylvania Ave", "green", 320));
        board.add(24, new Square("empty"));
        board.add(25, new Property("Park Place", "blue", 350));
        board.add(26, new Property("Boardwalk", "blue", 400));

    }

    public ArrayList<Square> getBoard() {
        return board;
    }

    public int size(){
        return board.size();
    }

    public Square getIndex(int x){
        return board.get(x);
    }

    public Property getProperty(int x){
        return (Property) board.get(players.get(x).getPosition());
    }

    public int getPrice(int x){
        return ((Property) board.get(players.get(x).getPosition())).getValue();
    }
}
