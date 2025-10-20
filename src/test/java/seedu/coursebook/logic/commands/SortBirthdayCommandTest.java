package seedu.coursebook.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.model.CourseBook;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.ModelManager;
import seedu.coursebook.model.UserPrefs;
import seedu.coursebook.model.person.Person;
import seedu.coursebook.testutil.PersonBuilder;

public class SortBirthdayCommandTest {

    private Model model;
    private CommandHistory history;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new CourseBook(), new UserPrefs());
        history = new CommandHistory();

        model.addPerson(new PersonBuilder().withName("Alice").withBirthday("02-06-2025").build());
        model.addPerson(new PersonBuilder().withName("Bob").withBirthday("01-06-2025").build());
        model.addPerson(new PersonBuilder().withName("Charlie").withBirthday("03-06-2025").build());
    }

    @Test
    public void execute_sortByUpcomingBirthday_success() {
        SortBirthdayCommand command = new SortBirthdayCommand();
        command.execute(model, history);

        List<Person> expectedOrder = Arrays.asList(
                new PersonBuilder().withName("Bob").withBirthday("01-06-2025").build(),
                new PersonBuilder().withName("Alice").withBirthday("02-06-2025").build(),
                new PersonBuilder().withName("Charlie").withBirthday("03-06-2025").build()
        );

        assertEquals(expectedOrder.toString(), model.getFilteredPersonList().toString());
    }

    @Test
    public void execute_emptyList_noError() {
        Model emptyModel = new ModelManager(new CourseBook(), new UserPrefs());
        SortBirthdayCommand command = new SortBirthdayCommand();
        command.execute(emptyModel, history);

        assertEquals(0, emptyModel.getFilteredPersonList().size());
    }

    @Test
    public void equals_sameInstance_returnsTrue() {
        SortBirthdayCommand a = new SortBirthdayCommand();
        SortBirthdayCommand b = new SortBirthdayCommand();
        assertEquals(a, b);
    }

    @Test
    public void equals_differentType_returnsFalse() {
        SortBirthdayCommand command = new SortBirthdayCommand();
        Object other = new Object();
        assertNotEquals(command, other);
    }
}
