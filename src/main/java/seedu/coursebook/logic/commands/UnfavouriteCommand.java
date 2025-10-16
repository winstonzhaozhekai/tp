package seedu.coursebook.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import seedu.coursebook.commons.core.index.Index;
import seedu.coursebook.commons.util.ToStringBuilder;
import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.logic.Messages;
import seedu.coursebook.logic.commands.exceptions.CommandException;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.person.Name;
import seedu.coursebook.model.person.Person;

/**
 * Removes favourite status from a person by index (from the displayed list) or by full name.
 */
public class UnfavouriteCommand extends Command {

    public static final String COMMAND_WORD = "unfavourite";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes favourite status from a person by index (from the displayed list) or by full name.\n"
            + "Parameters: INDEX (positive integer) | NAME (case-insensitive; trims edges)\n"
            + "Examples: \n"
            + "  " + COMMAND_WORD + " 1\n"
            + "  " + COMMAND_WORD + " John Doe";

    public static final String MESSAGE_UNFAVOURITE_PERSON_SUCCESS = "Removed from favourites: %1$s";
    public static final String MESSAGE_NOT_FAVOURITE = "This person is not marked as favourite.";
    public static final String MESSAGE_NO_MATCHING_NAME =
            "No contacts found! Please specify correct contact names.";
    public static final String MESSAGE_MULTIPLE_MATCHING_NAME =
            "Multiple contacts found with that name. Please unfavourite by index from the list:\n%1$s";

    private final Index targetIndex; // may be null when unfavouriting by name
    private final Name targetName; // may be null when unfavouriting by index

    /**
     * Creates an {@code UnfavouriteCommand} for unmarking by index.
     */
    public UnfavouriteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
        this.targetName = null;
    }

    /**
     * Creates an {@code UnfavouriteCommand} for unmarking by name.
     */
    public UnfavouriteCommand(Name targetName) {
        this.targetIndex = null;
        this.targetName = targetName;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex != null) {
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            Person personToUnfavourite = lastShownList.get(targetIndex.getZeroBased());
            if (!personToUnfavourite.isFavourite()) {
                throw new CommandException(MESSAGE_NOT_FAVOURITE);
            }

            Person unfavouritedPerson = createUnfavouritePerson(personToUnfavourite);
            model.setPerson(personToUnfavourite, unfavouritedPerson);
            model.commitCourseBook();
            return new CommandResult(String.format(MESSAGE_UNFAVOURITE_PERSON_SUCCESS,
                    Messages.format(unfavouritedPerson)));
        }

        // Unfavourite by name path
        String normalizedTarget = normalizeName(targetName.toString());
        List<Person> matches = lastShownList.stream()
                .filter(p -> normalizeName(p.getName().toString()).equals(normalizedTarget))
                .collect(Collectors.toList());

        if (matches.isEmpty()) {
            throw new CommandException(MESSAGE_NO_MATCHING_NAME);
        }
        if (matches.size() > 1) {
            String indexedList = buildIndexedList(matches, lastShownList);
            throw new CommandException(String.format(MESSAGE_MULTIPLE_MATCHING_NAME, indexedList));
        }

        Person personToUnfavourite = matches.get(0);
        if (!personToUnfavourite.isFavourite()) {
            throw new CommandException(MESSAGE_NOT_FAVOURITE);
        }

        Person unfavouritedPerson = createUnfavouritePerson(personToUnfavourite);
        model.setPerson(personToUnfavourite, unfavouritedPerson);
        model.commitCourseBook();
        return new CommandResult(String.format(MESSAGE_UNFAVOURITE_PERSON_SUCCESS,
                Messages.format(unfavouritedPerson)));
    }

    /**
     * Creates a new Person with isFavourite set to false.
     */
    private static Person createUnfavouritePerson(Person person) {
        return new Person(person.getName(), person.getPhone(), person.getEmail(),
                person.getAddress(), person.getTags(), person.getCourses(),
                person.getBirthday(), false);
    }

    private static String normalizeName(String name) {
        return name.trim().toLowerCase(Locale.ROOT);
    }

    private static String buildIndexedList(List<Person> matches, List<Person> currentList) {
        StringBuilder sb = new StringBuilder();
        for (Person p : matches) {
            int oneBasedIndex = currentList.indexOf(p) + 1;
            if (oneBasedIndex <= 0) {
                sb.append("- ").append(Messages.format(p)).append('\n');
            } else {
                sb.append(oneBasedIndex).append('.').append(' ').append(Messages.format(p)).append('\n');
            }
        }
        return sb.toString().trim();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnfavouriteCommand)) {
            return false;
        }

        UnfavouriteCommand otherCommand = (UnfavouriteCommand) other;
        if (targetIndex != null) {
            return targetIndex.equals(otherCommand.targetIndex);
        } else {
            return targetName.equals(otherCommand.targetName);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("targetName", targetName)
                .toString();
    }
}

