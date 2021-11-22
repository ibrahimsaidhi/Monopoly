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
                goToTheBottomOfTextField();

                break;

            case "Buy":
                gameModel.makePurchase();
                gameView.unlockRollDieButton();
                goToTheBottomOfTextField();
                break;
            case "Pass Turn":
                gameModel.manualPass();
                gameView.lockBuyButton();
                gameView.unlockRollDieButton();
                goToTheBottomOfTextField();

                break;
            case "State":

                break;
            case "Buy/Sell House":
                gameModel.checkingForHouseEligibility();
                String input = gameView.requestingHouseStatus();
                gameModel.buyingHouseEligibility();
                if (((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition())).getHouses().size() == 4){
                    JOptionPane.showMessageDialog(gameView, "Sorry, you can only add up to 4 houses to a property.");
                }

                else if (input.equals("buy")){
                    gameModel.purchaseAHouse();
                }
                else if (input.equals("sell") && !((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition())).getHouses().isEmpty()){
                    gameModel.sellAHouse();
                }
                else if (input.equals("sell") && ((Property) gameModel.getBoard().getIndex(gameModel.getCurrentPlayer().getPosition())).getHouses().isEmpty()) {
                    JOptionPane.showMessageDialog(gameView, "There are no houses for you to sell");
                }
                break;
            case "Buy/Sell Hotel":

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
