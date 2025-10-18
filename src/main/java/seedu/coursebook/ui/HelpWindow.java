package seedu.coursebook.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import seedu.coursebook.commons.core.LogsCenter;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://ay2526s1-cs2103t-f10-2.github.io/tp/UserGuide.html";
    public static final String HELP_MESSAGE = "Full User Guide: " + USERGUIDE_URL;

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Button copyButton;

    @FXML
    private Label helpMessage;

    @FXML
    private TableView<CommandSummary> commandTable;

    @FXML
    private TableColumn<CommandSummary, String> commandColumn;

    @FXML
    private TableColumn<CommandSummary, String> descriptionColumn;

    @FXML
    private TableColumn<CommandSummary, String> exampleColumn;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        helpMessage.setText(HELP_MESSAGE);
        initializeCommandTable();
        populateCommandTable();
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Initializes the command table columns.
     */
    private void initializeCommandTable() {
        commandColumn.setCellValueFactory(new PropertyValueFactory<>("command"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        exampleColumn.setCellValueFactory(new PropertyValueFactory<>("example"));

        // Set column widths
        commandColumn.setMinWidth(150);
        descriptionColumn.setMinWidth(300);
        exampleColumn.setMinWidth(350);
    }

    /**
     * Populates the command table with all available commands.
     */
    private void populateCommandTable() {
        commandTable.getItems().addAll(
            // Person Management Commands
            new CommandSummary("add", "Adds a person to the coursebook",
                    "add n/NAME p/PHONE e/EMAIL a/ADDRESS [t/TAG]..."),
            new CommandSummary("delete", "Deletes a person by index",
                    "delete INDEX"),
            new CommandSummary("edit", "Edits a person's details",
                    "edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]..."),
            new CommandSummary("find", "Finds persons by fields (OR across fields)",
                    "find [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]..."),
            new CommandSummary("list", "Lists all persons",
                    "list"),

            // Course Management Commands
            new CommandSummary("addcourse", "Adds a course to a person",
                    "addcourse INDEX c/COURSE_CODE[,COLOR]"),
            new CommandSummary("removecourse", "Removes a course from a person",
                    "removecourse INDEX c/COURSE_CODE"),
            new CommandSummary("editcourse", "Sets the color for a course code globally",
                    "editcourse c/COURSE_CODE,COLOR"),
            new CommandSummary("viewcourse", "Views details of a course",
                    "viewcourse COURSE_CODE"),
            new CommandSummary("listbycourse", "Lists persons in a course",
                    "listbycourse COURSE_CODE"),
            new CommandSummary("listcourses", "Lists all available courses",
                    "listcourses"),

            // General Commands
            new CommandSummary("theme", "Change theme to dark/blue/love/tree",
                    "theme blue"),
            new CommandSummary("home", "Go to home page",
                    "home"),
            new CommandSummary("summary", "Shows summary statistics",
                    "summary"),
            new CommandSummary("undo", "Undoes the last command",
                    "undo"),
            new CommandSummary("redo", "Redo the last undone command",
                    "redo"),
            new CommandSummary("history", "See list of commands executed",
                    "history"),
            new CommandSummary("clear", "Clears all entries",
                    "clear"),
            new CommandSummary("help", "Shows this help window",
                    "help"),
            new CommandSummary("exit", "Exits the application",
                    "exit")
        );
    }

    /**
     * Represents a command summary for display in the help window.
     */
    public static class CommandSummary {
        private final String command;
        private final String description;
        private final String example;

        /**
         * Creates a command summary.
         */
        public CommandSummary(String command, String description, String example) {
            this.command = command;
            this.description = description;
            this.example = example;
        }

        public String getCommand() {
            return command;
        }

        public String getDescription() {
            return description;
        }

        public String getExample() {
            return example;
        }
    }

    /**
     * Shows the help window.
     * @throws IllegalStateException
     *     <ul>
     *         <li>
     *             if this method is called on a thread other than the JavaFX Application Thread.
     *         </li>
     *         <li>
     *             if this method is called during animation or layout processing.
     *         </li>
     *         <li>
     *             if this method is called on the primary stage.
     *         </li>
     *         <li>
     *             if {@code dialogStage} is already showing.
     *         </li>
     *     </ul>
     */
    public void show() {
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Copies the URL to the user guide to the clipboard.
     */
    @FXML
    private void copyUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent url = new ClipboardContent();
        url.putString(USERGUIDE_URL);
        clipboard.setContent(url);
    }
}
