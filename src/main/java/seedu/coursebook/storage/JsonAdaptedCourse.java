package seedu.coursebook.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import seedu.coursebook.commons.exceptions.IllegalValueException;
import seedu.coursebook.model.course.Course;
import seedu.coursebook.model.course.CourseColor;

/**
 * Jackson-friendly version of {@link Course}.
 */
class JsonAdaptedCourse {

    private final String courseCode;
    private final String color; // optional for backward compatibility

    /**
     * Backward-compatible creator: a single string value.
     */
    @JsonCreator
    public JsonAdaptedCourse(String courseCode) {
        this.courseCode = courseCode;
        this.color = null;
    }

    /**
     * New-style creator with named properties.
     */
    @JsonCreator
    public JsonAdaptedCourse(@JsonProperty("courseCode") String courseCode,
                              @JsonProperty("color") String color) {
        this.courseCode = courseCode;
        this.color = color;
    }

    /**
     * Converts a given {@code Course} into this class for Jackson use.
     */
    public JsonAdaptedCourse(Course source) {
        this.courseCode = source.courseCode;
        this.color = source.color == null ? null : source.color.name();
    }

    @JsonValue
    @JsonIgnore
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
        CourseColor parsedColor = null;
        if (color != null && !color.trim().isEmpty()) {
            try {
                parsedColor = CourseColor.valueOf(color.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalValueException("Invalid course color: " + color);
            }
        }
        return new Course(courseCode, parsedColor);
    }
}
