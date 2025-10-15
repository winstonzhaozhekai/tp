package seedu.coursebook.logic.commands;

import static seedu.coursebook.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.coursebook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.coursebook.testutil.TypicalPersons.getTypicalCourseBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.ModelManager;
import seedu.coursebook.model.UserPrefs;

public class HistoryCommandTest {

    public static final String MESSAGE_FAILURE = "No commands in history!";

    private Model model;
    private Model expectedModel;
    private CommandHistory commandHistory = new CommandHistory();

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalCourseBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getCourseBook(), new UserPrefs());
    }

    @Test
    public void execute_noHistory() {
        HistoryCommand command = new HistoryCommand();

        assertCommandSuccess(command, model, commandHistory, MESSAGE_FAILURE, expectedModel);
    }

    @Test
    public void execute_oneHistory() {
        String command0 = "delete " + VALID_NAME_AMY;
        commandHistory.add(command0);
        String expectedMessage = HistoryCommand.MESSAGE_SUCCESS + "\n1. delete " + VALID_NAME_AMY;
        assertCommandSuccess(new HistoryCommand(), model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_manyHistory() {
        String command0 = "delete " + VALID_NAME_AMY;
        String command1 = "clear";
        commandHistory.add(command0);
        commandHistory.add(command1);
        String expectedMessage = HistoryCommand.MESSAGE_SUCCESS + "\n1. clear\n2. delete " + VALID_NAME_AMY;
        assertCommandSuccess(new HistoryCommand(), model, commandHistory, expectedMessage, expectedModel);
    }

}
