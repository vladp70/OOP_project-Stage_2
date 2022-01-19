package fileio;

import java.util.ArrayList;
import java.util.List;

public final class YearlyOutput {
    private List<AnnualChildReport> children;

    public YearlyOutput() {
        children = new ArrayList<>();
    }

    public YearlyOutput(final List<AnnualChildReport> children) {
        this.children = children;
    }

    public List<AnnualChildReport> getChildren() {
        return children;
    }

    public void setChildren(final List<AnnualChildReport> children) {
        this.children = children;
    }

    public void addReport(final AnnualChildReport report) {
        children.add(report);
    }
}
