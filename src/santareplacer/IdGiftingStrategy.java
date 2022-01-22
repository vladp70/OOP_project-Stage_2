package santareplacer;

import children.Child;

import java.util.Collections;
import java.util.Comparator;

public class IdGiftingStrategy implements GiftingStrategy{
    @Override
    public void apply(Database santaDB) {
        Collections.sort(santaDB.getChildren(), new Comparator<Child>() {
            @Override
            public int compare(Child o1, Child o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });

        for (var child : santaDB.getChildren()) {
            santaDB.assignGiftsToChild(child, santaDB.getChildrenBudgets().get(child));
        }
    }
}
