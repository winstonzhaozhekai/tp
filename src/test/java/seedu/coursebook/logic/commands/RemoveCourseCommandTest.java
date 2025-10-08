package seedu.coursebook.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import seedu.coursebook.commons.core.GuiSettings;
import seedu.coursebook.logic.commands.exceptions.CommandException;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.ReadOnlyCourseBook;
import seedu.coursebook.model.ReadOnlyUserPrefs;
import seedu.coursebook.model.course.Course;
import seedu.coursebook.model.person.Person;
import seedu.coursebook.testutil.PersonBuilder;

public class RemoveCourseCommandTest {

    @Test
    public void execute_existingCourse_success() throws Exception {
        ModelStubWithCourse modelStub = new ModelStubWithCourse();
        Course sampleCourse = new Course("CS2103T");
        modelStub.addCourse(sampleCourse);

        Set<Course> toRemove = new HashSet<>();
        toRemove.add(sampleCourse);

        RemoveCourseCommand command =
                new RemoveCourseCommand(modelStub.getFirstIndex(), toRemove);
        CommandResult result = command.execute(modelStub);

        assertTrue(result.getFeedbackToUser()
        .contains("Removed courses from Person:"));
    }

    @Test
    public void execute_courseNotFound_throwsCommandException() {
        ModelStubWithCourse modelStub = new ModelStubWithCourse();
        Course nonExisting = new Course("CS2109S");
        Set<Course> toRemove = new HashSet<>();
        toRemove.add(nonExisting);

        RemoveCourseCommand command =
                new RemoveCourseCommand(modelStub.getFirstIndex(), toRemove);

        CommandException ex = assertThrows(CommandException.class, () -> command.execute(modelStub));
        assertEquals(RemoveCourseCommand.MESSAGE_NO_MATCHING_COURSES, ex.getMessage());
    }

    // --- Model Stub ---

    private static class ModelStubWithCourse implements Model {
        final Set<Course> courses = new HashSet<>();
        final ObservableList<Person> persons = FXCollections.observableArrayList(
                new PersonBuilder().withCourses("CS2103T").build());

        public seedu.coursebook.commons.core.index.Index getFirstIndex() {
            return seedu.coursebook.commons.core.index.Index.fromOneBased(1);
        }

        // Course logic
        public boolean hasCourse(Course course) {
            return courses.contains(course);
        }

        public void addCourse(Course course) {
            courses.add(course);
        }

        public void deleteCourse(Course course) {
            courses.remove(course);
        }

        // --- Model interface boilerplate ---
        @Override public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {}
        @Override public ReadOnlyUserPrefs getUserPrefs() { return null; }
        @Override public GuiSettings getGuiSettings() { return null; }
        @Override public void setGuiSettings(GuiSettings guiSettings) {}
        @Override public Path getCourseBookFilePath() { return null; }
        @Override public void setCourseBookFilePath(Path courseBookFilePath) {}
        @Override public void setCourseBook(ReadOnlyCourseBook courseBook) {}
        @Override public ReadOnlyCourseBook getCourseBook() { return null; }
        @Override public boolean hasPerson(Person person) { return false; }
        @Override public void deletePerson(Person target) {}
        @Override public void addPerson(Person person) {}
        @Override public void setPerson(Person target, Person editedPerson) {}
        @Override public ObservableList<Person> getFilteredPersonList() { return persons; }
        @Override public void updateFilteredPersonList(Predicate<Person> predicate) {}
    }
}
