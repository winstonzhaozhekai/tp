package seedu.coursebook.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.coursebook.commons.util.ToStringBuilder;
import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.logic.Messages;
import seedu.coursebook.model.Model;
import seedu.coursebook.model.person.PersonContainsKeywordsPredicate;

/**
 * Finds and lists all persons whose fields match the given keywords.
 * Supports field prefixes and OR-across-fields matching.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";
    public static final String ALIAS_WORD = "f";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds persons by matching any provided field and "
            + "displays them as a list with index numbers.\n"
            + "Parameters: [n/NAME_KEYWORDS] [p/PHONE_KEYWORDS] [e/EMAIL_KEYWORDS] [a/ADDRESS_KEYWORDS] "
            + "[t/TAG]\n"
            + "- Prefixes can appear multiple times; values are aggregated per field (OR within field).\n"
            + "- OR across fields: a person matches if ANY provided field matches.\n"
            + "- Within a field, ANY keyword may match using partial/substring matching.\n"
            + "- All fields support case-insensitive partial matching (e.g., 'Ali' matches 'Alice').\n"
            + "- If no prefixes are present, input will be treated as a name.\n"
            + "- Only supports prefixes: n/, p/, e/, a/, t/. Any other prefix causes an error.\n"
            + "Alias: f\n"
            + "Examples: find n/John n/Alice | find p/9123 p/9876 | find t/friend t/colleague | find alex bob";

    private final PersonContainsKeywordsPredicate predicate;

    public FindCommand(PersonContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        int count = model.getFilteredPersonList().size();
        String message = count == 0
                ? Messages.MESSAGE_NO_CONTACT_FOUND
                : String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, count);
        return new CommandResult(message, false, false, true, false);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
