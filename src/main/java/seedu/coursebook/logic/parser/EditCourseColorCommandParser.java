package seedu.coursebook.logic.parser;

import static seedu.coursebook.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.coursebook.logic.parser.CliSyntax.PREFIX_COURSE;

import seedu.coursebook.logic.commands.EditCourseColorCommand;
import seedu.coursebook.logic.parser.exceptions.ParseException;
import seedu.coursebook.model.course.CourseColor;

/**
 * Parses input arguments and creates a new EditCourseColorCommand object
 */
public class EditCourseColorCommandParser implements Parser<EditCourseColorCommand> {
    @Override
    public EditCourseColorCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_COURSE);
        String token = argMultimap.getValue(PREFIX_COURSE)
                .orElseThrow(() -> new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        EditCourseColorCommand.MESSAGE_USAGE)));

        String[] parts = token.split(",", 2);
        if (parts.length != 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditCourseColorCommand.MESSAGE_USAGE));
        }
        String code = parts[0].trim();
        String colorName = parts[1].trim();
        CourseColor color;
        try {
            color = CourseColor.fromName(colorName);
        } catch (IllegalArgumentException e) {
            throw new ParseException("Invalid course color: " + colorName);
        }
        return new EditCourseColorCommand(code, color);
    }
}


