package seedu.coursebook.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.coursebook.logic.parser.CliSyntax.PREFIX_COURSE;

import java.util.HashSet;
import java.util.Set;

import seedu.coursebook.commons.core.index.Index;
import seedu.coursebook.commons.util.ToStringBuilder;
import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.logic.Messages;
import seedu.coursebook.logic.commands.exceptions.CommandException;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.course.Course;
import seedu.coursebook.model.course.CourseColor;
import seedu.coursebook.model.person.Person;

/**
 * Adds courses to an existing person in the course book.
 */
public class AddCourseCommand extends Command {

    public static final String COMMAND_WORD = "addcourse";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds courses to the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing courses will be preserved.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_COURSE + "COURSE_CODE[,COLOR] [" + PREFIX_COURSE + "MORE]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_COURSE + "CS2103T,green "
            + PREFIX_COURSE + "CS2040S";

    public static final String MESSAGE_ADD_COURSE_SUCCESS = "Added courses to Person: %1$s";
    public static final String MESSAGE_DUPLICATE_COURSE = "This person already has one or more of these courses";
    public static final String MESSAGE_NO_COURSES_ADDED = "No courses to add";

    private final Index index;
    private final Set<Course> coursesToAdd;

    /**
     * @param index of the person in the filtered person list to edit
     * @param coursesToAdd courses to add to the person
     */
    public AddCourseCommand(Index index, Set<Course> coursesToAdd) {
        requireNonNull(index);
        requireNonNull(coursesToAdd);

        this.index = index;
        this.coursesToAdd = new HashSet<>(coursesToAdd);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        if (index.getZeroBased() >= model.getFilteredPersonList().size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = model.getFilteredPersonList().get(index.getZeroBased());
        Set<Course> existingCourses = personToEdit.getCourses();

        // Determine global color consistency and handle conflicts
        for (Course toAdd : coursesToAdd) {
            // If any existing person has this course code, discover its color
            CourseColor existingColor = model.getFilteredCourseList().stream()
                    .filter(c -> c.courseCode.equalsIgnoreCase(toAdd.courseCode))
                    .map(c -> c.color)
                    .findFirst()
                    .orElse(null);
            if (existingColor != null && toAdd.color != null && existingColor != toAdd.color) {
                // Conflict: update globally to new color
                model.setCourseColor(toAdd.courseCode, toAdd.color);
            } else if (existingColor == null && toAdd.color == null) {
                // New course with no color specified: default to GREEN globally
                model.setCourseColor(toAdd.courseCode, CourseColor.GREEN);
            }
        }

        Set<Course> newCourses = new HashSet<>(existingCourses);
        newCourses.addAll(coursesToAdd);

        if (existingCourses.containsAll(coursesToAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_COURSE);
        }

        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getAddress(),
                personToEdit.getTags(),
                newCourses,
                personToEdit.getBirthday()
        );

        model.setPerson(personToEdit, editedPerson);
        model.commitCourseBook();
        return new CommandResult(
                String.format(MESSAGE_ADD_COURSE_SUCCESS, Messages.format(editedPerson))
        );
    }




    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCourseCommand)) {
            return false;
        }

        AddCourseCommand otherAddCourseCommand = (AddCourseCommand) other;
        return index.equals(otherAddCourseCommand.index)
                && coursesToAdd.equals(otherAddCourseCommand.coursesToAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("coursesToAdd", coursesToAdd)
                .toString();
    }
}
