package scorelist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class CliTest {

    private static String run(String script) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (PrintStream out = new PrintStream(buffer, true, StandardCharsets.UTF_8)) {
            new Cli(new Scanner(script), out).run();
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    @Test
    void printsScoreWhenSearchingExistingStudent() {
        String output = run("2\nmath\nAnn\n1001\n90\n6\n1001\n0\n");
        assertTrue(output.contains("90.0"), output);
    }

    @Test
    void listsAndSortsRecords() {
        String output = run("2\nmath\nAnn\n1001\n70\n2\nmath\nBob\n1002\n95\n7\n0\n");
        assertTrue(output.indexOf("1002") < output.indexOf("1001"), output);
    }

    @Test
    void rejectsDuplicateRecord() {
        String output = run("2\nmath\nAnn\n1001\n70\n2\nmath\nAnn\n1001\n80\n0\n");
        assertTrue(output.contains("already exists"), output);
    }

    @Test
    void updatesAndReportsCourseStatistics() {
        String output = run("2\nmath\nAnn\n1001\n70\n2\nmath\nBob\n1002\n90\n3\n1001\nmath\n80\n5\nmath\n0\n");
        assertTrue(output.contains("count=2 average=85.00 max=90.0 min=80.0"), output);
    }

    @Test
    void deletesAndThenMisses() {
        String output = run("2\nmath\nAnn\n1001\n70\n4\n1001\nmath\n6\n1001\n0\n");
        assertTrue(output.contains("Deleted."), output);
        assertTrue(output.contains("Record not found."), output);
    }

    @Test
    void repromptsOnInvalidMenuChoiceAndScore() {
        String output = run("x\n9\n2\nmath\nAnn\n1001\n150\n90\n1\n0\n");
        assertTrue(output.contains("Expected an integer in 0..7"), output);
        assertTrue(output.contains("Expected a number in 0..100"), output);
        assertTrue(output.contains("90.0"), output);
    }

    @Test
    void exitsOnZero() {
        String output = run("0\n1\n");
        assertTrue(output.contains("Bye."), output);
        assertFalse(output.contains("COURSE"), output);
    }

    @Test
    void emptyLineIsRejected() {
        String output = run("\n2\nmath\nAnn\n1001\n90\n1\n0\n");
        assertTrue(output.contains("Input must not be empty."), output);
        assertTrue(output.contains("Added."), output);
        assertEquals(1, output.split("COURSE").length - 1, output);
    }
}
