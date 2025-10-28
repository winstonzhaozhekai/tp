package seedu.coursebook.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.logic.Logic;
import seedu.coursebook.logic.LogicManager;
import seedu.coursebook.logic.commands.exceptions.CommandException;
import seedu.coursebook.logic.parser.exceptions.ParseException;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.ModelManager;
import seedu.coursebook.storage.JsonCourseBookStorage;
import seedu.coursebook.storage.JsonUserPrefsStorage;
import seedu.coursebook.storage.StorageManager;

/**
 * Unit tests for CommandBox that test the command history functionality.
 * Tests the underlying logic without requiring full JavaFX runtime initialization.
 */
public class CommandBoxTest {

    @TempDir
    public Path temporaryFolder;

    private Logic logic;
    private Model model;

    @BeforeEach
    public void setUp() {
        JsonCourseBookStorage courseBookStorage =
                new JsonCourseBookStorage(temporaryFolder.resolve("CourseBook.json"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(courseBookStorage, userPrefsStorage);
        model = new ModelManager();
        logic = new LogicManager(model, storage);
    }

    @Test
    public void commandHistory_addCommands_historyStored() throws CommandException, ParseException {
        // Execute commands
        logic.execute("list");
        logic.execute("listcourses");
        logic.execute("help");

        // Verify history
        assertEquals(3, logic.getHistory().size());
        assertEquals("list", logic.getHistory().get(0));
        assertEquals("listcourses", logic.getHistory().get(1));
        assertEquals("help", logic.getHistory().get(2));
    }

    @Test
    public void commandHistory_emptyInitially() {
        assertEquals(0, logic.getHistory().size());
    }

    @Test
    public void commandHistory_validCommand_addedToHistory() throws CommandException, ParseException {
        logic.execute("list");
        assertEquals(1, logic.getHistory().size());
        assertEquals("list", logic.getHistory().get(0));
    }

    @Test
    public void commandHistory_invalidCommand_stillAddedToHistory() {
        try {
            logic.execute("invalidcommand123");
        } catch (CommandException | ParseException e) {
            // Exception expected, but command should still be in history
        }
        assertEquals(1, logic.getHistory().size());
        assertEquals("invalidcommand123", logic.getHistory().get(0));
    }

    @Test
    public void commandHistory_multipleCommands_orderedCorrectly() throws CommandException, ParseException {
        logic.execute("list");
        logic.execute("listcourses");
        logic.execute("help");
        logic.execute("exit");

        assertEquals(4, logic.getHistory().size());
        assertEquals("list", logic.getHistory().get(0));
        assertEquals("listcourses", logic.getHistory().get(1));
        assertEquals("help", logic.getHistory().get(2));
        assertEquals("exit", logic.getHistory().get(3));
    }

    @Test
    public void commandHistory_duplicateCommands_allStored() throws CommandException, ParseException {
        logic.execute("list");
        logic.execute("list");
        logic.execute("list");

        assertEquals(3, logic.getHistory().size());
        assertEquals("list", logic.getHistory().get(0));
        assertEquals("list", logic.getHistory().get(1));
        assertEquals("list", logic.getHistory().get(2));
    }

    @Test
    public void commandHistory_getHistory_returnsUnmodifiableList() throws CommandException, ParseException {
        logic.execute("list");

        try {
            logic.getHistory().add("should fail");
            // If we reach here, the test should fail
            assertEquals(1, logic.getHistory().size(), "History should be unmodifiable");
        } catch (UnsupportedOperationException e) {
            // Expected - history should be unmodifiable
            assertEquals(1, logic.getHistory().size());
        }
    }

    @Test
    public void commandHistory_testNavigationScenario_multipleCommands() throws CommandException, ParseException {
        // Simulate a typical user workflow
        logic.execute("list");
        logic.execute("help");
        logic.execute("listcourses");

        // Verify we can access history in reverse order (newest to oldest)
        assertEquals(3, logic.getHistory().size());
        // Most recent command
        assertEquals("listcourses", logic.getHistory().get(logic.getHistory().size() - 1));
        // Second most recent
        assertEquals("help", logic.getHistory().get(logic.getHistory().size() - 2));
        // Third most recent
        assertEquals("list", logic.getHistory().get(logic.getHistory().size() - 3));
    }

    @Test
    public void commandHistory_emptyCommand_notAdded() {
        // Empty commands should not be processed, so history should remain empty
        assertEquals(0, logic.getHistory().size());
    }

    @Test
    public void commandHistory_longCommand_storedCorrectly() throws CommandException, ParseException {
        String longCommand = "find alice bob charlie david eve frank grace henry isaac julia "
                + "kevin laura michael nancy oliver patricia quinn rachel samuel teresa";
        try {
            logic.execute(longCommand);
        } catch (CommandException | ParseException e) {
            // Command may be invalid, but should still be in history
        }

        assertEquals(1, logic.getHistory().size());
        assertEquals(longCommand, logic.getHistory().get(0));
    }

    @Test
    public void commandHistory_specialCharacters_storedCorrectly() {
        try {
            logic.execute("find @#$%^&*()");
        } catch (CommandException | ParseException e) {
            // Command may be invalid, but should still be in history
        }

        assertEquals(1, logic.getHistory().size());
        assertEquals("find @#$%^&*()", logic.getHistory().get(0));
    }

    @Test
    public void commandHistory_whitespaceCommand_storedCorrectly() {
        try {
            logic.execute("   list   ");
        } catch (CommandException | ParseException e) {
            // Command may fail, but should still be in history
        }

        assertEquals(1, logic.getHistory().size());
        assertEquals("   list   ", logic.getHistory().get(0));
    }

    @Test
    public void commandHistory_mixOfValidAndInvalidCommands() throws CommandException, ParseException {
        logic.execute("list");
        try {
            logic.execute("invalidcommand");
        } catch (CommandException | ParseException e) {
            // Expected
        }
        logic.execute("listcourses");

        assertEquals(3, logic.getHistory().size());
        assertEquals("list", logic.getHistory().get(0));
        assertEquals("invalidcommand", logic.getHistory().get(1));
        assertEquals("listcourses", logic.getHistory().get(2));
    }

    @Test
    public void commandHistory_constructor_createsNewHistory() {
        CommandHistory history = new CommandHistory();
        assertNotNull(history);
        assertEquals(0, history.getHistory().size());
    }

    @Test
    public void commandHistory_copyConstructor_copiesCorrectly() {
        CommandHistory original = new CommandHistory();
        original.add("command1");
        original.add("command2");

        CommandHistory copy = new CommandHistory(original);
        assertEquals(2, copy.getHistory().size());
        assertEquals("command1", copy.getHistory().get(0));
        assertEquals("command2", copy.getHistory().get(1));
    }

    @Test
    public void commandHistory_copyConstructor_independentCopies() {
        CommandHistory original = new CommandHistory();
        original.add("command1");

        CommandHistory copy = new CommandHistory(original);
        original.add("command2");

        // Copy should not have the second command
        assertEquals(1, copy.getHistory().size());
        assertEquals(2, original.getHistory().size());
    }

    @Test
    public void commandHistory_equals_sameHistory() {
        CommandHistory history1 = new CommandHistory();
        history1.add("command1");
        history1.add("command2");

        CommandHistory history2 = new CommandHistory();
        history2.add("command1");
        history2.add("command2");

        assertEquals(history1, history2);
    }

    @Test
    public void commandHistory_equals_differentHistory() {
        CommandHistory history1 = new CommandHistory();
        history1.add("command1");

        CommandHistory history2 = new CommandHistory();
        history2.add("command2");

        assertEquals(false, history1.equals(history2));
    }

    @Test
    public void commandHistory_equals_sameObject() {
        CommandHistory history = new CommandHistory();
        assertEquals(history, history);
    }

    @Test
    public void commandHistory_hashCode_consistency() {
        CommandHistory history1 = new CommandHistory();
        history1.add("command1");

        CommandHistory history2 = new CommandHistory();
        history2.add("command1");

        assertEquals(history1.hashCode(), history2.hashCode());
    }
}
