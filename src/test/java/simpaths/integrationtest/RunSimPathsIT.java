package simpaths.integrationtest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class RunSimPathsIT {
    @Test
    @DisplayName("Initial database setup runs successfully")
    @Order(1)
    void testRunSetup() {
        runCommand(
            "java", "-jar", "singlerun.jar",
            "-c", "UK",
            "-s", "2017",
            "-Setup",
            "-g", "false",
            "--rewrite-policy-schedule"
        );
    }

    @Test
    @DisplayName("Database and configuration files are created")
    @Order(2)
    void testVerifySetupOutput() {
        assertFileExists("input/input.mv.db");
        assertFileExists("input/EUROMODpolicySchedule.xlsx");
        assertFileExists("input/DatabaseCountryYear.xlsx");
    }

    @Test
    @DisplayName("Simulation runs successfully")
    @Order(3)
    void testRunSimulation() {
        runCommand(
            "java", "-jar", "multirun.jar",
            "-p", "5000",
            "-s", "2019",
            "-e", "2022",
            "-r", "100",
            "-n", "2",
            "-g", "false"
        );
    }

    @Test
    @DisplayName("Simulation runs successfully")
    @Order(4)
    void testVerifySimulationOutput() throws IOException {
        Path outputDir = Paths.get("output");

        Path latestOutputDir = Files.list(outputDir)
                .filter(Files::isDirectory)
                .max(Comparator.comparingLong(p -> p.toFile().lastModified()))
                .get();

        Path statisticsFile = latestOutputDir.resolve("csv/Statistics1.csv");
        assertTrue(Files.exists(statisticsFile), "Expected output file is missing: " + statisticsFile);

        Path expectedFile = Paths.get("src/test/java/simpaths/integrationtest/expected/Statistics1.csv");
        assertEquals(-1, Files.mismatch(statisticsFile, expectedFile), fileMismatchMessage(statisticsFile, expectedFile));
    }

    String fileMismatchMessage(Path actualOutput, Path expectedOutput) {
        return String.format("""

            The actual output from the integration test does not match the expected output.

            The most recent output file

                %s

            was compared to the expected output file

                %s
            
            and differences were found.

            IF THIS IS EXPECTED - for example, if you have changed substantive processes within the model
            or the structure of the output, please:

            1. Verify that the output is correct and as expected.

            2. Replace the expected output file with the new output file:
                cp %s %s

            3. Commit this change to Git, so that the changes are visible in your pull request and this test passes.

            """, actualOutput, expectedOutput, actualOutput, expectedOutput);
    }

    private void runCommand(String... args) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command(args);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line); // Log output to console when running in Maven
                }
            }
            int exitCode = process.waitFor();

            assertEquals(0, exitCode, "Process exited with error code: " + exitCode);
        } catch (Exception e) {
            throw new RuntimeException("Failed to run: " + e.getMessage(), e);
        }
    }

    private void assertFileExists(String path) {
        assertTrue(new File(path).exists(), "Missing file " + path);
    }
}
