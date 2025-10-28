package seedu.coursebook.ui;

import java.util.Optional;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.coursebook.commons.core.GuiSettings;
import seedu.coursebook.commons.core.LogsCenter;
import seedu.coursebook.logic.Logic;
import seedu.coursebook.logic.commands.CommandResult;
import seedu.coursebook.logic.commands.ConfirmDeleteCommand;
import seedu.coursebook.logic.commands.exceptions.CommandException;
import seedu.coursebook.logic.parser.exceptions.ParseException;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";
    private static final String DARK_THEME_CSS = "view/DarkTheme.css";
    private static final String EXTENSIONS_CSS = "view/Extensions.css";


    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private PersonListPanel personListPanel;
    private CourseListPanel courseListPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;
    private PersonDetailWindow personDetailWindow;
    private String currentTheme = DARK_THEME_CSS; // Track current theme
    private String currentExtensions = EXTENSIONS_CSS;


    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane personListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        // Initialize stylesheets programmatically
        ObservableList<String> stylesheets = primaryStage.getScene().getStylesheets();
        stylesheets.add(DARK_THEME_CSS);
        stylesheets.add(EXTENSIONS_CSS);

        currentTheme = DARK_THEME_CSS;
        currentExtensions = EXTENSIONS_CSS;

        helpWindow = new HelpWindow();
        helpWindow.updateTheme("DarkTheme.css", "Extensions.css");

        personDetailWindow = new PersonDetailWindow();
        personDetailWindow.updateTheme("DarkTheme.css", "Extensions.css");
    }

    /**
     * Switches the application theme to the specified CSS file.
     */
    public void switchTheme(String cssFileName, String extensionsName) {
        String newThemePath = "view/" + cssFileName;
        String newExtensionsPath = "view/" + extensionsName;

        // Update the main scene stylesheets
        ObservableList<String> stylesheets = primaryStage.getScene().getStylesheets();

        // Remove the old theme (but keep Extensions.css)
        stylesheets.remove(currentTheme);
        stylesheets.remove(currentExtensions);

        // Add the new theme at the beginning
        stylesheets.add(0, newThemePath);
        stylesheets.add(1, newExtensionsPath);

        // Update current theme tracker
        currentTheme = newThemePath;
        currentExtensions = newExtensionsPath;

        // Update help window and person detail window themes
        helpWindow.updateTheme(cssFileName, extensionsName);
        personDetailWindow.updateTheme(cssFileName, extensionsName);

        UiManager.setAlertTheme(cssFileName, extensionsName);

        logger.info("Theme switched to: " + cssFileName + "\nExtensions switched to: " + extensionsName);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        personListPanel = new PersonListPanel(logic.getFilteredPersonList());
        courseListPanel = new CourseListPanel(logic.getFilteredCourseList(), logic.getFilteredPersonList());

        // Show courses view by default
        showCoursesView();

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getCourseBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand, logic);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    /**
     * Shows the courses list panel.
     */
    private void showCoursesView() {
        personListPanelPlaceholder.getChildren().clear();
        personListPanelPlaceholder.getChildren().add(courseListPanel.getRoot());
    }

    /**
     * Shows the persons list panel.
     */
    private void showPersonsView() {
        personListPanelPlaceholder.getChildren().clear();
        personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            bringHelpWindowToFront();
        }
    }

    /**
     * Brings the help window to the front and restores it if minimized.
     */
    private void bringHelpWindowToFront() {
        Stage helpStage = helpWindow.getRoot();
        if (helpStage.isIconified()) {
            helpStage.setIconified(false);
        }
        helpStage.show();
        helpStage.toFront();
        helpWindow.focus();
    }

    /**
     * Shows the person detail window for the specified person.
     */
    public void handleShowPersonDetail(seedu.coursebook.model.person.Person person) {
        personDetailWindow.setPerson(person);
        if (!personDetailWindow.isShowing()) {
            personDetailWindow.show();
        } else {
            personDetailWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        personDetailWindow.hide();
        primaryStage.hide();
    }

    public PersonListPanel getPersonListPanel() {
        return personListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.coursebook.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());

            // Handle confirmation-required commands (e.g., delete)
            if (commandResult.requiresConfirmation()) {
                handleDeleteConfirmation(commandResult);
                return commandResult;
            }

            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            if (commandResult.isShowPersons()) {
                showPersonsView();
            }

            if (commandResult.isShowCourses()) {
                showCoursesView();
            }

            if (commandResult.isShowPersonDetail()) {
                handleShowPersonDetail(commandResult.getPersonToShow());
            }

            if (commandResult.isThemeChange()) {
                switchTheme(commandResult.getThemeCssFile(), commandResult.getExtensionsFile());
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("An error occurred while executing command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }

    /**
     * Handles delete confirmation by showing a confirmation dialog.
     * If user confirms, executes the actual deletion.
     */
    private void handleDeleteConfirmation(CommandResult commandResult) {
        boolean confirmed = showConfirmationDialog(
                "Delete Confirmation",
                "Confirm Deletion",
                commandResult.getConfirmationMessage()
        );

        if (confirmed) {
            try {
                ConfirmDeleteCommand confirmCommand = new ConfirmDeleteCommand(commandResult.getPersonsToDelete());
                CommandResult deleteResult = logic.executeCommand(confirmCommand);
                logger.info("Deletion confirmed and executed: " + deleteResult.getFeedbackToUser());
                resultDisplay.setFeedbackToUser(deleteResult.getFeedbackToUser());
            } catch (CommandException e) {
                logger.warning("Error during confirmed deletion: " + e.getMessage());
                resultDisplay.setFeedbackToUser(e.getMessage());
            }
        } else {
            logger.info("Deletion cancelled by user");
            resultDisplay.setFeedbackToUser("Delete operation cancelled.");
        }
    }

    /**
     * Shows a confirmation dialog and returns the user's choice.
     * @return true if user clicked OK, false otherwise
     */
    private boolean showConfirmationDialog(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(primaryStage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);

        javafx.scene.control.TextArea content = new javafx.scene.control.TextArea(contentText);
        content.setEditable(false);
        content.setWrapText(true);
        content.setPrefRowCount(Math.min(12, contentText.split("\n").length + 1));
        content.setMaxWidth(Double.MAX_VALUE);
        content.setMaxHeight(Double.MAX_VALUE);
        alert.getDialogPane().setContent(content);

        alert.setResizable(true);
        alert.getDialogPane().setMinHeight(javafx.scene.layout.Region.USE_PREF_SIZE);

        // Apply current theme to dialog
        alert.getDialogPane().getStylesheets().add(currentTheme);
        alert.getDialogPane().getStylesheets().add(currentExtensions);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }


}
