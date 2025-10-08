package seedu.address.logic.commands;

/**
 * Reverts the {@code model}'s book to its previous state.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_SUCCESS = "Undo success!";
    public static final String MESSAGE_FAILURE = "No more commands to undo!";

    @Override
    public CommandResult execute(Model model, CommandHistory, history) throws CommandException {
        requireNonNull(model);

        if (!model.canUndoCourseBook()) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        model.undoCourseBook();
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
