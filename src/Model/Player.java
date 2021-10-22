package Model;


import java.util.ArrayList;

public class Player {
    private String name;
    //private String piece (not implementing for now)
    private int playerNumber; // ie. player1, player2 etc
    private int position;
    private int balance;
    private ArrayList<Property> ownedProperties = new ArrayList<>();

    //constructor
    public Player(String name, int playerNumber){
        this.name = name;
        this.playerNumber = playerNumber;
        this.balance = 1500;
        this.position = 0; //start at the first square
    }


    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public int getPosition() {
        return position;
    }

    public void addProperty(Property property){
        ownedProperties.add(property);
    }
}