package View;

import Controller.Controller;
import Model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class View extends JFrame implements ModelUpdateListener {
    Game gameModel;
    JButton newGameButton;
    JButton rollDieButton;
    JButton passTurnButton;
    JButton buyButton;
    JButton quitButton;
    JButton addHouseButton;
    ArrayList<JButton> listOfCommandButtons;
    JTextArea feedbackArea;
    JButton stateButton;
    BoardOverlay boardOverlay;
    MonopolyBoard monopolyBoard;
    //MyPanel panel;

    public View(Game gameModel) {
        super("Monopoly");
        this.gameModel = gameModel;
        initialize();
    }

    public static void main(String[] args) {
        Game gameModel = new Game();
        View gameView = new View(gameModel);
        gameModel.setViewer(gameView);
        Controller gameController = new Controller(gameModel, gameView);
        gameView.initialize(gameController);
    }

    static int askUser(Integer[] choices) {
        Integer s = (Integer) JOptionPane.showInputDialog(
                null,
                "How many players are playing today?",
                "Select the number of players!",
                JOptionPane.PLAIN_MESSAGE,
                null,
                choices,
                choices[0]);
        return s;
    }

    public void initialize() {
        //Initialize the View
        JFrame myFrame = new JFrame("Monopoly");
        Container root = getContentPane();
        root.setLayout(new BorderLayout());


        //The layered pane will have multiple layers in order for us to overlay components
        JLayeredPane jLayeredPane = new JLayeredPane();
        jLayeredPane.setSize(950, 550);
        monopolyBoard = new MonopolyBoard();
        jLayeredPane.add(monopolyBoard, JLayeredPane.DEFAULT_LAYER);

        boardOverlay = new BoardOverlay(gameModel);
        jLayeredPane.add(boardOverlay, JLayeredPane.POPUP_LAYER);
        root.add(jLayeredPane, BorderLayout.CENTER);

        //Menu Panel will have the set of commands that a user can choose from in order to play the game
        JPanel menuPanel = new JPanel();

        //Creating JButtons and adding actionlisteners for them
        newGameButton = new JButton("New Game");
        rollDieButton = new JButton("Roll Die");
        buyButton = new JButton("Buy");
        addHouseButton = new JButton("Add House");
        passTurnButton = new JButton("Pass Turn");
        quitButton = new JButton("Quit Game");
        stateButton = new JButton("State");
        rollDieButton.setEnabled(false);
        passTurnButton.setEnabled(false);
        buyButton.setEnabled(false);
        addHouseButton.setEnabled(false);
        stateButton.setEnabled(false);
        passTurnButton.setEnabled(false);
        quitButton.setEnabled(false);
        listOfCommandButtons = new ArrayList<JButton>();
        listOfCommandButtons.add(rollDieButton);
        listOfCommandButtons.add(buyButton);
        listOfCommandButtons.add(passTurnButton);
        listOfCommandButtons.add(stateButton);
        listOfCommandButtons.add(addHouseButton);
        listOfCommandButtons.add(quitButton);
        listOfCommandButtons.add(newGameButton);


        feedbackArea = new JTextArea("Welcome to Monopoly! Please press New Game in order to start!\n");
        feedbackArea.setRows(8);
        JScrollPane feedbackAreaScrollPane = new JScrollPane(feedbackArea);
        menuPanel.setLayout(new BorderLayout());
        menuPanel.add(feedbackAreaScrollPane, BorderLayout.NORTH);
        menuPanel.add(newGameButton, BorderLayout.WEST);

        JPanel centerPanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        centerPanel.setLayout(new BorderLayout());
        menuPanel.add(centerPanel, BorderLayout.CENTER);
        centerPanel.add(rollDieButton, BorderLayout.CENTER);
        centerPanel.add(buyButton, BorderLayout.WEST);
        centerPanel.add(stateButton, BorderLayout.EAST);
        bottomPanel.add(addHouseButton, BorderLayout.NORTH);
        menuPanel.add(passTurnButton, BorderLayout.EAST);

        bottomPanel.add(quitButton, BorderLayout.SOUTH);
        menuPanel.add(bottomPanel, BorderLayout.SOUTH);
        root.add(menuPanel, BorderLayout.SOUTH);

        //Initialization of the frame
        this.setVisible(true);
        this.setResizable(false);
        this.setSize(950, 650);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }


    public void promptUserToPurchase() {
        rollDieButton.setEnabled(false);
        int propertyPrice = ((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition())).getValue();
        setFeedbackArea("\nPlayer " + gameModel.getCurrentPlayer().getPlayerNumber() + ": Would you like to purchase " + gameModel.getBoardName() +
                "? It costs $" + propertyPrice + " and you currently have $" + gameModel.getCurrentPlayer().getBalance() + ".\nClick the 'Buy' button to purchase or 'Pass Turn' to move on.\n");
        checkPlayerBalance(gameModel.getCurrentPlayer());
        lookingForWinner();
        rollDieButton.setEnabled(true);
    }

    /**
     * @author John Afolayan
     * This method taxes a player whenver they land on another player's property
     */
    public void taxPlayer(){
        Player ownedBy = gameModel.whoOwnsProperty((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition())); //player who owns property
        if(!ownedBy.equals(gameModel.getCurrentPlayer())){ //If current player who lands on property doesn't own that property, tax them.
            int amount = (int) (((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition())).getValue() * 0.1); //amount to decrement by, 10%
            gameModel.getCurrentPlayer().decrementBalance(amount); //remove $amount from player being taxed
            ownedBy.incrementBalance(amount); //add $amount to player who owns property
            JOptionPane.showMessageDialog(null, "Player " + gameModel.getCurrentPlayer().getPlayerNumber() + ": You've landed on a property owned by player "+  ownedBy.getPlayerNumber() + ". You've been taxed $" + amount + ", your new balance is $" + gameModel.getCurrentPlayer().getBalance());
            checkPlayerBalance(gameModel.getCurrentPlayer());
            lookingForWinner();
        }
    }

    public void purchaseAHouse(){
        String currentColor = (((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition())).getColor());
        if (gameModel.isAbleToPurchaseBlue() && currentColor.equals("blue")){
            JOptionPane.showMessageDialog(this, "You are now able to purchase blue houses!");
            rollDieButton.setEnabled(false);
            int input = JOptionPane.showConfirmDialog(null, "Would you like to add a house to " + gameModel.getBoardName() + "? It will cost you S200");
            if (input == JOptionPane.YES_OPTION) {
                gameModel.getCurrentPlayer().getOwnedHouses().add(new House("blue house", 200, "blue"));
                ((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition())).getHouses().add(new House("blue house", 200, "blue"));
                gameModel.getCurrentPlayer().decrementBalance(200);
                JOptionPane.showMessageDialog(this,"You have purchased a house for this blue property. Your new balance is: $" + gameModel.getCurrentPlayer().getBalance());
            }
            if (input == JOptionPane.NO_OPTION) {
                gameModel.passTurn();
            }
            checkPlayerBalance(gameModel.getCurrentPlayer());
            lookingForWinner();
            rollDieButton.setEnabled(true);
        }
        else if (gameModel.isAbleToPurchaseBrown() && currentColor.equals("brown")){
            JOptionPane.showMessageDialog(this, "You are now able to purchase brown houses!");
            rollDieButton.setEnabled(false);
            int input = (JOptionPane.showConfirmDialog( null, "Would you like to add a house to " + gameModel.getBoardName() + "? It will cost you S50"));
            if (input == JOptionPane.YES_OPTION) {
                gameModel.getCurrentPlayer().getOwnedHouses().add(new House("brown house", 50, "brown"));
                ((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition())).getHouses().add(new House("brown house", 50, "brown"));
                gameModel.getCurrentPlayer().decrementBalance(50);
                JOptionPane.showMessageDialog(this,"You have purchased a house for this brown property. Your new balance is: $" + gameModel.getCurrentPlayer().getBalance());
            }
            if (input == JOptionPane.NO_OPTION) {
                gameModel.passTurn();
            }
            checkPlayerBalance(gameModel.getCurrentPlayer());
            lookingForWinner();
            rollDieButton.setEnabled(true);
        }
        else if (gameModel.isAbleToPurchasePurple() && currentColor.equals("purple")){
            JOptionPane.showMessageDialog(this, "You are now able to purchase purple houses!");
            rollDieButton.setEnabled(false);
            int input = JOptionPane.showConfirmDialog(null, "Would you like to add a house to " + gameModel.getBoardName() + "? It will cost you S50");
            if (input == JOptionPane.YES_OPTION) {
                gameModel.getCurrentPlayer().getOwnedHouses().add(new House("purple house", 50, "purple"));
                ((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition())).getHouses().add(new House("purple house", 50, "purple"));
                gameModel.getCurrentPlayer().decrementBalance(50);
                JOptionPane.showMessageDialog(this,"You have purchased a house for this purple property. Your new balance is: $" + gameModel.getCurrentPlayer().getBalance());
            }
            if (input == JOptionPane.NO_OPTION) {
                gameModel.passTurn();
            }
            checkPlayerBalance(gameModel.getCurrentPlayer());
            lookingForWinner();
            rollDieButton.setEnabled(true);
        }
        else if (gameModel.isAbleToPurchaseOrange() && currentColor.equals("orange")){
            JOptionPane.showMessageDialog(this, "You are now able to purchase orange houses!");
            rollDieButton.setEnabled(false);
            int input = JOptionPane.showConfirmDialog(null, "Would you like to add a house to " + gameModel.getBoardName() + "? It will cost you S100");
            if (input == JOptionPane.YES_OPTION) {
                gameModel.getCurrentPlayer().getOwnedHouses().add(new House("orange house", 100, "orange"));
                ((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition())).getHouses().add(new House("orange house", 100, "orange"));
                gameModel.getCurrentPlayer().decrementBalance(100);
                JOptionPane.showMessageDialog(this,"You have purchased a house for this orange property. Your new balance is: $" + gameModel.getCurrentPlayer().getBalance());
            }
            if (input == JOptionPane.NO_OPTION) {
                gameModel.passTurn();
            }
            checkPlayerBalance(gameModel.getCurrentPlayer());
            lookingForWinner();
            rollDieButton.setEnabled(true);
        }
        else if (gameModel.isAbleToPurchaseRed() && currentColor.equals("red")){
            JOptionPane.showMessageDialog(this, "You are now able to purchase red houses!");
            rollDieButton.setEnabled(false);
            int input = JOptionPane.showConfirmDialog(null, "Would you like to add a house to " + gameModel.getBoardName() + "? It will cost you S150");
            if (input == JOptionPane.YES_OPTION) {
                gameModel.getCurrentPlayer().getOwnedHouses().add(new House("red house", 150, "red"));
                ((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition())).getHouses().add(new House("red house", 150, "red"));
                gameModel.getCurrentPlayer().decrementBalance(150);
                JOptionPane.showMessageDialog(this,"You have purchased a house for this red property. Your new balance is: $" + gameModel.getCurrentPlayer().getBalance());
            }
            if (input == JOptionPane.NO_OPTION) {
                gameModel.passTurn();
            }
            checkPlayerBalance(gameModel.getCurrentPlayer());
            lookingForWinner();
            rollDieButton.setEnabled(true);
        }
        else if (gameModel.isAbleToPurchaseLightBlue() && currentColor.equals("light blue")){
            JOptionPane.showMessageDialog(this, "You are now able to purchase light blue houses!");
            rollDieButton.setEnabled(false);
            int input = JOptionPane.showConfirmDialog(null, "Would you like to add a house to " + gameModel.getBoardName() + "? It will cost you S50");
            if (input == JOptionPane.YES_OPTION) {
                gameModel.getCurrentPlayer().getOwnedHouses().add(new House("light blue house", 50, "light blue"));
                ((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition())).getHouses().add(new House("light blue house", 50, "light blue"));
                gameModel.getCurrentPlayer().decrementBalance(50);
                JOptionPane.showMessageDialog(this,"You have purchased a house for this light blue property. Your new balance is: $" + gameModel.getCurrentPlayer().getBalance());
            }
            if (input == JOptionPane.NO_OPTION) {
                gameModel.passTurn();
            }
            checkPlayerBalance(gameModel.getCurrentPlayer());
            lookingForWinner();
            rollDieButton.setEnabled(true);
        }
        else if (gameModel.isAbleToPurchaseYellow() && currentColor.equals("yellow")){
            JOptionPane.showMessageDialog(this, "You are now able to purchase yellow houses!");
            rollDieButton.setEnabled(false);
            int input = JOptionPane.showConfirmDialog(null, "Would you like to add a house to " + gameModel.getBoardName() + "? It will cost you $150");
            if (input == JOptionPane.YES_OPTION) {
                gameModel.getCurrentPlayer().getOwnedHouses().add(new House("yellow house", 150, "yellow"));
                ((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition())).getHouses().add(new House("yellow house", 150, "yellow"));
                gameModel.getCurrentPlayer().decrementBalance(150);
                JOptionPane.showMessageDialog(this,"You have purchased a house for this yellow property. Your new balance is: $" + gameModel.getCurrentPlayer().getBalance());
            }
            if (input == JOptionPane.NO_OPTION) {
                gameModel.passTurn();
            }
            checkPlayerBalance(gameModel.getCurrentPlayer());
            lookingForWinner();
            rollDieButton.setEnabled(true);
        }
        else if (gameModel.isAbleToPurchaseGreen() && currentColor.equals("green")){
            JOptionPane.showMessageDialog(this, "You are now able to purchase green houses!");
            rollDieButton.setEnabled(false);
            int input = JOptionPane.showConfirmDialog(null, "Would you like to add a house to " + gameModel.getBoardName() + "? It will cost you S200");
            if (input == JOptionPane.YES_OPTION) {
                gameModel.getCurrentPlayer().getOwnedHouses().add(new House("green house", 50, "green"));
                ((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition())).getHouses().add(new House("green house", 50, "green"));
                gameModel.getCurrentPlayer().decrementBalance(200);
                JOptionPane.showMessageDialog(this,"You have purchased a house for this green property. Your new balance is: $" + gameModel.getCurrentPlayer().getBalance());
            }
            if (input == JOptionPane.NO_OPTION) {
                gameModel.passTurn();
            }
            checkPlayerBalance(gameModel.getCurrentPlayer());
            lookingForWinner();
            rollDieButton.setEnabled(true);
        }

        else {
            JOptionPane.showMessageDialog(this, "You cannot purchase a house at the moment. Please try again later.");
        }
    }

    public void checkingForHotelEligibility() {
        if (((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition())).getHouses().size() == 1) {
            JOptionPane.showMessageDialog(this, "You can now purchase a hotel for this property");
        }
    }

    public void purchaseAHotel(){

    }

    /**
     * @author Ibrahim Said
     * This method checks the balance of a player and determines if they are eliminated or not.
     */
    public void checkPlayerBalance(Player player){
        int balance = player.getBalance();
        if (balance <= 0){
            gameModel.removeBankruptPlayer();
            JOptionPane.showMessageDialog(null, "Player " + gameModel.getCurrentPlayer().getPlayerNumber() + ": You are now bankrupt! You have been kicked out of the game. Too bad...");
        }
    }

    public void checkingForHouseEligibility(){
        if (gameModel.getCurrentPlayer().getBrownProperties() == 2) {
            gameModel.setAbleToPurchaseBrown(true);
        }
        if (gameModel.getCurrentPlayer().getPurpleProperties() == 3){
            gameModel.setAbleToPurchasePurple(true);
        }
        if (gameModel.getCurrentPlayer().getGreenProperties() == 3){
            gameModel.setAbleToPurchaseGreen(true);
        }
        if (gameModel.getCurrentPlayer().getBlueProperties() == 3){
            gameModel.setAbleToPurchaseBlue(true);
        }
        if (gameModel.getCurrentPlayer().getLightBlueProperties() == 3){
            gameModel.setAbleToPurchaseLightBlue(true);
        }
        if (gameModel.getCurrentPlayer().getYellowProperties() == 3){
            gameModel.setAbleToPurchaseYellow(true);
        }
        if (gameModel.getCurrentPlayer().getRedProperties() == 3){
            gameModel.setAbleToPurchaseRed(true);
        }
        if (gameModel.getCurrentPlayer().getOrangeProperties() == 3){
            gameModel.setAbleToPurchaseOrange(true);
        }

    }

    /**
     * @author Ibrahim Said
     * This method checks if a player has won the game.
     */
    public void lookingForWinner(){
        if (gameModel.getPlayers().size() == 1){
            JOptionPane.showMessageDialog(null, "Player " + gameModel.getPlayers().get(0).getPlayerNumber() + " has won the game! Congratulations");
            System.exit(0);
        }
    }


    private void initialize(Controller gameController) {
        for (JButton button : listOfCommandButtons) {
            button.addActionListener(gameController);
        }
    }

    /*
     * This method updates the model
     */
    @Override
    public void modelUpdated() {
        repaint();
    }

    /**
     * @author John Afolayan
     * This method unlocks GUI buttons after a player specifies amount of players
     */
    public void unlockButtons() {
        for (JButton button : listOfCommandButtons) {
            if(!button.getText().equalsIgnoreCase("Buy"))
                button.setEnabled(true);
        }
    }

    public void lockBuyButton(){
        buyButton.setEnabled(false);
    }

    public void lockRollDieButton(){
        rollDieButton.setEnabled(false);
    }

    public void unlockBuyButton(){
        buyButton.setEnabled(true);
    }



    public void unlockRollDieButton(){
        rollDieButton.setEnabled(true);
    }

    public JTextArea getFeedbackArea() {
        return feedbackArea;
    }

    public void setFeedbackArea(String feedbackAreaText) {
        this.feedbackArea.append(feedbackAreaText);
    }

    public JButton getNewGameButton() {
        return newGameButton;
    }

    public int numberOfPlayersRequest() {
        Integer[] choices = new Integer[]{2, 3, 4, 5, 6, 7, 8};
        int choice = askUser(choices);
        return choice;
    }

}