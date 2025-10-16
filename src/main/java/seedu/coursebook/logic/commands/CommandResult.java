package seedu.coursebook.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.coursebook.commons.util.ToStringBuilder;
import seedu.coursebook.model.person.Person;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    private final String feedbackToUser;

    /** Help information should be shown to the user. */
    private final boolean showHelp;

    /** The application should exit. */
    private final boolean exit;

    /** The UI should show persons view. */
    private final boolean showPersons;

    /** The UI should show courses view. */
    private final boolean showCourses;

    /** The UI should show person detail popup. */
    private final boolean showPersonDetail;

    /** The person to show in detail popup. */
    private final Person personToShow;

    /**
     * Constructs a {@code CommandResult} with all the specified fields.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit,
                        boolean showPersons, boolean showCourses) {
        this(feedbackToUser, showHelp, exit, showPersons, showCourses, false, null);
    }

    /**
     * Constructs a {@code CommandResult} with all the specified fields including person detail.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit,
                        boolean showPersons, boolean showCourses, boolean showPersonDetail, Person personToShow) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.showPersons = showPersons;
        this.showCourses = showCourses;
        this.showPersonDetail = showPersonDetail;
        this.personToShow = personToShow;
    }

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit) {
        this(feedbackToUser, showHelp, exit, false, false);
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, false, false, false, false);
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public boolean isExit() {
        return exit;
    }

    public boolean isShowPersons() {
        return showPersons;
    }

    public boolean isShowCourses() {
        return showCourses;
    }

    public boolean isShowPersonDetail() {
        return showPersonDetail;
    }

    public Person getPersonToShow() {
        return personToShow;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommandResult)) {
            return false;
        }

        CommandResult otherCommandResult = (CommandResult) other;
        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && showHelp == otherCommandResult.showHelp
                && exit == otherCommandResult.exit
                && showPersons == otherCommandResult.showPersons
                && showCourses == otherCommandResult.showCourses
                && showPersonDetail == otherCommandResult.showPersonDetail
                && Objects.equals(personToShow, otherCommandResult.personToShow);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, exit, showPersons, showCourses, showPersonDetail, personToShow);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("showHelp", showHelp)
                .add("exit", exit)
                .add("showPersons", showPersons)
                .add("showCourses", showCourses)
                .add("showPersonDetail", showPersonDetail)
                .add("personToShow", personToShow)
                .toString();
    }

}
