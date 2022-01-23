package santareplacer;

public interface GiftingStrategy {
    /**
     * Sorts the children in the database based on strategy and calls database
     * to assign gifts in the new order using the priorly calculated budgets
     * @param santaDB the database the gifting strategy operates on
     */
    void apply(Database santaDB);
}
