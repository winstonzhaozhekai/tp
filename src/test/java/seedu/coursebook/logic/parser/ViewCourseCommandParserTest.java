package seedu.coursebook.logic.parser;

import static seedu.coursebook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.coursebook.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.coursebook.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.coursebook.logic.commands.ViewCourseCommand;

/**
 * Tests for ViewCourseCommandParser
 */
public class ViewCourseCommandParserTest {

    private ViewCourseCommandParser parser = new ViewCourseCommandParser();

    @Test
    public void parse_validArgs_returnsViewCourseCommand() {
        // Valid course code
        assertParseSuccess(parser, " c/CS2103T",
                new ViewCourseCommand("CS2103T"));

        // Valid course code with extra spaces
        assertParseSuccess(parser, "  c/CS2103T  ",
                new ViewCourseCommand("CS2103T"));

        // Valid course code with hyphen
        assertParseSuccess(parser, " c/CS2103-T",
                new ViewCourseCommand("CS2103-T"));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // Missing course prefix
        assertParseFailure(parser, "CS2103T",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCourseCommand.MESSAGE_USAGE));

        // Empty course code
        assertParseFailure(parser, " c/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCourseCommand.MESSAGE_USAGE));

        // Only spaces for course code
        assertParseFailure(parser, " c/   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCourseCommand.MESSAGE_USAGE));

        // No arguments
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCourseCommand.MESSAGE_USAGE));

        // Invalid preamble
        assertParseFailure(parser, "invalid c/CS2103T",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCourseCommand.MESSAGE_USAGE));
    }
}
