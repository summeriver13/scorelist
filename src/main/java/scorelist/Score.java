package scorelist;

public record Score(String studentId, String studentName, String courseName, double score) {

    public static final double MIN_SCORE = 0.0;
    public static final double MAX_SCORE = 100.0;

    public Score {
        studentId = requireText(studentId, "studentId");
        studentName = requireText(studentName, "studentName");
        courseName = requireText(courseName, "courseName");
        if (!(score >= MIN_SCORE && score <= MAX_SCORE)) {
            throw new IllegalArgumentException(
                    "score must be within " + MIN_SCORE + ".." + MAX_SCORE + ", got " + score);
        }
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }
}
