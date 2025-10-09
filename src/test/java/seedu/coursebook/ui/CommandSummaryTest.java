package seedu.coursebook.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import seedu.coursebook.ui.HelpWindow.CommandSummary;

/**
 * Unit tests for the CommandSummary class.
 */
public class CommandSummaryTest {

    @Test
    public void constructor_validInputs_createsCommandSummary() {
        String command = "add";
        String description = "Adds a person to the coursebook";
        String example = "add n/NAME p/PHONE e/EMAIL a/ADDRESS [t/TAG]...";

        CommandSummary summary = new CommandSummary(command, description, example);

        assertNotNull(summary);
        assertEquals(command, summary.getCommand());
        assertEquals(description, summary.getDescription());
        assertEquals(example, summary.getExample());
    }

    @Test
    public void getCommand_validCommandSummary_returnsCorrectCommand() {
        CommandSummary summary = new CommandSummary("delete", "Deletes a person", "delete INDEX");
        assertEquals("delete", summary.getCommand());
    }

    @Test
    public void getDescription_validCommandSummary_returnsCorrectDescription() {
        CommandSummary summary = new CommandSummary("edit", "Edits a person's details", "edit INDEX [n/NAME]");
        assertEquals("Edits a person's details", summary.getDescription());
    }

    @Test
    public void getExample_validCommandSummary_returnsCorrectExample() {
        String example = "find KEYWORD [MORE_KEYWORDS]...";
        CommandSummary summary = new CommandSummary("find", "Finds persons by keywords", example);
        assertEquals(example, summary.getExample());
    }

    @Test
    public void constructor_withEmptyStrings_createsCommandSummary() {
        CommandSummary summary = new CommandSummary("", "", "");
        assertEquals("", summary.getCommand());
        assertEquals("", summary.getDescription());
        assertEquals("", summary.getExample());
    }

    @Test
    public void constructor_withSpecialCharacters_handlesCorrectly() {
        String command = "addcourse";
        String description = "Adds a course (e.g., CS2103T) to a person";
        String example = "addcourse INDEX c/COURSE_CODE [t/TAG]...";

        CommandSummary summary = new CommandSummary(command, description, example);

        assertEquals(command, summary.getCommand());
        assertEquals(description, summary.getDescription());
        assertEquals(example, summary.getExample());
    }

    @Test
    public void multipleInstances_differentValues_areIndependent() {
        CommandSummary summary1 = new CommandSummary("list", "Lists all persons", "list");
        CommandSummary summary2 = new CommandSummary("clear", "Clears all entries", "clear");

        assertNotEquals(summary1.getCommand(), summary2.getCommand());
        assertNotEquals(summary1.getDescription(), summary2.getDescription());
        assertNotEquals(summary1.getExample(), summary2.getExample());
    }

    @Test
    public void constructor_withLongStrings_handlesCorrectly() {
        String longDescription = "This is a very long description that explains in detail what the command does "
                + "and how it should be used in various scenarios";
        String longExample = "edit INDEX [n/VERY_LONG_NAME_PARAMETER] [p/PHONE_WITH_EXTENSION] "
                + "[e/EMAIL_WITH_SUBDOMAIN@DOMAIN.COM] [a/VERY_LONG_ADDRESS_WITH_MULTIPLE_LINES]";

        CommandSummary summary = new CommandSummary("edit", longDescription, longExample);

        assertEquals("edit", summary.getCommand());
        assertEquals(longDescription, summary.getDescription());
        assertEquals(longExample, summary.getExample());
    }

    @Test
    public void allCommandTypes_personManagement_createSuccessfully() {
        CommandSummary[] personCommands = {
            new CommandSummary("add", "Adds a person to the coursebook",
                    "add n/NAME p/PHONE e/EMAIL a/ADDRESS [t/TAG]..."),
            new CommandSummary("delete", "Deletes a person by index", "delete INDEX"),
            new CommandSummary("edit", "Edits a person's details",
                    "edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]..."),
            new CommandSummary("find", "Finds persons by keywords", "find KEYWORD [MORE_KEYWORDS]..."),
            new CommandSummary("list", "Lists all persons", "list")
        };

        for (CommandSummary command : personCommands) {
            assertNotNull(command);
            assertNotNull(command.getCommand());
            assertNotNull(command.getDescription());
            assertNotNull(command.getExample());
        }
    }

    @Test
    public void allCommandTypes_courseManagement_createSuccessfully() {
        CommandSummary[] courseCommands = {
            new CommandSummary("addcourse", "Adds a course to a person", "addcourse INDEX c/COURSE_CODE"),
            new CommandSummary("removecourse", "Removes a course from a person",
                    "removecourse INDEX c/COURSE_CODE"),
            new CommandSummary("viewcourse", "Views details of a course", "viewcourse COURSE_CODE"),
            new CommandSummary("listbycourse", "Lists persons in a course", "listbycourse COURSE_CODE"),
            new CommandSummary("listcourses", "Lists all available courses", "listcourses")
        };

        for (CommandSummary command : courseCommands) {
            assertNotNull(command);
            assertNotNull(command.getCommand());
            assertNotNull(command.getDescription());
            assertNotNull(command.getExample());
        }
    }

    @Test
    public void allCommandTypes_generalCommands_createSuccessfully() {
        CommandSummary[] generalCommands = {
            new CommandSummary("summary", "Shows summary statistics", "summary"),
            new CommandSummary("undo", "Undoes the last command", "undo"),
            new CommandSummary("clear", "Clears all entries", "clear"),
            new CommandSummary("help", "Shows this help window", "help"),
            new CommandSummary("exit", "Exits the application", "exit")
        };

        for (CommandSummary command : generalCommands) {
            assertNotNull(command);
            assertNotNull(command.getCommand());
            assertNotNull(command.getDescription());
            assertNotNull(command.getExample());
        }
    }

    @Test
    public void getters_multipleCallsSameInstance_returnSameValues() {
        CommandSummary summary = new CommandSummary("test", "Test description", "test example");

        // Call getters multiple times
        String command1 = summary.getCommand();
        String command2 = summary.getCommand();
        String description1 = summary.getDescription();
        String description2 = summary.getDescription();
        String example1 = summary.getExample();
        String example2 = summary.getExample();

        // Verify same values returned
        assertEquals(command1, command2);
        assertEquals(description1, description2);
        assertEquals(example1, example2);
    }
}
