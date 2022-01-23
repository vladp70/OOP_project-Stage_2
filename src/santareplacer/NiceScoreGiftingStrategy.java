package santareplacer;

import children.Child;

import java.util.Collections;
import java.util.Comparator;

public final class NiceScoreGiftingStrategy implements GiftingStrategy {
    @Override
    public void apply(final Database santaDB) {
        Collections.sort(santaDB.getChildren(), new Comparator<Child>() {
            @Override
            public int compare(final Child o1, final Child o2) {
                Double niceScore1 = o1.getAverageScore();
                Double niceScore2 = o2.getAverageScore();
                if (niceScore1.equals(niceScore2)) {
                    return o1.getId().compareTo(o2.getId());
                } else {
                    return niceScore2.compareTo(niceScore1);
                }
            }
        });

        for (var child : santaDB.getChildren()) {
            santaDB.assignGiftsToChild(child, santaDB.getChildrenBudgets().get(child));
        }
    }
}
