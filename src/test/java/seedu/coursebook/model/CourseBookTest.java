package seedu.coursebook.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.coursebook.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.coursebook.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.coursebook.testutil.Assert.assertThrows;
import static seedu.coursebook.testutil.TypicalPersons.ALICE;
import static seedu.coursebook.testutil.TypicalPersons.getTypicalCourseBook;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.coursebook.model.person.Person;
import seedu.coursebook.model.person.exceptions.DuplicatePersonException;
import seedu.coursebook.testutil.PersonBuilder;

public class CourseBookTest {

    private final CourseBook courseBook = new CourseBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), courseBook.getPersonList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> courseBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyCourseBook_replacesData() {
        CourseBook newData = getTypicalCourseBook();
        courseBook.resetData(newData);
        assertEquals(newData, courseBook);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Person> newPersons = Arrays.asList(ALICE, editedAlice);
        CourseBookStub newData = new CourseBookStub(newPersons);

        assertThrows(DuplicatePersonException.class, () -> courseBook.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> courseBook.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInCourseBook_returnsFalse() {
        assertFalse(courseBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInCourseBook_returnsTrue() {
        courseBook.addPerson(ALICE);
        assertTrue(courseBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInCourseBook_returnsTrue() {
        courseBook.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(courseBook.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> courseBook.getPersonList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = CourseBook.class.getCanonicalName() + "{persons=" + courseBook.getPersonList() + "}";
        assertEquals(expected, courseBook.toString());
    }

    /**
     * A stub ReadOnlyCourseBook whose persons list can violate interface constraints.
     */
    private static class CourseBookStub implements ReadOnlyCourseBook {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();

        CourseBookStub(Collection<Person> persons) {
            this.persons.setAll(persons);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }
    }

}
