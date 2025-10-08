package seedu.coursebook.logic.commands;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.person.Person;

import static java.util.Objects.requireNonNull;
import java.util.function.Predicate;

/**
 * Lists all persons in the given course in the course book to the user.
 * The command filters only the contacts in the given course and shows them.
 */
public class ListByCourseCommand extends Command {
    private final String course;

    /**
     * Constructs a {@code ListByCourseCommand} with the given course code.
     *
     * @param course The course code to filter the contacts with.
     */
    public ListByCourseCommand(String course){
        this.course = course;
    }

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all persons enrolled in the specified course.\n"
            + "Parameters: c/COURSE_NAME\n"
            + "Example: " + COMMAND_WORD + " c/CS2103T";


    /**
     * Executes the command by filtering the contacts list to include only those
     * enrolled in the given course. Returns a {@code CommandResult} with feedback.
     *
     * @param model The model containing the person list and filtering condition.
     * @return A {@code CommandResult} indicating the outcome of the command.
     */

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        Predicate<Person> PREDICATE_SHOW_PERSONS_IN_COURSE= person -> person.getCourses().stream().map(c -> c.courseCode).anyMatch(code ->code.equalsIgnoreCase(course));
        model.updateFilteredPersonList(PREDICATE_SHOW_PERSONS_IN_COURSE);
        int size = model.getFilteredPersonList().size();

        if(size == 0){
            return new CommandResult("No such course: " + course);
        }
        else {
            return new CommandResult("Listed all persons in " + course);
        }
    }
}
