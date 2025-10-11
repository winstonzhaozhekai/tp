package seedu.coursebook.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.coursebook.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.model.Model;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {
    public static final String COMMAND_WORD = "list";
    public static final String ALIAS_WORD = "ls";

    public static final String MESSAGE_SUCCESS = "Listed all persons";
    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
