package seedu.coursebook.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.coursebook.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_CONSTRAINTS =
            "Names should only contain alphanumeric characters and spaces, and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        fullName = capitalizeWords(name);
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Capitalizes the first letter of each word in the name.
     * Preserves the original spacing structure.
     *
     * @param name The name to capitalize.
     * @return The capitalized name.
     */
    private static String capitalizeWords(String name) {
        String[] words = name.split(" ", -1); // -1 to preserve trailing empty strings
        StringBuilder capitalized = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            if (words[i].length() > 0) {
                capitalized.append(Character.toUpperCase(words[i].charAt(0)));
                if (words[i].length() > 1) {
                    capitalized.append(words[i].substring(1).toLowerCase());
                }
            }
            if (i < words.length - 1) {
                capitalized.append(" ");
            }
        }

        return capitalized.toString();
    }


    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name)) {
            return false;
        }

        Name otherName = (Name) other;
        return fullName.equals(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
