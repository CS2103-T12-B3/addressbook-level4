# aggarwalRuchir
###### /java/seedu/address/logic/commands/ListCommand.java
``` java
    public static final String MESSAGE_SUCCESS_FULLLIST  = "Listed all persons";
    public static final String MESSAGE_SUCCESS_FILTEREDLIST  = "Listed all persons with tag: ";

    public static final String MESSAGE_NOENTRIESFOUND = "No person with given tags found.";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists all the people in address or people with certain tags.\n"
            + "Parameters: [optional]Tag\n"
            + "Example: " + COMMAND_WORD + "\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_TAG + "friends";

    private final PersonContainsKeywordsPredicate predicate;

    public ListCommand() {
        this.predicate = null;
    }

    public ListCommand (PersonContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute() {
        if (this.predicate != null) {
            model.updateFilteredPersonList(predicate);
        } else {
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
            return new CommandResult(MESSAGE_SUCCESS_FULLLIST);
        }

        if (areEntriesWithTagsFound()) {
            return new CommandResult(MESSAGE_SUCCESS_FILTEREDLIST + this.predicate.returnListOfTagsAsString());
        } else {
            return new CommandResult(MESSAGE_NOENTRIESFOUND);
        }
    }

    /**
     * Returns true if any entries are found for a tag in the address book
     */
    private boolean areEntriesWithTagsFound() {
        if (model.getFilteredPersonList().size() != 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ListCommand)) {
            return false;
        }

        // other has null predicate
        if ((((ListCommand) other).predicate == null) && (this.predicate == null)) {
            return true;
        }

        return this.predicate.equals(((ListCommand) other).predicate);
    }
}
```
###### /java/seedu/address/logic/parser/ListCommandParser.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.model.tag.Tag;


/**
 * Parses input arguments and creates a new ListCommand object
 */
public class ListCommandParser implements Parser<ListCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ListCommand
     * and returns an ListCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ListCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        try {
            if (!isPrefixPresent(argMultimap, PREFIX_TAG)) {
                return new ListCommand();
            }

            Set<Tag> inputTag = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
            List<Tag> inputTagNames = new ArrayList<>(inputTag);

            return new ListCommand(new PersonContainsKeywordsPredicate(inputTagNames));

        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Returns true if none of the tag prefix contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean isPrefixPresent(ArgumentMultimap argumentMultimap, Prefix tagPrefix) {
        return Stream.of(tagPrefix).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
```
###### /java/seedu/address/model/person/PersonContainsKeywordsPredicate.java
``` java
package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.model.tag.Tag;


/**
 * Tests that a {@code ReadOnlyPerson}'s {@code Tag} matches any of the keywords given.
 */
public class PersonContainsKeywordsPredicate implements Predicate<ReadOnlyPerson> {
    private final List<Tag> keywords;

    public PersonContainsKeywordsPredicate(List<Tag> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(ReadOnlyPerson person) {
        return keywords.stream()
                .anyMatch(keyword -> person.getTags().contains(keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PersonContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((PersonContainsKeywordsPredicate) other).keywords)); // state check
    }

    /**
     * Returns all the tags as a single string
     */
    public String returnListOfTagsAsString() {
        String stringOfTags = "";
        for (Tag t : this.keywords) {
            stringOfTags += t.toString() + " ";
        }
        return stringOfTags;
    }


}


```
###### /java/seedu/address/model/person/Phone.java
``` java
    /**
     * Format given phone number into typical mobile format
     * For example for Singapore numbers: xxxx-xxxx
     */
    public static String formatPhone(String trimmedPhone) {
        int phoneLength = trimmedPhone.length();
        String formattedPhone = generateFormattedPhone(trimmedPhone, phoneLength);

        return formattedPhone;
    }


    /**
     * Generates and returns a string with digits in the phone and hyphens inserted to get right format
     */
    private static String generateFormattedPhone(String trimmedPhone, int phoneLength) {
        int digitAdded = 0;
        StringBuilder formattedPhone = new StringBuilder();
        for (int count = phoneLength - 1; count >= 0; count--) {
            formattedPhone.insert(0, trimmedPhone.charAt(count));

            digitAdded += 1;
            if (count == 0) {
                continue;
            }

            if (isHyphenNeeded(digitAdded)) {
                formattedPhone.insert(0, "-");
                digitAdded = 0;
            }
        }

        return formattedPhone.toString();
    }


    /**
     * Returns true if 4 digits added to the string and hyphen needs to be inserted
     */
    private static boolean isHyphenNeeded(int digitAdded) {
        return (digitAdded % 4 == 0);
    }
```
