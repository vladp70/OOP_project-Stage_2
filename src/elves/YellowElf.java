package elves;

import children.Child;
import enums.ElvesType;
import gifts.Gift;
import santareplacer.Database;

public final class YellowElf implements ChildVisitor {
    private Database santaDB;

    public YellowElf(final Database santaDB) {
        this.santaDB = santaDB;
    }

    @Override
    public Double visit(final Child child) {
        if (!(child.getElf().equals(ElvesType.YELLOW))) {
            return null;
        }

        Double budget = santaDB.getBudgetUnit() * child.getAverageScore();
        if (child.getReceivedGifts().isEmpty()) {
            Gift possibleGift =
                    santaDB.findCheapestGiftByCategory(child.getGiftsPreferences().get(0), false);
            if (possibleGift != null && possibleGift.getQuantity() > 0) {
                child.receiveGift(possibleGift);
                possibleGift.decQuantity();
            }
        }
        return budget;
    }
}
