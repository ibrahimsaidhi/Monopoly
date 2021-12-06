package Controller;

import Model.*;
import View.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class Controller implements ActionListener, Serializable {
    View gameView;
    Game gameModel;
    int numberOfHumanPlayers, numberOfAIPlayers, initialNumberOfHumanPlayers, totalPlayerAmount;
    String customBoardChoice;
    private static final String newGame = "New Game", rollDie = "Roll Die", buy = "Buy", passTurn = "Pass Turn", state = "State", bsHouse = "Buy/Sell House", bsHotel = "Buy/Sell Hotel", quit = "Quit Game", save = "Save Current Game", load = "Load Game";

    public Controller(Game gameModel, View gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case newGame:
                customBoardChoice= gameView.customBoardRequest();
                gameModel.setCustomBoard(customBoardChoice);
                gameView.setBackground();
                gameView.getBoardOverlay().storeHashmapLocations(); //Stores x,y positions which will be used to display a player's position on the GUI

                initialNumberOfHumanPlayers = gameView.numberOfPlayersRequest();
                numberOfHumanPlayers = initialNumberOfHumanPlayers;
                numberOfAIPlayers= gameView.numberOfAIPlayersRequest(numberOfHumanPlayers);
                totalPlayerAmount = numberOfHumanPlayers + numberOfAIPlayers;
                gameModel.initializePlayers(numberOfHumanPlayers, numberOfAIPlayers);
                gameView.lockPassTurnButton();
                break;
            case rollDie:
                int diceRoll = gameModel.rollDie();
                gameView.repaint();
                gameView.lookingForWinner();
                gameModel.checkSquare(diceRoll);
                //gameView.unlockPassTurnButton();
                goToTheBottomOfTextField();

                break;

            case buy:
                gameView.repaint();
                gameModel.makePurchase();
                gameView.unlockRollDieButton();
                goToTheBottomOfTextField();
                break;
            case passTurn:
                gameModel.manualPass();
                gameView.lockBuyButton();
                gameView.unlockRollDieButton();
                goToTheBottomOfTextField();
                //gameModel.aiAlgorithm(); //source of bug?
                break;
            case state:
                gameView.setFeedbackArea(gameModel.printState()+"\n");
                goToTheBottomOfTextField();
                break;
            case bsHouse:
                gameModel.checkingForHouseEligibility();
                String input = gameView.requestingHouseStatus();
                gameModel.buyingHouseEligibility();

                if (input.equals("buy") && ((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition())).getHouses().size() < 4){
                    gameModel.purchaseAHouse();
                }
                else if (input.equals("buy") && ((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition())).getHouses().size() == 4){
                    gameView.setFeedbackArea("\nSorry, you can only add 4 houses to a property at a time");
                }
                else if (input.equals("sell") && !((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition())).getHouses().isEmpty()) {
                    gameModel.sellAHouse();
                }
                else if(input.equals("sell") && ((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition())).getHouses().isEmpty()){
                    gameView.setFeedbackArea("There are no houses for you to sell");
                }
                else {
                    gameView.setFeedbackArea("\nNot a valid entry. Please try again.");
                }

                gameModel.clear();
                break;
            case bsHotel:
                gameModel.checkingForHouseEligibility();
                String hotelInput = gameView.requestingHotelStatus();
                gameModel.buyingHotelEligibility();

                if (hotelInput.equals("buy") && ((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition())).getHotels().size() == 0){
                    gameModel.purchaseAHotel();
                }

                else if (hotelInput.equals("buy") && ((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition())).getHotels().size() == 1){
                    gameView.setFeedbackArea("\nSorry, you can only add 1 hotel to a property at a time");
                }

                else if(hotelInput.equals("sell") && !((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition())).getHotels().isEmpty()){
                    gameModel.sellAHotel();
                }

                else if(hotelInput.equals("sell") && ((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition())).getHotels().isEmpty()){
                    gameView.setFeedbackArea("\nThere are no hotels for you to sell");
                }

                else {
                    gameView.setFeedbackArea("\nNot a valid entry. Please try again.");
                }

                gameModel.clear();
                break;
            case save:
                try {
                    Game.writeToFile(gameModel);
                    //View.writeToFile(gameView);
                    gameView.setFeedbackArea("The game has been saved!");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                break;

            case load:
                try {

                    List<Object> gameStuff = Game.readFile();
                    gameModel.setCurrentPlayer((Player)gameStuff.get(1));
                    gameModel.setPlayers((List<Player>) gameStuff.get(2));
                    numberOfAIPlayers = (int) gameStuff.get(3);
                    numberOfHumanPlayers = (int) gameStuff.get(4);
                    initialNumberOfHumanPlayers = (int) gameStuff.get(5);
                    totalPlayerAmount = (int) gameStuff.get(6);
                    gameModel.setInitialNumberOfHumanPlayers(initialNumberOfHumanPlayers);
                    gameModel.setNumberOfHumanPlayers(numberOfHumanPlayers);
                    gameModel.setNumberOfAIPlayers(numberOfAIPlayers);
                    gameModel.setTotalNumberOfPlayers(totalPlayerAmount);
                    customBoardChoice = (String) gameStuff.get(7);
                    gameModel.setCustomBoard(customBoardChoice);
                    gameView.setBackground();
                    gameView.getBoardOverlay().storeHashmapLocations(); //Stores x,y positions which will be used to display a player's position on the GUI

                    gameView.repaint();
                    gameView.setFeedbackArea("Previous game has been loaded\n" + "\nCurrently turn of: Player " + gameModel.getCurrentPlayer().getPlayerNumber() + "\n");
                    gameView.unlockButtons();
                    gameView.lockNewGameButton();
                } catch (IOException | ClassNotFoundException ioException) {
                    ioException.printStackTrace();
                }
                break;
            case quit:
                gameModel.quitGame();
                break;
        }
    }

    private void goToTheBottomOfTextField() {
        gameView.getFeedbackArea().getCaret().setDot(Integer.MAX_VALUE);
    }


}
