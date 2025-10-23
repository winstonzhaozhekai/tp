package seedu.coursebook.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.stream.Collectors;

import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.logic.Messages;
import seedu.coursebook.logic.commands.exceptions.CommandException;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.person.Person;

/**
 * Confirms and executes the deletion of persons identified by the DeleteCommand.
 * This command is not directly invoked by users but by the UI after confirmation.
 */
public class ConfirmDeleteCommand extends Command {

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";
    public static final String MESSAGE_DELETE_PERSONS_SUCCESS = "Deleted %1$d person(s):\n%2$s";

    private final List<Person> personsToDelete;

    /**
     * Creates a ConfirmDeleteCommand to delete the specified persons.
     */
    public ConfirmDeleteCommand(List<Person> personsToDelete) {
        requireNonNull(personsToDelete);
        this.personsToDelete = personsToDelete;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        // Delete all persons
        for (Person person : personsToDelete) {
            model.deletePerson(person);
        }
        model.commitCourseBook();

        return formatResult(personsToDelete);
    }

    /**
     * Formats the result message based on the number of deleted persons.
     */
    private CommandResult formatResult(List<Person> deletedPersons) {
        String deletedList = deletedPersons.stream()
                .map(Messages::format)
                .collect(Collectors.joining("\n"));

        if (deletedPersons.size() == 1) {
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, deletedList));
        } else {
            return new CommandResult(String.format(MESSAGE_DELETE_PERSONS_SUCCESS,
                    deletedPersons.size(), deletedList));
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ConfirmDeleteCommand)) {
            return false;
        }

        ConfirmDeleteCommand otherCommand = (ConfirmDeleteCommand) other;
        return personsToDelete.equals(otherCommand.personsToDelete);
    }
}

