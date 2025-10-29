package seedu.coursebook.logic.commands;

import java.util.logging.Logger;
import static java.util.Objects.requireNonNull;

import seedu.coursebook.commons.core.index.Index;
import seedu.coursebook.commons.util.ToStringBuilder;
import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.logic.Messages;
import seedu.coursebook.logic.commands.exceptions.CommandException;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.person.Birthday;
import seedu.coursebook.model.person.Person;

/**
 * Adds a birthday to an existing person in the course book.
 */
public class BirthdayCommand extends Command {
    private static final Logger logger = Logger.getLogger(BirthdayCommand.class.getName());

    public static final String COMMAND_WORD = "bday";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a birthday to the person identified "
            + "by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "b/BIRTHDAY\n"
            + "Example: " + COMMAND_WORD + " 1 b/20-02-2007";

    public static final String MESSAGE_ADD_BIRTHDAY_SUCCESS = "Added birthday: ";
    public static final String MESSAGE_DUPLICATE_BIRTHDAY = "Birthday has already been added!";

    private final Index index;
    private final Birthday birthday;

    /**
     * Constructs a BirthdayCommand to add a birthday to the specified person.
     *
     * @param index Index of the person in the filtered person list.
     * @param birthday Birthday to be added to the person.
     */
    public BirthdayCommand(Index index, Birthday birthday) {
        requireNonNull(index);
        requireNonNull(birthday);

        this.index = index;
        this.birthday = birthday;
    }

    /**
     * Executes the command to add a birthday to the specified person.
     * If the person already has the same birthday, a CommandException is thrown.
     *
     * @param model The model containing the course book data.
     * @param history The command history (not used in this command).
     * @return A CommandResult indicating success or failure.
     * @throws CommandException If the index is invalid or the birthday is a duplicate.
     */
    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        logger.info("Executing BirthdayCommand for index: " + index);

        if (index.getZeroBased() >= model.getFilteredPersonList().size()) {
            logger.warning("Invalid index: " + index.getZeroBased());
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = model.getFilteredPersonList().get(index.getZeroBased());
        logger.fine("Person to edit: " + personToEdit.getName());

        Birthday existingBirthday = personToEdit.getBirthday();

        if (existingBirthday != null && existingBirthday.equals(birthday)) {
            throw new CommandException(MESSAGE_DUPLICATE_BIRTHDAY);
        }

        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getAddress(),
                personToEdit.getTags(),
                personToEdit.getCourses(),
                birthday,
                personToEdit.isFavourite()
        );

        logger.info("Birthday added successfully: " + birthday + " to " + editedPerson.getName());

        model.setPerson(personToEdit, editedPerson);
        model.commitCourseBook();
        return new CommandResult(
                String.format(MESSAGE_ADD_BIRTHDAY_SUCCESS + birthday, Messages.format(editedPerson)),
                false, false, true, false
        );
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof BirthdayCommand)) {
            return false;
        }

        BirthdayCommand otherBirthdayCommand = (BirthdayCommand) other;
        return index.equals(otherBirthdayCommand.index)
                && birthday.equals(otherBirthdayCommand.birthday);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("birthday", birthday)
                .toString();
    }
}
