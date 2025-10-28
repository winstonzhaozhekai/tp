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

    /**
     * Returns a comma-separated list of allowed color names in lowercase.
     */
    public static String allowedNamesCsv() {
        StringBuilder sb = new StringBuilder();
        CourseColor[] values = values();
        for (int i = 0; i < values.length; i++) {
            sb.append(values[i].name().toLowerCase());
            if (i < values.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}


