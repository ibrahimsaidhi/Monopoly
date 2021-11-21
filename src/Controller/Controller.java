package Controller;

import Game.Command;
import Model.*;
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
                //gameView.unlockButtons();
                //gameView.setFeedbackArea("A new game has begun with " + numberOfPlayers + " players\n" + "\nCurrently turn of: Player " + gameModel.getCurrentPlayer().getPlayerNumber() + "\n");
                //gameView.getNewGameButton().setEnabled(false);
                break;
            case "Roll Die":
                int diceRoll = gameModel.rollDie();
                gameView.repaint();
                gameModel.checkSquare(diceRoll);
                break;

            case "Buy":
                gameModel.makePurchase();
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
                //gameView.setFeedbackArea(gameModel.printState()+"\n");
                //goToTheBottomOfTextField();
                break;
            case "Quit Game":
                gameModel.quitGame();
                break;
        }
    }

    private void goToTheBottomOfTextField() {
        gameView.getFeedbackArea().getCaret().setDot(Integer.MAX_VALUE);
    }
}
