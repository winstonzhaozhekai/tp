package seedu.coursebook.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.coursebook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.coursebook.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.coursebook.testutil.Assert.assertThrows;
import static seedu.coursebook.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.coursebook.logic.commands.AddCommand;
import seedu.coursebook.logic.commands.ClearCommand;
import seedu.coursebook.logic.commands.DeleteCommand;
import seedu.coursebook.logic.commands.ExitCommand;
import seedu.coursebook.logic.commands.FindCommand;
import seedu.coursebook.logic.commands.HelpCommand;
import seedu.coursebook.logic.commands.HistoryCommand;
import seedu.coursebook.logic.commands.HomeCommand;
import seedu.coursebook.logic.commands.ListCommand;
import seedu.coursebook.logic.commands.ListCoursesCommand;
import seedu.coursebook.logic.commands.RedoCommand;
import seedu.coursebook.logic.commands.UndoCommand;
import seedu.coursebook.logic.commands.ViewCourseCommand;
import seedu.coursebook.logic.parser.exceptions.ParseException;
import seedu.coursebook.model.person.NameContainsKeywordsPredicate;
import seedu.coursebook.model.person.Person;
import seedu.coursebook.testutil.PersonBuilder;
import seedu.coursebook.testutil.PersonUtil;

public class CourseBookParserTest {

    private final CourseBookParser parser = new CourseBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertThrows(ParseException.class, () -> parser.parseCommand(ListCommand.COMMAND_WORD + " 3"));
    }

    @Test
    public void parseCommand_listCourses() throws Exception {
        assertTrue(parser.parseCommand(ListCoursesCommand.COMMAND_WORD) instanceof ListCoursesCommand);
        assertTrue(parser.parseCommand(ListCoursesCommand.COMMAND_WORD + " 3") instanceof ListCoursesCommand);
    }

    @Test
    public void parseCommand_viewCourse() throws Exception {
        ViewCourseCommand command = (ViewCourseCommand) parser.parseCommand(
                ViewCourseCommand.COMMAND_WORD + " c/CS2103T");
        assertEquals(new ViewCourseCommand("CS2103T"), command);
    }

    @Test
    public void parseCommand_undo() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD) instanceof UndoCommand);
    }


    @Test
    public void parseCommand_redo() throws Exception {
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_WORD) instanceof RedoCommand);
    }


    @Test
    public void parseCommand_history() throws Exception {
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD) instanceof HistoryCommand);
    }

    @Test
    public void parseCommand_home() throws Exception {
        assertTrue(parser.parseCommand(HomeCommand.COMMAND_WORD) instanceof HomeCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
