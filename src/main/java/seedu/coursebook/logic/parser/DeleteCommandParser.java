package seedu.coursebook.logic.parser;

import static seedu.coursebook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.coursebook.commons.core.index.Index;
import seedu.coursebook.logic.commands.DeleteCommand;
import seedu.coursebook.logic.parser.exceptions.ParseException;
import seedu.coursebook.model.person.Name;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        String trimmed = args == null ? "" : args.trim();

        if (trimmed.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        if (trimmed.matches("0+|-\\d+")) {
            throw new ParseException(seedu.coursebook.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        try {
            Index index = ParserUtil.parseIndex(trimmed);
            return new DeleteCommand(index);
        } catch (ParseException ignored) {
            // Input is not a valid index; attempt to parse as a name instead
        }

        if (!Name.isValidName(trimmed)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }
        return new DeleteCommand(new Name(trimmed));
    }
}
