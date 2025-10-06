package seedu.coursebook.logic;

import java.nio.file.Path;

import javafx.collections.ObservableList;
import seedu.coursebook.commons.core.GuiSettings;
import seedu.coursebook.logic.commands.CommandResult;
import seedu.coursebook.logic.commands.exceptions.CommandException;
import seedu.coursebook.logic.parser.exceptions.ParseException;
import seedu.coursebook.model.ReadOnlyCourseBook;
import seedu.coursebook.model.person.Person;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /**
     * Returns the CourseBook.
     *
     * @see seedu.coursebook.model.Model#getCourseBook()
     */
    ReadOnlyCourseBook getCourseBook();

    /** Returns an unmodifiable view of the filtered list of persons */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Returns the user prefs' address book file path.
     */
    Path getCourseBookFilePath();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Set the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);
}
