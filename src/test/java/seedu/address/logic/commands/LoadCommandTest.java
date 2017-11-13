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
