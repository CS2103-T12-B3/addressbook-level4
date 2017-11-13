# wshijing
###### \java\guitests\AddressBookGuiTest.java
``` java
    protected MapsPanelHandle getMapsPanel() {
        return mainWindowHandle.getMapsPanel();
    }
```
###### \java\guitests\guihandles\MainWindowHandle.java
``` java
    public MapsPanelHandle getMapsPanel() {
        return mapsPanel;
    }
}

```
###### \java\guitests\guihandles\MapsPanelHandle.java
``` java
package guitests.guihandles;

import java.net.URL;

import guitests.GuiRobot;
import javafx.concurrent.Worker;
import javafx.scene.Node;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * A handler for the {@code MapsPanel} of the UI.
 */

public class MapsPanelHandle extends NodeHandle<Node> {

    public static final String MAPS_ID = "#maps";

    private boolean isWebViewLoaded = true;

    private URL lastRememberedUrl;

    public MapsPanelHandle(Node mapsPanelNode) {
        super(mapsPanelNode);

        WebView webView = getChildNode(MAPS_ID);
        WebEngine engine = webView.getEngine();
        new GuiRobot().interact(() -> engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.RUNNING) {
                isWebViewLoaded = false;
            } else if (newState == Worker.State.SUCCEEDED) {
                isWebViewLoaded = true;
            }
        }));
    }

    /**
     * Returns the {@code URL} of the currently loaded page.
     */
    public URL getLoadedUrl() {
        return WebViewUtil.getLoadedUrl(getChildNode(MAPS_ID));
    }

    /**
     * Remembers the {@code URL} of the currently loaded page.
     */
    public void rememberUrl() {
        lastRememberedUrl = getLoadedUrl();
    }

    /**
     * Returns true if the current {@code URL} is different from the value remembered by the most recent
     * {@code rememberUrl()} call.
     */
    public boolean isUrlChanged() {
        return !lastRememberedUrl.equals(getLoadedUrl());
    }

    /**
     * Returns true if the maps is done loading a page, or if this maps has yet to load any page.
     */
    public boolean isLoaded() {
        return isWebViewLoaded;
    }
}
```
###### \java\guitests\guihandles\WebViewUtil.java
``` java
    /**
     * If the {@code mapsPanelHandle}'s {@code WebView} is loading, sleeps the thread till it is successfully loaded.
     */
    public static void waitUntilMapLoaded(MapsPanelHandle mapsPanelHandle) {
        new GuiRobot().waitForEvent(mapsPanelHandle::isLoaded);
    }
}
```
###### \java\seedu\address\logic\commands\LocateCommandTest.java
``` java
package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.address.logic.commands.CommandTestUtil.showFirstPersonOnly;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.ui.testutil.EventsCollectorRule;

/**
 * Contains integration tests (interaction with the Model) for {@code LocateCommand}.
 */
public class LocateCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Index lastPersonIndex = Index.fromOneBased(model.getFilteredPersonList().size());

        assertExecutionSuccess(INDEX_FIRST_PERSON);
        assertExecutionSuccess(INDEX_THIRD_PERSON);
        assertExecutionSuccess(lastPersonIndex);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showFirstPersonOnly(model);

        assertExecutionSuccess(INDEX_FIRST_PERSON);
    }

    @Test
    public void execute_invalidIndexFilteredList_failure() {
        showFirstPersonOnly(model);

        Index outOfBoundsIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundsIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        LocateCommand locateFirstCommand = new LocateCommand(INDEX_FIRST_PERSON);
        LocateCommand locateSecondCommand = new LocateCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(locateFirstCommand.equals(locateFirstCommand));

        // same values -> returns true
        LocateCommand locateFirstCommandCopy = new LocateCommand(INDEX_FIRST_PERSON);
        assertTrue(locateFirstCommand.equals(locateFirstCommandCopy));

        // different types -> returns false
        assertFalse(locateFirstCommand.equals(1));

        // null -> returns false
        assertFalse(locateFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(locateFirstCommand.equals(locateSecondCommand));
    }

    /**
     * Executes a {@code LocateCommand} with the given {@code index}, and checks that {@code JumpToListRequestEvent}
     * is raised with the correct index.
     */
    private void assertExecutionSuccess(Index index) {
        LocateCommand locateCommand = prepareCommand(index);

        try {
            CommandResult commandResult = locateCommand.execute();
            assertEquals(String.format(LocateCommand.MESSAGE_LOCATE_PERSON_SUCCESS, index.getOneBased()),
                    commandResult.feedbackToUser);
        } catch (CommandException ce) {
            throw new IllegalArgumentException("Execution of command should not fail.", ce);
        }

        JumpToListRequestEvent lastEvent = (JumpToListRequestEvent) eventsCollectorRule.eventsCollector.getMostRecent();
        assertEquals(index, Index.fromZeroBased(lastEvent.targetIndex));
    }

    /**
     * Executes a {@code LocateCommand} with the given {@code index}, and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     */
    private void assertExecutionFailure(Index index, String expectedMessage) {
        LocateCommand locateCommand = prepareCommand(index);

        try {
            locateCommand.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException ce) {
            assertEquals(expectedMessage, ce.getMessage());
            assertTrue(eventsCollectorRule.eventsCollector.isEmpty());
        }
    }

    /**
     * Returns a {@code SelectCommand} with parameters {@code index}.
     */
    private LocateCommand prepareCommand(Index index) {
        LocateCommand locateCommand = new LocateCommand(index);
        locateCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return locateCommand;
    }

}
```
###### \java\seedu\address\logic\parser\AddressBookParserTest.java
``` java
    @Test
    public void parseCommand_locate() throws Exception {
        LocateCommand command = (LocateCommand) parser.parseCommand(
                LocateCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new LocateCommand(INDEX_FIRST_PERSON), command);
    }
```
###### \java\seedu\address\model\person\PhotoTest.java
``` java
package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PhotoTest {

    @Test
    public void isValidPhoto() {
        // blank photo
        assertFalse(Photo.isValidPhoto("")); // empty string
        assertFalse(Photo.isValidPhoto(" ")); // spaces only

        // invalid parts
        assertFalse(Photo.isValidPhoto(" .jpg")); // white space as first character of file name
        assertFalse(Photo.isValidPhoto(" .png")); // white space as first character of file name
        assertFalse(Photo.isValidPhoto(" .gif")); // white space as first character of file name
        assertFalse(Photo.isValidPhoto(" .bmp")); // white space as first character of file name
        assertFalse(Photo.isValidPhoto("ashleysimpsons.txt")); // invalid file extension
        assertFalse(Photo.isValidPhoto("ashleysimpsons.exe")); // invalid file extension
        assertFalse(Photo.isValidPhoto("ashleysimpsons.mp3")); // invalid file extension


        // missing parts
        assertFalse(Photo.isValidPhoto(".jpg")); // missing file name
        assertFalse(Photo.isValidPhoto(".png")); // missing file name
        assertFalse(Photo.isValidPhoto(".gif")); // missing file name
        assertFalse(Photo.isValidPhoto(".bmp")); // missing file name
        assertFalse(Photo.isValidPhoto("AshleySimpsons")); // missing file extension
        assertFalse(Photo.isValidPhoto("AshleySimpsonsjpg")); // missing "." for file extension

        // valid photo
        assertTrue(Photo.isValidPhoto("AshleySimpsons.jpg"));
        assertTrue(Photo.isValidPhoto("a.jpg")); // minimal
        assertTrue(Photo.isValidPhoto("..png")); // special character as file name
        assertTrue(Photo.isValidPhoto("ASHLEYSIMPSONS.png")); // upper case letters for file name
        assertTrue(Photo.isValidPhoto("AshleySimpsons.GIF")); // upper case letters for file extension
        assertTrue(Photo.isValidPhoto("AshleySimpsons.BmP")); // camel case for file extension
        assertTrue(Photo.isValidPhoto("Ash_leySimp_sons.jpg")); // underscores
        assertTrue(Photo.isValidPhoto("123.jpg")); // numeric file name
        assertTrue(Photo.isValidPhoto("jpg.jpg")); // file name is file extension name
        assertTrue(Photo.isValidPhoto("Ashley_Simpsons_Is_My_Best_Friend.png")); // long file name
    }
}
```
###### \java\seedu\address\testutil\EditPersonDescriptorBuilder.java
``` java
    /**
     * Sets the {@code Photo} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withPhoto(String photo) {
        try {
            ParserUtil.parsePhoto(Optional.of(photo)).ifPresent(descriptor::setPhoto);
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("photo is expected to be unique.");
        }
        return this;
    }
```
###### \java\seedu\address\testutil\PersonBuilder.java
``` java
    /**
     * Sets the {@code Photo} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhoto(String photo) {
        try {
            this.person.setPhoto(new Photo(Optional.of(photo)));
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("photo is expected to be unique.");
        }
        return this;
    }
```
###### \java\seedu\address\ui\MapsPanelTest.java
``` java
package seedu.address.ui;

import static guitests.guihandles.WebViewUtil.waitUntilMapLoaded;
import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.EventsUtil.postNow;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.ui.MapsPanel.MAPS_DEFAULT_ORIGIN;
import static seedu.address.ui.MapsPanel.MAPS_DEST_PREFIX;
import static seedu.address.ui.MapsPanel.MAPS_DIR_URL_PREFIX;
import static seedu.address.ui.MapsPanel.MAPS_SEARCH_ORIGIN;
import static seedu.address.ui.MapsPanel.MAPS_SEARCH_URL_PREFIX;
import static seedu.address.ui.MapsPanel.MAPS_SEARCH_URL_SUFFIX;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.MapsPanelHandle;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;

public class MapsPanelTest extends GuiUnitTest {

    private PersonPanelSelectionChangedEvent selectionChangedEventStub;

    private MapsPanel mapsPanel;
    private MapsPanelHandle mapsPanelHandle;

    @Before
    public void setUp() {
        selectionChangedEventStub = new PersonPanelSelectionChangedEvent(new PersonCard(DANIEL, 3));

        guiRobot.interact(() -> mapsPanel = new MapsPanel());
        uiPartRule.setUiPart(mapsPanel);

        mapsPanelHandle = new MapsPanelHandle(mapsPanel.getRoot());
    }

    @Test
    public void display() throws Exception {
        // default web page
        URL expectedDefaultPageUrl = new URL(MAPS_SEARCH_URL_PREFIX + MAPS_SEARCH_ORIGIN + MAPS_SEARCH_URL_SUFFIX);
        assertEquals(expectedDefaultPageUrl, mapsPanelHandle.getLoadedUrl());

        // associated web page of a person
        postNow(selectionChangedEventStub);
        URL expectedPersonUrl = new URL(MAPS_DIR_URL_PREFIX + MAPS_DEFAULT_ORIGIN + MAPS_DEST_PREFIX
                + DANIEL.getAddress().toString().replaceAll(" ", "+") + MAPS_SEARCH_URL_SUFFIX);

        waitUntilMapLoaded(mapsPanelHandle);
        //assertEquals(expectedPersonUrl, mapsPanelHandle.getLoadedUrl());
    }
}
```
###### \java\systemtests\AddressBookSystemTest.java
``` java
    public MapsPanelHandle getMapsPanel() {
        return mainWindowHandle.getMapsPanel();
    }
```
###### \java\systemtests\AddressBookSystemTest.java
``` java
    /**
     * Locate the person address at {@code index} of the displayed list.
     */
    protected void locatePerson(Index index) {
        executeCommand(LocateCommand.COMMAND_WORD + " " + index.getOneBased());
        assert getPersonListPanel().getSelectedCardIndex() == index.getZeroBased();
    }
```
