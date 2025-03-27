package simpaths;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class RunSimpathsIT {
    @Test
    void testEndToEnd() {
        runCommand(
            "java", "-jar", "singlerun.jar",
            "-c", "UK",
            "-s", "2017",
            "-Setup",
            "-g", "false",
            "--rewrite-policy-schedule"
        );

        assertFileExists("input/input.mv.db");
        assertFileExists("input/EUROMODpolicySchedule.xlsx");
        assertFileExists("input/DatabaseCountryYear.xlsx");

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
