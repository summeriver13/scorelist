package scorelist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ScoreTableTest {

    private static Score score(String id, String name, String course, double value) {
        return new Score(id, name, course, value);
    }

    @Test
    void keepsInsertionOrder() {
        ScoreTable table = new ScoreTable();
        table.add(score("1001", "Ann", "math", 70));
        table.add(score("1002", "Bob", "math", 90));
        assertEquals(List.of("1001", "1002"), table.all().stream().map(Score::studentId).toList());
    }

    @Test
    void rejectsDuplicateStudentCoursePair() {
        ScoreTable table = new ScoreTable();
        table.add(score("1001", "Ann", "math", 70));
        assertThrows(IllegalStateException.class, () -> table.add(score("1001", "Ann", "math", 80)));
        assertEquals(1, table.size());
    }

    @Test
    void updatesExistingRecordAndKeepsName() {
        ScoreTable table = new ScoreTable();
        table.add(score("1001", "Ann", "math", 70));
        assertTrue(table.update("1001", "math", 85));
        Score updated = table.all().get(0);
        assertEquals(85, updated.score());
        assertEquals("Ann", updated.studentName());
    }

    @Test
    void updateReportsMissingRecordAndRejectsInvalidScore() {
        ScoreTable table = new ScoreTable();
        assertFalse(table.update("1001", "math", 85));
        table.add(score("1001", "Ann", "math", 70));
        assertThrows(IllegalArgumentException.class, () -> table.update("1001", "math", 120));
        assertEquals(70, table.all().get(0).score());
    }

    @Test
    void deleteRemovesOnlyTheMatchingRecord() {
        ScoreTable table = new ScoreTable();
        table.add(score("1001", "Ann", "math", 70));
        table.add(score("1001", "Ann", "physics", 60));
        assertTrue(table.delete("1001", "math"));
        assertFalse(table.delete("1001", "math"));
        assertEquals(List.of("physics"), table.all().stream().map(Score::courseName).toList());
        assertEquals(1, table.size());
    }

    @Test
    void filtersByStudent() {
        ScoreTable table = new ScoreTable();
        table.add(score("1001", "Ann", "math", 70));
        table.add(score("1002", "Bob", "math", 60));
        table.add(score("1001", "Ann", "physics", 80));
        assertEquals(List.of("math", "physics"), table.byStudent("1001").stream().map(Score::courseName).toList());
        assertTrue(table.byStudent("9999").isEmpty());
    }

    @Test
    void computesCourseStatistics() {
        ScoreTable table = new ScoreTable();
        table.add(score("1001", "Ann", "math", 80));
        table.add(score("1002", "Bob", "math", 90));
        table.add(score("1003", "Cid", "math", 100));
        table.add(score("1001", "Ann", "physics", 0));
        Optional<CourseStats> stats = table.stats("math");
        assertTrue(stats.isPresent());
        assertEquals(3, stats.get().count());
        assertEquals(90.0, stats.get().average(), 1e-9);
        assertEquals(100.0, stats.get().max());
        assertEquals(80.0, stats.get().min());
    }

    @Test
    void statisticsCoverRangeBoundariesAndUnknownCourse() {
        ScoreTable table = new ScoreTable();
        table.add(score("1001", "Ann", "math", 0));
        table.add(score("1002", "Bob", "math", 100));
        assertTrue(table.stats("physics").isEmpty());
        assertEquals(100.0, table.stats("math").orElseThrow().max());
        assertEquals(0.0, table.stats("math").orElseThrow().min());
    }

    @Test
    void sortsWithoutMutatingInsertionOrder() {
        ScoreTable table = new ScoreTable();
        table.add(score("1001", "Ann", "math", 70));
        table.add(score("1002", "Bob", "math", 95));
        table.add(score("1003", "Cid", "math", 80));
        assertEquals(List.of("1002", "1003", "1001"),
                table.sortedByScoreDesc().stream().map(Score::studentId).toList());
        assertEquals(List.of("1001", "1002", "1003"), table.all().stream().map(Score::studentId).toList());
    }
}
