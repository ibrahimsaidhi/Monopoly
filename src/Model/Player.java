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


    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void decrementBalance(float amount) {
        this.balance -= amount;
    }

    public void incrementBalance(float amount) {
        this.balance += amount;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() { return this.position; }

    public void addProperty(Property property){
        ownedProperties.add(property);
    }

    public ArrayList getOwnedProperties(){
        return ownedProperties;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

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
