package Controller;

import Game.Command;
import Model.Game;
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
                //gameModel.moveToken();
                int diceRoll = gameModel.rollDie();
                gameModel.setCurrentPlayerPosition(diceRoll);
                JOptionPane.showMessageDialog(null, "You have rolled two die that added up to " + diceRoll);
                gameView.setFeedbackArea("\nCurrently turn of: Player " + gameModel.getCurrentPlayer().getPlayerNumber() + "\n");
                int pos = gameModel.getCurrentPlayerPosition();
                gameView.setFeedbackArea("Your new position is now " + pos);
                goToTheBottomOfTextField();
                gameModel.passTurn();
                break;
            case "Pass Turn":
                gameView.setFeedbackArea("\nPlayer # " + gameModel.getCurrentPlayer().getPlayerNumber() + " has passed their turn\n");
                gameModel.passTurn();
                gameView.setFeedbackArea("\n!*-----------------------------------------------NEW TURN!-------------------------------------------------------*!");
                gameView.setFeedbackArea("\nPlayer " + gameModel.getCurrentPlayer().getPlayerNumber() + " it is now your turn");
                goToTheBottomOfTextField();
                break;
            case "State":
                gameView.setFeedbackArea("\nCurrent Player: " + gameModel.getCurrentPlayer().getName() + "Properties owned: " + gameModel.getCurrentPlayer().getOwnedProperties().toString());
                goToTheBottomOfTextField();
                break;
            case "Quit Game":
                gameView.setFeedbackArea("Quit game has been called!\n");
                goToTheBottomOfTextField();
                gameModel.quitGame();
                break;
        }
    }

    private void goToTheBottomOfTextField() {
        gameView.getFeedbackArea().getCaret().setDot(Integer.MAX_VALUE);
    }
}
