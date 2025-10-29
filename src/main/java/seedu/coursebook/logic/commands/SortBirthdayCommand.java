package seedu.coursebook.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.person.Person;

/**
 * Sorts the contact list by how soon each person's next birthday occurs, starting from today's date.
 */
public class SortBirthdayCommand extends Command {
    public static final String COMMAND_WORD = "sortb";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sorts the contact list by upcoming birthdays.\n"
            + "Example: " + COMMAND_WORD;

    /**
     * Constructs a SortBirthdayCommand.
     * This command does not require any parameters and always sorts by proximity to today's date.
     */
    public SortBirthdayCommand() {

    }

    /**
     * Executes the sortb command by sorting the contact list based on how soon each person's next birthday occurs.
     * Birthdays are normalized to the current or next year to ensure proper ordering.
     *
     * @param model The model containing the contact list.
     * @param history The command history (unused in this command).
     * @return A CommandResult indicating that the contacts have been sorted.
     */
    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);

        Comparator<Person> comparator = Comparator.comparingInt(person -> {
            if (person.getBirthday() == null) {
                return Integer.MAX_VALUE;
            }

            LocalDate today = LocalDate.now();
            LocalDate birthday = person.getBirthday().getDate();
            LocalDate nextBirthday = birthday.withYear(today.getYear());

            if (nextBirthday.isBefore(today)) {
                nextBirthday = nextBirthday.plusYears(1);
            }

            return (int) ChronoUnit.DAYS.between(today, nextBirthday);
        });

        model.sortSelectedPersons(comparator);
        if (model.getFilteredPersonList().isEmpty()) {
            return new CommandResult("No contacts to sort by birthday.", false, false, true, false);
        } else {
            return new CommandResult("Sorted contacts by upcoming birthday.", false, false, true, false);
        }
    }

    /**
     * Checks equality based on command type.
     * Since this command has no parameters, all instances are considered equal.
     *
     * @param other The object to compare against.
     * @return True if the other object is also a SortBirthdayCommand.
     */
    @Override
    public boolean equals(Object other) {
        return other instanceof SortBirthdayCommand;
    }

    @Override
    public int hashCode() {
        return COMMAND_WORD.hashCode();
    }
}
