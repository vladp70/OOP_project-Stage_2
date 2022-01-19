package main;

import checker.Checker;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.Constants;
import fileio.Input;
import fileio.Output;
import santareplacer.Database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Class used to run the code
 */
public final class Main {

    private Main() {
        ///constructor for checkstyle
    }
    /**
     * This method is used to call the checker which calculates the score
     * @param args
     *          the arguments used to call the main method
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.OUTPUT_DIR);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File[] outputDirectory = new File(Constants.OUTPUT_DIR).listFiles();
        if (outputDirectory != null) {
            for (File file : outputDirectory) {
                if (!file.delete()) {
                    System.out.println("Could not be deleted");
                }
            }
        }

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            String testNumber = file.getName().replaceAll("[^0-9]", "");
            String filepath = Constants.OUTPUT_PATH + testNumber
                    + Constants.FILE_EXTENSION;
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * Main logic of the program that reads data from the input file, simulates the rounds
     * and writes the result in the output file
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Input input = null;
        try {
            input = objectMapper.readValue(new File(filePath1), Input.class);
        } catch (Exception e) {
            System.out.println("It happens to the best of us! :(");
        }
        Output output = new Output();

        Database database = Database.getInstance();
        database.initDatabase(input);

        for (int i = 0; i <= database.getNumberOfYears(); i++) {
            output.addYearlyReport(database.simulateRound(i));
        }

        output.writeToFile(filePath2);
    }
}
