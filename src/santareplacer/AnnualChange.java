package santareplacer;

import children.Child;
import gifts.Gift;

import java.util.List;

public final class AnnualChange {
    private Double newSantaBudget;
    private List<Gift> newGifts;
    private List<Child> newChildren;
    private List<ChildUpdate> childrenUpdates;

    public Double getNewSantaBudget() {
        return newSantaBudget;
    }

    public void setNewSantaBudget(final Double newSantaBudget) {
        this.newSantaBudget = newSantaBudget;
    }

    public List<Gift> getNewGifts() {
        return newGifts;
    }

    public void setNewGifts(final List<Gift> newGifts) {
        this.newGifts = newGifts;
    }

    public List<Child> getNewChildren() {
        return newChildren;
    }

    public void setNewChildren(final List<Child> newChildren) {
        this.newChildren = newChildren;
    }

    public List<ChildUpdate> getChildrenUpdates() {
        return childrenUpdates;
    }

    public void setChildrenUpdates(final List<ChildUpdate> childrenUpdates) {
        this.childrenUpdates = childrenUpdates;
    }
}
