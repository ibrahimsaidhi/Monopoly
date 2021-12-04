package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Board Class: sets up the board to be played on
 * Date: November 5th, 2021
 * @author Hamza Zafar, 101158275
 * @version 2.0
 */

public class Board implements Serializable {

    private ArrayList<Square> board;
    private List<Player> players;

    public static String index = "", square = "", property = "", railroad = "", utility = "", name = "", color = "", value = "";

    public Board(){
        board = new ArrayList<>();
    }

    public void importFromXmlFile(String file) throws Exception {
        readSAX(new File(file));
        for(int i = 0; i < board.size(); i++){
            System.out.println(board.get(i).getName());
        }
    }

    public void checkConditions(){
        if((!index.equals("") && !index.equals(null)) && (!square.equals("") && !square.equals(null)) && (!name.equals("") && !name.equals(null)) && !squareExists(name)){
            System.out.println("Square Index is "+Integer.valueOf(index));
            addSquare(Integer.valueOf(index), name);
        } else if((!index.equals("") && !index.equals(null)) && (!property.equals("") && !property.equals(null)) && (!name.equals("") && !name.equals(null)) && (!color.equals("") && !color.equals(null)) && (!value.equals("") && !value.equals(null)) && !squareExists(name)){
            System.out.println("Property Index is "+Integer.valueOf(index));
            addProperty(Integer.valueOf(index), name, color, Integer.valueOf(value));
        } else if((!index.equals("") && !index.equals(null)) && (!railroad.equals("") && !railroad.equals(null)) && (!name.equals("") && !name.equals(null)) && (!value.equals("") && !value.equals(null)) && !squareExists(name)){
            System.out.println("Railroad Index is "+Integer.valueOf(index));
            addRailroad(Integer.valueOf(index), name, Integer.valueOf(value));
        } else if((!index.equals("") && !index.equals(null)) && (!utility.equals("") && !utility.equals(null)) && (!name.equals("") && !name.equals(null)) && (!value.equals("") && !value.equals(null)) && !squareExists(name)){
            System.out.println("Utility Index is "+Integer.valueOf(index));
            addUtility(Integer.valueOf(index), name, Integer.valueOf(value));
        }
    }

    public void readSAX(File file) throws Exception {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser s = spf.newSAXParser();

        DefaultHandler dh = new DefaultHandler(){
            boolean parseIndex = false, parseSquare = false, parseProperty = false, parseRailroad = false,
                    parseUtility = false, parseName = false, parseColor = false, parseValue = false;

            public void startElement(String u, String ln, String qname, Attributes a){
                if(qname.equalsIgnoreCase("index")){
                    parseIndex = true;
                } else if(qname.equalsIgnoreCase("square")){
                    parseSquare = true;
                } else if(qname.equalsIgnoreCase("property")){
                    parseProperty = true;
                } else if(qname.equalsIgnoreCase("railroad")){
                    parseRailroad = true;
                } else if(qname.equalsIgnoreCase("utility")){
                    parseUtility = true;
                } else if(qname.equalsIgnoreCase("name")){
                    parseName = true;
                } else if(qname.equalsIgnoreCase("color")){
                    parseColor = true;
                } else if(qname.equalsIgnoreCase("value")){
                    parseValue = true;
                }
                //System.out.println("START: " + qname);
            }

            public void endElement(String uri, String localName, String qname){
                //System.out.println("END: " + qname);
            }

            public void characters(char[] ch, int start, int length) throws SAXException {
                if(parseIndex == true){
                    index = new String(ch, start, length);
                    parseIndex = false;
                } else if(parseSquare == true){
                    square = new String(ch, start, length);
                    parseSquare = false;
                } else if(parseProperty == true){
                    property = "Property";
                    parseProperty = false;
                } else if(parseRailroad == true){
                    railroad = new String(ch, start, length);
                    parseRailroad = false;
                } else if(parseUtility == true){
                    utility = new String(ch, start, length);
                    parseUtility = false;
                } else if(parseName == true){
                    name = new String(ch, start, length);
                    parseName = false;
                } else if(parseColor == true){
                    color = new String(ch, start, length);
                    parseColor = false;
                } else if(parseValue == true){
                    value = new String(ch, start, length);
                    parseValue = false;
                }
                System.out.println("Index: " + index + " Name: " + name + " Square: " + square + " Property: " + property +
                        " Railroad: " + railroad + " Utility: " + utility + " Color: " + color + " Value: " + value);
                //System.out.println("CHARS: " + new String(ch, start, length));
                checkConditions();
                //index = "";
                //square = "";
                //property = "";
                //railroad = "";
                //utility = "";
                //name = "";
                //color = "";
                //value = "";
            }
        };
        s.parse(file, dh);
    }

    public void addSquare(int squareIndex, String squareName){
        board.add(squareIndex, new Square(squareName));
    }

    public void addProperty(int index, String squareName, String squareColor, int squareValue){
        board.add(index, new Property(squareName, squareColor, squareValue));
    }

    public void addRailroad(int index, String railroadName, int railroadValue){
        board.add(index, new Railroad(railroadName, railroadValue));
    }

    public void addUtility(int index, String utilityName, int utilityValue){
        board.add(index, new Utility(utilityName, utilityValue));
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

    /**
     * All squares, properties, railroad, utilities have unique names
     * This method checks to see if a square with a given name exists within the board
     * @return
     */
    public boolean squareExists(String name){
        for(int i = 0; i < this.size(); i++){
            if(this.getIndex(i).getName() == name){
                return true;
            }
        }
        return false;
    }

    public Property getProperty(int x){
        return (Property) board.get(players.get(x).getPosition());
    }
    public Utility getUtility(int x){
        return (Utility) board.get(players.get(x).getPosition());
    }

    public int getPrice(int x){
        return ((Property) board.get(players.get(x).getPosition())).getValue();
    }
}
