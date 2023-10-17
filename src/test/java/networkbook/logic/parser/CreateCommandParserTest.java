package networkbook.logic.parser;

import static networkbook.logic.commands.CommandTestUtil.VALID_COURSE_AMY;
import static networkbook.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static networkbook.logic.commands.CommandTestUtil.VALID_GRADUATING_YEAR_AMY;
import static networkbook.logic.commands.CommandTestUtil.VALID_LINK_AMY;
import static networkbook.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static networkbook.logic.commands.CommandTestUtil.VALID_PRIORITY_AMY;
import static networkbook.logic.commands.CommandTestUtil.VALID_SPECIALISATION_AMY;

import java.util.List;

import org.junit.jupiter.api.Test;

import networkbook.logic.Messages;
import networkbook.logic.commands.CommandTestUtil;
import networkbook.logic.commands.CreateCommand;
import networkbook.model.person.Course;
import networkbook.model.person.Email;
import networkbook.model.person.GraduatingYear;
import networkbook.model.person.Link;
import networkbook.model.person.Name;
import networkbook.model.person.Person;
import networkbook.model.person.Phone;
import networkbook.model.person.Specialisation;
import networkbook.model.tag.Tag;
import networkbook.testutil.PersonBuilder;
import networkbook.testutil.TypicalPersons;

public class CreateCommandParserTest {
    private CreateCommandParser parser = new CreateCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson = new PersonBuilder(TypicalPersons.BOB)
                .withTags(CommandTestUtil.VALID_TAG_FRIEND)
                .build();

        // whitespace only preamble
        CommandParserTestUtil.assertParseSuccess(parser, CommandTestUtil.PREAMBLE_WHITESPACE
                + CommandTestUtil.NAME_DESC_BOB + CommandTestUtil.PHONE_DESC_BOB + CommandTestUtil.EMAIL_DESC_BOB
                + CommandTestUtil.LINK_DESC_BOB + CommandTestUtil.GRADUATING_YEAR_DESC_BOB
                + CommandTestUtil.COURSE_DESC_BOB + CommandTestUtil.SPECIALISATION_DESC_BOB
                + CommandTestUtil.TAG_DESC_FRIEND,
                new CreateCommand(expectedPerson));


        // multiple tags - all accepted
        Person expectedPersonMultipleTags = new PersonBuilder(TypicalPersons.BOB)
                .withTags(CommandTestUtil.VALID_TAG_FRIEND, CommandTestUtil.VALID_TAG_HUSBAND)
                .build();
        CommandParserTestUtil.assertParseSuccess(parser,
                CommandTestUtil.NAME_DESC_BOB + CommandTestUtil.PHONE_DESC_BOB
                        + CommandTestUtil.EMAIL_DESC_BOB + CommandTestUtil.LINK_DESC_BOB
                        + CommandTestUtil.GRADUATING_YEAR_DESC_BOB + CommandTestUtil.COURSE_DESC_BOB
                        + CommandTestUtil.SPECIALISATION_DESC_BOB + CommandTestUtil.TAG_DESC_HUSBAND
                        + CommandTestUtil.TAG_DESC_FRIEND,
                new CreateCommand(expectedPersonMultipleTags));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedPersonString = CommandTestUtil.NAME_DESC_BOB
                + CommandTestUtil.PHONE_DESC_BOB + CommandTestUtil.EMAIL_DESC_BOB
                + CommandTestUtil.LINK_DESC_BOB + CommandTestUtil.GRADUATING_YEAR_DESC_BOB
                + CommandTestUtil.COURSE_DESC_BOB + CommandTestUtil.SPECIALISATION_DESC_BOB
                + CommandTestUtil.TAG_DESC_FRIEND;

