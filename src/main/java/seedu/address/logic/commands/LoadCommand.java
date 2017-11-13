package seedu.address.logic.commands;

import java.io.File;
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
