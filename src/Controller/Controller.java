package Controller;

import Game.Command;
import Model.Game;
import Model.Property;
import View.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Controller implements ActionListener {
    View gameView;
    Game gameModel;

    public Controller(Game gameModel, View gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "New Game":
                int numberOfPlayers = gameView.numberOfPlayersRequest();
                gameModel.initializePlayers(numberOfPlayers);
                gameView.unlockButtons();
                gameView.setFeedbackArea("A new game has begun with " + numberOfPlayers + " players\n" + "\nCurrently turn of: Player " + gameModel.getCurrentPlayer().getPlayerNumber() + "\n");
                gameView.getNewGameButton().setEnabled(false);
                break;
            case "Roll Die":
                int diceRoll = gameModel.rollDie();
                gameModel.setCurrentPlayerPosition(diceRoll);
                gameView.repaint();
                JOptionPane.showMessageDialog(null, "Player " + gameModel.getCurrentPlayer().getPlayerNumber() + ": You have rolled two die that added up to " + diceRoll);
                int pos = gameModel.getCurrentPlayerPosition();
                gameView.setFeedbackArea("\nYour new position is now " + pos + ": " + gameModel.getBoardName());
                if (gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition()) instanceof Property) {
                    if (!gameModel.propertyOwned((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition()))) { //If property landed on isn't owned
                        gameView.unlockBuyButton(); //Unlock the 'Buy' button.
                        gameView.promptUserToPurchase();
                        goToTheBottomOfTextField();
                        break;
                    }
                    else if (gameModel.propertyOwned((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition()))) { //If property landed on is owned by someone else
                        gameView.taxPlayer();
                        gameModel.passTurn();
                        gameView.setFeedbackArea("\nCurrently turn of: Player " + gameModel.getCurrentPlayer().getPlayerNumber() + "\n");
                        break;
                    }

                }

                gameModel.passTurn();
                gameView.setFeedbackArea("\nCurrently turn of: Player " + gameModel.getCurrentPlayer().getPlayerNumber() + "\n");
                goToTheBottomOfTextField();
                //gameModel.moveToken();
                break;
            case "Buy":
                gameView.lockBuyButton();
                if (gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition()) instanceof Property) {
                    String propertyColor = ((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition())).getColor();
                    switch (propertyColor) {
                        case "green": {
                            int x = gameModel.getCurrentPlayer().getGreenProperties();
                            gameModel.getCurrentPlayer().setGreenProperties(x + 1);
                            break;
                        }
                        case "red": {
                            int x = gameModel.getCurrentPlayer().getRedProperties();
                            gameModel.getCurrentPlayer().setRedProperties(x + 1);
                            break;
                        }
                        case "blue": {
                            int x = gameModel.getCurrentPlayer().getBlueProperties();
                            gameModel.getCurrentPlayer().setBlueProperties(x + 1);
                            break;
                        }
                        case "light blue": {
                            int x = gameModel.getCurrentPlayer().getLightBlueProperties();
                            gameModel.getCurrentPlayer().setLightBlueProperties(x + 1);
                            break;
                        }
                        case "yellow": {
                            int x = gameModel.getCurrentPlayer().getYellowProperties();
                            gameModel.getCurrentPlayer().setYellowProperties(x + 1);
                            break;
                        }
                        case "purple": {
                            int x = gameModel.getCurrentPlayer().getPurpleProperties();
                            gameModel.getCurrentPlayer().setPurpleProperties(x + 1);
                            break;
                        }
                        case "orange": {
                            int x = gameModel.getCurrentPlayer().getOrangeProperties();
                            gameModel.getCurrentPlayer().setOrangeProperties(x + 1);
                            break;
                        }
                        case "brown": {
                            int x = gameModel.getCurrentPlayer().getBrownProperties();
                            gameModel.getCurrentPlayer().setBrownProperties(x + 1);
                            break;
                        }
                    }
                    gameModel.getCurrentPlayer().addProperty((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition()));
                    gameModel.getCurrentPlayer().decrementBalance(((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition())).getValue());
                    gameView.setFeedbackArea("\nPlayer " + gameModel.getCurrentPlayer().getPlayerNumber() + ": Congratulations, you now own property: " + (Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition()) +
                            "\nYour new balance is: $" + gameModel.getCurrentPlayer().getBalance() + "\nSpend wisely!");
                    gameModel.passTurn();
                    gameView.setFeedbackArea("\nCurrently turn of: Player " + gameModel.getCurrentPlayer().getPlayerNumber() + "\n");
                }
                gameView.checkPlayerBalance(gameModel.getCurrentPlayer());
                gameView.lookingForWinner();
                break;
            case "Pass Turn":
                gameView.lockBuyButton();
                gameView.unlockRollDieButton();
                gameView.checkPlayerBalance(gameModel.getCurrentPlayer());
                gameView.lookingForWinner();
                gameView.setFeedbackArea("\nPlayer # " + gameModel.getCurrentPlayer().getPlayerNumber() + " has passed their turn\n");
                gameModel.passTurn();
                gameView.setFeedbackArea("\n!*-----------------------------------------------NEW TURN!-------------------------------------------------------*!");
                gameView.setFeedbackArea("\nPlayer " + gameModel.getCurrentPlayer().getPlayerNumber() + " it is now your turn");
                goToTheBottomOfTextField();
                break;
            case "State":
                gameView.setFeedbackArea(gameModel.printState()+"\n");
                goToTheBottomOfTextField();
                break;
            case "Add House":
                gameView.checkingForHouseEligibility();
                gameView.purchaseAHouse();
                gameView.checkingForHotelEligibility();
                break;
            case "Quit Game":
                gameView.setFeedbackArea("Quitting game...\n");
                goToTheBottomOfTextField();
                gameModel.quitGame();
                break;
        }
    }

    private void goToTheBottomOfTextField() {
        gameView.getFeedbackArea().getCaret().setDot(Integer.MAX_VALUE);
    }
}
