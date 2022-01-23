package elves;

import children.Child;

public interface ChildVisitor {
    /**
     * Method that elves need to implement to assign gifts
     * @param child for the elf to assign gifts to
     * @return the assigned budget for the child according
     * to each elf's strategy or null if the elf was
     * not responsible for the given child
     */
    Double visit(Child child);
}
