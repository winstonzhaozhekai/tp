package seedu.coursebook.logic.commands;

import static seedu.coursebook.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.coursebook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.coursebook.testutil.TypicalPersons.getTypicalCourseBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.logic.Messages;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.ModelManager;
import seedu.coursebook.model.UserPrefs;
import seedu.coursebook.model.person.Person;
import seedu.coursebook.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;
    private CommandHistory commandHistory = new CommandHistory();

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalCourseBook(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() {
        Person validPerson = new PersonBuilder().build();

        Model expectedModel = new ModelManager(model.getCourseBook(), new UserPrefs());
        expectedModel.addPerson(validPerson);

        assertCommandSuccess(new AddCommand(validPerson), model, commandHistory,
                String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validPerson)),
                expectedModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person personInList = model.getCourseBook().getPersonList().get(0);
        assertCommandFailure(new AddCommand(personInList), model, commandHistory,
                AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

}
