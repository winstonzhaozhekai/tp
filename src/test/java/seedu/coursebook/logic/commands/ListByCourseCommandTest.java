package seedu.coursebook.logic.commands;

import static seedu.coursebook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.coursebook.testutil.TypicalPersons.getTypicalCourseBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.coursebook.model.Model;
import seedu.coursebook.model.ModelManager;
import seedu.coursebook.model.UserPrefs;

/**
 * Contains tests for ListByCourseCommand.
 */
public class ListByCourseCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalCourseBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getCourseBook(), new UserPrefs());
    }

    @Test
    public void execute_courseWithMatches_showsFilteredList() {
        String courseCode = "CS2103T";
        ListByCourseCommand command = new ListByCourseCommand(courseCode);

        expectedModel.updateFilteredPersonList(person ->
                person.getCourses().stream()
                        .anyMatch(c -> c.courseCode.equalsIgnoreCase(courseCode)));

        String expectedMessage = "Listed all persons in " + courseCode;

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_courseWithNoMatches_showsNoPersonsMessage() {
        String courseCode = "FakeCourse";
        ListByCourseCommand command = new ListByCourseCommand(courseCode);

        expectedModel.updateFilteredPersonList(person ->
                person.getCourses().stream()
                        .anyMatch(c -> c.courseCode.equalsIgnoreCase(courseCode)));

        String expectedMessage = "No such course: " + courseCode;

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }
}
