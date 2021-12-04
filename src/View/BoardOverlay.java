package View;

import Model.Game;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;

/**
 * @author John Afolayan
 * A class which represents a player's coordinate on the Monopoly board.
 */
public class BoardOverlay extends JPanel implements Serializable {
    Game game;
    HashMap<Integer, Point> boardLocations = new HashMap<Integer, Point>();

    public BoardOverlay(Game gameModel) {
        setSize(950, 550);
        setOpaque(false);
        setLayout(null);
        this.game = gameModel;
        storeHashmapLocations();
    }

    /**
     * Stores the coordinates of every square on the monopoly board into a hashmap
     */
    public void storeHashmapLocations(){
        Point coord;
        for(int i = 0; i < game.getBoard().size(); i++){
            String boardName = game.getBoard().getIndex(i).getName();
            if(i == 0){
                coord = new Point(857, 395);
                boardLocations.put(i, coord);
            } else if(i == 1){
                coord = new Point(757, 395);
                boardLocations.put(i, coord);
            } else if(i == 2){
                coord = new Point(680, 395);
                boardLocations.put(i, coord);
            } else if(i == 3){
                coord = new Point(600, 395);
                boardLocations.put(i, coord);
            } else if(i == 4){
                coord = new Point(525, 395);
                boardLocations.put(i, coord);
            } else if(i == 5){
                coord = new Point(445, 395);
                boardLocations.put(i, coord);
            } else if(i == 6){
                coord = new Point(380, 395);
                boardLocations.put(i, coord);
            } else if(i == 7){
                coord = new Point(295, 395);
                boardLocations.put(i, coord);
            } else if(i == 8){
                coord = new Point(220, 395);
                boardLocations.put(i, coord);
            } else if(i == 9){
                coord = new Point(145, 395);
                boardLocations.put(i, coord);
            } else if(i == 10){
                coord = new Point(70, 385);
                boardLocations.put(i, coord);
            } else if(i == 11){
                coord = new Point(5, 410);
                boardLocations.put(i, coord);
            } else if(i == 12){
                coord = new Point(30, 345);
                boardLocations.put(i, coord);
            } else if(i == 13){
                coord = new Point(30, 310);
                boardLocations.put(i, coord);
            } else if(i == 14){
                coord = new Point(30, 275);
                boardLocations.put(i, coord);
            } else if(i == 15){
                coord = new Point(30, 240);
                boardLocations.put(i, coord);
            } else if(i == 16){
                coord = new Point(30, 205);
                boardLocations.put(i, coord);
            } else if(i == 17){
                coord = new Point(30, 170);
                boardLocations.put(i, coord);
            } else if(i == 18){
                coord = new Point(30, 135);
                boardLocations.put(i, coord);
            } else if(i == 19){
                coord = new Point(30, 98);
                boardLocations.put(i, coord);
            } else if(i == 20){
                coord = new Point(30, 65);
                boardLocations.put(i, coord);
            } else if(i == 21){
                coord = new Point(50, 25);
                boardLocations.put(i, coord);
            } else if(i == 22){
                coord = new Point(145, 10);
                boardLocations.put(i, coord);
            } else if(i == 23){
                coord = new Point(220, 20);
                boardLocations.put(i, coord);
            } else if(i == 24){
                coord = new Point(300, 10);
                boardLocations.put(i, coord);
            } else if(i == 25){
                coord = new Point(375, 10);
                boardLocations.put(i, coord);
            } else if(i == 26){
                coord = new Point(450, 30);
                boardLocations.put(i, coord);
            } else if(i == 27){
                coord = new Point(528, 10);
                boardLocations.put(i, coord);
            } else if(i == 28){
                coord = new Point(605, 10);
                boardLocations.put(i, coord);
            } else if(i == 29){
                coord = new Point(680, 15);
                boardLocations.put(i, coord);
            } else if(i == 30){
                coord = new Point(755, 10);
                boardLocations.put(i, coord);
            } else if(i == 31){
                coord = new Point(70, 385);
                boardLocations.put(i, coord);
            } else if(i == 32){
                coord = new Point(875, 67);
                boardLocations.put(i, coord);
            } else if(i == 33){
                coord = new Point(860, 135);
                boardLocations.put(i, coord);
            } else if(i == 34){
                coord = new Point(875, 170);
                boardLocations.put(i, coord);
            } else if(i == 35){
                coord = new Point(865, 205);
                boardLocations.put(i, coord);
            } else if(i == 36){
                coord = new Point(860, 240);
                boardLocations.put(i, coord);
            } else if(i == 37){
                coord = new Point(875, 275);
                boardLocations.put(i, coord);
            } else if(i == 38){
                coord = new Point(860, 313);
                boardLocations.put(i, coord);
            } else if(i == 39){
                coord = new Point(875, 348);
                boardLocations.put(i, coord);
            }
        }
    }

    public Color setPlayerColor(int num){
        if(num == 0){
            return Color.black;
        } else if(num == 1){
            return Color.PINK;
        } else if(num == 2){
            return Color.CYAN;
        } else if(num == 3){
            return Color.YELLOW;
        } else if(num == 4){
            return Color.MAGENTA;
        } else if(num == 5){
            return Color.GREEN;
        } else if(num == 6){
            return Color.ORANGE;
        } else if(num == 7){
            return Color.RED;
        } else if(num == 8){
            return Color.BLUE;
        }
        return Color.WHITE;
    }

    public static String getPlayerColor(int num){
        if(num == 0){
            return "BLACK";
        } else if(num == 1){
            return "PINK";
        } else if(num == 2){
            return "CYAN";
        } else if(num == 3){
            return "YELLOW";
        } else if(num == 4){
            return "MAGENTA";
        } else if(num == 5){
            return "GREEN";
        } else if(num == 6){
            return "ORANGE";
        } else if(num == 7){
            return "RED";
        } else if(num == 8){
            return "BLUE";
        }
        return "WHITE";
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        //Draw a circle for the amount of players in the game
        for (int i = 0; i < game.getPlayers().size(); i++) {
            int intPos = game.getCurrentPlayer().getPosition(); //Get the integer position of the current player
            //String positionName = game.getBoard().getIndex(intPos).getName(); //Get the name of the square which player is currently on
            if (boardLocations.containsKey(intPos)) {
                graphics.drawOval(boardLocations.get(intPos).x, boardLocations.get(intPos).y, getWidth() / 30, getHeight() / 30);
                graphics.fillOval(boardLocations.get(intPos).x, boardLocations.get(intPos).y, getWidth() / 30, getHeight() / 30);
                graphics.setColor(setPlayerColor(game.getCurrentPlayer().getPlayerNumber())); //Passes player's number as parameter for getPlayerColor() which has a player's assigned color
            }
        }
    }
}