package seedu.coursebook.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.coursebook.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.coursebook.commons.exceptions.IllegalValueException;
import seedu.coursebook.commons.util.JsonUtil;
import seedu.coursebook.model.CourseBook;
import seedu.coursebook.testutil.TypicalPersons;

public class JsonSerializableCourseBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableCourseBookTest");
    private static final Path TYPICAL_PERSONS_FILE = Paths.get("src", "test", "data", "JsonSerializableCourseBookTest",
            "typicalPersonsCourseBook.json");
    private static final Path INVALID_PERSON_FILE = Paths.get("src", "test", "data", "JsonSerializableCourseBookTest",
            "invalidPersonCourseBook.json");
    private static final Path DUPLICATE_PERSON_FILE = Paths.get("src", "test", "data", "JsonSerializableCourseBookTest",
            "duplicatePersonCourseBook.json");

    @Test
    public void toModelType_typicalPersonsFile_success() throws Exception {
        JsonSerializableCourseBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_PERSONS_FILE,
                JsonSerializableCourseBook.class).get();
        CourseBook courseBookFromFile = dataFromFile.toModelType();
        CourseBook typicalPersonsCourseBook = TypicalPersons.getTypicalCourseBook();
        assertEquals(courseBookFromFile, typicalPersonsCourseBook);
    }

    @Test
    public void toModelType_invalidPersonFile_throwsIllegalValueException() throws Exception {
        JsonSerializableCourseBook dataFromFile = JsonUtil.readJsonFile(INVALID_PERSON_FILE,
                JsonSerializableCourseBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicatePersons_throwsIllegalValueException() throws Exception {
        JsonSerializableCourseBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_PERSON_FILE,
                JsonSerializableCourseBook.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableCourseBook.MESSAGE_DUPLICATE_PERSON,
                dataFromFile::toModelType);
    }

}
