package seedu.coursebook.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.coursebook.testutil.Assert.assertThrows;
import static seedu.coursebook.testutil.TypicalPersons.ALICE;
import static seedu.coursebook.testutil.TypicalPersons.HOON;
import static seedu.coursebook.testutil.TypicalPersons.IDA;
import static seedu.coursebook.testutil.TypicalPersons.getTypicalCourseBook;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.coursebook.commons.exceptions.DataLoadingException;
import seedu.coursebook.model.CourseBook;
import seedu.coursebook.model.ReadOnlyCourseBook;

public class JsonCourseBookStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonCourseBookStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readCourseBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readCourseBook(null));
    }

    private java.util.Optional<ReadOnlyCourseBook> readCourseBook(String filePath) throws Exception {
        return new JsonCourseBookStorage(Paths.get(filePath)).readCourseBook(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readCourseBook("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataLoadingException.class, () -> readCourseBook("notJsonFormatCourseBook.json"));
    }

    @Test
    public void readCourseBook_invalidPersonCourseBook_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readCourseBook("invalidPersonCourseBook.json"));
    }

    @Test
    public void readCourseBook_invalidAndValidPersonCourseBook_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readCourseBook("invalidAndValidPersonCourseBook.json"));
    }

    @Test
    public void readAndSaveCourseBook_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempCourseBook.json");
        CourseBook original = getTypicalCourseBook();
        JsonCourseBookStorage jsonCourseBookStorage = new JsonCourseBookStorage(filePath);

        // Save in new file and read back
        jsonCourseBookStorage.saveCourseBook(original, filePath);
        ReadOnlyCourseBook readBack = jsonCourseBookStorage.readCourseBook(filePath).get();
        assertEquals(original, new CourseBook(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addPerson(HOON);
        original.removePerson(ALICE);
        jsonCourseBookStorage.saveCourseBook(original, filePath);
        readBack = jsonCourseBookStorage.readCourseBook(filePath).get();
        assertEquals(original, new CourseBook(readBack));

        // Save and read without specifying file path
        original.addPerson(IDA);
        jsonCourseBookStorage.saveCourseBook(original); // Use instance, not static
        readBack = jsonCourseBookStorage.readCourseBook().get(); // Use instance, not static
        assertEquals(original, new CourseBook(readBack));

    }

    @Test
    public void saveCourseBook_nullCourseBook_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveCourseBook(null, "SomeFile.json"));
    }

    /**
     * Saves {@code CourseBook} at the specified {@code filePath}.
     */
    private void saveCourseBook(ReadOnlyCourseBook courseBook, String filePath) {
        try {
            new JsonCourseBookStorage(Paths.get(filePath))
                    .saveCourseBook(courseBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveCourseBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveCourseBook(new CourseBook(), null));
    }
}
