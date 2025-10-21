package seedu.coursebook.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.person.Person;

/**
 * Lists all favourite contacts in the address book.
 *
 * <p>Usage: {@value #COMMAND_WORD}</p>
 *
 * <p>This command updates the model's filtered person list to show only persons
 * for whom {@link seedu.coursebook.model.person.Person#isFavourite()} returns true.
 * The resulting {@link seedu.coursebook.logic.commands.CommandResult} indicates whether
 * any favourite contacts were found.</p>
 *
 * @see seedu.coursebook.model.person.Person#isFavourite()
 * @see seedu.coursebook.model.Model#updateFilteredPersonList(java.util.function.Predicate)
 */
public class FavCommand extends Command {
    /**
     * The command word for listing favourite contacts.
     */
    public static final String COMMAND_WORD = "favs";

    /**
     * Usage message for the favs command.
     */
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all favourite contacts.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_NO_FAVOURITES = "No favourite contacts yet.";
    public static final String MESSAGE_LISTED_FAVOURITES = "Listed all favourite contacts.";



    /**
     * Constructs a new {@code FavCommand}.
     */
    public FavCommand() {

    }

    /**
     * Executes the favs command.
     *
     * <p>The method filters the model's person list to only include favourite persons
     * and returns a {@link CommandResult} indicating whether any favourites were listed.
     *
     * @param model   the model to operate on; must not be null
     * @param history the command history
     * @return a CommandResult containing a message and flags used by the UI
     * @throws NullPointerException if {@code model} is null
     */
    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        Predicate<Person> personsInFavs = person ->
                person.isFavourite();
        model.updateFilteredPersonList(personsInFavs);
        boolean hasFavourites = !model.getFilteredPersonList().isEmpty();
        String message = hasFavourites ? MESSAGE_LISTED_FAVOURITES : MESSAGE_NO_FAVOURITES;

        return new CommandResult(message, false, false, true, false);
    }
}
