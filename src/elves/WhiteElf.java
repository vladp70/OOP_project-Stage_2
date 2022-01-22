package elves;

import children.Child;
import enums.ElvesType;
import fileio.AnnualChildReport;
import santareplacer.Database;

public class WhiteElf implements ChildVisitor{
    private Database santaDB;

    public WhiteElf(Database santaDB) {
        this.santaDB = santaDB;
    }

    @Override
    public Double visit(Child child) {
        if (!(child.getElf().equals(ElvesType.WHITE))) {
            return null;
        }

        return santaDB.getBudgetUnit() * child.getAverageScore();
    }
}
