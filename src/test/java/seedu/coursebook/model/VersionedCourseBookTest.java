package seedu.address.model;

import org.junit.jupiter.api.Test;
import seedu.address.testutil.CourseBookBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.*;

public class VersionedCourseBookTest {

    private final ReadOnlyCourseBook courseBookWithAmy = new CourseBookBuilder().withPerson(AMY).build();
    private final ReadOnlyCourseBook courseBookWithBob = new CourseBookBuilder().withPerson(BOB).build();
    private final ReadOnlyCourseBook courseBookWithCarl = new CourseBookBuilder().withPerson(CARL).build();
    private final ReadOnlyCourseBook emptyCourseBook = new CourseBookBuilder().build();

    @Test
    public void commit_singleCourseBook_noStatesRemovedCurrentStateSaved() {
        VersionedCourseBook versionedCourseBook = prepareCourseBookList(emptyCourseBook);

        versionedCourseBook.commit();
        assertCourseBookListStatus(versionedCourseBook,
                Collections.singletonList(emptyCourseBook),
                emptyCourseBook,
                Collections.emptyList());
    }

    @Test
    public void commit_multipleCourseBookPointerAtEndOfStateList_noStatesRemovedCurrentStateSaved() {
        VersionedCourseBook versionedCourseBook = prepareCourseBookList(
                emptyCourseBook, courseBookWithAmy, courseBookWithBob);

        versionedCourseBook.commit();
        assertCourseBookListStatus(versionedCourseBook,
                Arrays.asList(emptyCourseBook, courseBookWithAmy, courseBookWithBob),
                courseBookWithBob,
                Collections.emptyList());
    }

    @Test
    public void commit_multipleCourseBookPointerNotAtEndOfStateList_statesAfterPointerRemovedCurrentStateSaved() {
        VersionedCourseBook versionedCourseBook = prepareCourseBookList(
                emptyCourseBook, courseBookWithAmy, courseBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedCourseBook, 2);

        versionedCourseBook.commit();
        assertCourseBookListStatus(versionedCourseBook,
                Collections.singletonList(emptyCourseBook),
                emptyCourseBook,
                Collections.emptyList());
    }

    @Test
    public void canUndo_multipleCourseBookPointerAtEndOfStateList_returnsTrue() {
        VersionedCourseBook versionedCourseBook = prepareCourseBookList(
                emptyCourseBook, courseBookWithAmy, courseBookWithBob);

        assertTrue(versionedCourseBook.canUndo());
    }

    @Test
    public void canUndo_multipleCourseBookPointerAtStartOfStateList_returnsTrue() {
        VersionedCourseBook versionedCourseBook = prepareCourseBookList(
                emptyCourseBook, courseBookWithAmy, courseBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedCourseBook, 1);

        assertTrue(versionedCourseBook.canUndo());
    }

    @Test
    public void canUndo_singleCourseBook_returnsFalse() {
        VersionedCourseBook versionedCourseBook = prepareCourseBookList(emptyCourseBook);

        assertFalse(versionedCourseBook.canUndo());
    }

    @Test
    public void canUndo_multipleCourseBookPointerAtStartOfStateList_returnsFalse() {
        VersionedCourseBook versionedCourseBook = prepareCourseBookList(
                emptyCourseBook, courseBookWithAmy, courseBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedCourseBook, 2);

        assertFalse(versionedCourseBook.canUndo());
    }

    @Test
    public void canRedo_multipleCourseBookPointerNotAtEndOfStateList_returnsTrue() {
        VersionedCourseBook versionedCourseBook = prepareCourseBookList(
                emptyCourseBook, courseBookWithAmy, courseBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedCourseBook, 1);

        assertTrue(versionedCourseBook.canRedo());
    }

