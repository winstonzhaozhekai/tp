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
 * Deletes a person by index (from the displayed list) or by full name.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";
    public static final String ALIAS_WORD = "rm";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes a person by index (from the displayed list) or by full name.\n"
            + "Parameters: INDEX (positive integer) | NAME (case-insensitive; trims edges)\n"
            + "Examples: \n"
            + "  " + COMMAND_WORD + " 1\n"
            + "  " + COMMAND_WORD + " John Doe\n"
            + "Alias: " + ALIAS_WORD;

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";
    public static final String MESSAGE_NO_MATCHING_NAME =
            "No contacts found! Please specify correct contact names.";
    public static final String MESSAGE_MULTIPLE_MATCHING_NAME =
            "Multiple contacts found with that name. Please delete by index from the list:\n%1$s";

    private final Index targetIndex; // may be null when deleting by name
    private final Name targetName; // may be null when deleting by index

    /**
     * Creates a {@code DeleteCommand} for deleting by index.
     */
    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
        this.targetName = null;
    }

    /**
     * Creates a {@code DeleteCommand} for deleting by name.
     */
    public DeleteCommand(Name targetName) {
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

            Person personToDelete = lastShownList.get(targetIndex.getZeroBased());
            model.deletePerson(personToDelete);
            model.commitCourseBook();
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
        }

        // Delete by name path
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

        Person personToDelete = matches.get(0);
        model.deletePerson(personToDelete);
        model.commitCourseBook();
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
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
        return other == this // short circuit if same object
                || (other instanceof DeleteCommand // instanceof handles nulls
                && targetIndex.equals(((DeleteCommand) other).targetIndex)); // state check
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
