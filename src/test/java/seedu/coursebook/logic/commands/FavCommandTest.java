package seedu.coursebook.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.coursebook.testutil.TypicalPersons.getTypicalCourseBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.ModelManager;
import seedu.coursebook.model.UserPrefs;
import seedu.coursebook.model.person.Person;
import seedu.coursebook.testutil.PersonBuilder;

public class FavCommandTest {
    private Model model;
    private Model expectedModel;
    private CommandHistory commandHistory = new CommandHistory();

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalCourseBook(), new UserPrefs());
        expectedModel = new ModelManager(getTypicalCourseBook(), new UserPrefs());
    }

    @Test
    public void execute_someFavourites_found() {
        Person personOne = model.getFilteredPersonList().get(0);
        Person personTwo = model.getFilteredPersonList().get(1);

        Person favOne = new PersonBuilder(personOne).withFavourite(true).build();
        Person favTwo = new PersonBuilder(personTwo).withFavourite(true).build();

        model.setPerson(personOne, favOne);
        model.setPerson(personTwo, favTwo);
        expectedModel.setPerson(personOne, favOne);
        expectedModel.setPerson(personTwo, favTwo);

        expectedModel.updateFilteredPersonList(person -> person.isFavourite());

        FavCommand favCommand = new FavCommand();
        CommandResult result = favCommand.execute(model, commandHistory);

        assertEquals(FavCommand.MESSAGE_LISTED_FAVOURITES, result.getFeedbackToUser().trim());
        assertEquals(expectedModel.getFilteredPersonList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_noFavourites_found() {
        model.getFilteredPersonList().forEach(person -> {
            Person unfav = new PersonBuilder(person).withFavourite(false).build();
            model.setPerson(person, unfav);
            expectedModel.setPerson(person, unfav);
        });

        expectedModel.updateFilteredPersonList(person -> person.isFavourite());

        FavCommand favCommand = new FavCommand();
        CommandResult result = favCommand.execute(model, commandHistory);

        assertEquals(FavCommand.MESSAGE_NO_FAVOURITES, result.getFeedbackToUser());
        assertEquals(expectedModel.getFilteredPersonList(), model.getFilteredPersonList());
    }
}
