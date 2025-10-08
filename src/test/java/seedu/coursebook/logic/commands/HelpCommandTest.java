package seedu.coursebook.logic.commands;

import static seedu.coursebook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.coursebook.logic.commands.HelpCommand.SHOWING_HELP_MESSAGE;

import org.junit.jupiter.api.Test;

import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.ModelManager;

public class HelpCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_help_success() {
        CommandResult expectedCommandResult = new CommandResult(SHOWING_HELP_MESSAGE, true, false);
        assertCommandSuccess(new HelpCommand(), model, commandHistory, expectedCommandResult, expectedModel);
    }
}
