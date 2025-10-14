package seedu.coursebook.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.coursebook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.coursebook.testutil.TypicalPersons.getTypicalCourseBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.ModelManager;
import seedu.coursebook.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ViewCourseCommand.
 */
public class ViewCourseCommandTest {

    private Model model;
    private CommandHistory commandHistory = new CommandHistory();

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalCourseBook(), new UserPrefs());
    }

    @Test
    public void execute_validCourseCode_success() {
        String courseCode = "CS2103T";
        ViewCourseCommand command = new ViewCourseCommand(courseCode);

        Model expectedModel = new ModelManager(model.getCourseBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(person ->
                person.getCourses().stream()
                        .anyMatch(course -> course.courseCode.equalsIgnoreCase(courseCode)));

        String expectedMessage = String.format(ViewCourseCommand.MESSAGE_SUCCESS, courseCode)
                + " (" + expectedModel.getFilteredPersonList().size() + " person(s))";
        CommandResult expectedResult = new CommandResult(expectedMessage, false, false, true, false);

        assertCommandSuccess(command, model, commandHistory, expectedResult, expectedModel);
    }

    @Test
    public void execute_courseCodeNoPersons_success() {
        String courseCode = "CS9999"; // Non-existent course
        ViewCourseCommand command = new ViewCourseCommand(courseCode);

        Model expectedModel = new ModelManager(model.getCourseBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(person ->
                person.getCourses().stream()
                        .anyMatch(course -> course.courseCode.equalsIgnoreCase(courseCode)));

        String expectedMessage = String.format(ViewCourseCommand.MESSAGE_NO_PERSONS, courseCode);
        CommandResult expectedResult = new CommandResult(expectedMessage, false, false, true, false);

        assertCommandSuccess(command, model, commandHistory, expectedResult, expectedModel);
    }

    @Test
    public void execute_caseInsensitiveCourseCode_success() {
        String courseCode = "cs2103t"; // lowercase
        ViewCourseCommand command = new ViewCourseCommand(courseCode);

        Model expectedModel = new ModelManager(model.getCourseBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(person ->
                person.getCourses().stream()
                        .anyMatch(course -> course.courseCode.equalsIgnoreCase(courseCode)));

        // Should find persons with CS2103 course
        assertTrue(expectedModel.getFilteredPersonList().size() > 0);
    }

    @Test
    public void equals() {
        ViewCourseCommand firstCommand = new ViewCourseCommand("CS2103T");
        ViewCourseCommand secondCommand = new ViewCourseCommand("CS2106");

        // same object -> returns true
        assertTrue(firstCommand.equals(firstCommand));

        // same values -> returns true
        ViewCourseCommand firstCommandCopy = new ViewCourseCommand("CS2103T");
        assertTrue(firstCommand.equals(firstCommandCopy));

        // different types -> returns false
        assertFalse(firstCommand.equals(1));

        // null -> returns false
        assertFalse(firstCommand.equals(null));

        // different course code -> returns false
        assertFalse(firstCommand.equals(secondCommand));
    }
}
