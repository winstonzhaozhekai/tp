package seedu.coursebook.ui;

import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.coursebook.commons.core.LogsCenter;
import seedu.coursebook.model.person.Person;

/**
 * Controller for a person detail window
 */
public class PersonDetailWindow extends UiPart<Stage> {

    private static final Logger logger = LogsCenter.getLogger(PersonDetailWindow.class);
    private static final String FXML = "PersonDetailWindow.fxml";

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox contentBox;

    @FXML
    private Label nameLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label birthdayLabel;

    @FXML
    private Label tagsLabel;

    @FXML
    private Label coursesLabel;

    /**
     * Creates a new PersonDetailWindow.
     *
     * @param root Stage to use as the root of the PersonDetailWindow.
     */
    public PersonDetailWindow(Stage root) {
        super(FXML, root);
    }

    /**
     * Creates a new PersonDetailWindow.
     */
    public PersonDetailWindow() {
        this(new Stage());
    }

    /**
     * Sets the person to display in the detail window.
     */
    public void setPerson(Person person) {
        if (person == null) {
            return;
        }

        nameLabel.setText("Name: " + person.getName().toString());
        phoneLabel.setText("Phone: " + person.getPhone().toString());
        emailLabel.setText("Email: " + person.getEmail().toString());
        addressLabel.setText("Address: " + person.getAddress().toString());

        if (person.getBirthday() != null) {
            birthdayLabel.setText("Birthday: " + person.getBirthday().toString());
        } else {
            birthdayLabel.setText("Birthday: Not specified");
        }

        if (person.getTags().isEmpty()) {
            tagsLabel.setText("Tags: None");
        } else {
            String tagsText = person.getTags().stream()
                    .map(tag -> tag.tagName)
                    .collect(Collectors.joining(", "));
            tagsLabel.setText("Tags: " + tagsText);
        }

        if (person.getCourses().isEmpty()) {
            coursesLabel.setText("Courses: None");
        } else {
            String coursesText = person.getCourses().stream()
                    .map(course -> course.courseCode)
                    .collect(Collectors.joining(", "));
            coursesLabel.setText("Courses: " + coursesText);
        }
    }

    /**
     * Shows the person detail window.
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
        logger.fine("Showing person detail window.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the person detail window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the person detail window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the person detail window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Updates the theme of the person detail window.
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
