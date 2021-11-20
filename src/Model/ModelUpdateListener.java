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
}