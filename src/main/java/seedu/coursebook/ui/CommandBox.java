package seedu.coursebook.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import seedu.coursebook.logic.Logic;
import seedu.coursebook.logic.commands.CommandResult;
import seedu.coursebook.logic.commands.exceptions.CommandException;
import seedu.coursebook.logic.parser.exceptions.ParseException;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";

    private final CommandExecutor commandExecutor;
    private final Logic logic;

    // History navigation state
    private int historyIndex = -1; // -1 means not currently navigating history
    private String currentEditText = ""; // Saves user's current text before navigating history

    @FXML
    private TextField commandTextField;

    /**
     * Creates a {@code CommandBox} with the given {@code CommandExecutor} and {@code Logic}.
     */
    public CommandBox(CommandExecutor commandExecutor, Logic logic) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        this.logic = logic;
        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextField.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());
        setupKeyPressHandler();
    }

    /**
     * Sets up the key press handler for command history navigation.
     */
    private void setupKeyPressHandler() {
        commandTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP) {
                showPreviousCommand();
                event.consume();
            } else if (event.getCode() == KeyCode.DOWN) {
                showNextCommand();
                event.consume();
            }
        });
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandEntered() {
        String commandText = commandTextField.getText();
        if (commandText.equals("")) {
            return;
        }

        try {
            commandExecutor.execute(commandText);
            commandTextField.setText("");
            resetHistoryNavigation(); // Reset history navigation after command execution
        } catch (CommandException | ParseException e) {
            setStyleToIndicateCommandFailure();
        }
    }

    /**
     * Navigates to the previous command in history (older command).
     */
    private void showPreviousCommand() {
        ObservableList<String> history = logic.getHistory();
        if (history.isEmpty()) {
            return;
        }

        // If not currently in history, save current text and start from most recent command
        if (historyIndex == -1) {
            currentEditText = commandTextField.getText();
            historyIndex = history.size() - 1;
        } else if (historyIndex > 0) {
            // Move to older command
            historyIndex--;
        } else {
            // Already at oldest command, do nothing
            return;
        }

        commandTextField.setText(history.get(historyIndex));
        commandTextField.positionCaret(commandTextField.getText().length());
    }

    /**
     * Navigates to the next command in history (newer command).
     */
    private void showNextCommand() {
        ObservableList<String> history = logic.getHistory();

        // If not currently navigating history, do nothing
        if (historyIndex == -1) {
            return;
        }

        if (historyIndex < history.size() - 1) {
            // Move to newer command
            historyIndex++;
            commandTextField.setText(history.get(historyIndex));
            commandTextField.positionCaret(commandTextField.getText().length());
        } else {
            // Reached most recent command, restore user's original text
            historyIndex = -1;
            commandTextField.setText(currentEditText);
            commandTextField.positionCaret(commandTextField.getText().length());
        }
    }

    /**
     * Resets the history navigation state.
     */
    private void resetHistoryNavigation() {
        historyIndex = -1;
        currentEditText = "";
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    /**
     * Represents a function that can execute commands.
     */
    @FunctionalInterface
    public interface CommandExecutor {
        /**
         * Executes the command and returns the result.
         *
         * @see seedu.coursebook.logic.Logic#execute(String)
         */
        CommandResult execute(String commandText) throws CommandException, ParseException;
    }

}
