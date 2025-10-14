package seedu.coursebook.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.coursebook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.coursebook.testutil.TypicalPersons.getTypicalCourseBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.ModelManager;
import seedu.coursebook.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCoursesCommand.
 */
public class ListCoursesCommandTest {

    private Model model;
    private Model expectedModel;
    private CommandHistory commandHistory = new CommandHistory();

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalCourseBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getCourseBook(), new UserPrefs());
    }

    @Test
    public void execute_listCoursesWithMultipleCourses_success() {
        // Model should have courses from typical persons
        expectedModel.updateFilteredCourseList(course -> true);
        int courseCount = expectedModel.getFilteredCourseList().size();

        String expectedMessage = courseCount == 0
                ? "No courses found in the course book"
                : String.format("Listed %d course(s)", courseCount);

        CommandResult expectedResult = new CommandResult(expectedMessage, false, false, false, true);
        assertCommandSuccess(new ListCoursesCommand(), model, commandHistory, expectedResult, expectedModel);
    }

    @Test
    public void execute_emptyAddressBook_showsNoCourses() {
        Model emptyModel = new ModelManager();
        Model expectedEmptyModel = new ModelManager();

        String expectedMessage = "No courses found in the course book";
        CommandResult expectedResult = new CommandResult(expectedMessage, false, false, false, true);

        assertCommandSuccess(new ListCoursesCommand(), emptyModel, commandHistory,
                expectedResult, expectedEmptyModel);
    }

    @Test
    public void execute_commandResultHasCorrectFlags() throws Exception {
        ListCoursesCommand command = new ListCoursesCommand();
        CommandResult result = command.execute(model, commandHistory);

        // Check that the command shows courses view
        assertEquals(false, result.isShowPersons());
        assertEquals(true, result.isShowCourses());
        assertEquals(false, result.isExit());
        assertEquals(false, result.isShowHelp());
    }
}
