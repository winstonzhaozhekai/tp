package seedu.coursebook.model.course;

/**
 * Fixed palette for course colors, with utility to parse by name and obtain hex.
 */
public enum CourseColor {
    RED("#d32f2f"),
    ORANGE("#f57c00"),
    YELLOW("#fbc02d"),
    GREEN("#388e3c"),
    BLUE("#1976d2"),
    PURPLE("#7b1fa2"),
    PINK("#c2185b"),
    GRAY("#616161");

    private final String hex;

    CourseColor(String hex) {
        this.hex = hex;
    }

    public String getHex() {
        return hex;
    }

    /**
     * Parses a case-insensitive color name into CourseColor.
     * Returns null if name is null or empty.
     * Throws IllegalArgumentException if name does not match any enum.
     */
    public static CourseColor fromName(String name) {
        if (name == null) {
            return null;
        }
        String normalized = name.trim();
        if (normalized.isEmpty()) {
            return null;
        }
        return CourseColor.valueOf(normalized.toUpperCase());
    }
}


