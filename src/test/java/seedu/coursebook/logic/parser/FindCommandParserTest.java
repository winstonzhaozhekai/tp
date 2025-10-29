package seedu.coursebook.logic.parser;

import static seedu.coursebook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.coursebook.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.coursebook.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.coursebook.logic.commands.FindCommand;
import seedu.coursebook.model.person.PersonContainsKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand = new FindCommand(
                new PersonContainsKeywordsPredicate(
                        Arrays.asList("Alice", "Bob"),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyList()));
        assertParseSuccess(parser, "Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedFindCommand);
    }

    @Test
    public void parse_unprefixedNonAlpha_throwsParseException() {
        assertParseFailure(parser, "Alice123", seedu.coursebook.logic.Messages.MESSAGE_NAME_ALPHA_ONLY);
        assertParseFailure(parser, "Bob-", seedu.coursebook.logic.Messages.MESSAGE_NAME_ALPHA_ONLY);
        assertParseFailure(parser, "Alice Bob-", seedu.coursebook.logic.Messages.MESSAGE_NAME_ALPHA_ONLY);
    }

    @Test
    public void parse_prefixedNameNonAlpha_throwsParseException() {
        assertParseFailure(parser, "n/Alice123", seedu.coursebook.logic.Messages.MESSAGE_NAME_ALPHA_ONLY);
        assertParseFailure(parser, "n/Bob- n/Alice", seedu.coursebook.logic.Messages.MESSAGE_NAME_ALPHA_ONLY);
    }

    @Test
    public void parse_prefixedOtherFields_allowNonAlpha() {
        FindCommand expected = new FindCommand(
                new PersonContainsKeywordsPredicate(
                        Collections.emptyList(),
                        Arrays.asList("9123"),
                        Arrays.asList("alice@example.com"),
                        Collections.emptyList(),
                        Collections.emptyList()));
        assertParseSuccess(parser, "p/9123 e/alice@example.com", expected);
    }

    @Test
    public void parse_duplicateNamePrefixes_aggregatesAllValues() {
        FindCommand expected = new FindCommand(
                new PersonContainsKeywordsPredicate(
                        Arrays.asList("John", "Alice"),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyList()));
        assertParseSuccess(parser, "n/John n/Alice", expected);
    }

    @Test
    public void parse_duplicatePhonePrefixes_aggregatesAllValues() {
        FindCommand expected = new FindCommand(
                new PersonContainsKeywordsPredicate(
                        Collections.emptyList(),
                        Arrays.asList("9123", "9876"),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyList()));
        assertParseSuccess(parser, "p/9123 p/9876", expected);
    }

    @Test
    public void parse_multipleTagPrefixes_stillAggregatesAllValues() {
        FindCommand expected = new FindCommand(
                new PersonContainsKeywordsPredicate(
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Arrays.asList("friend", "colleague")));
        assertParseSuccess(parser, "t/friend t/colleague", expected);
    }

    @Test
    public void parse_disallowedKnownPrefixes_throwsParseException() {
        assertParseFailure(parser, "b/2020-01-01",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "c/CS2103",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "by/name",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_mixedAllowedAndDisallowed_throwsParseException() {
        assertParseFailure(parser, "n/Alice c/CS2103",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "t/friend z/abc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_unknownPrefix_throwsParseException() {
        assertParseFailure(parser, "z/foo",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "n/Alice z/foo",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

}
