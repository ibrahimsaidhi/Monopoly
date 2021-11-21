package Model;

import Game.Command;
import Game.Parser;

import javax.swing.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;


/**
 * @author John Afolayan
 * <p>
 * This main class creates and initialises all the others. This class acts the Controller.
 * It creates the parser and starts the game.
 * It also evaluates and executes the commands that the parser returns.
 */

public class Game {
    private Parser parser;
    private Property property;
    private Utility utility;
    private Railroad railroad;
    private Player currentPlayer;
    private int currentPlayerInt = 0;
    private List<Player> players;
    private ModelUpdateListener viewer;
    private int numberOfPlayers;
    private String newPlayerName;
    private InputStream inputStream;
    private Board board = new Board();
    boolean wantToQuit = false;
    private List<ModelUpdateListener> views;
    public Game() {
        parser = new Parser();
        players = new ArrayList<>();
        this.views = new ArrayList<>();

    }

    public void addView(ModelUpdateListener view){
        views.add(view);
    }

    private void printCurrentPlayer() {
        System.out.println("\n!*-----------------------------------------------NEW TURN!-------------------------------------------------------*!");
        System.out.println("The current player is " + getCurrentPlayer().getName() + "\n");
    }

    public boolean processCommand(Command command) {

        if (command.isUnknown()) {
            System.out.println("Unknown command");
            return false;
        }

        String commandWord = command.getCommandWord();
        switch (commandWord) {
            case "move":
                moveToken();
                break;
            case "pass":
                passTurn();
                break;
            case "state":
                printState();
                break;
            case "quit":
                wantToQuit = true;
                break;
        }

        return wantToQuit;
    }

    /**
     * @author John Afolayan
     * @param property Property a player lands on
     * This method checks to see if a property is owned.
     */

    /**
     * @author John Afolayan
     * This method prints the state of the current player.
     */
    public void printState() {
        /**
         * @author John Afolayan
         *
         * Print a representation of the game's state
         *
         */
        for(ModelUpdateListener v: this.views) {
            v.printState(currentPlayerInt+1, getCurrentPlayer().getBalance(), getCurrentPlayer().getOwnedProperties().toString(), getCurrentPlayer().getBalance());
        }
    }

    public void passTurn() {
        /**
         * @author John Afolayan
         *
         * Passes turn to the next player
         *
         */
        this.currentPlayerInt = (this.currentPlayerInt == (this.numberOfPlayers - 1)) ? 0 : this.currentPlayerInt + 1;
        this.currentPlayer = this.players.get(this.currentPlayerInt);
        for(ModelUpdateListener v: this.views) {
            v.passTurn(getCurrentPlayer().getPlayerNumber());
        }
        if(this.currentPlayer.getType() == Player.PlayerType.AI){
            this.aiAlgorithm();
        }
        //newTurn();
    }

    public void manualPass(){
        checkPlayerBalance(getCurrentPlayer());
        lookingForWinner();
        for(ModelUpdateListener v: this.views) {
            v.manualPassUpdate(getCurrentPlayer().getPlayerNumber());
        }
        passTurn();
    }

    private void aiAlgorithm() {


    }

    private void newTurn() {
        printCurrentPlayer();
        //parser.showCommands();
    }

    private void createAIPlayers(){
        if(getPlayers().size() < 4){
            for (int i = 1; i <= (4 - getPlayers().size()); i++) {
                Player aiPlayer = new Player(i);
                aiPlayer.setAI();
                players.add(aiPlayer);
            }
        }
    }

    public void initializePlayers(int numberOfPlayers) {
        /**
         * @author John Afolayan
         *
         * Ask the user for the number of players that will be playing, and then
         * initializes them accordingly.
         *
         */

        this.numberOfPlayers = numberOfPlayers;
        createPlayers(numberOfPlayers);
        this.currentPlayer = players.get(0);

        for(ModelUpdateListener v: this.views) {
            v.initializeGame(numberOfPlayers, getCurrentPlayer().getPlayerNumber());
        }

    }

