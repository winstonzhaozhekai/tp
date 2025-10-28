package seedu.coursebook.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.logic.parser.SortCommandParser;
import seedu.coursebook.logic.parser.exceptions.ParseException;
import seedu.coursebook.model.CourseBook;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.ModelManager;
import seedu.coursebook.model.UserPrefs;
import seedu.coursebook.model.person.Person;
import seedu.coursebook.testutil.PersonBuilder;

public class SortCommandTest {

    private Model model;
    private CommandHistory history;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new CourseBook(), new UserPrefs());
        history = new CommandHistory();

        model.addPerson(new PersonBuilder()
                .withName("Charlie")
                .withPhone("90876543")
                .withEmail("Charlie@email.com").build());
        model.addPerson(new PersonBuilder()
                .withName("Alice")
                .withPhone("98765432")
                .withEmail("Alice@email.com").build());
        model.addPerson(new PersonBuilder()
                .withName("Bob")
                .withPhone("97865432")
                .withEmail("Bob@gmail.com").build());
    }

    @Test
    public void execute_sortAscending_success() {
        SortCommand command = new SortCommand("asc");
        command.execute(model, history);

        List<Person> expectedOrder = Arrays.asList(
                new PersonBuilder()
                    .withName("Alice")
                    .withPhone("98765432")
                    .withEmail("Alice@email.com").build(),
                new PersonBuilder()
                        .withName("Bob")
                        .withPhone("97865432")
                        .withEmail("Bob@gmail.com").build(),
                new PersonBuilder()
                        .withName("Charlie")
                        .withPhone("90876543")
                        .withEmail("Charlie@email.com").build()
        );

        assertEquals(expectedOrder.toString(), model.getFilteredPersonList().toString());
    }

    @Test
    public void execute_sortDescending_success() {
        SortCommand command = new SortCommand("desc");
        command.execute(model, history);

        List<Person> expectedOrder = Arrays.asList(
                new PersonBuilder()
                        .withName("Charlie")
                        .withPhone("90876543")
                        .withEmail("Charlie@email.com").build(),
                new PersonBuilder()
                        .withName("Bob")
                        .withPhone("97865432")
                        .withEmail("Bob@gmail.com").build(),
                new PersonBuilder()
                        .withName("Alice")
                        .withPhone("98765432")
                        .withEmail("Alice@email.com").build()
        );

        assertEquals(expectedOrder.toString(), model.getFilteredPersonList().toString());
    }

    @Test
    public void parse_invalidOrder_throwsParseException() {
        SortCommandParser parser = new SortCommandParser();
        assertThrows(ParseException.class, () -> parser.parse(" /by abcd"));
    }

    @Test
    public void execute_emptyList_noError() {
        Model emptyModel = new ModelManager(new CourseBook(), new UserPrefs());
        SortCommand command = new SortCommand("asc");
        command.execute(emptyModel, history);

        assertEquals(0, emptyModel.getFilteredPersonList().size());
    }

    @Test
    public void equals_sameOrder_returnsTrue() {
        SortCommand a = new SortCommand("asc");
        SortCommand b = new SortCommand("asc");
        assertEquals(a, b);
    }

    @Test
    public void equals_differentOrder_returnsFalse() {
        SortCommand a = new SortCommand("asc");
        SortCommand b = new SortCommand("desc");
        assertThrows(AssertionError.class, () -> assertEquals(a, b));
    }
}
