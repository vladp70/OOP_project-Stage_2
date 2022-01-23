package elves;

import children.Child;
import common.Constants;
import enums.ElvesType;
import santareplacer.Database;

public final class BlackElf implements ChildVisitor {
    private Database santaDB;

    public BlackElf(final Database santaDB) {
        this.santaDB = santaDB;
    }

    @Override
    public Double visit(final Child child) {
        if (!(child.getElf().equals(ElvesType.BLACK))) {
            return null;
        }

        Double budget = santaDB.getBudgetUnit() * child.getAverageScore();
        budget = budget - budget * Constants.ELF_PERCENTAGE_CHANGE / Constants.MAX_PERCENTAGE;
        return budget;
    }
}
