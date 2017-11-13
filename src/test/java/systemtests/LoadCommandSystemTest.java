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
