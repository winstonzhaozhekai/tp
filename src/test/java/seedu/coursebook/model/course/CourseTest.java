package seedu.coursebook.model.course;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.coursebook.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class CourseTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Course(null));
        assertThrows(NullPointerException.class, () -> new Course(null, CourseColor.BLUE));
    }

    @Test
    public void constructor_invalidCourseCode_throwsIllegalArgumentException() {
        String invalidCourseCode = "";
        assertThrows(IllegalArgumentException.class, () -> new Course(invalidCourseCode));
        assertThrows(IllegalArgumentException.class, () -> new Course(invalidCourseCode, CourseColor.RED));
    }

    @Test
    public void constructor_uppercasesCourseCode() {
        // lowercase course code -> uppercased
        assertEquals("CS2103T", new Course("cs2103t").courseCode);
        assertEquals("CS2103T", new Course("cs2103t", CourseColor.BLUE).courseCode);

        // mixed case course code -> uppercased
        assertEquals("CS2103T", new Course("Cs2103t").courseCode);
        assertEquals("CS2103T", new Course("Cs2103T", CourseColor.RED).courseCode);

        // uppercase course code -> remains uppercase
        assertEquals("CS2103T", new Course("CS2103T").courseCode);
        assertEquals("CS2103T", new Course("CS2103T", CourseColor.GREEN).courseCode);

        // course code with hyphens -> uppercased
        assertEquals("CS2103T-L1", new Course("cs2103t-l1").courseCode);
        assertEquals("CS2103T-L1", new Course("cs2103t-l1", CourseColor.PURPLE).courseCode);
    }

    @Test
    public void constructor_setsColorCorrectly() {
        // constructor without color -> defaults to GREEN
        assertEquals(CourseColor.GREEN, new Course("CS2103T").color);

        // constructor with color -> uses provided color
        assertEquals(CourseColor.BLUE, new Course("CS2103T", CourseColor.BLUE).color);
        assertEquals(CourseColor.RED, new Course("CS2101", CourseColor.RED).color);

        // constructor with null color -> defaults to GREEN
        assertEquals(CourseColor.GREEN, new Course("CS2103T", null).color);
    }

    @Test
    public void isValidCourseCode() {
        // null course code
        assertThrows(NullPointerException.class, () -> Course.isValidCourseCode(null));

        // invalid course codes
        assertFalse(Course.isValidCourseCode("")); // empty string
        assertFalse(Course.isValidCourseCode(" ")); // spaces only
        assertFalse(Course.isValidCourseCode("CS 2103T")); // contains space
        assertFalse(Course.isValidCourseCode("CS@2103T")); // contains special character

        // valid course codes
        assertTrue(Course.isValidCourseCode("CS2103T")); // alphanumeric
        assertTrue(Course.isValidCourseCode("cs2103t")); // lowercase
        assertTrue(Course.isValidCourseCode("CS2103T-L1")); // with hyphen
        assertTrue(Course.isValidCourseCode("MA1521")); // different format
    }

    @Test
    public void equals() {
        Course course = new Course("CS2103T", CourseColor.BLUE);

        // same values -> returns true
        assertTrue(course.equals(new Course("CS2103T", CourseColor.BLUE)));

        // same course code, different color -> returns true (color not considered in equals)
        assertTrue(course.equals(new Course("CS2103T", CourseColor.RED)));

        // same course code different case -> returns true (uppercased internally)
        assertTrue(course.equals(new Course("cs2103t", CourseColor.BLUE)));

        // same object -> returns true
        assertTrue(course.equals(course));

        // null -> returns false
        assertFalse(course.equals(null));

        // different types -> returns false
        assertFalse(course.equals(5.0f));

        // different course code -> returns false
        assertFalse(course.equals(new Course("CS2101", CourseColor.BLUE)));
    }

    @Test
    public void hashCode_test() {
        Course course1 = new Course("CS2103T", CourseColor.BLUE);
        Course course2 = new Course("cs2103t", CourseColor.RED); // same code, different case and color

        // same course code (after uppercasing) -> same hash code
        assertEquals(course1.hashCode(), course2.hashCode());

        Course course3 = new Course("CS2101", CourseColor.BLUE);

        // different course code -> likely different hash code (not guaranteed, but very likely)
        assertTrue(course1.hashCode() != course3.hashCode());
    }

    @Test
    public void toString_test() {
        assertEquals("[CS2103T]", new Course("CS2103T").toString());
        assertEquals("[CS2103T]", new Course("cs2103t").toString()); // uppercased
        assertEquals("[CS2103T]", new Course("CS2103T", CourseColor.BLUE).toString());
    }
}
