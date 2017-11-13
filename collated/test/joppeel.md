# joppeel
###### /java/seedu/address/logic/commands/LoadCommandTest.java
``` java
package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.testutil.TypicalPersons;


public class LoadCommandTest {

    private static UserPrefs userPrefs = new UserPrefs();
    private static String filePath = "src/test/data/LoadCommandTest/";
    private final String totallyNewContactsFileName = "testLoadTotallyNew.xml";
    private final String partiallyNewContactsFileName = "testLoadPartiallyNew.xml";


    @BeforeClass
    public static void setUserPrefs() {
        userPrefs.setFilePath(filePath);
    }

    @Test
    public void execute_newAddressBookContainsNewPersons() throws Exception {
        ModelManager model = new ModelManager(new AddressBook(), userPrefs);
        LoadCommand loadNewCommand = prepareLoadCommand(totallyNewContactsFileName, model);
        List<ReadOnlyPerson> expectedPersons = getListOfNewPersons();
        List<ReadOnlyPerson> oldPersons = model.getFilteredPersonList();

        for (ReadOnlyPerson person : oldPersons) {
            expectedPersons.add(person);
        }

        loadNewCommand.executeUndoableCommand();

        // test that the lists contain the same elements
        assertTrue(model.getFilteredPersonList().containsAll(expectedPersons));
        assertTrue(expectedPersons.containsAll(model.getFilteredPersonList()));
    }

    @Test
    public void execute_newAddressBookContainsPartiallyNewPersons() throws Exception {
        ModelManager model = new ModelManager(getTypicalAddressBook(), userPrefs);
        LoadCommand loadNewCommand = prepareLoadCommand(partiallyNewContactsFileName, model);
        List<ReadOnlyPerson> expectedPersons = getListOfPartiallyNewPersons();
        List<ReadOnlyPerson> oldPersons = model.getFilteredPersonList();

        for (ReadOnlyPerson person : oldPersons) {
            if (!expectedPersons.contains(person)) {
                expectedPersons.add(person);
            }
        }

        loadNewCommand.executeUndoableCommand();

        // test that the lists contain the same elements
        assertTrue(model.getFilteredPersonList().containsAll(expectedPersons));
        assertTrue(expectedPersons.containsAll(model.getFilteredPersonList()));
    }

    @Test
    public void execute_loadTheExistingPersons() throws Exception {
        ModelManager model = new ModelManager(getTypicalAddressBook(), userPrefs);
        LoadCommand loadNewCommand = prepareLoadCommand(totallyNewContactsFileName, model);
        List<ReadOnlyPerson> expectedPersons = model.getFilteredPersonList();

        loadNewCommand.executeUndoableCommand();

        // test that the lists contain the same elements
        assertTrue(model.getFilteredPersonList().containsAll(expectedPersons));
        assertTrue(expectedPersons.containsAll(model.getFilteredPersonList()));
    }

    @Test(expected = CommandException.class)
    public void execute_loadNonexistentAddressBook() throws CommandException {
        ModelManager model = new ModelManager(new AddressBook(), userPrefs);
        LoadCommand loadNewCommand = prepareLoadCommand("nonexistenfile123.xml", model);
        loadNewCommand.executeUndoableCommand();
    }

    @Test
    public void equals() {

        ModelManager model = new ModelManager(new AddressBook(), userPrefs);
        LoadCommand loadTypicalCommand = prepareLoadCommand(totallyNewContactsFileName, model);
        LoadCommand loadNewCommand = prepareLoadCommand("notATestFile.xml", model);

        // same object -> returns true
        assertTrue(loadTypicalCommand.equals(loadTypicalCommand));

        // same values -> returns true
        LoadCommand loadTypicalCommandCopy = prepareLoadCommand(totallyNewContactsFileName, model);
        assertTrue(loadTypicalCommand.equals(loadTypicalCommandCopy));

        // different types -> returns false
        assertFalse(loadTypicalCommand.equals(1));

        // null -> returns false
        assertFalse(loadTypicalCommand.equals(null));

        // different object -> returns false
        assertFalse(loadTypicalCommand.equals(loadNewCommand));
    }

    /**
     * Creates new instance of LoadCommand, the parameter represents the address book
     * that will be loaded to the existing address book.
     */
    private LoadCommand prepareLoadCommand(String fileName, ModelManager model) {
        LoadCommand loadCommand = new LoadCommand(fileName);
        loadCommand.setData(model, userPrefs, new CommandHistory(), new UndoRedoStack());
        return loadCommand;
    }

    /**
     * Returns list of completely new example persons
     */
    private List<ReadOnlyPerson> getListOfNewPersons() {
        return TypicalPersons.getTypicalPersons();
    }

    /**
     * Returns a list of ReadOnlyPersons that are partially included
     * in the list returned by {@code getListOfNewPersons}
     */
    private List<ReadOnlyPerson> getListOfPartiallyNewPersons() {
        ReadOnlyPerson fiona = TypicalPersons.FIONA;
        ReadOnlyPerson george = TypicalPersons.GEORGE;
        ReadOnlyPerson amy = TypicalPersons.AMY;
        ReadOnlyPerson bob = TypicalPersons.BOB;

        return new ArrayList<>(Arrays.asList(fiona, george, amy, bob));
    }

}
```
###### /java/seedu/address/logic/parser/LoadCommandParserTest.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.LoadCommand;

