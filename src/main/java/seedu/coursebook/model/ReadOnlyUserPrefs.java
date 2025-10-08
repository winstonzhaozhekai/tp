package seedu.coursebook.model;

import java.nio.file.Path;

import seedu.coursebook.commons.core.GuiSettings;

/**
 * Unmodifiable view of user prefs.
 */
public interface ReadOnlyUserPrefs {

    GuiSettings getGuiSettings();

    Path getCourseBookFilePath();

}
