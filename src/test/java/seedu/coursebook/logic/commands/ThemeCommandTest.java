package seedu.coursebook.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.coursebook.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.Test;

import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.logic.commands.exceptions.CommandException;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.ModelManager;
import seedu.coursebook.model.UserPrefs;
import seedu.coursebook.testutil.TypicalPersons;

/**
 * Contains integration tests (interaction with the Model) for {@code ThemeCommand}.
 */
public class ThemeCommandTest {
    private Model model = new ModelManager(TypicalPersons.getTypicalCourseBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(TypicalPersons.getTypicalCourseBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void equals() {
        ThemeCommand darkThemeCommand = new ThemeCommand(ThemeCommand.Theme.DARK);
        ThemeCommand blueThemeCommand = new ThemeCommand(ThemeCommand.Theme.BLUE);

        // same object -> returns true
        assertTrue(darkThemeCommand.equals(darkThemeCommand));

        // same values -> returns true
        ThemeCommand darkThemeCommandCopy = new ThemeCommand(ThemeCommand.Theme.DARK);
        assertTrue(darkThemeCommand.equals(darkThemeCommandCopy));

        // different types -> returns false
        assertFalse(darkThemeCommand.equals(1));

        // null -> returns false
        assertFalse(darkThemeCommand.equals(null));

        // different theme -> returns false
        assertFalse(darkThemeCommand.equals(blueThemeCommand));
    }

    @Test
    public void execute_validTheme_success() throws Exception {
        ThemeCommand.Theme targetTheme = ThemeCommand.Theme.BLUE;
        ThemeCommand command = new ThemeCommand(targetTheme);

        // Set expected model to different theme first
        expectedModel.setCurrentTheme(targetTheme);
        expectedModel.commitCourseBook();

        String expectedMessage = String.format(ThemeCommand.MESSAGE_SUCCESS, targetTheme.getName());
        CommandResult expectedCommandResult = new CommandResult(
                expectedMessage, false, false, false, false, false,
                null, targetTheme.getThemeCssFile(), targetTheme.getExtensionsFile()
        );

        assertCommandSuccess(command, model, commandHistory, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_sameTheme_throwsCommandException() throws Exception {
        // Get current theme from model
        ThemeCommand.Theme currentTheme = model.getCurrentTheme();
        ThemeCommand command = new ThemeCommand(currentTheme);

        String expectedMessage = String.format(ThemeCommand.MESSAGE_SAME_THEME, currentTheme.getName());

        assertThrows(CommandException.class, () -> command.execute(model, commandHistory), expectedMessage);
    }

    @Test
    public void execute_differentThemes_success() throws Exception {
        // Test changing to each theme
        ThemeCommand.Theme[] themes = {
                ThemeCommand.Theme.DARK,
                ThemeCommand.Theme.BLUE,
                ThemeCommand.Theme.LOVE,
                ThemeCommand.Theme.TREE
        };

        for (ThemeCommand.Theme targetTheme : themes) {
            // Skip if already on this theme
            if (model.getCurrentTheme() == targetTheme) {
                continue;
            }

            ThemeCommand command = new ThemeCommand(targetTheme);
            CommandResult result = command.execute(model, commandHistory);

            String expectedMessage = String.format(ThemeCommand.MESSAGE_SUCCESS, targetTheme.getName());
            assertEquals(expectedMessage, result.getFeedbackToUser());
            assertEquals(targetTheme, model.getCurrentTheme());
        }
    }

    @Test
    public void toStringMethod() {
        ThemeCommand.Theme targetTheme = ThemeCommand.Theme.LOVE;
        ThemeCommand themeCommand = new ThemeCommand(targetTheme);
        String expected = ThemeCommand.class.getCanonicalName() + "{targetTheme=" + targetTheme + "}";
        assertEquals(expected, themeCommand.toString());
    }

    @Test
    public void themeEnum_fromString_validTheme() {
        assertEquals(ThemeCommand.Theme.DARK, ThemeCommand.Theme.fromString("dark"));
        assertEquals(ThemeCommand.Theme.BLUE, ThemeCommand.Theme.fromString("blue"));
        assertEquals(ThemeCommand.Theme.LOVE, ThemeCommand.Theme.fromString("love"));
        assertEquals(ThemeCommand.Theme.TREE, ThemeCommand.Theme.fromString("tree"));
    }

    @Test
    public void themeEnum_fromString_caseInsensitive() {
        assertEquals(ThemeCommand.Theme.DARK, ThemeCommand.Theme.fromString("DARK"));
        assertEquals(ThemeCommand.Theme.BLUE, ThemeCommand.Theme.fromString("Blue"));
        assertEquals(ThemeCommand.Theme.LOVE, ThemeCommand.Theme.fromString("LOVE"));
        assertEquals(ThemeCommand.Theme.TREE, ThemeCommand.Theme.fromString("TrEe"));
    }

    @Test
    public void themeEnum_fromString_invalidTheme() {
        assertEquals(null, ThemeCommand.Theme.fromString("invalid"));
        assertEquals(null, ThemeCommand.Theme.fromString(""));
        assertEquals(null, ThemeCommand.Theme.fromString("red"));
    }

    @Test
    public void themeEnum_getters() {
        ThemeCommand.Theme darkTheme = ThemeCommand.Theme.DARK;
        assertEquals("dark", darkTheme.getName());
        assertEquals("DarkTheme.css", darkTheme.getThemeCssFile());
        assertEquals("Extensions.css", darkTheme.getExtensionsFile());

        ThemeCommand.Theme blueTheme = ThemeCommand.Theme.BLUE;
        assertEquals("blue", blueTheme.getName());
        assertEquals("BlueTheme.css", blueTheme.getThemeCssFile());
        assertEquals("BlueExtensions.css", blueTheme.getExtensionsFile());
    }
}
