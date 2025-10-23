package seedu.coursebook.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.coursebook.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.coursebook.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.coursebook.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.coursebook.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.coursebook.testutil.TypicalPersons.getTypicalCourseBook;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.coursebook.commons.core.index.Index;
import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.ModelManager;
import seedu.coursebook.model.UserPrefs;
import seedu.coursebook.model.person.Name;
import seedu.coursebook.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalCourseBook(), new UserPrefs());

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validIndexUnfilteredList_requestsConfirmation() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        try {
            CommandResult result = deleteCommand.execute(model, commandHistory);
            assertTrue(result.requiresConfirmation());
            assertEquals(1, result.getPersonsToDelete().size());
            assertTrue(result.getPersonsToDelete().contains(personToDelete));
            assertTrue(result.getConfirmationMessage().contains("Are you sure"));
        } catch (Exception e) {
            throw new AssertionError("Execution of command should not fail.", e);
        }
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, commandHistory, DeleteCommand.MESSAGE_NO_VALID_TARGETS);
    }

    @Test
    public void execute_validIndexFilteredList_requestsConfirmation() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        try {
            CommandResult result = deleteCommand.execute(model, commandHistory);
            assertTrue(result.requiresConfirmation());
            assertEquals(1, result.getPersonsToDelete().size());
            assertTrue(result.getPersonsToDelete().contains(personToDelete));
        } catch (Exception e) {
            throw new AssertionError("Execution of command should not fail.", e);
        }
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getCourseBook().getPersonList().size());

        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, commandHistory, DeleteCommand.MESSAGE_NO_VALID_TARGETS);
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        DeleteCommand deleteSecondCommand = new DeleteCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(INDEX_FIRST_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteCommand deleteCommand = new DeleteCommand(targetIndex);
        String expected = DeleteCommand.class.getCanonicalName() + "{targetIndices=[" + targetIndex + "]}";
        assertEquals(expected, deleteCommand.toString());
    }

    @Test
    public void execute_multipleValidIndices_requestsConfirmation() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        List<Index> indices = Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        DeleteCommand deleteCommand = new DeleteCommand(indices);

        try {
            CommandResult result = deleteCommand.execute(model, commandHistory);
            assertTrue(result.requiresConfirmation());
            assertEquals(2, result.getPersonsToDelete().size());
            assertTrue(result.getPersonsToDelete().contains(firstPerson));
            assertTrue(result.getPersonsToDelete().contains(secondPerson));
            assertTrue(result.getConfirmationMessage().contains("2 contacts"));
        } catch (Exception e) {
            throw new AssertionError("Execution of command should not fail.", e);
        }
    }

    @Test
    public void execute_multipleIndicesWithInvalid_requestsConfirmationWithWarning() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Index invalidIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        List<Index> indices = Arrays.asList(INDEX_FIRST_PERSON, invalidIndex);
        DeleteCommand deleteCommand = new DeleteCommand(indices);

        CommandResult result;
        try {
            result = deleteCommand.execute(model, commandHistory);
            assertTrue(result.requiresConfirmation());
            assertEquals(1, result.getPersonsToDelete().size());
            assertTrue(result.getPersonsToDelete().contains(firstPerson));
            assertTrue(result.getConfirmationMessage().contains("Warnings"));
            assertTrue(result.getConfirmationMessage().contains("Index " + invalidIndex.getOneBased() + " is invalid"));
        } catch (Exception e) {
            throw new AssertionError("Execution of command should not fail.", e);
        }
    }

    @Test
    public void execute_allInvalidIndices_throwsCommandException() {
        Index invalidIndex1 = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Index invalidIndex2 = Index.fromOneBased(model.getFilteredPersonList().size() + 2);
        List<Index> indices = Arrays.asList(invalidIndex1, invalidIndex2);
        DeleteCommand deleteCommand = new DeleteCommand(indices);

        assertCommandFailure(deleteCommand, model, commandHistory, DeleteCommand.MESSAGE_NO_VALID_TARGETS);
    }

    @Test
    public void execute_multipleValidNames_requestsConfirmation() {
        Model freshModel = new ModelManager(getTypicalCourseBook(), new UserPrefs());
        CommandHistory freshHistory = new CommandHistory();

        // Assuming typical course book has "Alice Pauline" and "Benson Meier"
        List<Name> names = Arrays.asList(new Name("Alice Pauline"), new Name("Benson Meier"));
        DeleteCommand deleteCommand = new DeleteCommand(names, true);

        Person alice = freshModel.getFilteredPersonList().stream()
                .filter(p -> p.getName().fullName.equals("Alice Pauline"))
                .findFirst()
                .orElse(null);
        Person benson = freshModel.getFilteredPersonList().stream()
                .filter(p -> p.getName().fullName.equals("Benson Meier"))
                .findFirst()
                .orElse(null);

        if (alice != null && benson != null) {
            CommandResult result;
            try {
                result = deleteCommand.execute(freshModel, freshHistory);
                assertTrue(result.requiresConfirmation());
                assertEquals(2, result.getPersonsToDelete().size());
            } catch (Exception e) {
                throw new AssertionError("Execution of command should not fail.", e);
            }
        }
    }

    @Test
    public void execute_namesWithInvalid_requestsConfirmationWithWarning() {
        Model freshModel = new ModelManager(getTypicalCourseBook(), new UserPrefs());
        CommandHistory freshHistory = new CommandHistory();

        List<Name> names = Arrays.asList(new Name("Alice Pauline"), new Name("Nonexistent Person"));
        DeleteCommand deleteCommand = new DeleteCommand(names, true);

        CommandResult result;
        try {
            result = deleteCommand.execute(freshModel, freshHistory);
            assertTrue(result.requiresConfirmation());
            assertEquals(1, result.getPersonsToDelete().size());
            String message = result.getConfirmationMessage();
            assertTrue(message.contains("Warning"),
                    "Expected 'Warning' but got: " + message);
            assertTrue(message.contains("No contact found with name"),
                    "Expected name warning but got: " + message);
        } catch (Exception e) {
            throw new AssertionError("Execution of command should not fail.", e);
        }
    }

    @Test
    public void execute_allInvalidNames_throwsCommandException() {
        Model freshModel = new ModelManager(getTypicalCourseBook(), new UserPrefs());
        CommandHistory freshHistory = new CommandHistory();

        List<Name> names = Arrays.asList(new Name("Nonexistent Person1"), new Name("Nonexistent Person2"));
        DeleteCommand deleteCommand = new DeleteCommand(names, true);

        try {
            deleteCommand.execute(freshModel, freshHistory);
            throw new AssertionError("Expected CommandException to be thrown");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains(DeleteCommand.MESSAGE_NO_VALID_TARGETS),
                    "Expected base error but got: " + e.getMessage());
            assertTrue(e.getMessage().contains("No contact found with name"),
                    "Expected warning details but got: " + e.getMessage());
        }
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}
