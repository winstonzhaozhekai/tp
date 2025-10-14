package seedu.coursebook.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.coursebook.ui.HelpWindow.CommandSummary;

/**
 * Simple unit tests for HelpWindow that don't require JavaFX runtime.
 */
public class HelpWindowSimpleTest {

    @Test
    public void constants_correctValues() {
        // Test URL constant
        assertEquals("https://ay2526s1-cs2103t-f10-2.github.io/tp/UserGuide.html", HelpWindow.USERGUIDE_URL);

        // Test help message constant
        assertEquals("Full User Guide: " + HelpWindow.USERGUIDE_URL, HelpWindow.HELP_MESSAGE);
    }

    @Test
    public void commandSummary_basicConstructor_success() {
        // Test basic constructor
        CommandSummary summary = new CommandSummary("test", "Test command", "test example");

        assertEquals("test", summary.getCommand());
        assertEquals("Test command", summary.getDescription());
        assertEquals("test example", summary.getExample());
    }

    @Test
    public void commandSummary_withPlaceholders_success() {
        // Test with placeholder syntax
        CommandSummary summary = new CommandSummary(
            "add",
            "Adds a person to the coursebook",
            "add n/NAME p/PHONE e/EMAIL a/ADDRESS [t/TAG]..."
        );

        assertEquals("add", summary.getCommand());
        assertTrue(summary.getExample().contains("n/NAME"));
        assertTrue(summary.getExample().contains("[t/TAG]..."));
    }

    @Test
    public void commandSummary_allCommands_haveCorrectFormat() {
        // Test person management commands format
        CommandSummary addCommand = new CommandSummary(
            "add",
            "Adds a person to the coursebook",
            "add n/NAME p/PHONE e/EMAIL a/ADDRESS [t/TAG]..."
        );
        assertTrue(addCommand.getExample().contains("n/"));
        assertTrue(addCommand.getExample().contains("p/"));
        assertTrue(addCommand.getExample().contains("e/"));
        assertTrue(addCommand.getExample().contains("a/"));

        // Test delete command format
        CommandSummary deleteCommand = new CommandSummary(
            "delete",
            "Deletes a person by index",
            "delete INDEX"
        );
        assertTrue(deleteCommand.getExample().contains("INDEX"));

        // Test edit command format
        CommandSummary editCommand = new CommandSummary(
            "edit",
            "Edits a person's details",
            "edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]..."
        );
        assertTrue(editCommand.getExample().contains("INDEX"));
        assertTrue(editCommand.getExample().contains("[n/NAME]"));

        // Test find command format
        CommandSummary findCommand = new CommandSummary(
            "find",
            "Finds persons by keywords",
            "find KEYWORD [MORE_KEYWORDS]..."
        );
        assertTrue(findCommand.getExample().contains("KEYWORD"));
        assertTrue(findCommand.getExample().contains("[MORE_KEYWORDS]..."));

        // Test course commands format
        CommandSummary addCourseCommand = new CommandSummary(
            "addcourse",
            "Adds a course to a person",
            "addcourse INDEX c/COURSE_CODE"
        );
        assertTrue(addCourseCommand.getExample().contains("INDEX"));
        assertTrue(addCourseCommand.getExample().contains("c/COURSE_CODE"));

        CommandSummary removeCourseCommand = new CommandSummary(
            "removecourse",
            "Removes a course from a person",
            "removecourse INDEX c/COURSE_CODE"
        );
        assertTrue(removeCourseCommand.getExample().contains("INDEX"));
        assertTrue(removeCourseCommand.getExample().contains("c/COURSE_CODE"));

        CommandSummary viewCourseCommand = new CommandSummary(
            "viewcourse",
            "Views details of a course",
            "viewcourse COURSE_CODE"
        );
        assertTrue(viewCourseCommand.getExample().contains("COURSE_CODE"));

        CommandSummary listByCourseCommand = new CommandSummary(
            "listbycourse",
            "Lists persons in a course",
            "listbycourse COURSE_CODE"
        );
        assertTrue(listByCourseCommand.getExample().contains("COURSE_CODE"));
    }

    @Test
    public void commandSummary_simpleCommands_noParameters() {
        // Test commands without parameters
        CommandSummary listCommand = new CommandSummary("list", "Lists all persons", "list");
        assertEquals("list", listCommand.getExample());

        CommandSummary listCoursesCommand = new CommandSummary(
                "listcourses", "Lists all available courses", "listcourses");
        assertEquals("listcourses", listCoursesCommand.getExample());

        CommandSummary summaryCommand = new CommandSummary("summary", "Shows summary statistics", "summary");
        assertEquals("summary", summaryCommand.getExample());

        CommandSummary undoCommand = new CommandSummary("undo", "Undoes the last command", "undo");
        assertEquals("undo", undoCommand.getExample());

        CommandSummary clearCommand = new CommandSummary("clear", "Clears all entries", "clear");
        assertEquals("clear", clearCommand.getExample());

        CommandSummary helpCommand = new CommandSummary("help", "Shows this help window", "help");
        assertEquals("help", helpCommand.getExample());

        CommandSummary exitCommand = new CommandSummary("exit", "Exits the application", "exit");
        assertEquals("exit", exitCommand.getExample());
    }

    @Test
    public void commandSummary_gettersMultipleCalls_returnSameValue() {
        CommandSummary summary = new CommandSummary("test", "description", "example");

        // Multiple calls should return the same value
        String firstCommand = summary.getCommand();
        String secondCommand = summary.getCommand();
        assertEquals(firstCommand, secondCommand);

        String firstDescription = summary.getDescription();
        String secondDescription = summary.getDescription();
        assertEquals(firstDescription, secondDescription);

        String firstExample = summary.getExample();
        String secondExample = summary.getExample();
        assertEquals(firstExample, secondExample);
    }

    @Test
    public void commandSummary_independentInstances() {
        CommandSummary summary1 = new CommandSummary("cmd1", "desc1", "ex1");
        CommandSummary summary2 = new CommandSummary("cmd2", "desc2", "ex2");

        // Verify independence
        assertEquals("cmd1", summary1.getCommand());
        assertEquals("cmd2", summary2.getCommand());

        assertEquals("desc1", summary1.getDescription());
        assertEquals("desc2", summary2.getDescription());

        assertEquals("ex1", summary1.getExample());
        assertEquals("ex2", summary2.getExample());
    }

    @Test
    public void commandSummary_emptyValues_handledCorrectly() {
        CommandSummary summary = new CommandSummary("", "", "");

        assertNotNull(summary);
        assertEquals("", summary.getCommand());
        assertEquals("", summary.getDescription());
        assertEquals("", summary.getExample());
    }

    @Test
    public void commandSummary_specialCharactersInExample() {
        CommandSummary summary = new CommandSummary(
            "complex",
            "Complex command",
            "complex [option1|option2] <required> {optional}"
        );

        String example = summary.getExample();
        assertTrue(example.contains("["));
        assertTrue(example.contains("]"));
        assertTrue(example.contains("|"));
        assertTrue(example.contains("<"));
        assertTrue(example.contains(">"));
        assertTrue(example.contains("{"));
        assertTrue(example.contains("}"));
    }
}