    @Test
    public void canRedo_multipleCourseBookPointerAtStartOfStateList_returnsTrue() {
        VersionedCourseBook versionedCourseBook = prepareCourseBookList(
                emptyCourseBook, courseBookWithAmy, courseBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedCourseBook, 2);

        assertTrue(versionedCourseBook.canRedo());
    }

    @Test
    public void canRedo_singleCourseBook_returnsFalse() {
        VersionedCourseBook versionedCourseBook = prepareCourseBookList(emptyCourseBook);

        assertFalse(versionedCourseBook.canRedo());
    }

    @Test
    public void canRedo_multipleCourseBookPointerAtEndOfStateList_returnsFalse() {
        VersionedCourseBook versionedCourseBook = prepareCourseBookList(
                emptyCourseBook, courseBookWithAmy, courseBookWithBob);

        assertFalse(versionedCourseBook.canRedo());
    }

    @Test
    public void undo_multipleCourseBookPointerAtEndOfStateList_success() {
        VersionedCourseBook versionedCourseBook = prepareCourseBookList(
                emptyCourseBook, courseBookWithAmy, courseBookWithBob);

        versionedCourseBook.undo();
        assertCourseBookListStatus(versionedCourseBook,
                Collections.singletonList(emptyCourseBook),
                courseBookWithAmy,
                Collections.singletonList(courseBookWithBob));
    }

    @Test
    public void undo_multipleCourseBookPointerNotAtStartOfStateList_success() {
        VersionedCourseBook versionedCourseBook = prepareCourseBookList(
                emptyCourseBook, courseBookWithAmy, courseBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedCourseBook, 1);

        versionedCourseBook.undo();
        assertCourseBookListStatus(versionedCourseBook,
                Collections.emptyList(),
                emptyCourseBook,
                Arrays.asList(courseBookWithAmy, courseBookWithBob));
    }

    @Test
    public void undo_singleCourseBook_throwsNoUndoableStateException() {
        VersionedCourseBook versionedCourseBook = prepareCourseBookList(emptyCourseBook);

        assertThrows(VersionedCourseBook.NoUndoableStateException.class, versionedCourseBook::undo);
    }

    @Test
    public void undo_multipleCourseBookPointerAtStartOfStateList_throwsNoUndoableStateException() {
        VersionedCourseBook versionedCourseBook = prepareCourseBookList(
                emptyCourseBook, courseBookWithAmy, courseBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedCourseBook, 2);

        assertThrows(VersionedCourseBook.NoUndoableStateException.class, versionedCourseBook::undo);
    }

    @Test
    public void redo_multipleCourseBookPointerNotAtEndOfStateList_success() {
        VersionedCourseBook versionedCourseBook = prepareCourseBookList(
                emptyCourseBook, courseBookWithAmy, courseBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedCourseBook, 1);

        versionedCourseBook.redo();
        assertCourseBookListStatus(versionedCourseBook,
                Arrays.asList(emptyCourseBook, courseBookWithAmy),
                courseBookWithBob,
                Collections.emptyList());
    }

    @Test
    public void redo_multipleCourseBookPointerAtStartOfStateList_success() {
        VersionedCourseBook versionedCourseBook = prepareCourseBookList(
                emptyCourseBook, courseBookWithAmy, courseBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedCourseBook, 2);

        versionedCourseBook.redo();
        assertCourseBookListStatus(versionedCourseBook,
                Collections.singletonList(emptyCourseBook),
                courseBookWithAmy,
                Collections.singletonList(courseBookWithBob));
    }

    @Test
    public void redo_singleCourseBook_throwsNoRedoableStateException() {
        VersionedCourseBook versionedCourseBook = prepareCourseBookList(emptyCourseBook);

        assertThrows(VersionedCourseBook.NoRedoableStateException.class, versionedCourseBook::redo);
    }

    @Test
    public void redo_multipleCourseBookPointerAtEndOfStateList_throwsNoRedoableStateException() {
        VersionedCourseBook versionedCourseBook = prepareCourseBookList(
                emptyCourseBook, courseBookWithAmy, courseBookWithBob);

        assertThrows(VersionedCourseBook.NoRedoableStateException.class, versionedCourseBook::redo);
    }

