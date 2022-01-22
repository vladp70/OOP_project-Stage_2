package children;

import elves.ChildVisitor;

public interface Visitable {
    Double accept(ChildVisitor visitor);
}
