package seedu.coursebook.logic.parser;

import static seedu.coursebook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.coursebook.logic.Messages.MESSAGE_SINGLE_BDAY_ONLY;
import static seedu.coursebook.logic.parser.CliSyntax.PREFIX_BDAY;

import java.time.LocalDate;
import java.util.Optional;

import seedu.coursebook.commons.core.index.Index;
import seedu.coursebook.logic.commands.BirthdayCommand;
import seedu.coursebook.logic.parser.exceptions.ParseException;
import seedu.coursebook.model.person.Birthday;

/**
 * Parses input arguments and creates a new {@link BirthdayCommand} object.
 * This parser expects an index followed by a birthday prefix (b/) with a valid date.
 * Example input: {@code bday 1 b/20-02-2007}
 */
public class BirthdayCommandParser implements Parser<BirthdayCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the {@link BirthdayCommand}
     * and returns a {@link BirthdayCommand} object for execution.
     *
     * @param args User input string containing the index and birthday.
     * @return A {@link BirthdayCommand} with the parsed index and birthday.
     * @throws ParseException If the input format is invalid or required fields are missing.
     */
    public BirthdayCommand parse(String args) throws ParseException {
        if (!args.contains(PREFIX_BDAY.getPrefix())) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, BirthdayCommand.MESSAGE_USAGE));
        }

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_BDAY);

        // index is taken from the preamble
        if (argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, BirthdayCommand.MESSAGE_USAGE));
        }
        if (argMultimap.getAllValues(PREFIX_BDAY).size() > 1) {
            throw new ParseException(String.format(MESSAGE_SINGLE_BDAY_ONLY, BirthdayCommand.MESSAGE_USAGE));
        }

        Index index = ParserUtil.parseIndex(argMultimap.getPreamble());

        Optional<String> birthdayOpt = argMultimap.getValue(PREFIX_BDAY);
        if (birthdayOpt.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, BirthdayCommand.MESSAGE_USAGE));
        }
        Birthday birthday = ParserUtil.parseBirthday(birthdayOpt.get());

        if (birthday.getDate().isAfter(LocalDate.now())) {
            throw new ParseException("Birthday cannot be in the future.");
        }

        if (birthday.getDate().isBefore(LocalDate.of(1900, 01, 01))) {
            throw new ParseException("The date entered is too far in the past.");
        }

        return new BirthdayCommand(index, birthday);
    }
}
