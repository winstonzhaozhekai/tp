package seedu.coursebook.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import seedu.coursebook.commons.exceptions.IllegalValueException;
import seedu.coursebook.model.course.Course;

/**
 * Jackson-friendly version of {@link Course}.
 */
class JsonAdaptedCourse {

    private final String courseCode;

    /**
     * Constructs a {@code JsonAdaptedCourse} with the given {@code courseCode}.
     */
    @JsonCreator
    public JsonAdaptedCourse(String courseCode) {
        this.courseCode = courseCode;
    }

    /**
     * Converts a given {@code Course} into this class for Jackson use.
     */
    public JsonAdaptedCourse(Course source) {
        this.courseCode = source.courseCode;
    }

    @JsonValue
    public String getCourseCode() {
        return courseCode;
    }

    /**
     * Converts this Jackson-friendly adapted course object into the model's {@code Course} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted course.
     */
    public Course toModelType() throws IllegalValueException {
        if (!Course.isValidCourseCode(courseCode)) {
            throw new IllegalValueException(Course.MESSAGE_CONSTRAINTS);
        }
        return new Course(courseCode);
    }
}
