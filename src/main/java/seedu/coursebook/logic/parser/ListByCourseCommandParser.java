package seedu.coursebook.logic.parser;

import static seedu.coursebook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.coursebook.logic.Messages.MESSAGE_SINGLE_COURSE_ONLY;
import static seedu.coursebook.logic.parser.CliSyntax.PREFIX_COURSE;

import seedu.coursebook.logic.commands.ListByCourseCommand;
import seedu.coursebook.logic.parser.exceptions.ParseException;
import seedu.coursebook.model.course.Course;

/**
 * Parses all the input arguments and creates a new {@code ListByCourseCommand} object.
 * It expects a single {@code c/COURSE_NAME} prefix and validates that
 * only one course is provided.
 */
public class ListByCourseCommandParser implements Parser<ListByCourseCommand> {

    /**
     * Parses the given arguments and returns a
     * {@code ListByCourseCommand} object for execution.
     *
     * @param args User input arguments following the {@code list} command.
     * @return A {@code ListByCourseCommand} that filters persons by the specified course.
     * @throws ParseException If the input does not contain a valid course prefix,
     *                        contains multiple courses or fails to parse the course.
     */
    public ListByCourseCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_COURSE);

        // must have at least one c/
        if (argMultimap.getAllValues(PREFIX_COURSE).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListByCourseCommand.MESSAGE_USAGE));
        }

        // must have only one c/
        if (argMultimap.getAllValues(PREFIX_COURSE).size() > 1) {
            throw new ParseException(String.format(MESSAGE_SINGLE_COURSE_ONLY, ListByCourseCommand.MESSAGE_USAGE));
        }

        Course courseToSearch = ParserUtil.parseCourse(
                argMultimap.getValue(PREFIX_COURSE)
                        .orElseThrow(() -> new ParseException(
                                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListByCourseCommand.MESSAGE_USAGE)
                                )
                        )
        );

        return new ListByCourseCommand(courseToSearch.courseCode);
    }
}
