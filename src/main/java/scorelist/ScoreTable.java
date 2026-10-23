package scorelist;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public final class ScoreTable {

    private final List<Score> scores = new ArrayList<>();

    public void add(Score score) {
        if (find(score.studentId(), score.courseName()).isPresent()) {
            throw new IllegalStateException(
                    "duplicate record for student " + score.studentId() + " / " + score.courseName());
        }
        scores.add(score);
    }

    public boolean update(String studentId, String courseName, double newScore) {
        OptionalInt index = find(studentId, courseName);
        if (index.isEmpty()) {
            return false;
        }
        Score current = scores.get(index.getAsInt());
        scores.set(index.getAsInt(),
                new Score(current.studentId(), current.studentName(), current.courseName(), newScore));
        return true;
    }

    public boolean delete(String studentId, String courseName) {
        OptionalInt index = find(studentId, courseName);
        if (index.isEmpty()) {
            return false;
        }
        scores.remove(index.getAsInt());
        return true;
    }

    public List<Score> all() {
        return List.copyOf(scores);
    }

    public List<Score> byStudent(String studentId) {
        return scores.stream().filter(score -> score.studentId().equals(studentId)).toList();
    }

    public Optional<CourseStats> stats(String courseName) {
        List<Score> matching = scores.stream().filter(score -> score.courseName().equals(courseName)).toList();
        if (matching.isEmpty()) {
            return Optional.empty();
        }
        double sum = 0.0;
        double max = Double.NEGATIVE_INFINITY;
        double min = Double.POSITIVE_INFINITY;
        for (Score score : matching) {
            sum += score.score();
            max = Math.max(max, score.score());
            min = Math.min(min, score.score());
        }
        return Optional.of(new CourseStats(courseName, matching.size(), sum / matching.size(), max, min));
    }

    public List<Score> sortedByScoreDesc() {
        return scores.stream().sorted(Comparator.comparingDouble(Score::score).reversed()).toList();
    }

    public int size() {
        return scores.size();
    }

    private OptionalInt find(String studentId, String courseName) {
        for (int i = 0; i < scores.size(); i++) {
            Score score = scores.get(i);
            if (score.studentId().equals(studentId) && score.courseName().equals(courseName)) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }
}
