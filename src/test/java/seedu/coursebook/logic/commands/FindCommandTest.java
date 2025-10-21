package seedu.coursebook.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.coursebook.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.coursebook.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.ModelManager;
import seedu.coursebook.model.UserPrefs;
import seedu.coursebook.model.person.PersonContainsKeywordsPredicate;
import seedu.coursebook.testutil.TypicalPersons;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(TypicalPersons.getTypicalCourseBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(TypicalPersons.getTypicalCourseBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void equals() {
        PersonContainsKeywordsPredicate firstPredicate = new PersonContainsKeywordsPredicate(
                Collections.singletonList("first"), Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());
        PersonContainsKeywordsPredicate secondPredicate = new PersonContainsKeywordsPredicate(
                Collections.singletonList("second"), Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        PersonContainsKeywordsPredicate predicate = preparePredicate(" ");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);

        CommandResult expectedCommandResult = new CommandResult(
                expectedMessage, false, false, true, false
        );

        assertCommandSuccess(command, model, commandHistory, expectedCommandResult, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        PersonContainsKeywordsPredicate predicate = preparePredicate("Kurz Elle Kunz");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);

        CommandResult expectedCommandResult = new CommandResult(
                expectedMessage, false, false, true, false
        );

        assertCommandSuccess(command, model, commandHistory, expectedCommandResult, expectedModel);
        assertEquals(Arrays.asList(TypicalPersons.CARL, TypicalPersons.ELLE, TypicalPersons.FIONA),
                model.getFilteredPersonList());
    }

    @Test
    public void execute_partialNameMatch_personsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        // "Ali" should partially match "Alice Pauline"
        PersonContainsKeywordsPredicate predicate = preparePredicate("Ali");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);

        CommandResult expectedCommandResult = new CommandResult(
                expectedMessage, false, false, true, false
        );

        assertCommandSuccess(command, model, commandHistory, expectedCommandResult, expectedModel);
        assertEquals(Arrays.asList(TypicalPersons.ALICE), model.getFilteredPersonList());
    }

    @Test
    public void execute_partialTagMatch_personsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        // "fri" should partially match "friends" tag (Alice, Benson, Daniel have "friends" tag)
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Arrays.asList("fri"));
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);

        CommandResult expectedCommandResult = new CommandResult(
                expectedMessage, false, false, true, false
        );

        assertCommandSuccess(command, model, commandHistory, expectedCommandResult, expectedModel);
        assertEquals(Arrays.asList(TypicalPersons.ALICE, TypicalPersons.BENSON, TypicalPersons.DANIEL),
                model.getFilteredPersonList());
    }

    @Test
    public void execute_partialAddressMatch_personsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        // "tokyo" should partially match "little tokyo" (Fiona's address)
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                Arrays.asList("tokyo"), Collections.emptyList());
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);

        CommandResult expectedCommandResult = new CommandResult(
                expectedMessage, false, false, true, false
        );

        assertCommandSuccess(command, model, commandHistory, expectedCommandResult, expectedModel);
        assertEquals(Arrays.asList(TypicalPersons.FIONA), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(
                Arrays.asList("keyword"), Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());
        FindCommand findCommand = new FindCommand(predicate);
        String expected = FindCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code PersonContainsKeywordsPredicate} with name keywords.
     */
    private PersonContainsKeywordsPredicate preparePredicate(String userInput) {
        List<String> names = Arrays.asList(userInput.split("\\s+"));
        return new PersonContainsKeywordsPredicate(names, Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());
    }
}
