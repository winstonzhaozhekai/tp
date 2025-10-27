package seedu.coursebook.ui;

import java.util.logging.Logger;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.coursebook.commons.core.LogsCenter;
import seedu.coursebook.model.course.Course;
import seedu.coursebook.model.person.Person;

/**
 * Panel containing the list of courses.
 */
public class CourseListPanel extends UiPart<Region> {
    private static final String FXML = "CourseListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(CourseListPanel.class);

    @FXML
    private ListView<Course> courseListView;

    private ObservableList<Person> personList;

    /**
     * Creates a {@code CourseListPanel} with the given {@code ObservableList}.
     */
    public CourseListPanel(ObservableList<Course> courseList, ObservableList<Person> personList) {
        super(FXML);
        this.personList = personList;
        courseListView.setItems(courseList);
        courseListView.setCellFactory(listView -> new CourseListViewCell());

        // Add listener to personList to refresh course cards when persons change
        personList.addListener((ListChangeListener<Person>) change -> {
            logger.fine("Person list changed, refreshing course list view");
            courseListView.refresh();
        });

        // Also add listener to courseList to handle course additions/removals
        courseList.addListener((ListChangeListener<Course>) change -> {
            logger.fine("Course list changed, refreshing course list view");
            courseListView.refresh();
        });
    }

    /**
     * Counts the number of students enrolled in a specific course.
     */
    private int getStudentCount(Course course) {
        return (int) personList.stream()
                .filter(person -> person.getCourses().contains(course))
                .count();
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Course} using a {@code CourseCard}.
     */
    class CourseListViewCell extends ListCell<Course> {
        @Override
        protected void updateItem(Course course, boolean empty) {
            super.updateItem(course, empty);

            if (empty || course == null) {
                setGraphic(null);
                setText(null);
            } else {
                int studentCount = getStudentCount(course);
                setGraphic(new CourseCard(course, getIndex() + 1, studentCount).getRoot());
            }
        }
    }
}
