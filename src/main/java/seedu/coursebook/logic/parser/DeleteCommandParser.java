package seedu.coursebook.logic.parser;

import static seedu.coursebook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.List;

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

        // Try parsing as indices first (space-separated)
        if (looksLikeIndices(trimmed)) {
            List<Index> indices = parseIndices(trimmed);
            if (indices.size() == 1) {
                return new DeleteCommand(indices.get(0));
            }
            return new DeleteCommand(indices);
        }

        // Parse as names (comma-separated)
        List<Name> names = parseNames(trimmed);
        if (names.size() == 1) {
            return new DeleteCommand(names.get(0));
        }
        return new DeleteCommand(names, true);
    }

    /**
     * Checks if the input looks like it could be indices (contains only digits and spaces).
     */
    private boolean looksLikeIndices(String input) {
        // If there's a comma, it's definitely names
        if (input.contains(",")) {
            return false;
        }
        // Check if all tokens are numeric (allow optional leading '-')
        String[] tokens = input.split("\\s+");
        for (String token : tokens) {
            if (!token.matches("-?\\d+")) {
                return false;
            }
        }
        return true;
    }

    /**
     * Parses space-separated indices.
     */
    private List<Index> parseIndices(String input) throws ParseException {
        String[] tokens = input.split("\\s+");
        List<Index> indices = new ArrayList<>();

        for (String token : tokens) {
            if (token.matches("0+")) {
                throw new ParseException(seedu.coursebook.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            indices.add(ParserUtil.parseIndex(token));
        }

        return indices;
    }

    /**
     * Parses comma-separated names.
     */
    private List<Name> parseNames(String input) throws ParseException {
        String[] tokens = input.split(",");
        List<Name> names = new ArrayList<>();

        for (String token : tokens) {
            String trimmedName = token.trim();
            if (trimmedName.isEmpty()) {
                continue; // Skip empty tokens from trailing commas
            }
            if (!Name.isValidName(trimmedName)) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
            }
            names.add(new Name(trimmedName));
        }

        if (names.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        return names;
    }
}
