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
                gameView.payToLeaveJail(); //Asks player if they'd like to pay to leave jail, if they're in jail.
                if(!gameModel.playerIsInJail()) { //If player is not in jail, then roll die is allowed.
                    int firstDieRoll = gameModel.rollDie();
                    int secondDieRoll = gameModel.rollDie();
                    gameModel.setCurrentPlayerPosition(firstDieRoll + secondDieRoll);
                    gameView.repaint();
                    int pos = gameModel.getCurrentPlayerPosition();
                    gameView.setFeedbackArea("\nPlayer " + gameModel.getCurrentPlayer().getPlayerNumber() + ": You have rolled two die, " + firstDieRoll + " and " + secondDieRoll + " which add up to " + (firstDieRoll + secondDieRoll));
                    if(gameModel.hasPlayerPassedGo()){
                        gameView.setFeedbackArea("\nCongratulations, you've passed GO! Your balance has increased by $200.");
                    }
                    gameView.setFeedbackArea("\nYour new position is now " + pos + ": " + gameModel.getBoardName());
                    if (gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition()) instanceof Property) {
                        if (!gameModel.propertyOwned((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition()))) { //If property landed on isn't owned
                            gameView.unlockBuyButton(); //Unlock the 'Buy' button.
                            gameView.promptUserToPurchase();
                            goToTheBottomOfTextField();
                            break;
                        } else if (gameModel.propertyOwned((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition()))) { //If property landed on is owned by someone else
                            gameView.taxPlayer();
                            gameModel.passTurn();
                            gameView.setFeedbackArea("\nCurrently turn of: Player " + gameModel.getCurrentPlayer().getPlayerNumber() + "\n");
                            break;
                        }
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
                    gameModel.getCurrentPlayer().addProperty((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition()));
                    gameModel.getCurrentPlayer().decrementBalance(((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition())).getValue());
                    gameView.setFeedbackArea("\nPlayer " + gameModel.getCurrentPlayer().getPlayerNumber() + ": Congratulations, you now own property: " + (Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition()) +
                            "\nYour new balance is: $" + gameModel.getCurrentPlayer().getBalance() + "\nSpend wisely!");
                    gameModel.passTurn();
                    gameView.setFeedbackArea("\nCurrently turn of: Player " + gameModel.getCurrentPlayer().getPlayerNumber() + "\n");
                }
                gameView.checkPlayerBalance(gameModel.getCurrentPlayer());
                gameView.lookingForWinner();
                gameView.unlockRollDieButton(); //Lock the roll die button
                gameView.payToLeaveJail(); //Asks player if they'd like to pay to leave jail, if they're in jail.
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
                gameView.payToLeaveJail(); //Asks player if they'd like to pay to leave jail, if they're in jail.
                goToTheBottomOfTextField();
                break;
            case "State":
                gameView.setFeedbackArea(gameModel.printState()+"\n");
                goToTheBottomOfTextField();
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