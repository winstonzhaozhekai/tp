package seedu.coursebook.logic;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.logging.Logger;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.coursebook.commons.core.GuiSettings;
import seedu.coursebook.commons.core.LogsCenter;
import seedu.coursebook.logic.commands.Command;
import seedu.coursebook.logic.commands.CommandResult;
import seedu.coursebook.logic.commands.exceptions.CommandException;
import seedu.coursebook.logic.parser.CourseBookParser;
import seedu.coursebook.logic.parser.exceptions.ParseException;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.ReadOnlyCourseBook;
import seedu.coursebook.model.course.Course;
import seedu.coursebook.model.person.Person;
import seedu.coursebook.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";

    public static final String FILE_OPS_ERROR_MESSAGE = "Could not save data to file: ";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final CommandHistory history;
    private final CourseBookParser courseBookParser;
    private boolean courseBookModified;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        history = new CommandHistory();
        courseBookParser = new CourseBookParser();

        //Set addressBookModified to true whenever the models' address book is modified
        model.getCourseBook().addListener(observable -> courseBookModified = true);
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        courseBookModified = false;

        CommandResult commandResult;
        try {
            Command command = courseBookParser.parseCommand(commandText);
            commandResult = command.execute(model, history);
        } finally {
            history.add(commandText);
        }

        if (courseBookModified) {
            logger.info("Course book modified, saving to file.");
            try {
                storage.saveCourseBook(model.getCourseBook());
            } catch (IOException ioe) {
                throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
            }
        }

        return commandResult;
    }

    @Override
    public ReadOnlyCourseBook getCourseBook() {
        return model.getCourseBook();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public ObservableList<Course> getFilteredCourseList() {
        return model.getFilteredCourseList();
    }

    @Override
    public ObservableList<String> getHistory() {
        return history.getHistory();
    }

    @Override
    public Path getCourseBookFilePath() {
        return model.getCourseBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }

    @Override
    public ReadOnlyProperty<Person> selectedPersonProperty() {
        return model.selectedPersonProperty();
    }

    @Override
    public void setSelectedPerson(Person person) {
        model.setSelectedPerson(person);
    }

    @Override
    public void sortSelectedPersons(Comparator<Person> comparator) {
        model.sortSelectedPersons(comparator);
    }
}
