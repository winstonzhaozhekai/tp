package seedu.coursebook.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.coursebook.testutil.PersonBuilder;

public class PersonContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstNameList = Collections.singletonList("first");
        List<String> secondNameList = Arrays.asList("first", "second");

        PersonContainsKeywordsPredicate firstPredicate = new PersonContainsKeywordsPredicate(
                firstNameList, Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());
        PersonContainsKeywordsPredicate secondPredicate = new PersonContainsKeywordsPredicate(
                secondNameList, Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        PersonContainsKeywordsPredicate firstPredicateCopy = new PersonContainsKeywordsPredicate(
                firstNameList, Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_namePartialMatch_returnsTrue() {
        // Partial name match - "Ali" matches "Alice"
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(
                Collections.singletonList("Ali"), Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Partial name match - case insensitive
        predicate = new PersonContainsKeywordsPredicate(
                Collections.singletonList("alice"), Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Partial match in middle of name
        predicate = new PersonContainsKeywordsPredicate(
                Collections.singletonList("lic"), Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_phonePartialMatch_returnsTrue() {
        // Partial phone match
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(
                Collections.emptyList(), Collections.singletonList("123"), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder().withPhone("91234567").build()));

        // Multiple phone keywords, any matches
        predicate = new PersonContainsKeywordsPredicate(
                Collections.emptyList(), Arrays.asList("999", "123"), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder().withPhone("91234567").build()));
    }

    @Test
    public void test_emailPartialMatch_returnsTrue() {
        // Partial email match
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(
                Collections.emptyList(), Collections.emptyList(), Collections.singletonList("example"),
                Collections.emptyList(), Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder().withEmail("alice@example.com").build()));

        // Partial email match - case insensitive
        predicate = new PersonContainsKeywordsPredicate(
                Collections.emptyList(), Collections.emptyList(), Collections.singletonList("EXAMPLE"),
                Collections.emptyList(), Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder().withEmail("alice@example.com").build()));
    }

    @Test
    public void test_addressPartialMatch_returnsTrue() {
        // Partial address match
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                Collections.singletonList("Main"), Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder().withAddress("Main Street").build()));

        // Partial address match - substring
        predicate = new PersonContainsKeywordsPredicate(
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                Collections.singletonList("ain"), Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder().withAddress("Main Street").build()));

        // Partial address match - case insensitive
        predicate = new PersonContainsKeywordsPredicate(
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                Collections.singletonList("main"), Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder().withAddress("Main Street").build()));
    }

    @Test
    public void test_tagPartialMatch_returnsTrue() {
        // Partial tag match - "fri" matches "friends"
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.singletonList("fri"));
        assertTrue(predicate.test(new PersonBuilder().withTags("friends").build()));

        // Partial tag match - case insensitive
        predicate = new PersonContainsKeywordsPredicate(
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.singletonList("FRIE"));
        assertTrue(predicate.test(new PersonBuilder().withTags("friends").build()));

        // Partial tag match - middle of tag
        predicate = new PersonContainsKeywordsPredicate(
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.singletonList("end"));
        assertTrue(predicate.test(new PersonBuilder().withTags("friends").build()));

        // Multiple tags, any partial match
        predicate = new PersonContainsKeywordsPredicate(
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.singletonList("col"));
        assertTrue(predicate.test(new PersonBuilder().withTags("colleagues", "friends").build()));
    }

    @Test
    public void test_multipleFieldsOrLogic_returnsTrue() {
        // OR across fields - name matches
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(
                Collections.singletonList("Ali"), Collections.singletonList("999"),
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").withPhone("12345678").build()));

        // OR across fields - phone matches
        predicate = new PersonContainsKeywordsPredicate(
                Collections.singletonList("Bob"), Collections.singletonList("123"),
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").withPhone("12345678").build()));

        // OR across fields - tag matches
        predicate = new PersonContainsKeywordsPredicate(
                Collections.singletonList("Bob"), Collections.singletonList("999"),
                Collections.emptyList(), Collections.emptyList(), Collections.singletonList("fri"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").withPhone("12345678")
                .withTags("friends").build()));
    }

    @Test
    public void test_noMatchingKeywords_returnsFalse() {
        // Zero keywords
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));

        // Non-matching name keyword
        predicate = new PersonContainsKeywordsPredicate(
                Collections.singletonList("Carol"), Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Non-matching keywords across all fields
        predicate = new PersonContainsKeywordsPredicate(
                Collections.singletonList("Carol"), Collections.singletonList("999"),
                Collections.singletonList("xyz"), Collections.singletonList("Mars"),
                Collections.singletonList("enemy"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").withPhone("12345678")
                .withEmail("alice@example.com").withAddress("Main Street").withTags("friends").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> nameKeywords = List.of("keyword1", "keyword2");
        List<String> phoneKeywords = List.of("123");
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(
                nameKeywords, phoneKeywords, Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());

        String expected = PersonContainsKeywordsPredicate.class.getCanonicalName()
                + "{nameKeywords=" + nameKeywords
                + ", phoneKeywords=" + phoneKeywords
                + ", emailKeywords=" + Collections.emptyList()
                + ", addressKeywords=" + Collections.emptyList()
                + ", tagKeywords=" + Collections.emptyList() + "}";
        assertEquals(expected, predicate.toString());
    }
}

