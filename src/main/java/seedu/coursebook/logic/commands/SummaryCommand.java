package seedu.coursebook.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Map;
import java.util.stream.Collectors;

import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.model.Model;

/**
 * Shows a summary of all persons in the course book with breakdown by course.
 */
public class SummaryCommand extends Command {

    public static final String COMMAND_WORD = "summary";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Show number of contacts and breakdown by course.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Summary: %d person(s) found\n\nBreakdown by course:\n%s";

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);

        // Get the person count from the course book
        int personCount = model.getCourseBook().getPersonList().size();

        // Group people by course and count (handling multiple courses per person)
        Map<String, Long> courseBreakdown = model.getCourseBook().getPersonList().stream()
                .flatMap(person -> person.getCourses().stream())
                .collect(Collectors.groupingBy(
                    course -> course.toString(),
                    Collectors.counting()
                ));

        // Build the breakdown string
        StringBuilder breakdown = new StringBuilder();
        if (courseBreakdown.isEmpty()) {
            breakdown.append("No courses found");
        } else {
            courseBreakdown.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> breakdown.append(String.format("%s: %d enrollment(s)\n",
                        entry.getKey(), entry.getValue())));
        }

        String message = String.format(MESSAGE_SUCCESS, personCount, breakdown.toString());

        return new CommandResult(message);
    }
}

