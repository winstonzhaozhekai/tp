package seedu.coursebook.logic.parser;

import static seedu.coursebook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.coursebook.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.coursebook.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.coursebook.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.coursebook.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.coursebook.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.coursebook.logic.commands.FindCommand;
import seedu.coursebook.logic.parser.exceptions.ParseException;
import seedu.coursebook.model.person.PersonContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);

        List<String> nameKeywords = new ArrayList<>();
        List<String> phoneKeywords = new ArrayList<>();
        List<String> emailKeywords = new ArrayList<>();
        List<String> addressKeywords = new ArrayList<>();
        List<String> tagKeywords = new ArrayList<>();

        // Collect prefixed values (split by whitespace into individual keywords)
        argMultimap.getValue(PREFIX_NAME).ifPresent(v -> nameKeywords.addAll(Arrays.asList(v.trim().split("\\s+"))));
        argMultimap.getValue(PREFIX_PHONE).ifPresent(v -> phoneKeywords.addAll(Arrays.asList(v.trim().split("\\s+"))));
        argMultimap.getValue(PREFIX_EMAIL)
                .ifPresent(v -> emailKeywords.addAll(Arrays.asList(v.trim().split("\\s+"))));
        argMultimap.getValue(PREFIX_ADDRESS)
                .ifPresent(v -> addressKeywords.addAll(Arrays.asList(v.trim().split("\\s+"))));
        // tags can appear multiple times; split each occurrence by whitespace
        for (String tagValue : argMultimap.getAllValues(PREFIX_TAG)) {
            tagKeywords.addAll(Arrays.asList(tagValue.trim().split("\\s+")));
        }

        boolean anyPrefixedProvided = !(nameKeywords.isEmpty() && phoneKeywords.isEmpty()
                && emailKeywords.isEmpty() && addressKeywords.isEmpty() && tagKeywords.isEmpty());

        if (!anyPrefixedProvided) {
            // Fallback to legacy behavior: treat entire args as name keywords
            String[] keywords = trimmedArgs.split("\\s+");
            nameKeywords.addAll(Arrays.asList(keywords));
        }

        // Validate that at least one non-empty keyword exists
        boolean hasAnyKeyword = nameKeywords.stream().anyMatch(s -> !s.isBlank())
                || phoneKeywords.stream().anyMatch(s -> !s.isBlank())
                || emailKeywords.stream().anyMatch(s -> !s.isBlank())
                || addressKeywords.stream().anyMatch(s -> !s.isBlank())
                || tagKeywords.stream().anyMatch(s -> !s.isBlank());

        if (!hasAnyKeyword) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(
                nameKeywords, phoneKeywords, emailKeywords, addressKeywords, tagKeywords);
        return new FindCommand(predicate);
    }

}
