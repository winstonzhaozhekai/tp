package seedu.coursebook.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
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
                    "add n/John Doe p/98765432 e/johnd@example.com a/311, Clementi Ave 2"),
            new CommandSummary("bday", "Adds a birthday to a person",
                    "bday 1 b/20-02-2007"),
            new CommandSummary("delete | rm", "Deletes person(s) by index or name (requires confirmation)",
                    "delete 1 2 3 OR delete John Doe, Jane Smith"),
            new CommandSummary("edit", "Edits a person's details",
                    "edit 1 p/91234567 e/johndoe@example.com"),
            new CommandSummary("favourite", "Marks a person as favourite by index or name",
                    "favourite 1 OR favourite John Doe"),
            new CommandSummary("unfavourite", "Removes favourite status from a person by index or name",
                    "unfavourite 1 OR unfavourite John Doe"),
            new CommandSummary("find | f", "Finds persons by fields using partial matching (OR across fields)",
                    "find n/Alice t/friend OR find alex bob"),
            new CommandSummary("list | ls", "Lists all persons",
                    "list"),
            new CommandSummary("favs", "Lists all favourite persons",
                    "favs"),
            new CommandSummary("viewperson", "Views detailed information of a person by index or name",
                    "viewperson 1 OR viewperson John Doe"),
            new CommandSummary("sortn", "Sorts contacts by name in ascending or descending order",
                    "sortn by/asc OR sortn by/desc"),
            new CommandSummary("sortb", "Sorts contacts by upcoming birthday",
                    "sortb"),

            // Course Management Commands
            new CommandSummary("addcourse", "Adds courses to a person",
                    "addcourse 1 c/CS2103T,green c/CS2040S"),
            new CommandSummary("removecourse", "Removes courses from a person",
                    "removecourse 1 c/CS2103T c/CS2040S"),
            new CommandSummary("editcourse", "Sets the color for a course code globally",
                    "editcourse c/CS2103T,yellow"),
            new CommandSummary("viewcourse", "Views all persons enrolled in a course",
                    "viewcourse c/CS2103T"),
            new CommandSummary("list | ls", "Lists persons in a course",
                    "list c/CS2103T"),
            new CommandSummary("listcourses", "Lists all unique courses in the coursebook",
                    "listcourses"),

            // General Commands
            new CommandSummary("theme", "Changes the application theme",
                    "theme blue OR theme love"),
            new CommandSummary("home", "Returns to the home page (courses view)",
                    "home"),
            new CommandSummary("summary", "Shows summary statistics with breakdown by course",
                    "summary"),
            new CommandSummary("undo", "Reverts the last change",
                    "undo"),
            new CommandSummary("redo", "Reapplies the last undone change",
                    "redo"),
            new CommandSummary("history", "Shows history of executed commands",
                    "history"),
            new CommandSummary("clear", "Removes all contacts from the coursebook",
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

    /**
     * Updates the theme of the help window.
     * @param themeCssFile the CSS file name for the theme (e.g., "DarkTheme.css")
     * @param extensionsFile the CSS file name for extensions (e.g., "Extensions.css")
     */
    public void updateTheme(String themeCssFile, String extensionsFile) {
        String themePath = "view/" + themeCssFile;
        String extensionsPath = "view/" + extensionsFile;

        ObservableList<String> stylesheets = getRoot().getScene().getStylesheets();
        stylesheets.clear();
        stylesheets.add(themePath);
        stylesheets.add(extensionsPath);
    }
}
