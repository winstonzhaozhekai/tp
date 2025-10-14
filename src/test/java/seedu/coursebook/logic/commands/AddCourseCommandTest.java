package seedu.coursebook.logic.commands;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.beans.property.ReadOnlyProperty;
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


public class AddCourseCommandTest {

    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validCourse_success() throws Exception {
        ModelStubWithOnePerson modelStub = new ModelStubWithOnePerson();
        Course sampleCourse = new Course("CS2103T");
        Set<Course> courseSet = new HashSet<>();
        courseSet.add(sampleCourse);

        AddCourseCommand command = new AddCourseCommand(Index.fromOneBased(1), courseSet);
        CommandResult result = command.execute(modelStub, commandHistory);

        System.out.println("Actual message: " + result.getFeedbackToUser());
        assertTrue(result.getFeedbackToUser().toLowerCase().contains("added"));
    }

    @Test
    public void execute_duplicateCourse_throwsCommandException() {
        ModelStub modelStub = new ModelStub() {
            @Override
            public boolean hasPerson(Person person) {
                return true; // Simulate duplicate person
            }
        };

        Course sampleCourse = new Course("CS2103T");
        Set<Course> duplicateCourseSet = new HashSet<>();
        duplicateCourseSet.add(sampleCourse);

        AddCourseCommand command = new AddCourseCommand(Index.fromOneBased(1), duplicateCourseSet);

        assertThrows(CommandException.class, () -> command.execute(modelStub, commandHistory));
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        ModelStubWithOnePerson modelStub = new ModelStubWithOnePerson();
        Course sampleCourse = new Course("CS2103T");
        Set<Course> courses = new HashSet<>();
        courses.add(sampleCourse);

        AddCourseCommand command = new AddCourseCommand(Index.fromOneBased(5), courses); // invalid
        assertThrows(CommandException.class, () -> command.execute(modelStub, commandHistory));
    }


    // =========================================================
    // =============== SUPPORTING STUB CLASSES =================
    // =========================================================

    private static class ModelStub implements Model {
        @Override public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {

        }
        @Override public ReadOnlyUserPrefs getUserPrefs() {
            return null; }
        @Override public GuiSettings getGuiSettings() {
            return null; }
        @Override public void setGuiSettings(GuiSettings guiSettings) {

        }
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
            return FXCollections.observableArrayList(); }
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

        @Override
        public ObservableList<seedu.coursebook.model.course.Course> getFilteredCourseList() {
            return FXCollections.observableArrayList();
        }

        @Override
        public void updateFilteredCourseList(Predicate<seedu.coursebook.model.course.Course> predicate) {}
    }


    private static class ModelStubWithOnePerson implements Model {
        private final ObservableList<Person> persons =
                FXCollections.observableArrayList(new PersonBuilder().withName("Alice").build());

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return persons;
        }

        @Override public boolean hasPerson(Person person) {
            return false; }
        @Override public void addPerson(Person person) {}
        @Override public void setPerson(Person target, Person editedPerson) {}
        @Override public void deletePerson(Person target) {}
        @Override public void updateFilteredPersonList(Predicate<Person> predicate) {}
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

        @Override
        public ObservableList<seedu.coursebook.model.course.Course> getFilteredCourseList() {
            return FXCollections.observableArrayList();
        }

        @Override
        public void updateFilteredCourseList(Predicate<seedu.coursebook.model.course.Course> predicate) {}
    }
}
