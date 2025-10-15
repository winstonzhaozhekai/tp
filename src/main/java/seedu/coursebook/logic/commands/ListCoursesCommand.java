package seedu.coursebook.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.model.Model;

/**
 * Lists all unique courses in the course book to the user.
 */
public class ListCoursesCommand extends Command {

    public static final String COMMAND_WORD = "listcourses";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all courses.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Listed all courses";

    /**
     * Predicate that always evaluates to true to show all courses
     */
    private static final java.util.function.Predicate<seedu.coursebook.model.course.Course>
            PREDICATE_SHOW_ALL_COURSES = course -> true;

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.updateFilteredCourseList(PREDICATE_SHOW_ALL_COURSES);

        int courseCount = model.getFilteredCourseList().size();
        String message = courseCount == 0
            ? "No courses found in the course book"
            : String.format("Listed %d course(s)", courseCount);

        return new CommandResult(message, false, false, false, true);
    }
}
