package networkbook.storage;

import static networkbook.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import networkbook.commons.exceptions.IllegalValueException;
import networkbook.model.person.Address;
import networkbook.model.person.Course;
import networkbook.model.person.Email;
import networkbook.model.person.GraduatingYear;
import networkbook.model.person.Name;
import networkbook.model.person.Phone;
import networkbook.model.person.Specialisation;
import networkbook.model.person.WebLink;
import networkbook.model.tag.Tag;
import networkbook.testutil.TypicalPersons;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_WEBLINK = "facebookcom";
    private static final String INVALID_GRADUATING_YEAR = "123a";
    private static final String INVALID_COURSE = "";
    private static final String INVALID_SPECIALISATION  = "";
    private static final List<JsonAdaptedProperty<Email>> INVALID_EMAILS =
            List.of(new JsonAdaptedProperty<>("example.com"));
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_PRIORITY = "hi";

    private static final String VALID_NAME = TypicalPersons.BENSON.getName().toString();
    private static final String VALID_PHONE = TypicalPersons.BENSON.getPhone().toString();
    private static final List<JsonAdaptedProperty<Email>> VALID_EMAILS = TypicalPersons.BENSON.getEmails().stream()
            .map(JsonAdaptedProperty::new)
            .collect(Collectors.toList());
    private static final String VALID_WEBLINK = "www.facebook.com/john-d";
    private static final String VALID_GRADUATING_YEAR = "2000";
    private static final String VALID_COURSE = "Computer Science";
    private static final String VALID_SPECIALISATION  = "Game Development";
    private static final List<JsonAdaptedProperty<Tag>> VALID_TAGS = TypicalPersons.BENSON.getTags().stream()
            .map(JsonAdaptedProperty::new)
            .collect(Collectors.toList());
    private static final String VALID_PRIORITY = "mEDiUM";

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(TypicalPersons.BENSON);
        assertEquals(TypicalPersons.BENSON, person.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(INVALID_NAME, VALID_PHONE, VALID_EMAILS,
                        VALID_WEBLINK, VALID_GRADUATING_YEAR, VALID_COURSE,
                        VALID_SPECIALISATION, VALID_TAGS, VALID_PRIORITY);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(null, VALID_PHONE, VALID_EMAILS,
                VALID_WEBLINK, VALID_GRADUATING_YEAR, VALID_COURSE,
                VALID_SPECIALISATION, VALID_TAGS, VALID_PRIORITY);
        String expectedMessage = String.format(
                JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT,
                Name.class.getSimpleName()
        );
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, INVALID_PHONE, VALID_EMAILS,
                        VALID_WEBLINK, VALID_GRADUATING_YEAR, VALID_COURSE,
                        VALID_SPECIALISATION, VALID_TAGS, VALID_PRIORITY);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, null, VALID_EMAILS,
                VALID_WEBLINK, VALID_GRADUATING_YEAR, VALID_COURSE,
                VALID_SPECIALISATION, VALID_TAGS, VALID_PRIORITY);
        String expectedMessage = String.format(
                JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT,
                Phone.class.getSimpleName()
        );
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, INVALID_EMAILS,
                        VALID_WEBLINK, VALID_GRADUATING_YEAR, VALID_COURSE,
                        VALID_SPECIALISATION, VALID_TAGS, VALID_PRIORITY);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidWebLink_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAILS,
                        INVALID_WEBLINK, VALID_GRADUATING_YEAR, VALID_COURSE,
                        VALID_SPECIALISATION, VALID_TAGS, VALID_PRIORITY);
        String expectedMessage = WebLink.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullGraduatingYear_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAILS,
                VALID_WEBLINK, null, VALID_COURSE,
                VALID_SPECIALISATION, VALID_TAGS, VALID_PRIORITY);
        String expectedMessage = String.format(
                JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT,
                GraduatingYear.class.getSimpleName()
        );
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidGraduatingYear_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAILS,
                        VALID_WEBLINK, INVALID_GRADUATING_YEAR, VALID_COURSE,
                        VALID_SPECIALISATION, VALID_TAGS, VALID_PRIORITY);
        String expectedMessage = GraduatingYear.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullCourse_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAILS,
                VALID_WEBLINK, VALID_GRADUATING_YEAR, null,
                VALID_SPECIALISATION, VALID_TAGS, VALID_PRIORITY);
        String expectedMessage = String.format(
                JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT,
                Course.class.getSimpleName()
        );
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullSpecialisation_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAILS,
                VALID_WEBLINK, VALID_GRADUATING_YEAR, VALID_COURSE,
                null, VALID_TAGS, VALID_PRIORITY);
        String expectedMessage = String.format(
                JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT,
                Specialisation.class.getSimpleName()
        );
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullWebLink_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAILS,
                null, VALID_GRADUATING_YEAR, VALID_COURSE,
                VALID_SPECIALISATION, VALID_TAGS, VALID_PRIORITY);
        String expectedMessage = String.format(
                JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT,
                WebLink.class.getSimpleName()
        );
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedProperty<Tag>> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedProperty<>(INVALID_TAG));
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAILS,
                        VALID_WEBLINK, VALID_GRADUATING_YEAR, VALID_COURSE,
                        VALID_SPECIALISATION, invalidTags, VALID_PRIORITY);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_invalidPriority_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAILS,
                VALID_WEBLINK, VALID_GRADUATING_YEAR, VALID_COURSE,
                VALID_SPECIALISATION, VALID_TAGS, INVALID_PRIORITY);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_nullPriority_success() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAILS,
                VALID_WEBLINK, VALID_GRADUATING_YEAR, VALID_COURSE,
                VALID_SPECIALISATION, VALID_TAGS, null);
        try {
            assertEquals(TypicalPersons.BENSON, person.toModelType());
        } catch (IllegalValueException e) {
            fail();
        }
    }

}
