package Model;

public interface ModelUpdateListener {
    void modelUpdated();

    void dieCount(int value, int position);

    void unlockPropertyBuy();

    void unlockUtilityBuy();

    void unlockRailroadBuy();

    void passTurn(int playerNumber);

    void taxProperty(int tax, Player ownedBy, int playerNumber, int balance);

    void confirmPurchase(int playerNumber, String name, int balance);

    void printState(int i, int balance, String toString, int balance1);

    void initializeGame(int numberOfPlayers, int playerNumber);

    void manualPassUpdate(int playerNumber);
}