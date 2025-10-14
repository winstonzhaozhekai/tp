package seedu.coursebook.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.coursebook.model.course.Course;

/**
 * An UI component that displays information of a {@code Course}.
 */
public class CourseCard extends UiPart<Region> {

    private static final String FXML = "CourseListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Course course;

    @FXML
    private HBox cardPane;
    @FXML
    private Label courseCode;
    @FXML
    private Label id;
    @FXML
    private Label studentCount;

    /**
     * Creates a {@code CourseCard} with the given {@code Course} and index to display.
     */
    public CourseCard(Course course, int displayedIndex, int studentCount) {
        super(FXML);
        this.course = course;
        id.setText(displayedIndex + ". ");
        courseCode.setText(course.courseCode);
        this.studentCount.setText(studentCount + " student(s) enrolled");
    }
}
