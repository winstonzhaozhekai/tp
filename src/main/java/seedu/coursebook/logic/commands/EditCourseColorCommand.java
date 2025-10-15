package seedu.coursebook.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.coursebook.logic.parser.CliSyntax.PREFIX_COURSE;

import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.logic.commands.exceptions.CommandException;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.course.CourseColor;

/**
 * Changes the color of a course code globally across all persons.
 */
public class EditCourseColorCommand extends Command {
    public static final String COMMAND_WORD = "editcourse";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sets color globally for a course code.\n"
            + "Parameters: " + PREFIX_COURSE + "COURSE_CODE,COLOR\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_COURSE + "CS2103T,yellow";

    private final String courseCode;
    private final CourseColor color;

    /**
     * Creates an EditCourseColorCommand.
     * @param courseCode the course code to update
     * @param color the new color to apply globally
     */
    public EditCourseColorCommand(String courseCode, CourseColor color) {
        this.courseCode = courseCode;
        this.color = color;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        if (courseCode == null || color == null) {
            throw new CommandException("Course code and color must be provided");
        }
        model.setCourseColor(courseCode, color);
        return new CommandResult("Updated color for " + courseCode + " to " + color.name());
    }
}


