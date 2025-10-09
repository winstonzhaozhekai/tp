package seedu.coursebook.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.model.CourseBook;
import seedu.coursebook.model.Model;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";


    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.setCourseBook(new CourseBook());
        model.commitCourseBook();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
