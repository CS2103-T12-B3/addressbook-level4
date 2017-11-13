# joppeel
###### /java/seedu/address/logic/commands/LoadCommand.java
``` java
package seedu.address.logic.commands;

import java.io.IOException;
import java.util.Optional;

import javafx.collections.ObservableList;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.storage.XmlAddressBookStorage;

/**
 * Loads contacts from a pre-existing address book to the current one.
 * The pre-existing address book' name is given as a parameter.
 */
public class LoadCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "load";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Loads contacts from a pre-existing address "
        + "book to the current one. The pre-existing address book's name is given as a parameter.\n"
        + "Parameters: FILENAME\n"
        + "Example: " + COMMAND_WORD + " myaddressbook.xml";

    public static final String MESSAGE_LOAD_ADDRESSBOOK_SUCCESS = "Successfully loaded the address book.";
    public static final String MESSAGE_ERROR_LOADING_ADDRESSBOOK = "The address book couldn't be read. "
        + "Make sure your file is in the right directory and that it's in the correct format.";

    private final String fileName;

    public LoadCommand(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        ReadOnlyAddressBook addressBook = this.readAddressBook();
        addPersonsFromAddressBook(addressBook);

        return new CommandResult(MESSAGE_LOAD_ADDRESSBOOK_SUCCESS);
    }


    /**
     * Creates new XmlAddressBookStorage based on the file the user has inputted. Tries to read the
     * ReadOnlyAddressBook from the storage and return it, throws CommandException if fails.
     */
    private ReadOnlyAddressBook readAddressBook() throws CommandException {
        String filePathWithFileName = userPrefs.getFilePath() + fileName;
        XmlAddressBookStorage addressBookStorage = new XmlAddressBookStorage(filePathWithFileName);

        Optional<ReadOnlyAddressBook> inputtedAddressBook;
        try {
            inputtedAddressBook = addressBookStorage.readAddressBook();
        } catch (DataConversionException | IOException e) {
            throw new CommandException(MESSAGE_ERROR_LOADING_ADDRESSBOOK);
        }

        return inputtedAddressBook.orElseThrow(() -> new CommandException(MESSAGE_ERROR_LOADING_ADDRESSBOOK));
    }

    /**
     * Adds persons from the {@code addressBook} to the current model.
     */
    private void addPersonsFromAddressBook(ReadOnlyAddressBook addressBook) {
        ObservableList<ReadOnlyPerson> persons = addressBook.getPersonList();

        for (ReadOnlyPerson person : persons) {
            try {
                model.addPerson(person);

            } catch (DuplicatePersonException dpe) {
                // don't have to do anything as the person is already in the address book
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof LoadCommand // instanceof handles nulls
            && this.fileName.equals(((LoadCommand) other).fileName)); // state check
    }

}
```
###### /java/seedu/address/logic/parser/LoadCommandParser.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.LoadCommand;

import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new LoadCommand object
 */
public class LoadCommandParser implements Parser<LoadCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the LoadCommand
     * and returns an LoadCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public LoadCommand parse(String args) throws ParseException {

        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, LoadCommand.MESSAGE_USAGE));
        }

        return new LoadCommand(trimmedArgs);
    }

}

```
###### /java/seedu/address/logic/parser/ParserUtil.java
``` java
    /**
     * Parses a {@code Optional<String> birthday} into an {@code Optional<Birthday>} if {@code birthday} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Birthday> parseBirthday(Optional<String> birthday) throws IllegalValueException {
        requireNonNull(birthday);
        return birthday.isPresent() ? Optional.of(new Birthday(birthday.get())) : Optional.empty();
    }

    //@author
    /**
     * Parses a {@code Optional<String> photo} into an {@code Optional<Photo>} if {@code photo} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Photo> parsePhoto(Optional<String> photo) throws IllegalValueException {
        requireNonNull(photo);
        return Optional.of(new Photo(photo));
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws IllegalValueException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }
        return tagSet;
    }
}
```
###### /java/seedu/address/model/person/Birthday.java
``` java
package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Person's birthday in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidBirthday(String)}
 */
public class Birthday {

    public static final String MESSAGE_BIRTHDAY_CONSTRAINTS =
            "Person's birthday should be in format: DD/MM/YYYY and the date should be valid.";

    /*
     * Birthday must be in the following format: DD/MM/YYYY,
     * Otherwise it's invalid.
     */
    public static final String BIRTHDAY_VALIDATION_REGEX = "\\d\\d/\\d\\d/\\d\\d\\d\\d";

    public final String value;

    /**
     * Validates given birthday.
     *
     * @throws IllegalValueException if given birthday address string is invalid.
     */
    public Birthday(String birthday) throws IllegalValueException {
        requireNonNull(birthday);
        String trimmedBirthday = birthday.trim();
        if (!isValidBirthday(trimmedBirthday)) {
            throw new IllegalValueException(MESSAGE_BIRTHDAY_CONSTRAINTS);
        }
        this.value = trimmedBirthday;
    }

    /**
     * Returns if a given string is a valid person birthday.
     */
    public static boolean isValidBirthday(String test) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        formatter.setLenient(false);

        try {
            formatter.parse(test); // throws exception if the date is invalid
        } catch (ParseException pe) {
            return false;
        }
        return test.matches(BIRTHDAY_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Birthday // instanceof handles nulls
                && this.value.equals(((Birthday) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
```
