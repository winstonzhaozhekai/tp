package seedu.coursebook.model;

import java.util.ArrayList;
import java.util.List;

/**
 *  {@code AddressBook} that keeps track of its own history.
 */
public class VersionedCourseBook extends CourseBook {

    private final List<ReadOnlyCourseBook> courseBookStateList;
    private int currentStatePointer;

    public VersionedCourseBook(ReadOnlyCourseBook initialState) {
        super(initialState);

        courseBookStateList = new ArrayList<>();
        courseBookStateList.add(new CourseBook(initialState));
        currentStatePointer = 0;
    }

    /**
     * Saves a copy of the current {@code CourseBook} state at the end of the state list.
     * Undone states are removed from the state list.
     */
    public void commit() {
        removeStatesAfterCurrentPointer();
        courseBookStateList.add(new CourseBook(this));
        currentStatePointer++;
        indicateModified();
    }

    private void removeStatesAfterCurrentPointer() {
        courseBookStateList.subList(currentStatePointer + 1, courseBookStateList.size()).clear();
    }

    /**
     * Restores the address book to its previous state.
     */
    public void undo() {
        if (!canUndo()) {
            throw new NoUndoableStateException();
        }
        currentStatePointer--;
        resetData(courseBookStateList.get(currentStatePointer));
    }

    /**
     * Restores the address book to its previously undone state.
     */
    public void redo() {
        if (!canRedo()) {
            throw new NoRedoableStateException();
        }
        currentStatePointer++;
        resetData(courseBookStateList.get(currentStatePointer));
    }

    /**
     * Returns true if (@code undo()} has address book states to undo.
     */
    public boolean canUndo() {
        return currentStatePointer > 0;
    }

    /**
     * Returns true if {@code redo()} has address book states to redo.
     */
    public boolean canRedo() {
        return currentStatePointer < courseBookStateList.size() - 1;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }
        //instanceof handles nulls
        if (!(other instanceof VersionedCourseBook)) {
            return false;
        }

        VersionedCourseBook otherVersionedCourseBook = (VersionedCourseBook) other;

        //state check
        return super.equals(otherVersionedCourseBook)
                && courseBookStateList.equals(otherVersionedCourseBook.courseBookStateList)
                && currentStatePointer == otherVersionedCourseBook.currentStatePointer;
    }

    /**
     * Thrown when trying to {@code undo()} but can't.
     */
    public static class NoUndoableStateException extends RuntimeException {
        private NoUndoableStateException() {
            super("Current state pointer at start of addressBookState list, unable to undo.");
        }
    }

    /**
     * Thrown when trying to {@code redo()} but can't.
     */
    public static class NoRedoableStateException extends RuntimeException {
        private NoRedoableStateException() {
            super("Current state pointer at end of addressBookState list, unable to redo.");
        }
    }
}
