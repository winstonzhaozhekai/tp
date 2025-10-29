package seedu.coursebook.logic.parser;

import static seedu.coursebook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
            String[] tokens = trimmed.split("\\s+");
            List<Index> indices = new ArrayList<>();
            List<String> warnings = new ArrayList<>();
            boolean sawInvalidNonPositive = false;
            java.util.Set<Integer> seen = new java.util.HashSet<>();
            boolean hasDuplicate = false;

            for (String token : tokens) {
                if (token.matches("0+") || token.matches("-\\d+")) {
                    sawInvalidNonPositive = true;
                    warnings.add("Index " + token + " is invalid");
                    continue;
                }
                // token is numeric and positive, parse normally
                int oneBased = Integer.parseInt(token);
                if (!seen.add(oneBased)) {
                    hasDuplicate = true;
                }
                indices.add(ParserUtil.parseIndex(token));
            }

            if (hasDuplicate) {
                throw new ParseException("Duplicate indices are not allowed.");
            }
            if (!indices.isEmpty()) {
                // If there are any warnings (e.g., zeros/negatives) but at least one valid index,
                // propagate warnings even when there is only one valid index.
                if (!warnings.isEmpty()) {
                    return new DeleteCommand(indices, warnings);
                }
                if (indices.size() == 1) {
                    // single valid index with no warnings
                    return new DeleteCommand(indices.get(0));
                }
                // multiple indices -> include any parsing-time warnings (may be empty)
                return new DeleteCommand(indices, warnings);
            }

            // No valid indices at all
            if (sawInvalidNonPositive) {
                throw new ParseException(seedu.coursebook.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
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
        Set<String> seen = new HashSet<>();

        for (String token : tokens) {
            String trimmedName = token.trim();
            if (trimmedName.isEmpty()) {
                continue; // Skip empty tokens from trailing commas
            }
            if (!Name.isValidName(trimmedName)) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
            }
            String key = trimmedName.toLowerCase(Locale.ROOT);
            if (!seen.add(key)) {
                throw new ParseException("Duplicate names are not allowed.");
            }
            names.add(new Name(trimmedName));
        }

        if (names.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        return names;
    }
}
