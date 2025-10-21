package seedu.coursebook.logic.parser;

import static seedu.coursebook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.coursebook.logic.commands.ThemeCommand;
import seedu.coursebook.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ThemeCommand object
 */
public class ThemeCommandParser implements Parser<ThemeCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ThemeCommand
     * and returns a ThemeCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ThemeCommand parse(String args) throws ParseException {
        String trimmed = args == null ? "" : args.trim();

        if (trimmed.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ThemeCommand.MESSAGE_USAGE)
            );
        }

        ThemeCommand.Theme theme = ThemeCommand.Theme.fromString(trimmed);

        if (theme == null) {
            throw new ParseException(ThemeCommand.MESSAGE_INVALID_THEME);
        }

        return new ThemeCommand(theme);
    }
}
