package seedu.coursebook.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.coursebook.commons.util.ToStringBuilder;
import seedu.coursebook.logic.CommandHistory;
import seedu.coursebook.logic.commands.exceptions.CommandException;
import seedu.coursebook.model.Model;

/**
 * Changes the application theme.
 */
public class ThemeCommand extends Command {

    public static final String COMMAND_WORD = "theme";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Changes the application theme.\n"
            + "Parameters: THEME_NAME (dark/ blue/ love/ tree)\n"
            + "Examples: \n"
            + "  " + COMMAND_WORD + " love\n"
            + "  " + COMMAND_WORD + " blue";

    public static final String MESSAGE_SUCCESS = "Theme changed to: %1$s";
    public static final String MESSAGE_INVALID_THEME = "Invalid theme name. Available themes: dark, blue, love, tree";
    public static final String MESSAGE_SAME_THEME = "Theme is already %s!";

    /**
     * Enumerator for themes of Theme
     */
    public enum Theme {
        DARK("dark", "DarkTheme.css", "Extensions.css"),
        BLUE("blue", "BlueTheme.css", "BlueExtensions.css"),
        LOVE("love", "LoveTheme.css", "LoveExtensions.css"),
        TREE("tree", "TreeTheme.css", "TreeExtensions.css");

        private final String name;
        private final String cssFile;
        private final String extensionsFile;

        Theme(String name, String cssFile, String extensionsFile) {
            this.name = name;
            this.cssFile = cssFile;
            this.extensionsFile = extensionsFile;
        }

        public String getName() {
            return name;
        }

        public String getThemeCssFile() {
            return cssFile;
        }

        public String getExtensionsFile() {
            return extensionsFile;
        }

        /**
         * Returns the Theme enum corresponding to the given theme name.
         * @param themeName the name of the theme
         * @return the corresponding Theme enum, or null if not found
         */
        public static Theme fromString(String themeName) {
            for (Theme theme : Theme.values()) {
                if (theme.name.equalsIgnoreCase(themeName)) {
                    return theme;
                }
            }
            return null;
        }

    }

    private final Theme targetTheme;

    /**
     * Creates a ThemeCommand to change to the specified theme.
     */
    public ThemeCommand(Theme targetTheme) {
        this.targetTheme = targetTheme;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        if (targetTheme == model.getCurrentTheme()) {
            throw new CommandException(String.format(MESSAGE_SAME_THEME, targetTheme.getName()));
        }
        CommandResult result = model.setCurrentTheme(targetTheme);
        model.commitCourseBook();
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ThemeCommand)) {
            return false;
        }

        ThemeCommand otherCommand = (ThemeCommand) other;
        return targetTheme.equals(otherCommand.targetTheme);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetTheme", targetTheme)
                .toString();
    }
}