    private void update() {
        if (this.viewer != null)
            this.viewer.modelUpdated();
    }

    public void setViewer(ModelUpdateListener viewer) {
        this.viewer = viewer;
    }

    /**
     * @author John Afolayan
     * This method creates the specified amount players for a new game
     */
    public void createPlayers(int numberOfPlayers) {
        players = new ArrayList<Player>();
        for (int i = 1; i <= numberOfPlayers; i++) {
            players.add(new Player(i));
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int rollDie(){
        int value = getCurrentPlayer().rollDice();
        setCurrentPlayerPosition(value);
        for(ModelUpdateListener v: this.views) {
            v.dieCount(value, getCurrentPlayerPosition());
        }
        return value;

    }

    /**
     * @author John Afolayan
     * This method sets the new position of a player after a die is rolled
     */
    public void setCurrentPlayerPosition(int pos) {
        getCurrentPlayer().setPosition((getCurrentPlayerPosition() + pos) % board.size());
    }

    /**
     * @author John Afolayan
     * This method gets the position of the current player
     */
    public int getCurrentPlayerPosition() {
        return getCurrentPlayer().getPosition();
    }

    public boolean propertyOwned(Property property){
        for (int i = 0; i < players.size(); i++){
            if (players.get(i).getOwnedProperties().contains(property)){
                return true;
            }
        }
        return false;
    }

    public boolean utilityOwned(Utility utility){
        for (int i = 0; i < players.size(); i++){
            if (players.get(i).getOwnedUtility().contains(utility)){
                return true;
            }
        }
        return false;
    }

    public boolean railroadsOwned(Railroad railroad){
        for (int i = 0; i < players.size(); i++){
            if (players.get(i).getOwnedRailroads().contains(railroad)){
                return true;
            }
        }
        return false;
    }

    /**
     * @author John Afolayan
     * @param property Property a player lands on
     * This method checks who owns the property landed on.
     */
    public Player whoOwnsProperty(Property property){
        for (int i = 0; i < players.size(); i++){
            if (players.get(i).getOwnedProperties().contains(property)){
                return players.get(i);
            }
        }
        return null;
    }

    public Player whoOwnsUtility(Utility utility){
        for (int i = 0; i < players.size(); i++){
            if (players.get(i).getOwnedUtility().contains(utility)){
                return players.get(i);
            }
        }
        return null;
    }

    public Player whoOwnsRailroad(Railroad railroad){
        for (int i = 0; i < players.size(); i++){
            if (players.get(i).getOwnedRailroads().contains(railroad)){
                return players.get(i);
            }
        }
        return null;
    }

    public int getUtilityRent(int diceValue){
        int amount = (int) (((Utility) getBoard().getIndex(getCurrentPlayer().getPosition())).getValue()) * diceValue;
        return amount;
    }

    public int getRailroadRent(){
        Player ownedBy = whoOwnsRailroad((Railroad) getBoard().getIndex(getCurrentPlayer().getPosition()));
        return ownedBy.totalRailroadsOwned() * 25;
    }



    public String getBoardName() {
        return board.getIndex(getCurrentPlayer().getPosition()).getName();
    }

    public Board getBoard() {
        return board;
    }

    public void moveToken() {
        /**
         * @author John Afolayan and Ibrahim Said
         *
         * Checks the syntax of the command passed and moves token n amount of times
         * where n is the value which is rolled on a dice.
         *
         */

        if (board.getIndex(getCurrentPlayer().getPosition()) instanceof Property){
            if(propertyOwned((Property) board.getIndex(getCurrentPlayer().getPosition()))){
                //taxPlayer();
                passTurn();
            }
        }
        else if (board.getIndex(getCurrentPlayer().getPosition())instanceof Square) {
            passTurn();
        }
        update();
    }


    /**
     * @author John Afolayan, Ibrahim Said
     * A method to prompt a user to purchase a property or not
     *//*
    public void promptUserToPurchase(){
        int propertyPrice = ((Property) board.getIndex(getCurrentPlayer().getPosition())).getValue();
        int input = JOptionPane.showConfirmDialog(null, "Player " + getCurrentPlayer().getPlayerNumber() + ": Would you like to purchase " + getBoardName() + "? It costs $" + propertyPrice + " and you currently have $" + getCurrentPlayer().getBalance() + ". Click yes to purchase or no to move on.", "Purchase " + getBoardName() + "?", JOptionPane.YES_NO_OPTION);
        if(input == JOptionPane.YES_OPTION){
            getCurrentPlayer().addProperty((Property) board.getIndex(getCurrentPlayer().getPosition()));
            getCurrentPlayer().decrementBalance(((Property) board.getIndex(getCurrentPlayer().getPosition())).getValue());
            JOptionPane.showMessageDialog(null, "Player " + getCurrentPlayer().getPlayerNumber() + ": Congratulations, you now own property: " + (Property) board.getIndex(getCurrentPlayer().getPosition())
                    + ". Your new balance is: $" + getCurrentPlayer().getBalance() + "\nSpend wisely!");
            checkPlayerBalance(players.get(currentPlayerInt));
            lookingForWinner();
            passTurn();
        } else if (input == JOptionPane.NO_OPTION){
            checkPlayerBalance(players.get(currentPlayerInt));
            lookingForWinner();
            passTurn();
        }
        checkPlayerBalance(players.get(currentPlayerInt));
        lookingForWinner();
    }*/



    /**
     * @author John Afolayan
     * This method removes a banrupt player from the game.
     */
    public void removeBankruptPlayer(){
        for (final Iterator<Player> iterator = players.iterator(); iterator.hasNext();) {
            Player temp = iterator.next();
            if (temp.getBalance() <= 0) {
                iterator.remove();
                this.numberOfPlayers -= 1;
                this.currentPlayerInt -= 1;
            }
        }
    }

    /**
     * @author John Afolayan
     * This method returns the current player of the game.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void startGame(int numberOfPlayers) {
        initializePlayers(numberOfPlayers);
        //System.out.println("There will be " + numberOfPlayers + " players this game!");
        newTurn();
    }

    public void quitGame() {
        System.exit(0);
    }

    /**
     * @author Ibrahim Said
     * This method checks the balance of a player and determines if they are eliminated or not.
     */
    public void checkPlayerBalance(Player player){
        int balance = player.getBalance();
        if (balance <= 0){
            removeBankruptPlayer();
            JOptionPane.showMessageDialog(null, "Player " + getCurrentPlayer().getPlayerNumber() + ": You are now bankrupt! You have been kicked out of the game. Too bad...");
        }
    }

    /**
     * @author Ibrahim Said
     * This method checks if a player has won the game.
     */
    public void lookingForWinner(){
        if (getPlayers().size() == 1){
            JOptionPane.showMessageDialog(null, "Player " + getPlayers().get(0).getPlayerNumber() + " has won the game! Congratulations");
            System.exit(0);
        }
    }

    /**
     * @author John Afolayan
     * This method taxes a player whenver they land on another player's property
     */
    public void taxProperty(){
        Player ownedBy = whoOwnsProperty((Property) getBoard().getIndex(getCurrentPlayer().getPosition())); //player who owns property
            int amount = (int) (((Property) getBoard().getIndex(getCurrentPlayer().getPosition())).getTax()); //amount to decrement by, 10%
            getCurrentPlayer().decrementBalance(amount); //remove $amount from player being taxed
            ownedBy.incrementBalance(amount); //add $amount to player who owns property
            //JOptionPane.showMessageDialog(null, "Player " + getCurrentPlayer().getPlayerNumber() + ": You've landed on a property owned by player "+  ownedBy.getPlayerNumber() + ". You've been taxed $" + amount + ", your new balance is $" + getCurrentPlayer().getBalance());
            checkPlayerBalance(getCurrentPlayer());
            lookingForWinner();

        for(ModelUpdateListener v: this.views) {
            v.taxProperty(amount, ownedBy, getCurrentPlayer().getPlayerNumber(), getCurrentPlayer().getBalance());
        }
    }

    /**
     * @author Hamza
     * This method taxes a player whenever they land of another players utility
     */

    public void taxUtility(int tax){
            Player ownedBy = whoOwnsUtility((Utility) getBoard().getIndex(getCurrentPlayer().getPosition()));
            getCurrentPlayer().decrementBalance(tax);
            ownedBy.incrementBalance(tax);
            //JOptionPane.showMessageDialog(null, "Player " + getCurrentPlayer().getPlayerNumber() + ": You've landed on a utility owned by player "+  ownedBy.getPlayerNumber() + ". You've been taxed $" + tax + ", your new balance is $" + getCurrentPlayer().getBalance());
            checkPlayerBalance(getCurrentPlayer());
            lookingForWinner();
        for(ModelUpdateListener v: this.views) {
            v.taxProperty(tax, ownedBy, getCurrentPlayer().getPlayerNumber(), getCurrentPlayer().getBalance());
        }

    }

    /**
     * @author Hamza
     * This method taxes a player whenever they land of another players railroad
     */
    public void taxRailroad(int tax) {
        Player ownedBy = whoOwnsRailroad((Railroad) getBoard().getIndex(getCurrentPlayer().getPosition()));
        if(!ownedBy.equals(getCurrentPlayer())){
            getCurrentPlayer().decrementBalance(tax);
            ownedBy.incrementBalance(tax);
            //JOptionPane.showMessageDialog(null, "Player " + getCurrentPlayer().getPlayerNumber() + ": You've landed on a railroad owned by player "+  ownedBy.getPlayerNumber() + ". You've been taxed $" + tax + ", your new balance is $" + getCurrentPlayer().getBalance());

            for(ModelUpdateListener v: this.views) {
                v.taxProperty(tax, ownedBy, getCurrentPlayer().getPlayerNumber(), getCurrentPlayer().getBalance());
            }
            checkPlayerBalance(getCurrentPlayer());
            lookingForWinner();
        }
    }

    public void checkSquare(int diceRoll) {
        if (getBoard().getIndex(getCurrentPlayer().getPosition()) instanceof Property) {
            if (!propertyOwned((Property) getBoard().getIndex(getCurrentPlayer().getPosition()))) { //If property landed on isn't owned
                //gameView.unlockBuyButton(); //Unlock the 'Buy' button.
                //gameView.promptPropertyPurchase();
                //goToTheBottomOfTextField();
                //break;
                for(ModelUpdateListener v: this.views) {
                    v.unlockPropertyBuy();
                }
            } else if (propertyOwned((Property) getBoard().getIndex(getCurrentPlayer().getPosition()))) { //If property landed on is owned by someone else
                //gameView.taxProperty();
                //passTurn();
                //gameView.setFeedbackArea("\nCurrently turn of: Player " + gameModel.getCurrentPlayer().getPlayerNumber() + "\n");

                //break;
                this.taxProperty();
                passTurn();
            }
        } else if (getBoard().getIndex(getCurrentPlayer().getPosition()) instanceof Utility) {
            if (!utilityOwned((Utility) getBoard().getIndex(getCurrentPlayer().getPosition()))) { //If utility landed on isn't owned
                //gameView.unlockBuyButton(); //Unlock the 'Buy' button.
                //gameView.promptUtilityPurchase();
                //goToTheBottomOfTextField();
                //break;
                for(ModelUpdateListener v: this.views) {
                    v.unlockUtilityBuy();
                }
            } else if (utilityOwned((Utility) getBoard().getIndex(getCurrentPlayer().getPosition()))) { //If utility landed on is owned by someone else
                int tax = getUtilityRent(diceRoll);
                //gameView.taxUtility(tax);
                //gameModel.passTurn();
                //gameView.setFeedbackArea("\nCurrently turn of: Player " + gameModel.getCurrentPlayer().getPlayerNumber() + "\n");

                //break;
                this.taxUtility(tax);
                passTurn();
            }
        } else if (getBoard().getIndex(getCurrentPlayer().getPosition()) instanceof Railroad) {
            if (!railroadsOwned((Railroad) getBoard().getIndex(getCurrentPlayer().getPosition()))) { //If RailRoad landed on isn't owned
                //gameView.unlockBuyButton(); //Unlock the 'Buy' button.
                //gameView.promptRailroadPurchase();
                //goToTheBottomOfTextField();
                //break;
                for(ModelUpdateListener v: this.views) {
                    v.unlockRailroadBuy();
                }
            } else if (railroadsOwned((Railroad) getBoard().getIndex(getCurrentPlayer().getPosition()))) { //If Railroad landed on is owned by someone else
                //gameView.taxRailroad(tax);
                //gameModel.passTurn();
                //gameView.setFeedbackArea("\nCurrently turn of: Player " + gameModel.getCurrentPlayer().getPlayerNumber() + "\n");

                //break;
                int rent = getRailroadRent();
                this.taxRailroad(rent);
                passTurn();
            }
        }
    }

    public void makePurchase(){
        //gameView.lockBuyButton();
        if (getBoard().getIndex(getCurrentPlayer().getPosition()) instanceof Property) {
            getCurrentPlayer().addProperty((Property) getBoard().getIndex(getCurrentPlayer().getPosition()));
            getCurrentPlayer().decrementBalance(((Property) getBoard().getIndex(getCurrentPlayer().getPosition())).getValue());
            //gameView.setFeedbackArea("\nPlayer " + gameModel.getCurrentPlayer().getPlayerNumber() + ": Congratulations, you now own property: " + gameModel.getBoardName() +
                    //"\nYour new balance is: $" + gameModel.getCurrentPlayer().getBalance() + "\nSpend wisely!");
            for(ModelUpdateListener v: this.views) {
                v.confirmPurchase(getCurrentPlayer().getPlayerNumber(), getBoardName(), getCurrentPlayer().getBalance());
            }
            passTurn();

        }
        else if (getBoard().getIndex(getCurrentPlayer().getPosition()) instanceof Utility) {
            getCurrentPlayer().addUtility((Utility) getBoard().getIndex(getCurrentPlayer().getPosition()));
            getCurrentPlayer().decrementBalance(((Utility) getBoard().getIndex(getCurrentPlayer().getPosition())).getValue());
            //gameView.setFeedbackArea("\nPlayer " + gameModel.getCurrentPlayer().getPlayerNumber() + ": Congratulations, you now own Utility: " + gameModel.getBoardName() +
                    //"\nYour new balance is: $" + gameModel.getCurrentPlayer().getBalance() + "\nSpend wisely!");

            for(ModelUpdateListener v: this.views) {
                v.confirmPurchase(getCurrentPlayer().getPlayerNumber(), getBoardName(), getCurrentPlayer().getBalance());
            }

            passTurn();
        }
        else if (getBoard().getIndex(getCurrentPlayer().getPosition()) instanceof Railroad) {
            getCurrentPlayer().addRailroad((Railroad) getBoard().getIndex(getCurrentPlayer().getPosition()));
            getCurrentPlayer().decrementBalance(((Railroad) getBoard().getIndex(getCurrentPlayer().getPosition())).getValue());
            //gameView.setFeedbackArea("\nPlayer " + gameModel.getCurrentPlayer().getPlayerNumber() + ": Congratulations, you now own RailRoad: " + gameModel.getBoardName() +
                    //"\nYour new balance is: $" + gameModel.getCurrentPlayer().getBalance() + "\nSpend wisely!");

            for(ModelUpdateListener v: this.views) {
                v.confirmPurchase(getCurrentPlayer().getPlayerNumber(), getBoardName(), getCurrentPlayer().getBalance());
            }
            passTurn();
        }
    }


}