package seedu.coursebook.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.coursebook.logic.parser.CliSyntax.PREFIX_COURSE;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.coursebook.commons.core.index.Index;
import seedu.coursebook.commons.util.ToStringBuilder;
import seedu.coursebook.logic.Messages;
import seedu.coursebook.logic.commands.exceptions.CommandException;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.course.Course;
import seedu.coursebook.model.person.Person;

/**
 * Removes courses from an existing person in the course book.
 */
public class RemoveCourseCommand extends Command {

    public static final String COMMAND_WORD = "removecourse";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes courses from the person identified "
            + "by the index number used in the displayed person list. "
            + "Only specified existing courses will be removed.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_COURSE + "COURSE_CODE [" + PREFIX_COURSE + "MORE_COURSES]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_COURSE + "CS2103T "
            + PREFIX_COURSE + "CS2040S";

    public static final String MESSAGE_REMOVE_COURSE_SUCCESS = "Removed courses from Person: %1$s";
    public static final String MESSAGE_NO_MATCHING_COURSES = "None of the specified courses exist for this person";

    private final Index index;
    private final Set<Course> coursesToRemove;

    public RemoveCourseCommand(Index index, Set<Course> coursesToRemove) {
        requireNonNull(index);
        requireNonNull(coursesToRemove);
        this.index = index;
        this.coursesToRemove = new HashSet<>(coursesToRemove);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        Set<Course> existing = personToEdit.getCourses();
        Set<Course> intersection = new HashSet<>(existing);
        intersection.retainAll(coursesToRemove);

        if (intersection.isEmpty()) {
            throw new CommandException(MESSAGE_NO_MATCHING_COURSES);
        }

        Set<Course> updatedCourses = new HashSet<>(existing);
        updatedCourses.removeAll(intersection);

        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getAddress(),
                personToEdit.getTags(),
                updatedCourses
        );

        model.setPerson(personToEdit, editedPerson);
        return new CommandResult(String.format(MESSAGE_REMOVE_COURSE_SUCCESS, Messages.format(editedPerson)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof RemoveCourseCommand)) return false;
        RemoveCourseCommand o = (RemoveCourseCommand) other;
        return index.equals(o.index) && coursesToRemove.equals(o.coursesToRemove);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("coursesToRemove", coursesToRemove)
                .toString();
    }
}