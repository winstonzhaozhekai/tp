package seedu.coursebook.logic.parser;

import static seedu.coursebook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.coursebook.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.coursebook.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.coursebook.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.coursebook.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.coursebook.testutil.TypicalIndexes.INDEX_THIRD_PERSON;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.coursebook.logic.commands.DeleteCommand;
import seedu.coursebook.model.person.Name;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class DeleteCommandParserTest {

    private DeleteCommandParser parser = new DeleteCommandParser();

    @Test
    public void parse_validSingleIndex_returnsDeleteCommand() {
        assertParseSuccess(parser, "1", new DeleteCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_validMultipleIndices_returnsDeleteCommand() {
        DeleteCommand expectedCommand = new DeleteCommand(
                Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON, INDEX_THIRD_PERSON));
        assertParseSuccess(parser, "1 2 3", expectedCommand);
    }

    @Test
    public void parse_validSingleName_returnsDeleteCommand() {
        DeleteCommand expectedCommand = new DeleteCommand(new Name("John Doe"));
        assertParseSuccess(parser, "John Doe", expectedCommand);
    }

    @Test
    public void parse_validMultipleNames_returnsDeleteCommand() {
        DeleteCommand expectedCommand = new DeleteCommand(
                Arrays.asList(new Name("John Doe"), new Name("Jane Smith")), true);
        assertParseSuccess(parser, "John Doe, Jane Smith", expectedCommand);
    }

    @Test
    public void parse_multipleNamesWithExtraSpaces_returnsDeleteCommand() {
        DeleteCommand expectedCommand = new DeleteCommand(
                Arrays.asList(new Name("John Doe"), new Name("Jane Smith")), true);
        assertParseSuccess(parser, "John Doe,  Jane Smith", expectedCommand);
    }

    @Test
    public void parse_multipleNamesWithTrailingComma_returnsDeleteCommand() {
        DeleteCommand expectedCommand = new DeleteCommand(
                Arrays.asList(new Name("John Doe"), new Name("Jane Smith")), true);
        assertParseSuccess(parser, "John Doe, Jane Smith,", expectedCommand);
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertParseFailure(parser, "0", seedu.coursebook.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void parse_negativeIndex_throwsInvalidIndex() {
        assertParseFailure(parser, "-1", seedu.coursebook.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void parse_mixedNegativeAndValidIndex_returnsDeleteCommandWithWarning() {
        // We don't have direct access to warnings from parser, but we assert parse success
        // to ensure classification as indices still succeeds for the valid token.
        DeleteCommand expectedCommand = new DeleteCommand(INDEX_SECOND_PERSON);
        assertParseSuccess(parser, "-1 2", expectedCommand);
    }

    @Test
    public void parse_negativesZeroAndValidIndex_returnsDeleteCommandWithWarnings() {
        DeleteCommand expectedCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        assertParseSuccess(parser, "-1 0 1", expectedCommand);
    }

    @Test
    public void parse_duplicateIndices_throwsParseException() {
        assertParseFailure(parser, "1 1", "Duplicate indices are not allowed.");
    }

    @Test
    public void parse_mixedInvalidAndDuplicate_throwsDuplicateError() {
        assertParseFailure(parser, "0 1 1", "Duplicate indices are not allowed.");
    }

    @Test
    public void parse_invalidName_throwsParseException() {
        assertParseFailure(parser, "@#$%", String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_duplicateNames_throwsParseException() {
        assertParseFailure(parser, "John Doe, John Doe", "Duplicate names are not allowed.");
    }
}
