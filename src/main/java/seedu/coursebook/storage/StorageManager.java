package seedu.coursebook.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.coursebook.commons.core.LogsCenter;
import seedu.coursebook.commons.exceptions.DataLoadingException;
import seedu.coursebook.model.ReadOnlyCourseBook;
import seedu.coursebook.model.ReadOnlyUserPrefs;
import seedu.coursebook.model.UserPrefs;

/**
 * Manages storage of CourseBook data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private CourseBookStorage courseBookStorage;
    private UserPrefsStorage userPrefsStorage;

    /**
     * Creates a {@code StorageManager} with the given {@code CourseBookStorage} and {@code UserPrefStorage}.
     */
    public StorageManager(CourseBookStorage courseBookStorage, UserPrefsStorage userPrefsStorage) {
        this.courseBookStorage = courseBookStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ CourseBook methods ==============================

    @Override
    public Path getCourseBookFilePath() {
        return courseBookStorage.getCourseBookFilePath();
    }

    @Override
    public Optional<ReadOnlyCourseBook> readCourseBook() throws DataLoadingException {
        return readCourseBook(courseBookStorage.getCourseBookFilePath());
    }

    @Override
    public Optional<ReadOnlyCourseBook> readCourseBook(Path filePath) throws DataLoadingException {
        logger.fine("Attempting to read data from file: " + filePath);
        return courseBookStorage.readCourseBook(filePath);
    }

    @Override
    public void saveCourseBook(ReadOnlyCourseBook courseBook) throws IOException {
        saveCourseBook(courseBook, courseBookStorage.getCourseBookFilePath());
    }

    @Override
    public void saveCourseBook(ReadOnlyCourseBook courseBook, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        courseBookStorage.saveCourseBook(courseBook, filePath);
    }

}
