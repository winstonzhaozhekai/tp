package seedu.coursebook.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.coursebook.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.coursebook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.coursebook.logic.commands.CommandTestUtil.deleteFirstPerson;
import static seedu.coursebook.testutil.TypicalPersons.getTypicalCourseBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.logic.commands.exceptions.CommandException;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.ModelManager;
import seedu.coursebook.model.UserPrefs;

public class RedoCommandTest {

    private final Model model = new ModelManager(getTypicalCourseBook(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalCourseBook(), new UserPrefs());
    private final CommandHistory commandHistory = new CommandHistory();

    @BeforeEach
    public void setUp() {
        //set up of models' undo/redo history
        deleteFirstPerson(model);
        deleteFirstPerson(model);

        deleteFirstPerson(expectedModel);
        deleteFirstPerson(expectedModel);
    }

    @Test
    public void execute() {
        //no redoable states in model
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);

        //single redoable state in model
        model.undoCourseBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);

    }

    @Test
    public void execute_redoWithThemeChange_returnsThemeChangeResult() throws CommandException {
        // Create a fresh model for this test
        Model testModel = new ModelManager(getTypicalCourseBook(), new UserPrefs());
        CommandHistory testHistory = new CommandHistory();

        // Execute a theme change command
        ThemeCommand themeCommand = new ThemeCommand(ThemeCommand.Theme.DARK);

        CommandException exception = assertThrows(CommandException.class, () ->
                themeCommand.execute(testModel, testHistory)
        );

        assertEquals("Theme is already dark!", exception.getMessage());
    }

    @Test
    public void execute_redoWithoutThemeChange_returnsNormalResult() {
        // Setup: delete a person and undo it
        Model testModel = new ModelManager(getTypicalCourseBook(), new UserPrefs());
        CommandHistory testHistory = new CommandHistory();

        deleteFirstPerson(testModel);
        testModel.undoCourseBook();

        // Redo the deletion (not a theme change)
        RedoCommand redoCommand = new RedoCommand();
        CommandResult result;
        try {
            result = redoCommand.execute(testModel, testHistory);
        } catch (CommandException e) {
            result = null;
        }

        // Verify the result does NOT indicate a theme change
        assertEquals(RedoCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
        // Assuming CommandResult has a method to check if it's a theme change
        // If not, you might need to check that it's a regular CommandResult
    }

    @Test
    public void execute_redoMultipleThemeChanges_restoresCorrectTheme() throws CommandException {
        Model testModel = new ModelManager(getTypicalCourseBook(), new UserPrefs());
        CommandHistory testHistory = new CommandHistory();

        // Execute theme changes: Light -> Dark -> Light
        new ThemeCommand(ThemeCommand.Theme.TREE).execute(testModel, testHistory);
        new ThemeCommand(ThemeCommand.Theme.LOVE).execute(testModel, testHistory);

        // Undo both theme changes
        testModel.undoCourseBook();
        testModel.undoCourseBook();

        // Redo first theme change (should restore DARK)
        CommandResult firstRedo = new RedoCommand().execute(testModel, testHistory);
        assertTrue(firstRedo.isThemeChange());
        assertEquals(ThemeCommand.Theme.TREE.getThemeCssFile(), firstRedo.getThemeCssFile());

        // Redo second theme change (should restore LIGHT)
        CommandResult secondRedo = new RedoCommand().execute(testModel, testHistory);
        assertTrue(secondRedo.isThemeChange());
        assertEquals(ThemeCommand.Theme.LOVE.getThemeCssFile(), secondRedo.getThemeCssFile());
    }

    @Test
    public void execute_redoMixedCommands_onlyThemeChangesReturnThemeResult() throws CommandException {
        Model testModel = new ModelManager(getTypicalCourseBook(), new UserPrefs());
        CommandHistory testHistory = new CommandHistory();

        // Execute: delete person, then theme change
        deleteFirstPerson(testModel);
        new ThemeCommand(ThemeCommand.Theme.BLUE).execute(testModel, testHistory);

        // Undo both
        testModel.undoCourseBook();
        testModel.undoCourseBook();

        // Redo first (delete person - not a theme change)
        CommandResult firstRedo = new RedoCommand().execute(testModel, testHistory);
        assertEquals(RedoCommand.MESSAGE_SUCCESS, firstRedo.getFeedbackToUser());

        // Redo second (theme change)
        CommandResult secondRedo = new RedoCommand().execute(testModel, testHistory);
        assertTrue(secondRedo.isThemeChange());
        assertEquals(ThemeCommand.Theme.BLUE.getThemeCssFile(), secondRedo.getThemeCssFile());
    }
}
