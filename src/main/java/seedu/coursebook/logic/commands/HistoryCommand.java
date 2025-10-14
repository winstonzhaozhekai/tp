package seedu.coursebook.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;

import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.logic.commands.exceptions.CommandException;
import seedu.coursebook.model.Model;

/**
 * Reverts the {@code model}'s book to its previous state.
 */
public class HistoryCommand extends Command {

    public static final String COMMAND_WORD = "history";
    public static final String MESSAGE_SUCCESS = "History (from most latest to earliest)";
    public static final String MESSAGE_FAILURE = "No commands in history!";

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        //filter out the history/execute commands?
        ArrayList<String> previousCommands = new ArrayList<>(history.getHistory());

        if (previousCommands.isEmpty()) {
            return new CommandResult(MESSAGE_FAILURE);
        }

        Collections.reverse(previousCommands);

        StringBuilder output = new StringBuilder();
        for (int i = 0; i < previousCommands.size(); i++) {
            output.append("\n").append((i + 1)).append(". ").append(previousCommands.get(i));
        }

        return new CommandResult(MESSAGE_SUCCESS + output);
    }
}
