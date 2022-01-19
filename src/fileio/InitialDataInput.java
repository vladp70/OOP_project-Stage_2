package fileio;

import children.Child;
import gifts.Gift;

import java.util.List;

public final class InitialDataInput {
    private List<Child> children;
    private List<Gift> santaGiftsList;

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(final List<Child> children) {
        this.children = children;
    }

    public List<Gift> getSantaGiftsList() {
        return santaGiftsList;
    }

    public void setSantaGiftsList(final List<Gift> santaGiftsList) {
        this.santaGiftsList = santaGiftsList;
    }

    @Override
    public String toString() {
        return "InitialDataInput{\n"
                + "children=" + children
                + ",\nsantaGiftsList=" + santaGiftsList
                + '}';
    }
}
