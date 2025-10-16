package seedu.coursebook.logic.parser;

import static seedu.coursebook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.coursebook.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.coursebook.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.coursebook.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.coursebook.logic.commands.ViewPersonCommand;
import seedu.coursebook.model.person.Name;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the ViewPersonCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the ViewPersonCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class ViewPersonCommandParserTest {

    private ViewPersonCommandParser parser = new ViewPersonCommandParser();

    @Test
    public void parse_validIndex_returnsViewPersonCommand() {
        assertParseSuccess(parser, "1", new ViewPersonCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_validName_returnsViewPersonCommand() {
        Name validName = new Name("Alice Pauline");
        assertParseSuccess(parser, "Alice Pauline", new ViewPersonCommand(validName));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewPersonCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_whitespaceArgs_throwsParseException() {
        assertParseFailure(parser, "   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewPersonCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidName_throwsParseException() {
        assertParseFailure(parser, "Alice@Pauline",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewPersonCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_zeroIndex_returnsViewPersonCommandWithName() {
        // "0" fails as an index but is accepted as a valid name
        Name zeroName = new Name("0");
        assertParseSuccess(parser, "0", new ViewPersonCommand(zeroName));
    }

    @Test
    public void parse_negativeNumber_throwsParseException() {
        // "-1" fails as an index and also fails as a name (hyphen not allowed)
        assertParseFailure(parser, "-1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewPersonCommand.MESSAGE_USAGE));
    }
}
