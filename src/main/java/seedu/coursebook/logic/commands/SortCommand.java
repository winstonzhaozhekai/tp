package seedu.coursebook.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;

import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.person.Person;

/**
 * Sorts the contact list by name in ascending or descending order.
 */
public class SortCommand extends Command {
    public static final String COMMAND_WORD = "sortn";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sorts the contact list by name.\n"
            + "Parameters: by/ [asc|desc]\n"
            + "Example: " + COMMAND_WORD + " by/ asc";

    private final String order;

    /**
     * Constructs a SortCommand with the given sort order.
     *
     * @param order The sorting order: must be "asc" or "desc".
     */
    public SortCommand(String order) {
        this.order = order;
    }

    /**
     * Executes the sort command by sorting the contact list in the given order.
     *
     * @param model The model containing the contact list.
     * @param history The command history (unused here).
     * @return A CommandResult with sort success message.
     */
    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);

        Comparator<Person> comparator = Comparator.comparing(p -> p.getName().fullName);

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }

        model.sortSelectedPersons(comparator);

        return new CommandResult("Sorted contacts by name in " + order + "ending order.", false, false, true, false);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof SortCommand)) {
            return false;
        }

        SortCommand otherCommand = (SortCommand) other;
        return order.equalsIgnoreCase(otherCommand.order);
    }

    @Override
    public int hashCode() {
        return order.toLowerCase().hashCode();
    }
}
