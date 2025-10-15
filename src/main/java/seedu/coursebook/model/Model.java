package seedu.coursebook.model;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.coursebook.commons.core.GuiSettings;
import seedu.coursebook.model.course.Course;
import seedu.coursebook.model.course.CourseColor;
import seedu.coursebook.model.person.Person;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' course book file path.
     */
    Path getCourseBookFilePath();

    /**
     * Sets the user prefs' course book file path.
     */
    void setCourseBookFilePath(Path courseBookFilePath);

    /**
     * Replaces course book data with the data in {@code CourseBook}.
     */
    void setCourseBook(ReadOnlyCourseBook courseBook);

    /** Returns the CourseBook */
    ReadOnlyCourseBook getCourseBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the course book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the course book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the course book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the course book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the course book.
     */
    void setPerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Returns true if the model has previous course book states to restore.
     */
    boolean canUndoCourseBook();

    /**
     * Returns true if the model has undone course book states to restore.
     */
    boolean canRedoCourseBook();

    /**
     * Restores the model's course book to its previous state.
     */
    void undoCourseBook();

    /**
     * Restores the model's course book to its previously undone state.
     */
    void redoCourseBook();

    /**
     * Saves the current course book state for undo/redo.
     */
    void commitCourseBook();

    /**
     * Selected person in the filtered person list.
     * null if no person is selected.
     */
    ReadOnlyProperty<Person> selectedPersonProperty();

    /**
     * Returns the selected person in the filtered person list.
     * null if no person is selected.
     */
    Person getSelectedPerson();

    /**
     * Sets the selected person in the filtered person list.
     */
    void setSelectedPerson(Person person);

    /**
     * Sorts the filtered list of persons using the specified comparator.
     */
    void sortSelectedPersons(Comparator<Person> comparator);
    /** Returns an unmodifiable view of the filtered course list */
    ObservableList<Course> getFilteredCourseList();

    /**
     * Updates the filter of the filtered course list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredCourseList(Predicate<Course> predicate);

    /**
     * Sets the color for a given course code globally across all persons.
     */
    void setCourseColor(String courseCode, CourseColor color);

}