        // multiple names
        CommandParserTestUtil.assertParseFailure(parser,
                CommandTestUtil.NAME_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_NAME));

        // multiple phones
        CommandParserTestUtil.assertParseFailure(parser,
                CommandTestUtil.PHONE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_PHONE));

        // multiple emails
        CommandParserTestUtil.assertParseFailure(parser,
                CommandTestUtil.EMAIL_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_EMAIL));

        // multiple addresses
        CommandParserTestUtil.assertParseFailure(parser,
                CommandTestUtil.LINK_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_LINK));

        // multiple fields repeated
        CommandParserTestUtil.assertParseFailure(parser,
                validExpectedPersonString + CommandTestUtil.PHONE_DESC_AMY
                        + CommandTestUtil.EMAIL_DESC_AMY + CommandTestUtil.NAME_DESC_AMY
                        + CommandTestUtil.LINK_DESC_AMY + CommandTestUtil.GRADUATING_YEAR_DESC_AMY
                        + CommandTestUtil.COURSE_DESC_AMY + CommandTestUtil.SPECIALISATION_DESC_AMY
                        + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(
                        CliSyntax.PREFIX_NAME,
                        CliSyntax.PREFIX_PHONE,
                        CliSyntax.PREFIX_EMAIL,
                        CliSyntax.PREFIX_LINK,
                        CliSyntax.PREFIX_GRADUATING_YEAR,
                        CliSyntax.PREFIX_COURSE,
                        CliSyntax.PREFIX_SPECIALISATION));

        // invalid value followed by valid value

        // invalid name
        CommandParserTestUtil.assertParseFailure(parser,
                CommandTestUtil.INVALID_NAME_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_NAME));

        // invalid email
        CommandParserTestUtil.assertParseFailure(parser,
                CommandTestUtil.INVALID_EMAIL_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_EMAIL));

        // invalid phone
        CommandParserTestUtil.assertParseFailure(parser,
                CommandTestUtil.INVALID_PHONE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_PHONE));

        // invalid link
        CommandParserTestUtil.assertParseFailure(parser,
                CommandTestUtil.INVALID_LINK_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_LINK));

        // invalid graduating year
        CommandParserTestUtil.assertParseFailure(parser,
                CommandTestUtil.INVALID_GRADUATING_YEAR_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_GRADUATING_YEAR));

        // invalid course
        CommandParserTestUtil.assertParseFailure(parser,
                CommandTestUtil.INVALID_COURSE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_COURSE));

        // invalid specialisation
        CommandParserTestUtil.assertParseFailure(parser,
                CommandTestUtil.INVALID_SPECIALISATION_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_SPECIALISATION));

        // valid value followed by invalid value

        // invalid name
        CommandParserTestUtil.assertParseFailure(parser,
                validExpectedPersonString + CommandTestUtil.INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_NAME));

        // invalid email
        CommandParserTestUtil.assertParseFailure(parser,
                validExpectedPersonString + CommandTestUtil.INVALID_EMAIL_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_EMAIL));

        // invalid phone
        CommandParserTestUtil.assertParseFailure(parser,
                validExpectedPersonString + CommandTestUtil.INVALID_PHONE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_PHONE));

        // invalid link
        CommandParserTestUtil.assertParseFailure(parser,
                validExpectedPersonString + CommandTestUtil.INVALID_LINK_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_LINK));

        // invalid graduating year
        CommandParserTestUtil.assertParseFailure(parser,
                validExpectedPersonString + CommandTestUtil.INVALID_GRADUATING_YEAR_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_GRADUATING_YEAR));

        // invalid course
        CommandParserTestUtil.assertParseFailure(parser,
                validExpectedPersonString + CommandTestUtil.INVALID_COURSE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_COURSE));

        // invalid specialisation
        CommandParserTestUtil.assertParseFailure(parser,
                validExpectedPersonString + CommandTestUtil.INVALID_SPECIALISATION_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_SPECIALISATION));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // no phone
        Person expectedPersonWithoutPhone = new PersonBuilder(TypicalPersons.AMY)
                .withoutOptionalFields()
                .withEmails(List.of(VALID_EMAIL_AMY))
                .withLinks(List.of(VALID_LINK_AMY))
                .withGraduatingYear(VALID_GRADUATING_YEAR_AMY)
                .withCourse(VALID_COURSE_AMY)
                .withSpecialisation(VALID_SPECIALISATION_AMY)
                .withPriority(VALID_PRIORITY_AMY)
                .build();
        CommandParserTestUtil.assertParseSuccess(parser,
                CommandTestUtil.NAME_DESC_AMY
                        + CommandTestUtil.EMAIL_DESC_AMY + CommandTestUtil.LINK_DESC_AMY
                        + CommandTestUtil.GRADUATING_YEAR_DESC_AMY + CommandTestUtil.COURSE_DESC_AMY
                        + CommandTestUtil.SPECIALISATION_DESC_AMY + CommandTestUtil.PRIORITY_DESC_AMY,
                new CreateCommand(expectedPersonWithoutPhone));

        // no emails
        Person expectedPersonWithoutEmails = new PersonBuilder(TypicalPersons.AMY)
                .withoutOptionalFields()
                .withPhone(VALID_PHONE_AMY)
                .withLinks(List.of(VALID_LINK_AMY))
                .withGraduatingYear(VALID_GRADUATING_YEAR_AMY)
                .withCourse(VALID_COURSE_AMY)
                .withSpecialisation(VALID_SPECIALISATION_AMY)
                .withPriority(VALID_PRIORITY_AMY)
                .build();
        CommandParserTestUtil.assertParseSuccess(parser,
                CommandTestUtil.NAME_DESC_AMY
                        + CommandTestUtil.PHONE_DESC_AMY + CommandTestUtil.LINK_DESC_AMY
                        + CommandTestUtil.GRADUATING_YEAR_DESC_AMY + CommandTestUtil.COURSE_DESC_AMY
                        + CommandTestUtil.SPECIALISATION_DESC_AMY + CommandTestUtil.PRIORITY_DESC_AMY,
                new CreateCommand(expectedPersonWithoutEmails));

        // no links
        Person expectedPersonWithoutLinks = new PersonBuilder(TypicalPersons.AMY)
                .withoutOptionalFields()
                .withPhone(VALID_PHONE_AMY)
                .withEmails(List.of(VALID_EMAIL_AMY))
                .withGraduatingYear(VALID_GRADUATING_YEAR_AMY)
                .withCourse(VALID_COURSE_AMY)
                .withSpecialisation(VALID_SPECIALISATION_AMY)
                .withPriority(VALID_PRIORITY_AMY)
                .build();
        CommandParserTestUtil.assertParseSuccess(parser,
                CommandTestUtil.NAME_DESC_AMY
                        + CommandTestUtil.PHONE_DESC_AMY + CommandTestUtil.EMAIL_DESC_AMY
                        + CommandTestUtil.GRADUATING_YEAR_DESC_AMY + CommandTestUtil.COURSE_DESC_AMY
                        + CommandTestUtil.SPECIALISATION_DESC_AMY + CommandTestUtil.PRIORITY_DESC_AMY,
                new CreateCommand(expectedPersonWithoutLinks));

        // no graduating year
        Person expectedPersonWithoutGraduatingYear = new PersonBuilder(TypicalPersons.AMY)
                .withoutOptionalFields()
                .withPhone(VALID_PHONE_AMY)
                .withEmails(List.of(VALID_EMAIL_AMY))
                .withLinks(List.of(VALID_LINK_AMY))
                .withCourse(VALID_COURSE_AMY)
                .withSpecialisation(VALID_SPECIALISATION_AMY)
                .withPriority(VALID_PRIORITY_AMY)
                .build();
        CommandParserTestUtil.assertParseSuccess(parser,
                CommandTestUtil.NAME_DESC_AMY
                        + CommandTestUtil.PHONE_DESC_AMY + CommandTestUtil.EMAIL_DESC_AMY
                        + CommandTestUtil.LINK_DESC_AMY + CommandTestUtil.COURSE_DESC_AMY
                        + CommandTestUtil.SPECIALISATION_DESC_AMY + CommandTestUtil.PRIORITY_DESC_AMY,
                new CreateCommand(expectedPersonWithoutGraduatingYear));

        // no course
        Person expectedPersonWithoutCourse = new PersonBuilder(TypicalPersons.AMY)
                .withoutOptionalFields()
                .withPhone(VALID_PHONE_AMY)
                .withEmails(List.of(VALID_EMAIL_AMY))
                .withLinks(List.of(VALID_LINK_AMY))
                .withGraduatingYear(VALID_GRADUATING_YEAR_AMY)
                .withSpecialisation(VALID_SPECIALISATION_AMY)
                .withPriority(VALID_PRIORITY_AMY)
                .build();
        CommandParserTestUtil.assertParseSuccess(parser,
                CommandTestUtil.NAME_DESC_AMY
                        + CommandTestUtil.PHONE_DESC_AMY + CommandTestUtil.EMAIL_DESC_AMY
                        + CommandTestUtil.LINK_DESC_AMY + CommandTestUtil.GRADUATING_YEAR_DESC_AMY
                        + CommandTestUtil.SPECIALISATION_DESC_AMY + CommandTestUtil.PRIORITY_DESC_AMY,
                new CreateCommand(expectedPersonWithoutCourse));

        // no specialisation
        Person expectedPersonWithoutSpecialisation = new PersonBuilder(TypicalPersons.AMY)
                .withoutOptionalFields()
                .withPhone(VALID_PHONE_AMY)
                .withEmails(List.of(VALID_EMAIL_AMY))
                .withLinks(List.of(VALID_LINK_AMY))
                .withGraduatingYear(VALID_GRADUATING_YEAR_AMY)
                .withCourse(VALID_COURSE_AMY)
                .withPriority(VALID_PRIORITY_AMY)
                .build();
        CommandParserTestUtil.assertParseSuccess(parser,
                CommandTestUtil.NAME_DESC_AMY
                        + CommandTestUtil.PHONE_DESC_AMY + CommandTestUtil.EMAIL_DESC_AMY
                        + CommandTestUtil.LINK_DESC_AMY + CommandTestUtil.GRADUATING_YEAR_DESC_AMY
                        + CommandTestUtil.COURSE_DESC_AMY + CommandTestUtil.PRIORITY_DESC_AMY,
                new CreateCommand(expectedPersonWithoutSpecialisation));

        // zero tags
        Person expectedPersonWithoutTags = new PersonBuilder(TypicalPersons.AMY).withTags().build();
        CommandParserTestUtil.assertParseSuccess(parser,
                CommandTestUtil.NAME_DESC_AMY + CommandTestUtil.PHONE_DESC_AMY
                        + CommandTestUtil.EMAIL_DESC_AMY + CommandTestUtil.LINK_DESC_AMY
                        + CommandTestUtil.GRADUATING_YEAR_DESC_AMY + CommandTestUtil.COURSE_DESC_AMY
                        + CommandTestUtil.SPECIALISATION_DESC_AMY + CommandTestUtil.PRIORITY_DESC_AMY,
                new CreateCommand(expectedPersonWithoutTags));

        // no non-name fields
        Person expectedPersonWithoutOptionalFields = new PersonBuilder(TypicalPersons.AMY)
                .withoutOptionalFields()
                .build();
        CommandParserTestUtil.assertParseSuccess(parser,
                CommandTestUtil.NAME_DESC_AMY,
                new CreateCommand(expectedPersonWithoutOptionalFields));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, CreateCommand.MESSAGE_USAGE);

        // missing name prefix
        CommandParserTestUtil.assertParseFailure(parser,
                CommandTestUtil.VALID_NAME_BOB + CommandTestUtil.PHONE_DESC_BOB
                        + CommandTestUtil.EMAIL_DESC_BOB + CommandTestUtil.LINK_DESC_BOB
                        + CommandTestUtil.GRADUATING_YEAR_DESC_BOB + CommandTestUtil.COURSE_DESC_BOB
                        + CommandTestUtil.SPECIALISATION_DESC_BOB,
                expectedMessage);

        // all prefixes missing
        CommandParserTestUtil.assertParseFailure(parser,
                CommandTestUtil.VALID_NAME_BOB + CommandTestUtil.VALID_PHONE_BOB
                        + CommandTestUtil.VALID_EMAIL_BOB + CommandTestUtil.VALID_LINK_BOB
                        + CommandTestUtil.VALID_GRADUATING_YEAR_BOB + CommandTestUtil.VALID_COURSE_BOB
                        + CommandTestUtil.VALID_SPECIALISATION_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        CommandParserTestUtil.assertParseFailure(parser,
                CommandTestUtil.INVALID_NAME_DESC + CommandTestUtil.PHONE_DESC_BOB
                        + CommandTestUtil.EMAIL_DESC_BOB + CommandTestUtil.LINK_DESC_BOB
                        + CommandTestUtil.GRADUATING_YEAR_DESC_BOB + CommandTestUtil.COURSE_DESC_BOB
                        + CommandTestUtil.SPECIALISATION_DESC_BOB
                + CommandTestUtil.TAG_DESC_HUSBAND + CommandTestUtil.TAG_DESC_FRIEND, Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        CommandParserTestUtil.assertParseFailure(parser,
                CommandTestUtil.NAME_DESC_BOB + CommandTestUtil.INVALID_PHONE_DESC
                        + CommandTestUtil.EMAIL_DESC_BOB + CommandTestUtil.LINK_DESC_BOB
                        + CommandTestUtil.GRADUATING_YEAR_DESC_BOB + CommandTestUtil.COURSE_DESC_BOB
                        + CommandTestUtil.SPECIALISATION_DESC_BOB
                        + CommandTestUtil.TAG_DESC_HUSBAND + CommandTestUtil.TAG_DESC_FRIEND,
                Phone.MESSAGE_CONSTRAINTS);

        // invalid email
        CommandParserTestUtil.assertParseFailure(parser,
                CommandTestUtil.NAME_DESC_BOB + CommandTestUtil.PHONE_DESC_BOB
                        + CommandTestUtil.INVALID_EMAIL_DESC + CommandTestUtil.LINK_DESC_BOB
                        + CommandTestUtil.GRADUATING_YEAR_DESC_BOB + CommandTestUtil.COURSE_DESC_BOB
                        + CommandTestUtil.SPECIALISATION_DESC_BOB
                        + CommandTestUtil.TAG_DESC_HUSBAND + CommandTestUtil.TAG_DESC_FRIEND,
                Email.MESSAGE_CONSTRAINTS);

        // invalid link
        CommandParserTestUtil.assertParseFailure(parser,
                CommandTestUtil.NAME_DESC_BOB + CommandTestUtil.PHONE_DESC_BOB
                        + CommandTestUtil.EMAIL_DESC_BOB + CommandTestUtil.INVALID_LINK_DESC
                        + CommandTestUtil.GRADUATING_YEAR_DESC_BOB + CommandTestUtil.COURSE_DESC_BOB
                        + CommandTestUtil.SPECIALISATION_DESC_BOB
                        + CommandTestUtil.TAG_DESC_HUSBAND + CommandTestUtil.TAG_DESC_FRIEND,
                Link.MESSAGE_CONSTRAINTS);

        // invalid graduating year
        CommandParserTestUtil.assertParseFailure(parser,
                CommandTestUtil.NAME_DESC_BOB + CommandTestUtil.PHONE_DESC_BOB
                        + CommandTestUtil.EMAIL_DESC_BOB + CommandTestUtil.LINK_DESC_BOB
                        + CommandTestUtil.INVALID_GRADUATING_YEAR_DESC + CommandTestUtil.COURSE_DESC_BOB
                        + CommandTestUtil.SPECIALISATION_DESC_BOB
                        + CommandTestUtil.TAG_DESC_HUSBAND + CommandTestUtil.TAG_DESC_FRIEND,
                GraduatingYear.MESSAGE_CONSTRAINTS);

        // invalid course
        CommandParserTestUtil.assertParseFailure(parser,
                CommandTestUtil.NAME_DESC_BOB + CommandTestUtil.PHONE_DESC_BOB
                        + CommandTestUtil.EMAIL_DESC_BOB + CommandTestUtil.LINK_DESC_BOB
                        + CommandTestUtil.GRADUATING_YEAR_DESC_BOB + CommandTestUtil.INVALID_COURSE_DESC
                        + CommandTestUtil.SPECIALISATION_DESC_BOB
                        + CommandTestUtil.TAG_DESC_HUSBAND + CommandTestUtil.TAG_DESC_FRIEND,
                Course.MESSAGE_CONSTRAINTS);

        // invalid specialisation
        CommandParserTestUtil.assertParseFailure(parser,
                CommandTestUtil.NAME_DESC_BOB + CommandTestUtil.PHONE_DESC_BOB
                        + CommandTestUtil.EMAIL_DESC_BOB + CommandTestUtil.LINK_DESC_BOB
                        + CommandTestUtil.GRADUATING_YEAR_DESC_BOB + CommandTestUtil.COURSE_DESC_BOB
                        + CommandTestUtil.INVALID_SPECIALISATION_DESC
                        + CommandTestUtil.TAG_DESC_HUSBAND + CommandTestUtil.TAG_DESC_FRIEND,
                Specialisation.MESSAGE_CONSTRAINTS);

        // invalid tag
        CommandParserTestUtil.assertParseFailure(parser,
                CommandTestUtil.NAME_DESC_BOB + CommandTestUtil.PHONE_DESC_BOB
                        + CommandTestUtil.EMAIL_DESC_BOB + CommandTestUtil.LINK_DESC_BOB
                        + CommandTestUtil.GRADUATING_YEAR_DESC_BOB + CommandTestUtil.COURSE_DESC_BOB
                        + CommandTestUtil.SPECIALISATION_DESC_BOB
                        + CommandTestUtil.INVALID_TAG_DESC + CommandTestUtil.VALID_TAG_FRIEND,
                Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        CommandParserTestUtil.assertParseFailure(parser,
                CommandTestUtil.INVALID_NAME_DESC + CommandTestUtil.PHONE_DESC_BOB
                        + CommandTestUtil.EMAIL_DESC_BOB + CommandTestUtil.INVALID_LINK_DESC
                        + CommandTestUtil.GRADUATING_YEAR_DESC_BOB + CommandTestUtil.COURSE_DESC_BOB
                        + CommandTestUtil.SPECIALISATION_DESC_BOB,
                Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        CommandParserTestUtil.assertParseFailure(parser,
                CommandTestUtil.PREAMBLE_NON_EMPTY + CommandTestUtil.NAME_DESC_BOB
                        + CommandTestUtil.PHONE_DESC_BOB + CommandTestUtil.EMAIL_DESC_BOB
                        + CommandTestUtil.LINK_DESC_BOB
                        + CommandTestUtil.GRADUATING_YEAR_DESC_BOB + CommandTestUtil.COURSE_DESC_BOB
                        + CommandTestUtil.SPECIALISATION_DESC_BOB + CommandTestUtil.TAG_DESC_HUSBAND
                        + CommandTestUtil.TAG_DESC_FRIEND,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, CreateCommand.MESSAGE_USAGE));
    }
}
