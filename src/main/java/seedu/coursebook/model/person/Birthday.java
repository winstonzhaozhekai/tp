package seedu.coursebook.model.person;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Represents a person's birthday in the course book.
 * The {@code Birthday} class ensures that the date is valid and follows the format {@code dd-MM-yyyy}.
 * It uses strict date resolution to reject invalid calendar dates such as 31-02-2023.
 */
public class Birthday {
    public static final String MESSAGE_CONSTRAINTS = "Birthdays should be in the format dd-MM-yyyy "
            + "and must be a valid calendar date.";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    public final String value;

    /**
     * Constructs a {@code Birthday}.
     *
     * @param birthday A valid birthday string.
     */
    public Birthday(String birthday) {
        requireNonNull(birthday);
        if (!isValidDate(birthday)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        this.value = birthday;
    }

    /**
     * Returns true if the given string is a valid date in dd-MM-yyyy format.
     */
    public static boolean isValidDate(String test) {
        if (test == null) {
            return false;
        }

        try {
            LocalDate.parse(test, FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Returns the given date in dd-MM-yyyy format.
     */
    public LocalDate getDate() {
        return LocalDate.parse(value, FORMATTER);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Birthday
                && value.equals(((Birthday) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
