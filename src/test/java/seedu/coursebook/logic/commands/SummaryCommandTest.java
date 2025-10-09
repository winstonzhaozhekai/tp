package seedu.coursebook.logic.commands;

import static seedu.coursebook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.coursebook.testutil.TypicalPersons.getTypicalCourseBook;

import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.ModelManager;
import seedu.coursebook.model.UserPrefs;

/**
 * Contains integration tests and unit tests for SummaryCommand.
 */
public class SummaryCommandTest {

    private Model model = new ModelManager(getTypicalCourseBook(), new UserPrefs());

    @Test
    public void execute_summaryCommand_success() {
        SummaryCommand summaryCommand = new SummaryCommand();
        int expectedPersonCount = model.getCourseBook().getPersonList().size();

        // Generate the expected breakdown
        Map<String, Long> courseBreakdown = model.getCourseBook().getPersonList().stream()
                .flatMap(person -> person.getCourses().stream())
                .collect(Collectors.groupingBy(
                    course -> course.toString(),
                    Collectors.counting()
                ));

        StringBuilder breakdown = new StringBuilder();
        if (courseBreakdown.isEmpty()) {
            breakdown.append("No courses found");
        } else {
            courseBreakdown.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> breakdown.append(String.format("%s: %d enrollment(s)\n",
                        entry.getKey(), entry.getValue())));
        }

        String expectedMessage = String.format(SummaryCommand.MESSAGE_SUCCESS,
            expectedPersonCount, breakdown.toString());

        Model expectedModel = new ModelManager(model.getCourseBook(), new UserPrefs());

        assertCommandSuccess(summaryCommand, model, new CommandHistory(), expectedMessage, expectedModel);
    }

    @Test
    public void execute_emptyList_success() {
        Model emptyModel = new ModelManager();
        SummaryCommand summaryCommand = new SummaryCommand();

        String expectedMessage = String.format(SummaryCommand.MESSAGE_SUCCESS, 0, "No courses found");
        Model expectedModel = new ModelManager();

        assertCommandSuccess(summaryCommand, emptyModel, new CommandHistory(), expectedMessage, expectedModel);
    }
}
