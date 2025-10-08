package seedu.coursebook.logic.parser;

import static seedu.coursebook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.coursebook.logic.parser.CliSyntax.PREFIX_COURSE;

import java.util.Set;

import seedu.coursebook.commons.core.index.Index;
import seedu.coursebook.logic.commands.AddCourseCommand;
import seedu.coursebook.logic.parser.exceptions.ParseException;
import seedu.coursebook.model.course.Course;

/**
 * Parses input arguments and creates a new AddCourseCommand object
 */
public class AddCourseCommandParser implements Parser<AddCourseCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCourseCommand
     * and returns an AddCourseCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCourseCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_COURSE);

        // index is taken from the preamble
        if (argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCourseCommand.MESSAGE_USAGE));
        }

        Index index = ParserUtil.parseIndex(argMultimap.getPreamble());

        // must have at least one c/
        if (argMultimap.getAllValues(PREFIX_COURSE).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCourseCommand.MESSAGE_USAGE));
        }

        Set<Course> coursesToAdd = ParserUtil.parseCourses(argMultimap.getAllValues(PREFIX_COURSE));

        return new AddCourseCommand(index, coursesToAdd);
    }
}
