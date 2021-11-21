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

                gameModel.manualPass();


                break;
            case "State":

                break;
            case "Quit Game":
                gameModel.quitGame();
                break;
        }
    }


}
