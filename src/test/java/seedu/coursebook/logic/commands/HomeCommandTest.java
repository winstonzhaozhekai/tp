package seedu.coursebook.logic.commands;

import static seedu.coursebook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.coursebook.logic.commands.HomeCommand.MESSAGE_SUCCESS;

import org.junit.jupiter.api.Test;

import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.ModelManager;

public class HomeCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_help_success() {
        CommandResult expectedCommandResult = new CommandResult(MESSAGE_SUCCESS, false, false, false, true);
        assertCommandSuccess(new HomeCommand(), model, commandHistory, expectedCommandResult, expectedModel);
    }
}
