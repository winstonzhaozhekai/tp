package seedu.coursebook.logic.commands;
import static java.util.Objects.requireNonNull;
import static seedu.coursebook.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.logic.commands.exceptions.CommandException;
import seedu.coursebook.model.Model;



/**
 * Reverts the {@code model}'s book to its previous state.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Undo last change.\n"
            + "Example: " + COMMAND_WORD;
    public static final String MESSAGE_SUCCESS = "Undo success!";
    public static final String MESSAGE_FAILURE = "No more commands to undo!";

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        if (!model.canUndoCourseBook()) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        model.undoCourseBook();
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // Check if theme changed during undo
        if (model.hasThemeChangedDuringUndo()) {
            ThemeCommand.Theme restoredTheme = model.getCurrentTheme();
            return CommandResult.forThemeChange(
                    MESSAGE_SUCCESS,
                    restoredTheme.getThemeCssFile(),
                    restoredTheme.getExtensionsFile()
            );
        }

        return new CommandResult(MESSAGE_SUCCESS);
    }
}
