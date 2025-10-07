package seedu.coursebook.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.coursebook.commons.exceptions.DataLoadingException;
import seedu.coursebook.model.ReadOnlyCourseBook;

/**
 * Represents a storage for {@link seedu.coursebook.model.CourseBook}.
 */
public interface CourseBookStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getCourseBookFilePath();

    /**
     * Returns CourseBook data as a {@link ReadOnlyCourseBook}.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataLoadingException if loading the data from storage failed.
     */
    Optional<ReadOnlyCourseBook> readCourseBook() throws DataLoadingException;

    /**
     * @see #getCourseBookFilePath()
     */
    Optional<ReadOnlyCourseBook> readCourseBook(Path filePath) throws DataLoadingException;

    /**
     * Saves the given {@link ReadOnlyCourseBook} to the storage.
     * @param courseBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveCourseBook(ReadOnlyCourseBook courseBook) throws IOException;

    /**
     * @see #saveCourseBook(ReadOnlyCourseBook)
     */
    void saveCourseBook(ReadOnlyCourseBook courseBook, Path filePath) throws IOException;

}
