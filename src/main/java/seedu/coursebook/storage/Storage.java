package seedu.coursebook.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.coursebook.commons.exceptions.DataLoadingException;
import seedu.coursebook.model.ReadOnlyCourseBook;
import seedu.coursebook.model.ReadOnlyUserPrefs;
import seedu.coursebook.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends CourseBookStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataLoadingException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getCourseBookFilePath();

    @Override
    Optional<ReadOnlyCourseBook> readCourseBook() throws DataLoadingException;

    @Override
    void saveCourseBook(ReadOnlyCourseBook courseBook) throws IOException;

}
