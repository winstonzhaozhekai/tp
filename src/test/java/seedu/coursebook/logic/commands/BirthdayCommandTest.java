package seedu.coursebook.logic.commands;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.Comparator;
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
import seedu.coursebook.model.person.Birthday;
import seedu.coursebook.model.person.Person;
import seedu.coursebook.testutil.PersonBuilder;

public class BirthdayCommandTest {

    private final CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validBirthday_success() throws Exception {
        ModelStubWithOnePerson modelStub = new ModelStubWithOnePerson();
        Birthday birthday = new Birthday("20-02-2007");

        BirthdayCommand command = new BirthdayCommand(Index.fromOneBased(1), birthday);
        CommandResult result = command.execute(modelStub, commandHistory);

        System.out.println("Actual message: " + result.getFeedbackToUser());
        assertTrue(result.getFeedbackToUser().toLowerCase().contains("birthday"));
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        ModelStubWithOnePerson modelStub = new ModelStubWithOnePerson();
        Birthday birthday = new Birthday("01-01-2000");

        BirthdayCommand command = new BirthdayCommand(Index.fromOneBased(5), birthday); // out of bounds
        assertThrows(CommandException.class, () -> command.execute(modelStub, commandHistory));
    }

    private static class ModelStubWithOnePerson implements Model {
        private final ObservableList<Person> persons =
                FXCollections.observableArrayList(new PersonBuilder().withName("Alice").build());

        @Override public ObservableList<Person> getFilteredPersonList() {
            return persons;
        }
        @Override public void setPerson(Person target, Person editedPerson) {
            persons.set(0, editedPerson); // simulate update
        }

        // All other methods can be stubbed as no-op or null
        @Override public boolean hasPerson(Person person) {
            return false;
        }
        @Override public void addPerson(Person person) {

        }
        @Override public void deletePerson(Person target) {

        }
        @Override public void updateFilteredPersonList(Predicate<Person> predicate) {

        }
        @Override public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {

        }
        @Override public ReadOnlyUserPrefs getUserPrefs() {
            return null;
        }
        @Override public GuiSettings getGuiSettings() {
            return null;
        }
        @Override public void setGuiSettings(GuiSettings guiSettings) {

        }
        @Override public Path getCourseBookFilePath() {
            return null;
        }
        @Override public void setCourseBookFilePath(Path courseBookFilePath) {

        }
        @Override public void setCourseBook(ReadOnlyCourseBook courseBook) {

        }
        @Override public ReadOnlyCourseBook getCourseBook() {
            return null;
        }
        @Override public boolean canUndoCourseBook() {
            return false;
        }
        @Override public boolean canRedoCourseBook() {
            return false;
        }
        @Override public void undoCourseBook() {

        }
        @Override public void redoCourseBook() {

        }
        @Override public void commitCourseBook() {

        }
        @Override public ReadOnlyProperty<Person> selectedPersonProperty() {
            return null;
        }
        @Override public Person getSelectedPerson() {
            return null;
        }
        @Override public void setSelectedPerson(Person person) {

        }
        @Override public void sortSelectedPersons(Comparator<Person> comparator) {
            throw new AssertionError("This method should not be called.");
        }
        @Override public ObservableList<Course> getFilteredCourseList() {
            return FXCollections.observableArrayList();
        }
        @Override public void updateFilteredCourseList(Predicate<Course> predicate) {

        }
        @Override
        public void setCourseColor(String courseCode, seedu.coursebook.model.course.CourseColor color) {

        }

        @Override
        public CommandResult setCurrentTheme(ThemeCommand.Theme theme) { return null; }

        @Override
        public ThemeCommand.Theme getCurrentTheme() { return null; }

        @Override
        public boolean hasThemeChangedDuringRedo() { return false; }

        @Override
        public boolean hasThemeChangedDuringUndo() { return false; }
    }
}
