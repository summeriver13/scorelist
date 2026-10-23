package scorelist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ScoreTest {

  @Test
  void rejectsBlankFields() {
    assertThrows(IllegalArgumentException.class, () -> new Score(" ", "Ann", "math", 90));
    assertThrows(IllegalArgumentException.class, () -> new Score("1001", "", "math", 90));
    assertThrows(IllegalArgumentException.class, () -> new Score("1001", "Ann", null, 90));
  }

  @Test
  void rejectsScoresOutsideRange() {
    assertThrows(IllegalArgumentException.class, () -> new Score("1001", "Ann", "math", -0.5));
    assertThrows(IllegalArgumentException.class, () -> new Score("1001", "Ann", "math", 100.5));
    assertThrows(
        IllegalArgumentException.class, () -> new Score("1001", "Ann", "math", Double.NaN));
  }

  @Test
  void acceptsBoundaries() {
    assertEquals(0.0, new Score("1001", "Ann", "math", 0.0).score());
    assertEquals(100.0, new Score("1001", "Ann", "math", 100.0).score());
  }

  @Test
  void trimsFields() {
    Score score = new Score("  1001  ", " Ann ", " math ", 90);
    assertEquals("1001", score.studentId());
    assertEquals("Ann", score.studentName());
    assertEquals("math", score.courseName());
  }
}
