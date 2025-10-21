package seedu.coursebook.model.person;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import seedu.coursebook.commons.util.ToStringBuilder;
import seedu.coursebook.model.tag.Tag;

/**
 * Tests that a {@code Person} matches ANY of the provided field-specific keyword lists.
 * - OR across fields: person matches if any provided field matches
 * - Within a field, ANY keyword may match
 * - Matching rules:
 *   - All fields: case-insensitive partial/substring match
 */
public class PersonContainsKeywordsPredicate implements Predicate<Person> {

    private final List<String> nameKeywords;
    private final List<String> phoneKeywords;
    private final List<String> emailKeywords;
    private final List<String> addressKeywords;
    private final List<String> tagKeywords;

    /**
     * Creates a predicate with keyword lists for each supported field.
     * Any of the lists may be empty; the person matches if ANY provided field matches.
     */
    public PersonContainsKeywordsPredicate(List<String> nameKeywords,
                                           List<String> phoneKeywords,
                                           List<String> emailKeywords,
                                           List<String> addressKeywords,
                                           List<String> tagKeywords) {
        this.nameKeywords = nameKeywords;
        this.phoneKeywords = phoneKeywords;
        this.emailKeywords = emailKeywords;
        this.addressKeywords = addressKeywords;
        this.tagKeywords = tagKeywords;
    }

    @Override
    public boolean test(Person person) {
        Objects.requireNonNull(person);

        boolean nameMatches = containsAnyIgnoreCase(person.getName().fullName, nameKeywords);
        boolean addressMatches = containsAnyIgnoreCase(person.getAddress().value, addressKeywords);
        boolean phoneMatches = containsAnyIgnoreCase(person.getPhone().value, phoneKeywords);
        boolean emailMatches = containsAnyIgnoreCase(person.getEmail().value, emailKeywords);
        boolean tagMatches = containsAnyTagIgnoreCase(person.getTags(), tagKeywords);

        return nameMatches || addressMatches || phoneMatches || emailMatches || tagMatches;
    }

    private static boolean containsAnyIgnoreCase(String text, List<String> keywords) {
        return Optional.ofNullable(keywords)
                .map(list -> list.stream()
                        .filter(kw -> kw != null && !kw.isBlank())
                        .anyMatch(keyword -> containsIgnoreCase(text, keyword)))
                .orElse(false);
    }

    private static boolean containsIgnoreCase(String text, String fragment) {
        if (text == null || fragment == null) {
            return false;
        }
        String lowerText = text.toLowerCase();
        String lowerFragment = fragment.toLowerCase();
        return lowerText.contains(lowerFragment);
    }

    private static boolean containsAnyTagIgnoreCase(Set<Tag> tags, List<String> keywords) {
        if (tags == null || tags.isEmpty()) {
            return false;
        }
        return Optional.ofNullable(keywords)
                .map(Collection::stream)
                .orElseGet(java.util.stream.Stream::empty)
                .filter(kw -> kw != null && !kw.isBlank())
                .anyMatch(keyword -> tags.stream()
                        .anyMatch(tag -> containsIgnoreCase(tag.tagName, keyword)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof PersonContainsKeywordsPredicate)) {
            return false;
        }
        PersonContainsKeywordsPredicate o = (PersonContainsKeywordsPredicate) other;
        return Objects.equals(nameKeywords, o.nameKeywords)
                && Objects.equals(phoneKeywords, o.phoneKeywords)
                && Objects.equals(emailKeywords, o.emailKeywords)
                && Objects.equals(addressKeywords, o.addressKeywords)
                && Objects.equals(tagKeywords, o.tagKeywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("nameKeywords", nameKeywords)
                .add("phoneKeywords", phoneKeywords)
                .add("emailKeywords", emailKeywords)
                .add("addressKeywords", addressKeywords)
                .add("tagKeywords", tagKeywords)
                .toString();
    }
}


