package seedu.coursebook.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
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
 * Views detailed information of a person by index (from the displayed list) or by full name.
 */
public class ViewPersonCommand extends Command {

    public static final String COMMAND_WORD = "viewperson";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Views detailed information of a person by index (from the displayed list) or by full name.\n"
            + "Parameters: INDEX (positive integer) | NAME (case-insensitive; trims edges)\n"
            + "Examples: \n"
            + "  " + COMMAND_WORD + " 1\n"
            + "  " + COMMAND_WORD + " John Doe\n";

    public static final String MESSAGE_VIEW_PERSON_SUCCESS = "Showing details for: %1$s";
    public static final String MESSAGE_NO_MATCHING_NAME =
            "No contacts found! Please specify correct contact names.";
    public static final String MESSAGE_MULTIPLE_MATCHING_NAME =
            "Multiple contacts found with that name. Please view by index from the list:\n%1$s";

    private final Index targetIndex; // may be null when viewing by name
    private final Name targetName; // may be null when viewing by index

    /**
     * Creates a {@code ViewPersonCommand} for viewing by index.
     */
    public ViewPersonCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
        this.targetName = null;
    }

    /**
     * Creates a {@code ViewPersonCommand} for viewing by name.
     */
    public ViewPersonCommand(Name targetName) {
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

            Person personToView = lastShownList.get(targetIndex.getZeroBased());
            return new CommandResult(String.format(MESSAGE_VIEW_PERSON_SUCCESS, Messages.format(personToView)),
                    false, false, false, false, true, personToView);
        }

        // View by name path
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

        Person personToView = matches.get(0);
        return new CommandResult(String.format(MESSAGE_VIEW_PERSON_SUCCESS, Messages.format(personToView)),
                false, false, false, false, true, personToView);
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
        if (!(other instanceof ViewPersonCommand)) {
            return false;
        }

        ViewPersonCommand otherCommand = (ViewPersonCommand) other;
        return Objects.equals(targetIndex, otherCommand.targetIndex)
                && Objects.equals(targetName, otherCommand.targetName);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("targetName", targetName)
                .toString();
    }
}
