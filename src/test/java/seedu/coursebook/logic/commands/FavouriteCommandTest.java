package seedu.coursebook.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.coursebook.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.coursebook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.coursebook.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.coursebook.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.coursebook.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.coursebook.testutil.TypicalPersons.getTypicalCourseBook;

import org.junit.jupiter.api.Test;

import seedu.coursebook.commons.core.index.Index;
import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.logic.Messages;
import seedu.coursebook.model.CourseBook;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.ModelManager;
import seedu.coursebook.model.UserPrefs;
import seedu.coursebook.model.person.Name;
import seedu.coursebook.model.person.Person;
import seedu.coursebook.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code FavouriteCommand}.
 */
public class FavouriteCommandTest {

    private Model model = new ModelManager(getTypicalCourseBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToFavourite = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        FavouriteCommand favouriteCommand = new FavouriteCommand(INDEX_FIRST_PERSON);

        Person favouritedPerson = new PersonBuilder(personToFavourite).withFavourite(true).build();

        String expectedMessage = String.format(FavouriteCommand.MESSAGE_FAVOURITE_PERSON_SUCCESS,
                Messages.format(favouritedPerson));

        Model expectedModel = new ModelManager(new CourseBook(model.getCourseBook()), new UserPrefs());
        expectedModel.setPerson(personToFavourite, favouritedPerson);
        expectedModel.commitCourseBook();

        assertCommandSuccess(favouriteCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        FavouriteCommand favouriteCommand = new FavouriteCommand(outOfBoundIndex);

        assertCommandFailure(favouriteCommand, model, commandHistory,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToFavourite = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        FavouriteCommand favouriteCommand = new FavouriteCommand(INDEX_FIRST_PERSON);

        Person favouritedPerson = new PersonBuilder(personToFavourite).withFavourite(true).build();

        String expectedMessage = String.format(FavouriteCommand.MESSAGE_FAVOURITE_PERSON_SUCCESS,
                Messages.format(favouritedPerson));

        Model expectedModel = new ModelManager(new CourseBook(model.getCourseBook()), new UserPrefs());
        expectedModel.setPerson(personToFavourite, favouritedPerson);
        expectedModel.commitCourseBook();
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        assertCommandSuccess(favouriteCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of course book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getCourseBook().getPersonList().size());

        FavouriteCommand favouriteCommand = new FavouriteCommand(outOfBoundIndex);

        assertCommandFailure(favouriteCommand, model, commandHistory,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_alreadyFavourite_throwsCommandException() {
        // First, favourite the person
        Person personToFavourite = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person favouritedPerson = new PersonBuilder(personToFavourite).withFavourite(true).build();
        model.setPerson(personToFavourite, favouritedPerson);

        // Try to favourite again
        FavouriteCommand favouriteCommand = new FavouriteCommand(INDEX_FIRST_PERSON);

        assertCommandFailure(favouriteCommand, model, commandHistory,
                FavouriteCommand.MESSAGE_ALREADY_FAVOURITE);
    }

    @Test
    public void execute_validName_success() {
        Person personToFavourite = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Name targetName = personToFavourite.getName();
        FavouriteCommand favouriteCommand = new FavouriteCommand(targetName);

        Person favouritedPerson = new PersonBuilder(personToFavourite).withFavourite(true).build();

        String expectedMessage = String.format(FavouriteCommand.MESSAGE_FAVOURITE_PERSON_SUCCESS,
                Messages.format(favouritedPerson));

        Model expectedModel = new ModelManager(new CourseBook(model.getCourseBook()), new UserPrefs());
        expectedModel.setPerson(personToFavourite, favouritedPerson);
        expectedModel.commitCourseBook();

        assertCommandSuccess(favouriteCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidName_throwsCommandException() {
        Name nonExistentName = new Name("Nonexistent Person");
        FavouriteCommand favouriteCommand = new FavouriteCommand(nonExistentName);

        assertCommandFailure(favouriteCommand, model, commandHistory,
                FavouriteCommand.MESSAGE_NO_MATCHING_NAME);
    }

    @Test
    public void equals() {
        FavouriteCommand favouriteFirstCommand = new FavouriteCommand(INDEX_FIRST_PERSON);
        FavouriteCommand favouriteSecondCommand = new FavouriteCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(favouriteFirstCommand.equals(favouriteFirstCommand));

        // same values -> returns true
        FavouriteCommand favouriteFirstCommandCopy = new FavouriteCommand(INDEX_FIRST_PERSON);
        assertTrue(favouriteFirstCommand.equals(favouriteFirstCommandCopy));

        // different types -> returns false
        assertFalse(favouriteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(favouriteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(favouriteFirstCommand.equals(favouriteSecondCommand));
    }

    @Test
    public void equalsWithName() {
        Name firstName = new Name("Alice");
        Name secondName = new Name("Bob");
        FavouriteCommand favouriteFirstCommand = new FavouriteCommand(firstName);
        FavouriteCommand favouriteSecondCommand = new FavouriteCommand(secondName);

        // same object -> returns true
        assertTrue(favouriteFirstCommand.equals(favouriteFirstCommand));

        // same values -> returns true
        FavouriteCommand favouriteFirstCommandCopy = new FavouriteCommand(firstName);
        assertTrue(favouriteFirstCommand.equals(favouriteFirstCommandCopy));

        // different name -> returns false
        assertFalse(favouriteFirstCommand.equals(favouriteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        FavouriteCommand favouriteCommand = new FavouriteCommand(targetIndex);
        String expected = FavouriteCommand.class.getCanonicalName()
                + "{targetIndex=" + targetIndex + ", targetName=null}";
        assertEquals(expected, favouriteCommand.toString());
    }

    @Test
    public void toStringMethodWithName() {
        Name targetName = new Name("Alice");
        FavouriteCommand favouriteCommand = new FavouriteCommand(targetName);
        String expected = FavouriteCommand.class.getCanonicalName()
                + "{targetIndex=null, targetName=" + targetName + "}";
        assertEquals(expected, favouriteCommand.toString());
    }
}

