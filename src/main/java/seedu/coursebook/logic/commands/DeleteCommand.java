package seedu.coursebook.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import seedu.coursebook.commons.core.index.Index;
import seedu.coursebook.commons.util.ToStringBuilder;
import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.logic.commands.exceptions.CommandException;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.person.Name;
import seedu.coursebook.model.person.Person;

/**
 * Deletes one or more persons by index (from the displayed list) or by full name.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";
    public static final String ALIAS_WORD = "rm";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes person(s) by index (from the displayed list) or by full name.\n"
            + "Parameters: INDEX [INDEX]... (space-separated positive integers) | NAME[, NAME]... "
            + "(comma-separated, case-insensitive)\n"
            + "Examples: \n"
            + "  " + COMMAND_WORD + " 1\n"
            + "  " + COMMAND_WORD + " 1 2 3\n"
            + "  " + COMMAND_WORD + " John Doe\n"
            + "  " + COMMAND_WORD + " John Doe, Jane Smith\n"
            + "Alias: " + ALIAS_WORD;

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";
    public static final String MESSAGE_DELETE_PERSONS_SUCCESS = "Deleted %1$d person(s):\n%2$s";
    public static final String MESSAGE_DELETE_PARTIAL_SUCCESS = "Deleted %1$d person(s):\n%2$s\n\nWarnings:\n%3$s";
    public static final String MESSAGE_NO_MATCHING_NAME =
            "No contacts found! Please specify correct contact names.";
    public static final String MESSAGE_MULTIPLE_MATCHING_NAME =
            "Multiple contacts found with that name. Please delete by index from the list:\n%1$s";
    public static final String MESSAGE_NO_VALID_TARGETS = "No valid persons to delete.";

    private final List<Index> targetIndices; // may be null when deleting by name
    private final List<Name> targetNames; // may be null when deleting by index
    private final List<String> initialWarnings; // warnings collected during parsing (e.g., invalid indices)

    /**
     * Creates a {@code DeleteCommand} for deleting by index.
     */
    public DeleteCommand(Index targetIndex) {
        this.targetIndices = List.of(targetIndex);
        this.targetNames = null;
        this.initialWarnings = List.of();
    }

    /**
     * Creates a {@code DeleteCommand} for deleting by name.
     */
    public DeleteCommand(Name targetName) {
        this.targetIndices = null;
        this.targetNames = List.of(targetName);
        this.initialWarnings = List.of();
    }

    /**
     * Creates a {@code DeleteCommand} for deleting by multiple indices.
     */
    public DeleteCommand(List<Index> targetIndices) {
        this.targetIndices = new ArrayList<>(targetIndices);
        this.targetNames = null;
        this.initialWarnings = List.of();
    }

    /**
     * Creates a {@code DeleteCommand} for deleting by multiple names.
     */
    public DeleteCommand(List<Name> targetNames, boolean isByName) {
        this.targetIndices = null;
        this.targetNames = new ArrayList<>(targetNames);
        this.initialWarnings = List.of();
    }

    /**
     * Creates a {@code DeleteCommand} for deleting by indices with pre-collected warnings.
     */
    public DeleteCommand(List<Index> targetIndices, List<String> initialWarnings) {
        this.targetIndices = new ArrayList<>(targetIndices);
        this.targetNames = null;
        this.initialWarnings = new ArrayList<>(initialWarnings);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndices != null) {
            return executeByIndices(model, lastShownList);
        } else {
            return executeByNames(model, lastShownList);
        }
    }

    /**
     * Executes deletion by indices.
     */
    private CommandResult executeByIndices(Model model, List<Person> lastShownList) throws CommandException {
        List<Person> personsToDelete = new ArrayList<>();
        List<String> warnings = new ArrayList<>(initialWarnings);

        for (Index index : targetIndices) {
            if (index.getZeroBased() >= lastShownList.size()) {
                warnings.add("Index " + index.getOneBased() + " does not exist (max " + lastShownList.size() + ")");
            } else {
                Person person = lastShownList.get(index.getZeroBased());
                if (!personsToDelete.contains(person)) {
                    personsToDelete.add(person);
                }
            }
        }

        if (personsToDelete.isEmpty()) {
            throw new CommandException(MESSAGE_NO_VALID_TARGETS);
        }

        // Return confirmation request instead of deleting immediately
        String confirmationMessage = formatConfirmationMessage(personsToDelete, warnings);
        return CommandResult.forDeleteConfirmation(confirmationMessage, personsToDelete);
    }

    /**
     * Executes deletion by names.
     */
    private CommandResult executeByNames(Model model, List<Person> lastShownList) throws CommandException {
        List<Person> personsToDelete = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        for (Name name : targetNames) {
            String normalizedTarget = normalizeName(name.toString());
            List<Person> matches = lastShownList.stream()
                    .filter(p -> normalizeName(p.getName().toString()).equals(normalizedTarget))
                    .collect(Collectors.toList());

            if (matches.isEmpty()) {
                warnings.add("No contact found with name: " + name);
            } else if (matches.size() > 1) {
                warnings.add("Multiple contacts found with name: " + name + " (skipped)");
            } else {
                Person person = matches.get(0);
                if (!personsToDelete.contains(person)) {
                    personsToDelete.add(person);
                }
            }
        }

        if (personsToDelete.isEmpty()) {
            throw new CommandException(MESSAGE_NO_VALID_TARGETS + "\n" + String.join("\n", warnings));
        }

        // Return confirmation request instead of deleting immediately
        String confirmationMessage = formatConfirmationMessage(personsToDelete, warnings);
        return CommandResult.forDeleteConfirmation(confirmationMessage, personsToDelete);
    }

    /**
     * Formats the confirmation message based on the number of persons to delete and warnings.
     */
    private String formatConfirmationMessage(List<Person> personsToDelete, List<String> warnings) {
        String namesList = personsToDelete.stream()
                .map(p -> "- " + p.getName().toString())
                .collect(Collectors.joining("\n"));

        if (warnings.isEmpty()) {
            return namesList;
        }

        String warningText = warnings.stream()
                .map(w -> "- " + w)
                .collect(Collectors.joining("\n"));

        return namesList + "\n\nWarnings:\n" + warningText;
    }

    private static String normalizeName(String name) {
        return name.trim().toLowerCase(Locale.ROOT);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherCommand = (DeleteCommand) other;

        if (targetIndices != null && otherCommand.targetIndices != null) {
            return targetIndices.equals(otherCommand.targetIndices);
        }

        if (targetNames != null && otherCommand.targetNames != null) {
            return targetNames.equals(otherCommand.targetNames);
        }

        return false;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        if (targetIndices != null) {
            builder.add("targetIndices", targetIndices);
        }
        if (targetNames != null) {
            builder.add("targetNames", targetNames);
        }
        return builder.toString();
    }
}
