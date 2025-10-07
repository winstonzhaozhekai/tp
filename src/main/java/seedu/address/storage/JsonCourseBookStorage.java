package seedu.coursebook.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.coursebook.commons.core.LogsCenter;
import seedu.coursebook.commons.exceptions.DataLoadingException;
import seedu.coursebook.commons.exceptions.IllegalValueException;
import seedu.coursebook.commons.util.FileUtil;
import seedu.coursebook.commons.util.JsonUtil;
import seedu.coursebook.model.ReadOnlyCourseBook;

/**
 * A class to access CourseBook data stored as a json file on the hard disk.
 */
public class JsonCourseBookStorage implements CourseBookStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonCourseBookStorage.class);

    private Path filePath;

    public JsonCourseBookStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getCourseBookFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyCourseBook> readCourseBook() throws DataLoadingException {
        return readCourseBook(filePath);
    }

    /**
     * Similar to {@link #readCourseBook()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataLoadingException if loading the data from storage failed.
     */
    public Optional<ReadOnlyCourseBook> readCourseBook(Path filePath) throws DataLoadingException {
        requireNonNull(filePath);

        Optional<JsonSerializableCourseBook> jsonCourseBook = JsonUtil.readJsonFile(
                filePath, JsonSerializableCourseBook.class);
        if (!jsonCourseBook.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonCourseBook.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataLoadingException(ive);
        }
    }

    @Override
    public void saveCourseBook(ReadOnlyCourseBook courseBook) throws IOException {
        saveCourseBook(courseBook, filePath);
    }

    /**
     * Similar to {@link #saveCourseBook(ReadOnlyCourseBook)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveCourseBook(ReadOnlyCourseBook courseBook, Path filePath) throws IOException {
        requireNonNull(courseBook);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableCourseBook(courseBook), filePath);
    }

}
