package seedu.coursebook.logic.parser;

import static seedu.coursebook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.coursebook.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.coursebook.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.coursebook.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.coursebook.logic.commands.UnfavouriteCommand;
import seedu.coursebook.model.person.Name;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the UnfavouriteCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the UnfavouriteCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class UnfavouriteCommandParserTest {

    private UnfavouriteCommandParser parser = new UnfavouriteCommandParser();

    @Test
    public void parse_validIndexArgs_returnsUnfavouriteCommand() {
        assertParseSuccess(parser, "1", new UnfavouriteCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_validNameArgs_returnsUnfavouriteCommand() {
        String nameInput = "John Doe";
        assertParseSuccess(parser, nameInput, new UnfavouriteCommand(new Name(nameInput)));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnfavouriteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_whitespaceArgs_throwsParseException() {
        assertParseFailure(parser, "   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnfavouriteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidName_throwsParseException() {
        // Names with special characters are invalid
        assertParseFailure(parser, "John@Doe",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnfavouriteCommand.MESSAGE_USAGE));
    }
}

