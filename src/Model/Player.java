package Model;

/**
 * Player class: Creates players, name, position and balance
 * Date: October 22, 2021
 * @author Hamza Zafar, 101158275
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {
    private String name;
    //private String piece (not implementing for now)
    private int playerNumber; // ie. player1, player2 etc
    private int position;
    private int balance;
    private ArrayList<Property> ownedProperties = new ArrayList<>();

    //constructor
    public Player(int playerNumber){
        //this.name = name;
        this.playerNumber = playerNumber;
        this.balance = 1500; //default starting balance
        this.position = 0; //start at the first square
    }

    /**
     * returns Player's name
     */
    public String getName() {
        return name;
    }

    /**
     * returns Player's balance
     */
    public int getBalance() {
        return balance;
    }

    /**
     * @param balance specified balance
     * sets a Player's balance
     */
    public void setBalance(int balance) {
        this.balance = balance;
    }

    /**
     * @param amount to be decremented
     * decrements a Player's balance
     */
    public void decrementBalance(float amount) {
        this.balance -= amount;
    }

    /**
     * @param amount to be incremented
     * increments a Player's balance
     */
    public void incrementBalance(float amount) {
        this.balance += amount;
    }

    /**
     * @param position new position
     * sets a player's new position
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * gets a player's new position
     */
    public int getPosition() { return this.position; }

    /**
     * adds a new property a player's collected
     */
    public void addProperty(Property property){
        ownedProperties.add(property);
    }

    /**
     * returns a player's property collection
     */
    public ArrayList getOwnedProperties(){
        return ownedProperties;
    }

    /**
     * returns a player's number
     */
    public int getPlayerNumber() {
        return playerNumber;
    }

    /**
     * Calculates a random die roll based on 2 die
     */
    public Integer rollDice() {
        Integer numberOfDiceToRoll = 2; //rolling two dices
        List<Integer> diceRolls = new ArrayList<>();
        int dieRoll = 0;
        Random random = new Random();
        for (int i = 0; i < numberOfDiceToRoll; i++) {
            int diceRoll = random.nextInt(6) + 1;
            diceRolls.add(diceRoll);
            dieRoll += diceRoll;
        }
        if(dieRoll >=0){
            return dieRoll;
        } else {
            rollDice();
        }
        return 2;
    }

}
