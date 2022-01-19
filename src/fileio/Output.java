package fileio;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Output {
    private List<YearlyOutput> annualChildren = new ArrayList<>();

    public Output() {
    }

    public List<YearlyOutput> getAnnualChildren() {
        return annualChildren;
    }

    public void setAnnualChildren(final List<YearlyOutput> annualChildren) {
        this.annualChildren = annualChildren;
    }

    /**
     * Adds a single child report in the last yearly output stored in the list
     * @param report of the child to be added
     */
    public void addChildReport(final AnnualChildReport report) {
        annualChildren.get(annualChildren.size() - 1).addReport(report);
    }

    /**
     * Adds the (assumed fully-formed) report in the output hierarchy as a new yearly output
     * @param report to be added
     */
    public void addYearlyReport(final List<AnnualChildReport> report) {
        annualChildren.add(new YearlyOutput(report));
    }

    /**
     * "Moves" to the next year in the list of reports
     */
    public void addNewYear() {
        annualChildren.add(new YearlyOutput());
    }

    /**
     * Writes the output (this object) in a file then closes it
     * @param outputPath of the file the output should be written in
     * @throws IOException in case of exceptions to reading / writing
     */
    public void writeToFile(final String outputPath) throws IOException {
        FileWriter file = new FileWriter(outputPath);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);

        try {
            file.write(jsonString);
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
