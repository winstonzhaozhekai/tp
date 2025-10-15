package seedu.coursebook.logic.parser;

import static seedu.coursebook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.coursebook.logic.Messages.MESSAGE_SINGLE_ORDER_ONLY;
import static seedu.coursebook.logic.parser.CliSyntax.PREFIX_SORT;

import seedu.coursebook.logic.commands.SortCommand;
import seedu.coursebook.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@link SortCommand} object.
 */
public class SortCommandParser implements Parser<SortCommand> {
    /**
     * Parses the given {@code String} of arguments and returns a {@code SortCommand}.
     *
     * @param args User input string containing sort parameters.
     * @return A {@code SortCommand} with the specified sort order.
     * @throws ParseException If the input is invalid or the sort order is not recognized.
     */
    public SortCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_SORT);

        // must have only one by/
        if (argMultimap.getAllValues(PREFIX_SORT).size() > 1) {
            throw new ParseException(String.format(MESSAGE_SINGLE_ORDER_ONLY, SortCommand.MESSAGE_USAGE));
        }

        String order = ParserUtil.parseOrder(
                argMultimap.getValue(PREFIX_SORT)
                        .orElseThrow(() -> new ParseException(
                                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE)
                                )
                        )
        );

        return new SortCommand(order);
    }
}
