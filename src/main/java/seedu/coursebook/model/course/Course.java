package seedu.coursebook.model.course;

import static java.util.Objects.requireNonNull;
import static seedu.coursebook.commons.util.AppUtil.checkArgument;

/**
 * Represents a Course in the course book.
 * Guarantees: immutable; is valid as declared in {@link #isValidCourse(String)}
 */
public class Course {

    public static final String MESSAGE_CONSTRAINTS = "Course codes should be alphanumeric and can contain hyphens";
    public static final String VALIDATION_REGEX = "[\\p{Alnum}-]+";

    public final String courseCode;
    public final CourseColor color;

    /**
     * Constructs a {@code Course}.
     *
     * @param courseCode A valid course code.
     */
    public Course(String courseCode) {
        requireNonNull(courseCode);
        checkArgument(isValidCourseCode(courseCode), MESSAGE_CONSTRAINTS);
        this.courseCode = courseCode.toUpperCase();
        this.color = CourseColor.GREEN; // default for legacy construction
    }

    /**
     * Constructs a {@code Course} with explicit color.
     * If color is null, defaults to GREEN.
     */
    public Course(String courseCode, CourseColor color) {
        requireNonNull(courseCode);
        checkArgument(isValidCourseCode(courseCode), MESSAGE_CONSTRAINTS);
        this.courseCode = courseCode.toUpperCase();
        this.color = (color == null) ? CourseColor.GREEN : color;
    }

    /**
     * Returns true if a given string is a valid course code.
     */
    public static boolean isValidCourseCode(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Course)) {
            return false;
        }

        Course otherCourse = (Course) other;
        return courseCode.equals(otherCourse.courseCode);
    }

    @Override
    public int hashCode() {
        return courseCode.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + courseCode + ']';
    }
}
