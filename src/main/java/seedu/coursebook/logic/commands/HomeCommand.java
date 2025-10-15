package seedu.coursebook.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.model.Model;

/**
 * Views all persons enrolled in a specific course.
 */
public class HomeCommand extends Command {

    public static final String COMMAND_WORD = "home";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Brings you to home page";

    public static final String MESSAGE_SUCCESS = "Welcome home!";

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);

        // Show courses view
        return new CommandResult(MESSAGE_SUCCESS, false, false, false, true);
    }

}
