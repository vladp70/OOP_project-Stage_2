package elves;

import children.Child;
import enums.ElvesType;
import santareplacer.Database;

public final class WhiteElf implements ChildVisitor {
    private Database santaDB;

    public WhiteElf(final Database santaDB) {
        this.santaDB = santaDB;
    }

    @Override
    public Double visit(final Child child) {
        if (!(child.getElf().equals(ElvesType.WHITE))) {
            return null;
        }

        return santaDB.getBudgetUnit() * child.getAverageScore();
    }
}
