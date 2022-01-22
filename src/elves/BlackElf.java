package elves;

import children.Child;
import enums.ElvesType;
import fileio.AnnualChildReport;
import santareplacer.Database;

public class BlackElf implements ChildVisitor{
    private Database santaDB;

    public BlackElf(Database santaDB) {
        this.santaDB = santaDB;
    }

    @Override
    public Double visit(Child child) {
        if (!(child.getElf().equals(ElvesType.BLACK))) {
            return null;
        }

        Double budget = santaDB.getBudgetUnit() * child.getAverageScore();
        budget = budget - budget * 30 / 100;
        return budget;
    }
}
