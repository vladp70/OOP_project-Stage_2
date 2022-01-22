package elves;

import children.Child;
import enums.ElvesType;
import fileio.AnnualChildReport;
import gifts.Gift;
import santareplacer.Database;

public class YellowElf implements ChildVisitor{
    private Database santaDB;

    public YellowElf(Database santaDB) {
        this.santaDB = santaDB;
    }

    @Override
    public Double visit(Child child) {
        if (!(child.getElf().equals(ElvesType.YELLOW))) {
            return null;
        }

        Double budget = santaDB.getBudgetUnit() * child.getAverageScore();
        if (child.getReceivedGifts().isEmpty()) {
            Gift possibleGift =
                    santaDB.findCheapestGiftByCategory(child.getGiftsPreferences().get(0));
            if (possibleGift != null && possibleGift.getQuantity() > 0) {
                child.receiveGift(possibleGift);
                possibleGift.decQuantity();
            }
        }
        return budget;
    }
}
