package seedu.coursebook.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.coursebook.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.coursebook.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.coursebook.testutil.TypicalPersons.getTypicalCourseBook;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.ModelManager;
import seedu.coursebook.model.UserPrefs;
import seedu.coursebook.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code ConfirmDeleteCommand}.
 */
public class ConfirmDeleteCommandTest {

    private Model model = new ModelManager(getTypicalCourseBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validSinglePerson_success() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        List<Person> personsToDelete = Arrays.asList(personToDelete);
        ConfirmDeleteCommand confirmDeleteCommand = new ConfirmDeleteCommand(personsToDelete);

        try {
            CommandResult result = confirmDeleteCommand.execute(model, commandHistory);
            assertTrue(result.getFeedbackToUser().contains("Deleted Person"));
            assertTrue(result.getFeedbackToUser().contains(personToDelete.getName().toString()));
            assertFalse(model.getFilteredPersonList().contains(personToDelete));
        } catch (Exception e) {
            throw new AssertionError("Execution of command should not fail.", e);
        }
    }

    @Test
    public void execute_validMultiplePersons_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        List<Person> personsToDelete = Arrays.asList(firstPerson, secondPerson);
        ConfirmDeleteCommand confirmDeleteCommand = new ConfirmDeleteCommand(personsToDelete);

        try {
            CommandResult result = confirmDeleteCommand.execute(model, commandHistory);
            assertTrue(result.getFeedbackToUser().contains("Deleted 2 person(s)"));
            assertFalse(model.getFilteredPersonList().contains(firstPerson));
            assertFalse(model.getFilteredPersonList().contains(secondPerson));
        } catch (Exception e) {
            throw new AssertionError("Execution of command should not fail.", e);
        }
    }

    @Test
    public void equals() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());

        ConfirmDeleteCommand deleteFirstCommand = new ConfirmDeleteCommand(Arrays.asList(firstPerson));
        ConfirmDeleteCommand deleteSecondCommand = new ConfirmDeleteCommand(Arrays.asList(secondPerson));

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        ConfirmDeleteCommand deleteFirstCommandCopy = new ConfirmDeleteCommand(Arrays.asList(firstPerson));
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }
}

