package seedu.coursebook.logic.parser;

import static seedu.coursebook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.coursebook.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.coursebook.logic.parser.CliSyntax.PREFIX_COURSE;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.coursebook.commons.core.LogsCenter;
import seedu.coursebook.logic.commands.AddCommand;
import seedu.coursebook.logic.commands.AddCourseCommand;
import seedu.coursebook.logic.commands.BirthdayCommand;
import seedu.coursebook.logic.commands.ClearCommand;
import seedu.coursebook.logic.commands.Command;
import seedu.coursebook.logic.commands.DeleteCommand;
import seedu.coursebook.logic.commands.EditCommand;
import seedu.coursebook.logic.commands.EditCourseColorCommand;
import seedu.coursebook.logic.commands.ExitCommand;
import seedu.coursebook.logic.commands.FavCommand;
import seedu.coursebook.logic.commands.FavouriteCommand;
import seedu.coursebook.logic.commands.FindCommand;
import seedu.coursebook.logic.commands.HelpCommand;
import seedu.coursebook.logic.commands.HistoryCommand;
import seedu.coursebook.logic.commands.HomeCommand;
import seedu.coursebook.logic.commands.ListByCourseCommand;
import seedu.coursebook.logic.commands.ListCommand;
import seedu.coursebook.logic.commands.ListCoursesCommand;
import seedu.coursebook.logic.commands.RedoCommand;
import seedu.coursebook.logic.commands.RemoveCourseCommand;
import seedu.coursebook.logic.commands.SortBirthdayCommand;
import seedu.coursebook.logic.commands.SortCommand;
import seedu.coursebook.logic.commands.SummaryCommand;
import seedu.coursebook.logic.commands.UndoCommand;
import seedu.coursebook.logic.commands.UnfavouriteCommand;
import seedu.coursebook.logic.commands.ViewCourseCommand;
import seedu.coursebook.logic.commands.ViewPersonCommand;
import seedu.coursebook.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class CourseBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final Logger logger = LogsCenter.getLogger(CourseBookParser.class);

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public static Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        // Note to developers: Change the log level in config.json to enable lower level (i.e., FINE, FINER and lower)
        // log messages such as the one below.
        // Lower level log messages are used sparingly to minimize noise in the code.
        logger.fine("Command word: " + commandWord + "; Arguments: " + arguments);

        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
        case DeleteCommand.ALIAS_WORD:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            if (!arguments.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearCommand.MESSAGE_USAGE));
            }
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
        case FindCommand.ALIAS_WORD:
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
        case ListCommand.ALIAS_WORD:
            ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(arguments, PREFIX_COURSE);
            if (argMultimap.getValue(PREFIX_COURSE).isPresent()) {
                return new ListByCourseCommandParser().parse(arguments);
            } else if (arguments.trim().isEmpty()) {
                return new ListCommand();
            } else {
                throw new ParseException("Invalid command format.\n" + ListByCourseCommand.MESSAGE_USAGE);
            }

        case SummaryCommand.COMMAND_WORD:
            if (!arguments.trim().isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        SummaryCommand.MESSAGE_USAGE));
            }
            return new SummaryCommand();

        case ExitCommand.COMMAND_WORD:
            if (!arguments.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExitCommand.MESSAGE_USAGE));
            }
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            if (!arguments.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
            }
            return new HelpCommand();

        case AddCourseCommand.COMMAND_WORD:
            return new AddCourseCommandParser().parse(arguments);
        case EditCourseColorCommand.COMMAND_WORD:
            return new EditCourseColorCommandParser().parse(arguments);

        case RemoveCourseCommand.COMMAND_WORD:
            return new RemoveCourseCommandParser().parse(arguments);

        case UndoCommand.COMMAND_WORD:
            if (!arguments.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UndoCommand.MESSAGE_USAGE));
            }
            return new UndoCommand();

        case SortCommand.COMMAND_WORD:
            return new SortCommandParser().parse(arguments);

        case SortBirthdayCommand.COMMAND_WORD:
            if (!arguments.trim().isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        SortBirthdayCommand.MESSAGE_USAGE));
            }
            return new SortBirthdayCommand();

        case RedoCommand.COMMAND_WORD:
            if (!arguments.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RedoCommand.MESSAGE_USAGE));
            }
            return new RedoCommand();

        case ListCoursesCommand.COMMAND_WORD:
            if (!arguments.isEmpty()) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCoursesCommand.MESSAGE_USAGE)
                );
            }
            return new ListCoursesCommand();

        case ViewCourseCommand.COMMAND_WORD:
            return new ViewCourseCommandParser().parse(arguments);

        case HistoryCommand.COMMAND_WORD:
            if (!arguments.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HistoryCommand.MESSAGE_USAGE));
            }
            return new HistoryCommand();

        case HomeCommand.COMMAND_WORD:
            if (!arguments.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HomeCommand.MESSAGE_USAGE));
            }
            return new HomeCommand();

        case BirthdayCommand.COMMAND_WORD:
            return new BirthdayCommandParser().parse(arguments);

        case ViewPersonCommand.COMMAND_WORD:
            return new ViewPersonCommandParser().parse(arguments);
        case FavouriteCommand.COMMAND_WORD:
            return new FavouriteCommandParser().parse(arguments);

        case UnfavouriteCommand.COMMAND_WORD:
            return new UnfavouriteCommandParser().parse(arguments);

        case FavCommand.COMMAND_WORD:
            if (!arguments.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
            }
            return new FavCommand();

        default:
            logger.finer("This user input caused a ParseException: " + userInput);
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
