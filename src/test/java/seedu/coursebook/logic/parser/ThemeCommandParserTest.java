package seedu.coursebook.logic.parser;

import static seedu.coursebook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.coursebook.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.coursebook.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.coursebook.logic.commands.ThemeCommand;

public class ThemeCommandParserTest {

    private ThemeCommandParser parser = new ThemeCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ThemeCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nullArg_throwsParseException() {
        assertParseFailure(parser, null,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ThemeCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidTheme_throwsParseException() {
        assertParseFailure(parser, "red", ThemeCommand.MESSAGE_INVALID_THEME);
        assertParseFailure(parser, "invalid", ThemeCommand.MESSAGE_INVALID_THEME);
        assertParseFailure(parser, "123", ThemeCommand.MESSAGE_INVALID_THEME);
        assertParseFailure(parser, "dark blue", ThemeCommand.MESSAGE_INVALID_THEME);
    }

    @Test
    public void parse_validDarkTheme_returnsThemeCommand() {
        ThemeCommand expectedCommand = new ThemeCommand(ThemeCommand.Theme.DARK);

        // no leading and trailing whitespaces
        assertParseSuccess(parser, "dark", expectedCommand);

        // with leading and trailing whitespaces
        assertParseSuccess(parser, "  dark  ", expectedCommand);

        // case-insensitive
        assertParseSuccess(parser, "DARK", expectedCommand);
        assertParseSuccess(parser, "Dark", expectedCommand);
    }

    @Test
    public void parse_validBlueTheme_returnsThemeCommand() {
        ThemeCommand expectedCommand = new ThemeCommand(ThemeCommand.Theme.BLUE);

        // no leading and trailing whitespaces
        assertParseSuccess(parser, "blue", expectedCommand);

        // with leading and trailing whitespaces
        assertParseSuccess(parser, "  blue  ", expectedCommand);

        // case-insensitive
        assertParseSuccess(parser, "BLUE", expectedCommand);
        assertParseSuccess(parser, "Blue", expectedCommand);
    }

    @Test
    public void parse_validLoveTheme_returnsThemeCommand() {
        ThemeCommand expectedCommand = new ThemeCommand(ThemeCommand.Theme.LOVE);

        // no leading and trailing whitespaces
        assertParseSuccess(parser, "love", expectedCommand);

        // with leading and trailing whitespaces
        assertParseSuccess(parser, "  love  ", expectedCommand);

        // case-insensitive
        assertParseSuccess(parser, "LOVE", expectedCommand);
        assertParseSuccess(parser, "Love", expectedCommand);
    }

    @Test
    public void parse_validTreeTheme_returnsThemeCommand() {
        ThemeCommand expectedCommand = new ThemeCommand(ThemeCommand.Theme.TREE);

        // no leading and trailing whitespaces
        assertParseSuccess(parser, "tree", expectedCommand);

        // with leading and trailing whitespaces
        assertParseSuccess(parser, "  tree  ", expectedCommand);

        // case-insensitive
        assertParseSuccess(parser, "TREE", expectedCommand);
        assertParseSuccess(parser, "Tree", expectedCommand);
    }
}
