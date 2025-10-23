package seedu.coursebook.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.coursebook.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/CourseBook-level4/issues/336">The issue on CourseBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label birthday;
    @FXML
    private Label favouriteIcon;
    @FXML
    private FlowPane tags;
    @FXML
    private FlowPane courses;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        birthday.setText(person.getBirthday() != null ? person.getBirthday().value : "No birthday");

        // Set favourite icon
        if (person.isFavourite()) {
            favouriteIcon.setText("★");
            favouriteIcon.setVisible(true);
            favouriteIcon.setManaged(true);
        } else {
            favouriteIcon.setVisible(false);
            favouriteIcon.setManaged(false);
        }

        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
        person.getCourses().stream()
                .sorted(Comparator.comparing(course -> course.courseCode))
                .forEach(course -> {
                    Label courseLabel = new Label(course.courseCode);
                    courseLabel.getStyleClass().add("course-label");
                    String hex = course.color == null ? "#388e3c" : course.color.getHex();
                    courseLabel.setStyle("-fx-background-color: " + hex + "; -fx-text-fill: white;");
                    courses.getChildren().add(courseLabel);
                });
    }

    @FXML
    private void copyUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent url = new ClipboardContent();
        url.putString(person.getPhone().value);
        clipboard.setContent(url);
    }
}
