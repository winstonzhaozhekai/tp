package seedu.coursebook.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.person.Person;

/**
 * Views all persons enrolled in a specific course.
 */
public class ViewCourseCommand extends Command {

    public static final String COMMAND_WORD = "viewcourse";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Views all persons enrolled in the specified course.\n"
            + "Parameters: c/COURSE_CODE\n"
            + "Example: " + COMMAND_WORD + " c/CS2103T";

    public static final String MESSAGE_SUCCESS = "Viewing persons in course: %s";
    public static final String MESSAGE_NO_PERSONS = "No persons enrolled in course: %s";

    private final String courseCode;

    /**
     * Creates a ViewCourseCommand to view persons in the specified {@code courseCode}
     */
    public ViewCourseCommand(String courseCode) {
        requireNonNull(courseCode);
        this.courseCode = courseCode;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);

        Predicate<Person> personsInCourse = person ->
                person.getCourses().stream()
                        .anyMatch(course -> course.courseCode.equalsIgnoreCase(courseCode));

        model.updateFilteredPersonList(personsInCourse);

        int personCount = model.getFilteredPersonList().size();
        String message = personCount == 0
                ? String.format(MESSAGE_NO_PERSONS, courseCode)
                : String.format(MESSAGE_SUCCESS, courseCode) + " (" + personCount + " person(s))";

        // Show persons view
        return new CommandResult(message, false, false, true, false);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ViewCourseCommand)) {
            return false;
        }

        ViewCourseCommand otherCommand = (ViewCourseCommand) other;
        return courseCode.equals(otherCommand.courseCode);
    }
}
