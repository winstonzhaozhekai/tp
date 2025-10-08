package seedu.coursebook.model;

import static java.util.Objects.requireNonNull;
import static seedu.coursebook.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.model.VersionedCourseBook;
import seedu.coursebook.commons.core.GuiSettings;
import seedu.coursebook.commons.core.LogsCenter;
import seedu.coursebook.model.person.Person;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final VersionedCourseBook versionedCourseBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;

    /**
     * Initializes a ModelManager with the given CourseBook and userPrefs.
     */
    public ModelManager(ReadOnlyCourseBook courseBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(courseBook, userPrefs);

        logger.fine("Initializing with address book: " + courseBook + " and user prefs " + userPrefs);

        versionedCourseBook = new VersionedCourseBook(courseBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(versionedCourseBook.getPersonList());
        filteredPersons.addListener(this::ensureSelectedPersonIsValid);
    }

    public ModelManager() {
        this(new CourseBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getCourseBookFilePath() {
        return userPrefs.getCourseBookFilePath();
    }

    @Override
    public void setCourseBookFilePath(Path courseBookFilePath) {
        requireNonNull(courseBookFilePath);
        userPrefs.setCourseBookFilePath(courseBookFilePath);
    }

    //=========== CourseBook ================================================================================

    @Override
    public void setCourseBook(ReadOnlyCourseBook courseBook) {
        versionedCourseBook.resetData(courseBook);
    }

    @Override
    public ReadOnlyCourseBook getCourseBook() {
        return versionedCourseBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return versionedCourseBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        versionedCourseBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        versionedCourseBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        versionedCourseBook.setPerson(target, editedPerson);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedCourseBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    //============ Undo/Redo =====================================================================================

    @Override
    public boolean canUndoCourseBook() { return versionedCourseBook.canUndo(); }

    @Override
    public boolean canRedoCourseBook() { return versionedCourseBook.canRedo(); }

    @Override
    public void undoCourseBook() { versionedCourseBook.undo(); }

    @Override
    public void redoCourseBook() { versionedCourseBook.redo(); }

    @Override
    public void commitCourseBook() { versionedCourseBook.commit(); }

    //=========== Selected person ===========================================================================

    @Override
    public ReadOnlyProperty<Person> selectedPersonProperty() {
        return selectedPerson;
    }

    @Override
    public Person getSelectedPerson() {
        return selectedPerson.getValue();
    }

    @Override
    public void setSelectedPerson(Person person) {
        if (person != null && !filteredPersons.contains(person)) {
            throw new PersonNotFoundException();
        }
        selectedPerson.setValue(person);
    }

    /**
     * Ensures {@code selectedPerson} is a valid person in {@code filteredPersons}.
     */
    private void ensureSelectedPersonIsValid(ListChangeListener.Change<? extends Person> change) {
        while (change.next()) {
            if (selectedPerson.getValue() == null) {
                // null is always a valid selected person, so we do not need to check that it is valid anymore.
                return;
            }

            boolean wasSelectedPersonReplaced = change.wasReplaced() && change.getAddedSize() == change.getRemovedSize()
                    && change.getRemoved().contains(selectedPerson.getValue());
            if (wasSelectedPersonReplaced) {
                // Update selectedPerson to its new value.
                int index = change.getRemoved().indexOf(selectedPerson.getValue());
                selectedPerson.setValue(change.getAddedSubList().get(index));
                continue;
            }

            boolean wasSelectedPersonRemoved = change.getRemoved().stream()
                    .anyMatch(removedPerson -> selectedPerson.getValue().isSamePerson(removedPerson));
            if (wasSelectedPersonRemoved) {
                // Select the person that came before it in the list,
                // or clear the selection if there is no such person.
                selectedPerson.setValue(change.getFrom() > 0 ? change.getList().get(change.getFrom() - 1) : null);
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return versionedCourseBook.equals(otherModelManager.versionedCourseBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons);
    }

}
