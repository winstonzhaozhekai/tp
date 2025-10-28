package seedu.coursebook.logic.parser;

import static seedu.coursebook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.coursebook.commons.core.index.Index;
import seedu.coursebook.logic.commands.UnfavouriteCommand;
import seedu.coursebook.logic.parser.exceptions.ParseException;
import seedu.coursebook.model.person.Name;

/**
 * Parses input arguments and creates a new UnfavouriteCommand object
 */
public class UnfavouriteCommandParser implements Parser<UnfavouriteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the UnfavouriteCommand
     * and returns an UnfavouriteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public UnfavouriteCommand parse(String args) throws ParseException {
        String trimmed = args == null ? "" : args.trim();

        if (trimmed.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnfavouriteCommand.MESSAGE_USAGE));
        }

        // Try parse as index first
        try {
            Index index = ParserUtil.parseIndex(trimmed);
            return new UnfavouriteCommand(index);
        } catch (ParseException pe) {
            // If it's a negative index error, re-throw it
            if (pe.getMessage().equals(ParserUtil.MESSAGE_NEGATIVE_INDEX)) {
                throw pe;
            }
            // Fall through to parse as name for other index errors
        }

        // Parse as name
        if (!Name.isValidName(trimmed)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnfavouriteCommand.MESSAGE_USAGE));
        }
        return new UnfavouriteCommand(new Name(trimmed));
    }

}

