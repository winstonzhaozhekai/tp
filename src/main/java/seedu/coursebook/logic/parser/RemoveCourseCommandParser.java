package seedu.coursebook.logic.parser;

import static seedu.coursebook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.coursebook.logic.parser.CliSyntax.PREFIX_COURSE;

import java.util.Set;

import seedu.coursebook.commons.core.index.Index;
import seedu.coursebook.logic.commands.RemoveCourseCommand;
import seedu.coursebook.logic.parser.exceptions.ParseException;
import seedu.coursebook.model.course.Course;

/**
 * Parses input arguments and creates a new RemoveCourseCommand object
 */
public class RemoveCourseCommandParser implements Parser<RemoveCourseCommand> {

    @Override
    public RemoveCourseCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_COURSE);

        if (argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemoveCourseCommand.MESSAGE_USAGE));
        }

        Index index = ParserUtil.parseIndex(argMultimap.getPreamble());

        if (argMultimap.getAllValues(PREFIX_COURSE).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemoveCourseCommand.MESSAGE_USAGE));
        }

        Set<Course> courses = ParserUtil.parseCourses(argMultimap.getAllValues(PREFIX_COURSE));
        return new RemoveCourseCommand(index, courses);
    }
}