public class LoadCommandParserTest {

    private LoadCommandParser parser = new LoadCommandParser();

    @Test
    public void parse_failure() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, LoadCommand.MESSAGE_USAGE);

        // test with empty string
        assertParseFailure(parser, " ", expectedMessage);
    }

    @Test
    public void parse_success() throws Exception {
        LoadCommand expectedCommand = new LoadCommand("testbook.xml");

        assertParseSuccess(parser, "testbook.xml", expectedCommand);
    }


}
```
###### /java/seedu/address/model/person/BirthdayTest.java
``` java
package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BirthdayTest {

    @Test
    public void isValidBirthday() {
        // blank birthday
        assertFalse(Birthday.isValidBirthday("")); // empty string
        assertFalse(Birthday.isValidBirthday(" ")); // spaces only

        // missing parts
        assertFalse(Birthday.isValidBirthday("11/11")); // missing local part
        assertFalse(Birthday.isValidBirthday("11")); // missing '@' symbol
        assertFalse(Birthday.isValidBirthday("19/10/")); // missing domain name

        // invalid parts
        assertFalse(Birthday.isValidBirthday("-11/23/2332")); // using illegal characters
        assertFalse(Birthday.isValidBirthday("22/12/12222")); // too many numbers in the year part
        assertFalse(Birthday.isValidBirthday("q/qw/qweq")); // using letters instead of numbers
        assertFalse(Birthday.isValidBirthday("000/99/2323")); // too many numbers for the day
        assertFalse(Birthday.isValidBirthday("23 /12/1122")); // space between
        assertFalse(Birthday.isValidBirthday("29/02/2009")); // not a leap day

        // valid birthday
        assertTrue(Birthday.isValidBirthday("11/12/1099"));
        assertTrue(Birthday.isValidBirthday("09/03/2010"));
        assertTrue(Birthday.isValidBirthday("08/11/1992"));
        assertTrue(Birthday.isValidBirthday("29/02/2008")); // lead day

    }
}
```
###### /java/seedu/address/testutil/EditPersonDescriptorBuilder.java
``` java
    /**
     * Sets the {@code Birthday} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withBirthday(String birthday) {
        try {
            ParserUtil.parseBirthday(Optional.of(birthday)).ifPresent(descriptor::setBirthday);
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("birthday is expected to be unique.");
        }
        return this;
    }

```
###### /java/seedu/address/testutil/PersonBuilder.java
``` java
    /**
     * Sets the {@code Birthday} of the {@code Person} that we are building.
     */
    public PersonBuilder withBirthday(String birthday) {
        try {
            this.person.setBirthday(new Birthday(birthday));
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("birthday is expected to be unique.");
        }
        return this;
    }

```
###### /java/systemtests/LoadCommandSystemTest.java
``` java
package systemtests;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.testutil.TypicalPersons.getTypicalPersons;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import seedu.address.logic.commands.LoadCommand;
import seedu.address.model.Model;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;

public class LoadCommandSystemTest extends AddressBookSystemTest {

    private final String totallyNewContactsFileName = "testLoadTotallyNew.xml";

    @Test
    public void load_success() throws Exception {

        List<ReadOnlyPerson> newPersons = getTypicalPersons();
        String command = LoadCommand.COMMAND_WORD + " " + totallyNewContactsFileName;

        // try to load new address book, should load new persons
        assertCommandSuccess(command, newPersons);

        //try to load the same address book again, shouldn't add new persons
        assertCommandSuccess(command, new ArrayList());

    }

    @Test
    public void load_failure() throws Exception {

        // test with a non-existing file
        String command = LoadCommand.COMMAND_WORD + " notATestFile1234.xml";
        assertCommandFailure(command, LoadCommand.MESSAGE_ERROR_LOADING_ADDRESSBOOK);

        // test without giving any parameters
        command = LoadCommand.COMMAND_WORD;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, LoadCommand.MESSAGE_USAGE));
    }

    /**
     * Executes the {@code LoadCommand} that adds {@code personsToAdd} to the model and verifies that the command box
     * displays an empty string, the result display box displays the success message of executing {@code LoadCommand},
     * and the model related components equal to the current model added with {@code personsToAdd}.
     * These verifications are done by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the command box has the default style class, the status bar's sync status changes,
     * the browser url and selected card remains unchanged.
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, List<ReadOnlyPerson> personsToAdd) {
        Model expectedModel = getModel();
        for (ReadOnlyPerson person : personsToAdd) {
            try {
                expectedModel.addPerson(person);
            } catch (DuplicatePersonException dpe) {
                // don't have to do anything
            }
        }
        String expectedResultMessage = String.format(LoadCommand.MESSAGE_LOAD_ADDRESSBOOK_SUCCESS);

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, List)} except that the
     * result display box displays {@code expectedResultMessage} and the model related components equal to
     * {@code expectedModel}.
     * @see LoadCommandSystemTest#assertCommandSuccess(String, List)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the result display
     * box displays {@code expectedResultMessage} and the model related components equal to the current model.
     * These verifications are done by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the browser url, selected card and status bar remain unchanged, and the command box has the
     * error style.
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }


}
```
