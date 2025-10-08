package seedu.coursebook.model;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import seedu.coursebook.model.person.Person;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyCourseBook extends Observable {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

}
