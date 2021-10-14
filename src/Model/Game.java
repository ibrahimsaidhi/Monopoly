package Model;

import Game.Command;
import Game.Parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    private int currentPlayer = 0;
    private int initialAmountOfMoney;
    private List<Player> players;

    private int numberOfPlayers;
    private Info myInfo;
    private InputStream inputStream;

    public Game() {
        parser = new Parser();
    }


    private void printCurrentPlayer() {
        System.out.println("\n!*-----------------------------------------------NEW TURN!-------------------------------------------------------*!");
        System.out.println("The current player is player-" + (currentPlayer + 1) + "\n");
    }

    public boolean processCommand(Command command) {

        boolean wantToQuit = false;

        if (command.isUnknown()) {
            System.out.println("Unknown command");
            return false;
        }

        String commandWord = command.getCommandWord();
        switch (commandWord) {
            case "move":
                moveToken(command);
                break;
            case "pass":
                passTurn();
                break;
            case "buy":
                buyProperty();
                break;
            case "quit":
                wantToQuit = true;
                break;
        }

        return wantToQuit;
    }

    private void printState() {
        /**
         * @author John Afolayan
         *
         * Print a representation of the game's state
         *
         */
        System.out.println(this.myInfo);
    }

    private void passTurn() {
        /**
         * @author John Afolayan
         *
         * Pass the turn to the next player
         *
         */
        this.currentPlayer = (this.currentPlayer == this.numberOfPlayers) ? 0 : this.currentPlayer + 1;
        newTurn();
    }

    private void newTurn() {
        printCurrentPlayer();
        parser.showCommands();
    }

    private void initializePlayers() {
        /**
         * @author John Afolayan
         *
         * Ask the user for the number of players that will be playing, and then calls setup() to
         * determine how much money/items each player will start off with.
         *
         */
        Scanner sc = new Scanner(System.in);
        this.numberOfPlayers = sc.nextInt();
        boolean correctNumberOfPlayers;
        do {
            if (numberOfPlayers <= 8 && numberOfPlayers >= 2) {
                correctNumberOfPlayers = true;

            } else {
                correctNumberOfPlayers = false;
                System.out.println("The number of players allowed is 2,3,4,5,6,7 or 8 players. Please try again.");
                numberOfPlayers = sc.nextInt();

            }
        } while (!correctNumberOfPlayers);
        createPlayers(numberOfPlayers, setup(numberOfPlayers));
    }

    private int setup(int numberOfPlayers) {
        initialAmountOfMoney = 1500;
        return initialAmountOfMoney;
    }

    private void createPlayers(int numberOfPlayers) {
        players = new ArrayList<Player>();
        for (int i = 1; i <= numberOfPlayers; i++) {
            players.add(new Player(i));
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    private void printListOfCurrentPlayerStats() {
        System.out.println("Player " + (currentPlayer + 1) + " currently owns the following properties: ");
        System.out.println(players.get(currentPlayer).getMyProperties().toString());
    }

    private void moveToken(Command command) {
        /**
         * @author John Afolayan
         *
         * Checks the syntax of the command passed and moves token n amount of times
         * where n is the value which is rolled on a dice.
         *
         */
        if (!checkCommandSyntax(command)) return;


    }
    
    //Unfinished Implementation of move outcome
    private List<Integer> checkOutcomeOfMove(List<Integer> playerDiceResults) {
        int highestRoll = 0;
        int secondHighestRoll = 0;
        System.out.println("\n******************************Dice Rolled!******************************");
        System.out.println("Current player's dice roll :" + playerDiceResults);

        for (Integer i : playerDiceResults) {
            if (i.intValue() >= highestRoll) {
                secondHighestRoll = highestRoll;
                highestRoll = i.intValue();
            }
            if (i.intValue() > secondHighestRoll && i.intValue() != highestRoll)
                secondHighestRoll = i.intValue();
            System.out.println("Checking dice; Highest roll is = " + highestRoll + " Second highest roll is = " + secondHighestRoll);
        }
        return null;
    }

    private List<Integer> rollDice(Integer numberOfDiceToRoll) {
        List<Integer> diceRolls = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < numberOfDiceToRoll; i++) {
            int diceRoll = random.nextInt(6) + 1;
            diceRolls.add(diceRoll);
        }
        return diceRolls;
    }

    private boolean checkCommandSyntax(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Which property are you purchasing?");
            return false;
        }
        return true;
    }

    public void play() {
        /**
         * @author John Afolayan
         *
         * The main loop of the game. Takes user input until the user inputs Quit.
         *
         */
        boolean startedGame = false;
        System.out.println("Welcome to Monopoly! How many players will be playing today? This version of Monopoly can hold up to 8 players.");
        do {
            try {
                startGame();
                startedGame = true;
            } catch (Exception exception) {
                System.err.println("Please enter a valid integer for the number of players. Your options are 2,3,4,5,6,7,8. ");
            }
        }
        while (!startedGame);
        boolean finished = false;
        while (!finished) {
            try {
                Command command = parser.getCommand(this.currentPlayer);
                finished = processCommand(command);
            } catch (Exception exception) {
                System.err.println("You have encountered an error :/\n Please report it to the developer");
                exception.printStackTrace();
            }
        }
        System.out.println("Thank you for playing!");
    }

    public void startGame() {
        initializePlayers();
        System.out.println("There will be " + numberOfPlayers + " players this game!");
        newTurn();

    }

    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }
}
