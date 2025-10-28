package seedu.coursebook.logic.parser;

import static seedu.coursebook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.coursebook.commons.core.index.Index;
import seedu.coursebook.logic.commands.ViewPersonCommand;
import seedu.coursebook.logic.parser.exceptions.ParseException;
import seedu.coursebook.model.person.Name;

/**
 * Parses input arguments and creates a new ViewPersonCommand object
 */
public class ViewPersonCommandParser implements Parser<ViewPersonCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ViewPersonCommand
     * and returns a ViewPersonCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ViewPersonCommand parse(String args) throws ParseException {
        String trimmed = args == null ? "" : args.trim();

        if (trimmed.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewPersonCommand.MESSAGE_USAGE));
        }

        // Try parse as index first
        try {
            Index index = ParserUtil.parseIndex(trimmed);
            return new ViewPersonCommand(index);
        } catch (ParseException pe) {
            // Fall through to parse as name
        }

        // Parse as name
        if (!Name.isValidName(trimmed)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewPersonCommand.MESSAGE_USAGE));
        }
        return new ViewPersonCommand(new Name(trimmed));
    }
}
