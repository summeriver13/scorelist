package scorelist;

import java.io.PrintStream;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public final class Cli {

  private static final int MENU_EXIT = 0;
  private static final int MENU_MAX = 7;

  private final Scanner in;
  private final PrintStream out;
  private final ScoreTable table = new ScoreTable();

  public Cli(Scanner in, PrintStream out) {
    this.in = in;
    this.out = out;
  }

  public void run() {
    while (in.hasNextLine()) {
      printMenu();
      int choice = readInt("Enter your choice: ", MENU_EXIT, MENU_MAX);
      switch (choice) {
        case 1 -> showAll();
        case 2 -> add();
        case 3 -> update();
        case 4 -> delete();
        case 5 -> showStats();
        case 6 -> showStudentScores();
        case 7 -> showSorted();
        default -> {
          out.println("Bye.");
          return;
        }
      }
    }
  }

  private void showAll() {
    if (table.size() == 0) {
      out.println("No records yet.");
      return;
    }
    printRows(table.all());
  }

  private void add() {
    String courseName = readText("Course: ");
    String studentName = readText("Student name: ");
    String studentId = readText("Student id: ");
    double score = readScore();
    try {
      table.add(new Score(studentId, studentName, courseName, score));
      out.println("Added.");
    } catch (IllegalStateException duplicate) {
      out.println("Record already exists, use update instead.");
    }
  }

  private void update() {
    String studentId = readText("Student id: ");
    String courseName = readText("Course: ");
    double score = readScore();
    out.println(table.update(studentId, courseName, score) ? "Updated." : "Record not found.");
  }

  private void delete() {
    String studentId = readText("Student id: ");
    String courseName = readText("Course: ");
    out.println(table.delete(studentId, courseName) ? "Deleted." : "Record not found.");
  }

  private void showStats() {
    String courseName = readText("Course: ");
    Optional<CourseStats> stats = table.stats(courseName);
    if (stats.isEmpty()) {
      out.println("No records for this course.");
      return;
    }
    CourseStats value = stats.get();
    out.printf(
        "count=%d average=%.2f max=%.1f min=%.1f%n",
        value.count(), value.average(), value.max(), value.min());
  }

  private void showStudentScores() {
    String studentId = readText("Student id: ");
    List<Score> found = table.byStudent(studentId);
    if (found.isEmpty()) {
      out.println("Record not found.");
      return;
    }
    printRows(found);
  }

  private void showSorted() {
    if (table.size() == 0) {
      out.println("No records yet.");
      return;
    }
    printRows(table.sortedByScoreDesc());
  }

  private void printRows(List<Score> rows) {
    out.printf("%-10s%-10s%-10s%8s%n", "COURSE", "NAME", "ID", "SCORE");
    for (Score score : rows) {
      out.printf(
          "%-10s%-10s%-10s%8.1f%n",
          score.courseName(), score.studentName(), score.studentId(), score.score());
    }
  }

  private void printMenu() {
    out.println("1 show all  2 add  3 update  4 delete");
    out.println("5 course stats  6 student scores  7 sort by score  0 exit");
  }

  private int readInt(String prompt, int min, int max) {
    while (true) {
      String raw = readText(prompt);
      try {
        int value = Integer.parseInt(raw);
        if (value >= min && value <= max) {
          return value;
        }
      } catch (NumberFormatException ignored) {
        // fall through to the re-prompt below
      }
      out.printf("Expected an integer in %d..%d, got '%s'.%n", min, max, raw);
    }
  }

  private double readScore() {
    while (true) {
      String raw =
          readText("Score (" + (int) Score.MIN_SCORE + "-" + (int) Score.MAX_SCORE + "): ");
      try {
        double value = Double.parseDouble(raw);
        if (value >= Score.MIN_SCORE && value <= Score.MAX_SCORE) {
          return value;
        }
      } catch (NumberFormatException ignored) {
        // fall through to the re-prompt below
      }
      out.printf(
          "Expected a number in %d..%d, got '%s'.%n",
          (int) Score.MIN_SCORE, (int) Score.MAX_SCORE, raw);
    }
  }

  private String readText(String prompt) {
    while (true) {
      out.print(prompt);
      if (!in.hasNextLine()) {
        throw new IllegalStateException("input exhausted");
      }
      String line = in.nextLine().trim();
      if (!line.isEmpty()) {
        return line;
      }
      out.println("Input must not be empty.");
    }
  }
}
