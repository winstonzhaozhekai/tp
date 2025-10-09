package seedu.coursebook.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import javafx.beans.property.ReadOnlyProperty;
import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.coursebook.commons.core.GuiSettings;
import seedu.coursebook.commons.core.index.Index;
import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.logic.commands.exceptions.CommandException;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.ReadOnlyCourseBook;
import seedu.coursebook.model.ReadOnlyUserPrefs;
import seedu.coursebook.model.course.Course;
import seedu.coursebook.model.person.Person;
import seedu.coursebook.testutil.PersonBuilder;

public class RemoveCourseCommandTest {

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_existingCourse_success() throws Exception {
        ModelStubWithCourse modelStub = new ModelStubWithCourse();
        Course sampleCourse = new Course("CS2101");
        modelStub.addCourse(sampleCourse);

        Set<Course> toRemove = new HashSet<>();
        toRemove.add(sampleCourse);

        RemoveCourseCommand command =
                new RemoveCourseCommand(modelStub.getFirstIndex(), toRemove);
        CommandResult result = command.execute(modelStub, commandHistory);

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

        CommandException ex = assertThrows(CommandException.class, () -> command.execute(modelStub, commandHistory));
        assertEquals(RemoveCourseCommand.MESSAGE_NO_MATCHING_COURSES, ex.getMessage());
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        // Use your existing ModelStubWithCourse (contains one valid person)
        ModelStubWithCourse modelStub = new ModelStubWithCourse();

        // Prepare a valid course but pass an invalid index
        Course sampleCourse = new Course("CS2101");
        Set<Course> courses = new HashSet<>();
        courses.add(sampleCourse);

        // Index 5 is invalid since modelStub likely has only 1 person
        RemoveCourseCommand command = new RemoveCourseCommand(Index.fromOneBased(5), courses);

        assertThrows(CommandException.class, () -> command.execute(modelStub, commandHistory));
    }

    @Test
    public void execute_courseNotInPerson_throwsCommandException() {
        // Person in the model stub has some course, but not this one
        ModelStubWithCourse modelStub = new ModelStubWithCourse();

        // This course is NOT in the stub person's existing courses
        Course nonExistentCourse = new Course("CS2103T");
        Set<Course> coursesToRemove = new HashSet<>();
        coursesToRemove.add(nonExistentCourse);

        RemoveCourseCommand command = new RemoveCourseCommand(Index.fromOneBased(1), coursesToRemove);

        CommandException exception = assertThrows(CommandException.class, () -> command.execute(modelStub, commandHistory));
        System.out.println("Actual message: " + exception.getMessage());
        assertTrue(exception.getMessage().contains(RemoveCourseCommand.MESSAGE_NO_MATCHING_COURSES));
    }



    // --- Model Stub ---

    private static class ModelStubWithCourse implements Model {
        final Set<Course> courses = new HashSet<>();
        final ObservableList<Person> persons = FXCollections.observableArrayList(
                new PersonBuilder().withCourses("CS2101").build());

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
        @Override public ReadOnlyUserPrefs getUserPrefs() {
            return null; }
        @Override public GuiSettings getGuiSettings() {
            return null; }
        @Override public void setGuiSettings(GuiSettings guiSettings) {}
        @Override public Path getCourseBookFilePath() {
            return null; }
        @Override public void setCourseBookFilePath(Path courseBookFilePath) {}
        @Override public void setCourseBook(ReadOnlyCourseBook courseBook) {}
        @Override public ReadOnlyCourseBook getCourseBook() {
            return null; }
        @Override public boolean hasPerson(Person person) {
            return false; }
        @Override public void deletePerson(Person target) {}
        @Override public void addPerson(Person person) {}
        @Override public void setPerson(Person target, Person editedPerson) {}
        @Override public ObservableList<Person> getFilteredPersonList() {
            return persons; }
        @Override public void updateFilteredPersonList(Predicate<Person> predicate) {}

        @Override
        public boolean canUndoCourseBook() {
            return false;
        }

        @Override
        public boolean canRedoCourseBook() {
            return false;
        }

        @Override
        public void undoCourseBook() {}

        @Override
        public void redoCourseBook() {}

        @Override
        public void commitCourseBook() {}

        @Override
        public ReadOnlyProperty<Person> selectedPersonProperty() {
            return null;
        }

        @Override
        public Person getSelectedPerson() {
            return null;
        }

        @Override
        public void setSelectedPerson(Person person) {}

    }
}