    @Test
    public void equals() {
        VersionedCourseBook versionedCourseBook = prepareCourseBookList(courseBookWithAmy, courseBookWithBob);

        // same values -> returns true
        VersionedCourseBook copy = prepareCourseBookList(courseBookWithAmy, courseBookWithBob);
        assertTrue(versionedCourseBook.equals(copy));

        // same object -> returns true
        assertTrue(versionedCourseBook.equals(versionedCourseBook));

        // null -> returns false
        assertFalse(versionedCourseBook.equals(null));

        // different types -> returns false
        assertFalse(versionedCourseBook.equals(1));

        // different state list -> returns false
        VersionedCourseBook differentCourseBookList = prepareCourseBookList(courseBookWithBob, courseBookWithCarl);
        assertFalse(versionedCourseBook.equals(differentCourseBookList));

        // different current pointer index -> returns false
        VersionedCourseBook differentCurrentStatePointer = prepareCourseBookList(
                courseBookWithAmy, courseBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedCourseBook, 1);
        assertFalse(versionedCourseBook.equals(differentCurrentStatePointer));
    }

    /**
     * Asserts that {@code versionedCourseBook} is currently pointing at {@code expectedCurrentState},
     * states before {@code versionedCourseBook#currentStatePointer} is equal to {@code expectedStatesBeforePointer},
     * and states after {@code versionedCourseBook#currentStatePointer} is equal to {@code expectedStatesAfterPointer}.
     */
    private void assertCourseBookListStatus(VersionedCourseBook versionedCourseBook,
            List<ReadOnlyCourseBook> expectedStatesBeforePointer,
            ReadOnlyCourseBook expectedCurrentState,
            List<ReadOnlyCourseBook> expectedStatesAfterPointer) {
        // check state currently pointing at is correct
        assertEquals(new CourseBook(versionedCourseBook), expectedCurrentState);

        // shift pointer to start of state list
        while (versionedCourseBook.canUndo()) {
            versionedCourseBook.undo();
        }

        // check states before pointer are correct
        for (ReadOnlyCourseBook expectedCourseBook : expectedStatesBeforePointer) {
            assertEquals(expectedCourseBook, new CourseBook(versionedCourseBook));
            versionedCourseBook.redo();
        }

        // check states after pointer are correct
        for (ReadOnlyCourseBook expectedCourseBook : expectedStatesAfterPointer) {
            versionedCourseBook.redo();
            assertEquals(expectedCourseBook, new CourseBook(versionedCourseBook));
        }

        // check that there are no more states after pointer
        assertFalse(versionedCourseBook.canRedo());

        // revert pointer to original position
        expectedStatesAfterPointer.forEach(unused -> versionedCourseBook.undo());
    }

    /**
     * Creates and returns a {@code VersionedCourseBook} with the {@code CourseBookStates} added into it, and the
     * {@code VersionedCourseBook#currentStatePointer} at the end of list.
     */
    private VersionedCourseBook prepareCourseBookList(ReadOnlyCourseBook... courseBookStates) {
        assertFalse(courseBookStates.length == 0);

        VersionedCourseBook versionedCourseBook = new VersionedCourseBook(courseBookStates[0]);
        for (int i = 1; i < CourseBookStates.length; i++) {
            versionedCourseBook.resetData(CourseBookStates[i]);
            versionedCourseBook.commit();
        }

        return versionedCourseBook;
    }

    /**
     * Shifts the {@code versionedCourseBook#currentStatePointer} by {@code count} to the left of its list.
     */
    private void shiftCurrentStatePointerLeftwards(VersionedCourseBook versionedCourseBook, int count) {
        for (int i = 0; i < count; i++) {
            versionedCourseBook.undo();
        }
    }
}
