package seedu.coursebook.logic.parser;

import static seedu.coursebook.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.coursebook.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.coursebook.commons.core.index.Index;
import seedu.coursebook.logic.commands.RemoveCourseCommand;
import seedu.coursebook.model.course.Course;

public class RemoveCourseCommandParserTest {
    private final RemoveCourseCommandParser parser = new RemoveCourseCommandParser();

    @Test
    public void parse_validArgs_success() {
        String userInput = "1 c/CS2103T";
        Set<Course> expectedCourses = Collections.singleton(new Course("CS2103T"));
        RemoveCourseCommand expectedCommand = new RemoveCourseCommand(Index.fromOneBased(1), expectedCourses);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingArgs_failure() {
        String userInput = "1"; // no course code
        assertParseFailure(parser, userInput,
                String.format("Invalid command format! \n%s", RemoveCourseCommand.MESSAGE_USAGE));
    }
}
