package seedu.coursebook.testutil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.coursebook.model.course.Course;
import seedu.coursebook.model.person.Address;
import seedu.coursebook.model.person.Birthday;
import seedu.coursebook.model.person.Email;
import seedu.coursebook.model.person.Name;
import seedu.coursebook.model.person.Person;
import seedu.coursebook.model.person.Phone;
import seedu.coursebook.model.tag.Tag;
import seedu.coursebook.model.util.SampleDataUtil;

/**
 * A utility class to help construct {@link Person} objects for testing purposes.
 * Allows setting individual fields using a fluent builder pattern.
 */
public class PersonBuilder {
    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private Birthday birthday;
    private Set<Tag> tags;
    private Set<Course> courses;

    /**
     * Constructs a {@code PersonBuilder} with default values.
     * Fields are initialized with default name, phone, email, and address.
     * Tags and courses are empty by default. Birthday is null.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        tags = new HashSet<>();
        courses = new HashSet<>();
        birthday = null;
    }

    /**
     * Constructs a {@code PersonBuilder} with the data of an existing {@code Person}.
     *
     * @param personToCopy The person whose data is copied into the builder.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        address = personToCopy.getAddress();
        birthday = personToCopy.getBirthday();
        tags = new HashSet<>(personToCopy.getTags());
        courses = new HashSet<>(personToCopy.getCourses());
    }

    /**
     * Sets the {@code Name} of the {@code Person} being built.
     *
     * @param name The name to set.
     * @return This builder instance.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Sets the {@code Tag}s of the {@code Person} being built.
     *
     * @param tags One or more tag strings.
     * @return This builder instance.
     */
    public PersonBuilder withTags(String... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Course}s of the {@code Person} being built.
     *
     * @param courseCodes One or more course code strings.
     * @return This builder instance.
     */
    public PersonBuilder withCourses(String... courseCodes) {
        this.courses = Arrays.stream(courseCodes)
                .map(Course::new)
                .collect(Collectors.toSet());
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} being built.
     *
     * @param address The address to set.
     * @return This builder instance.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} being built.
     *
     * @param phone The phone number to set.
     * @return This builder instance.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} being built.
     *
     * @param email The email address to set.
     * @return This builder instance.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code Birthday} of the {@code Person} being built.
     *
     * @param birthday The birthday string to set. Can be null.
     * @return This builder instance.
     */
    public PersonBuilder withBirthday(String birthday) {
        this.birthday = birthday != null ? new Birthday(birthday) : null;
        return this;
    }

    /**
     * Builds and returns a {@code Person} object with the current builder values.
     *
     * @return A new {@code Person} instance.
     */
    public Person build() {
        return new Person(name, phone, email, address, tags, courses, birthday);
    }
}
